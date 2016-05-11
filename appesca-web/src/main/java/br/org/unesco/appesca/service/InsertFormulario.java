package br.org.unesco.appesca.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.org.unesco.appesca.data.UsuarioRepository;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.rest.BaseREST;
import br.org.unesco.appesca.rest.model.FormularioREST;
import br.org.unesco.appesca.rest.model.RespEnvioFormulario;

/**
 * Servlet implementation class InsertFormulario
 */

@MultipartConfig
public class InsertFormulario extends HttpServlet {

	@Inject
	private FormularioService formularioService;

	@Inject
	private UsuarioRepository usuarioRespository;
	
	private BaseREST baseREST = new BaseREST();

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/xml;charset=UTF-8");
		
		final PrintWriter writer = response.getWriter();
		
		

		Usuario usr = usuarioRespository.findByLoginSenha(request.getParameter("login"), request.getParameter("senha"));
		if (usr != null) {
			System.out.println("Inserindo o formulário vindo do Android.");
			XStream xStream = new XStream(new DomDriver());

			try {
				FormularioREST formularioRest = (FormularioREST) xStream.fromXML(request.getParameter("xmlFormularioRest"));
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

				
				try{
					if(request.getPart("audioB9Q1")!=null){
						byte audioB9Q1[] = getBytesFromPart(request.getPart("audioB9Q1"));
						
						int ordemUltQ = formularioService.getOrdemUltimaQuestao(formulario);

						Questao Q1B9 = formularioService.getQuestaoPorOrdem(ordemUltQ, formulario);
						
						if(Q1B9==null){
							Q1B9 = new Questao();
							Q1B9.setOrdem(ordemUltQ);
							Q1B9.setTitulo("Questão "+ordemUltQ);
							Q1B9.setFormulario(formulario);
							
							if(formulario.getListaQuestoes()!=null){
								formulario.getListaQuestoes().add(Q1B9);
							}
						}
						
						Pergunta perg = formularioService.getPerguntaPorOrdem(2, Q1B9); 
						
						if(perg==null){
							perg = new Pergunta();
							perg.setOrdem(2);
							perg.setRespBooleana(false); 
							perg.setQuestao(Q1B9);
							
							if(Q1B9.getListaPerguntas()==null){
								Q1B9.setListaPerguntas(new ArrayList<Pergunta>());
							}
							Q1B9.getListaPerguntas().add(perg);
						}
						
						Resposta resposta = formularioService.getRespostaPorOrdem(2, perg);
						
						if(resposta==null){
							resposta = new Resposta();
							resposta.setOrdem(1);
							resposta.setPergunta(perg);
							
							if(perg.getListaRespostas()==null){
								perg.setListaRespostas(new ArrayList<Resposta>());
							}
							perg.getListaRespostas().add(resposta);
						}
						
						resposta.setAudio(audioB9Q1);
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
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

				writer.println(baseREST.getXML(rs));
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			RespEnvioFormulario rs = new RespEnvioFormulario(true,
					"Não foi possível receber o formulário.Falha na autenticação. Tente se logar novamente online.");
			writer.println(baseREST.getXML(rs));
			return;
		}

		RespEnvioFormulario rs = new RespEnvioFormulario(true, "O formulário não foi processado corretamente.");
		writer.println(baseREST.getXML(rs));
		return;
	}

	public byte[] getBytesFromPart(Part filePart) {
//		final String fileName = getFileName(filePart);

		InputStream filecontent;
		try {
			filecontent = filePart.getInputStream();
			return getBytes(filecontent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static byte[] getBytes(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	public void doPostBackup(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		final PrintWriter writer = response.getWriter();

		// Create path components to save the file
		final String path = request.getParameter("destination");

		Collection<Part> parts = request.getParts();

		Iterator<Part> iter = parts.iterator();

		OutputStream out = null;
		InputStream filecontent = null;

		while (iter.hasNext()) {
			final Part filePart = iter.next();
			final String fileName = getFileName(filePart);

			try {
				out = new FileOutputStream(new File(path + File.separator + fileName));
				filecontent = filePart.getInputStream();

				int read = 0;
				final byte[] bytes = new byte[1024];

				while ((read = filecontent.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}

			} catch (FileNotFoundException fne) {
				writer.println("You either did not specify a file to upload or are "
						+ "trying to upload a file to a protected or nonexistent " + "location.");
				writer.println("<br/> ERROR: " + fne.getMessage());
			}

			finally {
				if (out != null) {
					out.close();
				}
				if (filecontent != null) {
					filecontent.close();
				}
			}
		}

		try {

		} finally {
			writer.println("Files are created at " + path);

			if (writer != null) {
				writer.close();
			}
		}
	}

}
