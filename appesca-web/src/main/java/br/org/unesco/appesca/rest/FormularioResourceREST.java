package br.org.unesco.appesca.rest;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import br.org.unesco.appesca.rest.model.FormularioREST;
import br.org.unesco.appesca.rest.model.RespEnvioFormulario;
import br.org.unesco.appesca.rest.model.RespFormularioREST;
import br.org.unesco.appesca.service.FormularioService;

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
	private UsuarioRepository usuarioRespository;

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
		
		if(resp!=null && resp.getAudio()!=null){
			return Response.ok(new ByteArrayInputStream(resp.getAudio()),"video/mp4").build();
		}else{
			return Response.noContent().build();
		}
	}
	
//	@POST
////	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_XML)
//	@Path("/inserir_vs2")
//	public String doUpload(@Context HttpServletRequest request) {
//		MultipartRequestMap map = new MultipartRequestMap(request);
//		
//		map.getFileParameter("audioQ9");
//		map.getStringParameter("login");
//
//		RespEnvioFormulario rs = new RespEnvioFormulario(true, "O formulário não foi processado corretamente.");
//		return getXML(rs);
//	}


}