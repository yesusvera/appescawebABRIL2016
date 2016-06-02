package br.org.unesco.appesca.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.org.unesco.appesca.data.UsuarioRepository;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.model.exp.RowExportCVS;
import br.org.unesco.appesca.rest.model.FormularioREST;
import br.org.unesco.appesca.rest.model.RespEnvioFormulario;
import br.org.unesco.appesca.rest.model.RespFormularioREST;
import br.org.unesco.appesca.service.FormularioService;
import br.org.unesco.appesca.service.TemplateCVS;
import br.org.unesco.appesca.service.UsuarioService;

/**
 * @author yesus
 */
@Path("/formulario")
@RequestScoped
public class FormularioResourceREST extends BaseREST {
	// @Inject
	// private FormularioRepository formularioRepository;

	@Inject
	private FormularioService formularioService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private UsuarioRepository usuarioRespository;

	@Context
	private ServletContext context;

	@GET
	@Path("/lista")
	@Produces(MediaType.APPLICATION_XML)
	public String listaPorLogin(@QueryParam("login") String login, @QueryParam("senha") String senha) {
		RespFormularioREST resp = new RespFormularioREST();
		Usuario usr = usuarioRespository.findByLoginSenha(login, senha);
		if (usr != null) {
			List<Formulario> listaFormularios = formularioService.findListByUsuario(usr);
			if (listaFormularios != null && listaFormularios.size() > 0) {
				for (Formulario form : listaFormularios) {
					form.setListaQuestoes(null);
				}
				resp.setListaFormularios(listaFormularios);
			} else {
				resp.setErro(true);
				resp.setMensagemErro("Não existem formulários para este pesquisador.");
			}
		} else {
			resp.setErro(true);
			resp.setMensagemErro("Autenticação inválida.");
		}
		return getXML(resp);
	}

