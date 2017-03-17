package br.org.unesco.appesca.rest;

import br.org.unesco.appesca.controller.FormularioController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import br.org.unesco.appesca.model.exp.TypeVariables;
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

    @Inject
    private FormularioService formularioService;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private UsuarioRepository usuarioRespository;

    @Inject
    FormularioController formularioController;

//	@Inject
//	private ExportacaoCSVService exportacaoCSVService;
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
    public Response exportacaoDeFormulario(@QueryParam("tipo") String strTipo, @QueryParam("todos") boolean todos) {
        Integer tipoFormulario = Integer.valueOf(strTipo);
        String nomeTemplate = "";
        String nomeGerado = "";

        switch (tipoFormulario) {
            case 1: // camarao
                nomeTemplate = "camaraoRegionalVS1.csv";
                nomeGerado = "camaraoRegional";
                break;
            case 2:// caranguejo
                // return Response.noContent().build();
                nomeTemplate = "caranguejoVS1.csv";
                nomeGerado = "caranguejo";
                break;
            case 3:// piticaia
                nomeTemplate = "camaraoPiticaiaEBrancoVS1.csv";
                nomeGerado = "camaraoPiticaiaEBranco";
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmm");
        nomeGerado += sdf.format(new Date());
        nomeGerado += ".csv";

        TemplateCVS templateCVS = new TemplateCVS();
        templateCVS.execute(new File(context.getRealPath("/WEB-INF/exportacaoTemplates/" + nomeTemplate)));

         List<Formulario> listaFormulario = formularioController.getListaFormularios();

        if (todos) {
            listaFormulario = formularioService.listByTipoFormulario((int) tipoFormulario);
        }
        
        String conteudoCSV = "";

        // CABEÇALHO
        conteudoCSV = templateCVS.montaCabecalho(conteudoCSV, formularioController.getFiltroFormulario(), listaFormulario.size());

       

        List<Usuario> listaUsuarios = new ArrayList<>();

        try {
            listaUsuarios = usuarioService.listAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int x = 1;

        for (Formulario form : listaFormulario) {
            if (todos) {
                // SOMENTE PARA FORMULARIOS FINALIZADOS.
                if (form.getSituacao() < 2) {
                    continue;
                }
            }
            System.out.println("-_-_-> Processando formulário (" + x++ + ") " + form.getIdSincronizacao());

//			ExportacaoCSV expCSV = exportacaoCSVService.findByIdFormulario(form.getId());
            // if (expCSV != null && expCSV.getLinhaCSV() != null &&
            // !expCSV.getLinhaCSV().isEmpty()) {
            // conteudoCSV += expCSV.getLinhaCSV();
            // } else {
            String linhaCSV = "";
            Usuario usr = null;

            for (Usuario usrTmp : listaUsuarios) {
                if (form.getIdUsuario() == usrTmp.getId().intValue()) {
                    usr = usrTmp;
                }
            }
            linhaCSV += form.getIdSincronizacao() + ";";
            linhaCSV += formularioService.getRespostaTexto(0, 1, 1, form) + ";";
            linhaCSV += "?" + ";";
            linhaCSV += formularioService.getUF(form) + ";";
            linhaCSV += formularioService.getRespostaTexto(0, 4, 1, form) + ";";
            linhaCSV += formularioService.getRespostaTexto(0, 5, 1, form) + ";";
            linhaCSV += "?" + ";";
            linhaCSV += (form.getLatitude() == null ? "" : form.getLatitude()) + ";";
            linhaCSV += (form.getLongitude() == null ? "" : form.getLongitude()) + ";";

            for (RowExportCVS row : templateCVS.getListRowCVS()) {
                try {
                    if (!TemplateCVS.codigoExportacaoValido(row.getCodigoExportacao())) {
                        continue;
                    }

                    if (row.getCod1AppescaAndroid() != null && !row.getCod1AppescaAndroid().trim().isEmpty()) {
                        if (row.getCod1AppescaAndroid().equalsIgnoreCase(TypeVariables.DATA_APLICACAO)) {
                            linhaCSV += form.getData();
                        } else if (row.getCod1AppescaAndroid().equalsIgnoreCase(TypeVariables.NOME_PESQUISADOR)) {
                            if (usr != null) {
                                linhaCSV += usr.getNome();
                            }
                        }else if (TemplateCVS.temDoisCodigos(row)) {
                            linhaCSV = templateCVS.priorizaRespostaComDoisCodigos(linhaCSV,
                                    formularioService.getResposta(row.getCod2AppescaAndroid(), form).getTexto(),
                                    formularioService.getResposta(row.getCod1AppescaAndroid(), form).getTexto());
                        } else {
                            String respStr = "";

                            if (row.getCod1AppescaAndroid() != null) {
                                if (row.getCod1AppescaAndroid().indexOf(",") == -1) {
                                    respStr = "\"" + formularioService
                                            .getResposta(row.getCod1AppescaAndroid(), form).getTexto() + "\"";
                                } else {
                                    String[] listaCodigos = row.getCod1AppescaAndroid().split(",");

                                    for (String codAppescaAndroid : listaCodigos) {
                                        Resposta resp = formularioService.getResposta(codAppescaAndroid, form);

                                        if (resp != null && resp.getTexto() != null
                                                && resp.getTexto().trim().length() > 0) {
                                            respStr = "\"" + resp.getTexto() + "\"";

                                            break;
                                        }
                                    }
                                }
                            }

                            if (respStr != null) {
                                respStr = respStr.replaceAll("\n", "").replaceAll("\r", "");
                            }
                            linhaCSV += respStr;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                linhaCSV += ";";
            }
            conteudoCSV += linhaCSV.replace("\n", "").replace("\r", "");

//			expCSV = new ExportacaoCSV();
//			expCSV.setIdFormulario(form.getId());
//			expCSV.setIdSincronizacao(form.getIdSincronizacao());
//			expCSV.setIdTipoFormulario(form.getIdTipoFormulario());
//			expCSV.setLinhaCSV(linhaCSV);
            // exportacaoCSVService.save(expCSV);
            // }
            conteudoCSV += "\n";
        }

        return Response.ok(conteudoCSV).header("Content-Disposition", "attachment; filename=" + nomeGerado).build();
    }
}