	@POST
	@Path("/inserir")
	@Produces(MediaType.APPLICATION_XML)
	public String inserir(@FormParam("xmlFormularioRest") String xmlFormularioRest, @FormParam("login") String login,
			@FormParam("senha") String senha) {
		Usuario usr = usuarioRespository.findByLoginSenha(login, senha);
		if (usr != null) {
			System.out.println("Inserindo o formulário vindo do Android.");
			XStream xStream = new XStream(new DomDriver());

			try {
				FormularioREST formularioRest = (FormularioREST) xStream.fromXML(xmlFormularioRest);
				Formulario formulario = formularioRest.getFormulario();

				if (formulario.getIdSincronizacao() == null || formulario.getIdSincronizacao().equals("")) {
					formulario.setIdSincronizacao(formularioService.generateIdSincronizacao(formulario));
				} else {
					Formulario formTmp = formularioService.findByIdSincronizacao(formulario.getIdSincronizacao());
					if (formTmp != null) {
						formulario.setIdSincronizacao(formulario.getIdSincronizacao());
						// TODO EXCLUIR TODAS AS QUESTOES DO FORMULARIO
						formularioService.delete(formTmp);
						// formulario.setId(formTmp.getId());
					}
				}
				List<Questao> lstQuestoesBKP = formulario.getListaQuestoes();
				formulario.setListaQuestoes(null);
				formulario.setIdUsuario(usr.getId());
				formulario.setSituacao(1);

				formularioService.save(formulario);
				formulario.setListaQuestoes(lstQuestoesBKP);

				if (formulario.getListaQuestoes() != null) {
					for (Questao questao : formulario.getListaQuestoes()) {
						questao.setFormulario(formulario);
						if (questao.getListaPerguntas() != null) {
							for (Pergunta pergunta : questao.getListaPerguntas()) {
								pergunta.setQuestao(questao);
								if (pergunta.getListaRespostas() != null) {
									for (Resposta resposta : pergunta.getListaRespostas()) {
										resposta.setPergunta(pergunta);
									}
								}
							}
						}
					}
				}
				formularioService.save(formulario);
				RespEnvioFormulario rs = new RespEnvioFormulario();
				rs.setErro(false);
				rs.setMensagemErro("Formulário recebido com sucesso. Aguarde a aprovação do coordenador.");
				rs.setIdSincronizacao(formulario.getIdSincronizacao());

				return getXML(rs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			RespEnvioFormulario rs = new RespEnvioFormulario(true,
					"Não foi possível receber o formulário.Falha na autenticação. Tente se logar novamente online.");
			return getXML(rs);
		}

		RespEnvioFormulario rs = new RespEnvioFormulario(true, "O formulário não foi processado corretamente.");
		return getXML(rs);
	}

	@GET
	@Path("/audioB9Q1")
	@Produces("video/mp4")
	public Response audioB9Q1(@QueryParam("idFormulario") String idFormulario) {

		Formulario formulario = formularioService.findById(new Integer(idFormulario));
		int ordemUltQ = formularioService.getOrdemUltimaQuestao(formulario);
		Resposta resp = formularioService.getResposta(ordemUltQ, 2, 1, formulario);

		if (resp != null && resp.getAudio() != null) {
			return Response.ok(new ByteArrayInputStream(resp.getAudio()), "video/mp4").build();
		} else {
			return Response.noContent().build();
		}
	}

	@GET
	@Path("/exportacaoDeFormulario")
	@Produces("text/csv")
	public Response exportacaoDeFormulario(@QueryParam("tipo") String strTipo) {
		Integer tipoFormulario = Integer.valueOf(strTipo);
		String nomeTemplate = "";

		switch (tipoFormulario) {
		case 1: // camarao
			nomeTemplate = "camaraoRegionalVS1.csv";
			break;
		case 2:// caranguejo
			// return Response.noContent().build();
			nomeTemplate = "caranguejoVS1.csv";
			break;
		case 3:// piticaia
			nomeTemplate = "camaraoPiticaiaEBrancoVS1.csv";
			break;
		}

		TemplateCVS templateCVS = new TemplateCVS();
		templateCVS
				.execute(new File(context.getRealPath("/WEB-INF/exportacaoTemplates/camaraoPiticaiaEBrancoVS1.csv")));

		String conteudoCSV = "";

		// CABEÇALHO
		conteudoCSV = montaCabecalho(templateCVS, conteudoCSV);

		List<Formulario> listaFormulario = formularioService.listByTipoFormulario((int) tipoFormulario);

		List<Usuario> listaUsuarios = new ArrayList<>();

		try {
			listaUsuarios = usuarioService.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Formulario form : listaFormulario) {
			
			if(form.getSituacao() < 2){
				continue;
			}
			
			System.out.println("-_-_-> Processando formulário " + form.getIdSincronizacao());
			Usuario usr = null;

			for (Usuario usrTmp : listaUsuarios) {
				if (form.getIdUsuario() == usrTmp.getId().intValue()) {
					usr = usrTmp;
				}
			}
			conteudoCSV += form.getIdSincronizacao() + ";";
			conteudoCSV += formularioService.getRespostaTexto(0, 1, 1, form) + ";";
			conteudoCSV += "?" + ";";
			conteudoCSV += formularioService.getUF(form) + ";";
			conteudoCSV += formularioService.getRespostaTexto(0, 4, 1, form) + ";";
			conteudoCSV += formularioService.getRespostaTexto(0, 5, 1, form) + ";";
			conteudoCSV += "?" + ";";
			conteudoCSV += (form.getLatitude() == null ? "" : form.getLatitude()) + ";";
			conteudoCSV += (form.getLongitude() == null ? "" : form.getLongitude()) + ";";

			for (RowExportCVS row : templateCVS.getListRowCVS()) {
				if (row.getCod1AppescaAndroid() != null && !row.getCod1AppescaAndroid().trim().isEmpty()) {
					if (TemplateCVS.rowIsDataAplicacao(row)) {
						conteudoCSV += form.getData();
					}
					if (TemplateCVS.rowIsNomePesquisador(row)) {
						if (usr != null) {
							conteudoCSV += usr.getNome();
						}
					}

					if (TemplateCVS.rowIsText(row) || TemplateCVS.rowIsNumber(row) || TemplateCVS.rowIsMultiple(row)) {
						if (TemplateCVS.temDoisCodigos(row)) {
							conteudoCSV = priorizaRespostaComDoisCodigos(conteudoCSV, form, row);
						} else {
							String respStr = formularioService.getResposta(row.getCod1AppescaAndroid(), form)
									.getTexto();
							if (respStr != null) {
								respStr = respStr.replaceAll("\n", "");
							}
							conteudoCSV += respStr;
						}
					}

					if (TemplateCVS.rowIsUnique(row)) {
						if (TemplateCVS.temDoisCodigos(row)) {
							conteudoCSV = priorizaRespostaComDoisCodigos(conteudoCSV, form, row);
						} else {
							String respStr = formularioService.getStringRespostaUnica(row.getCod1AppescaAndroid(),
									form);
							if (respStr != null) {
								respStr = respStr.replaceAll("\n", "");
							}
							conteudoCSV += respStr;
						}
					}
				}
				conteudoCSV += ";";
			}
			conteudoCSV += "\n";
		}

		return Response.ok(conteudoCSV).header("Content-Disposition", "attachment; filename=" + nomeTemplate).build();
	}

	private String priorizaRespostaComDoisCodigos(String conteudoCSV, Formulario form, RowExportCVS row) {
		String respCod2 = formularioService.getResposta(row.getCod2AppescaAndroid(), form).getTexto();

		if (respCod2 != null && !respCod2.trim().isEmpty()) {
			conteudoCSV += respCod2;
		} else {
			conteudoCSV += formularioService.getResposta(row.getCod1AppescaAndroid(), form).getTexto();
		}
		return conteudoCSV;
	}

	private String montaCabecalho(TemplateCVS templateCVS, String conteudoCSV) {
		conteudoCSV += "Codigo;Pesquisador;Recurso;Estado;Municipio;Comunidade;UC;lat;long;";
		for (RowExportCVS row : templateCVS.getListRowCVS()) {
			conteudoCSV += row.getCodigoExportacao() + ";";
		}
		conteudoCSV += "\n";
		return conteudoCSV;
	}
}