package br.org.unesco.appesca.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.org.unesco.appesca.data.LocalizacaoUsuarioRepository;
import br.org.unesco.appesca.model.LocalizacaoUsuario;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.rest.model.LocalizacaoREST;
import br.org.unesco.appesca.rest.model.RespEnvioLocalizacao;
import br.org.unesco.appesca.service.UsuarioService;

/**
 * @author yesus
 */
@Path("/localizacao")
@RequestScoped
public class LocalizacaoResourceREST extends BaseREST {

	@Inject
	private LocalizacaoUsuarioRepository localizacaoUsuarioRepository;
	
	@Inject
	private UsuarioService usuarioService;


	@POST
	@Path("/inserir")
	@Produces(MediaType.APPLICATION_XML)
	public String inserir(@FormParam("xmlLocalizacao") String xmlLocalizacao, @FormParam("login") String login) {
		
		Usuario usuario = usuarioService.findByLogin(login);
		
		System.out.println("Inserindo as localizações");
		XStream xStream = new XStream(new DomDriver());

		try {
			LocalizacaoREST localizacaoREST = (LocalizacaoREST) xStream.fromXML(xmlLocalizacao);
			if(localizacaoREST!=null){
				List<LocalizacaoUsuario> lista = localizacaoREST.getListaLocalizacaoUsuario();
				
				if(lista!=null){
					for(LocalizacaoUsuario loc: lista){
							loc.setUsuario(usuario);
//							localizacaoUsuarioRepository.save(loc);
					}
				}
				
				List<LocalizacaoUsuario> listaBD =  usuario.getListaLocalizacoes();
				if(listaBD==null){
					listaBD = lista;
				}else{
					listaBD.addAll(lista);
				}
				
//				usuario.setListaLocalizacoes(listaBD);
				
				usuarioService.save(usuario);
			}
			
			

			RespEnvioLocalizacao rs = new RespEnvioLocalizacao();
			rs.setErro(false);
			rs.setMensagemErro("Localizações recebidas com sucesso!");
			return getXML(rs);
		} catch (Exception e) {
			e.printStackTrace();
			
			RespEnvioLocalizacao rs = new RespEnvioLocalizacao();
			rs.setErro(true);
			rs.setMensagemErro("Aconteceu alguma falha ao receber as localizações.");
			return getXML(rs);
		}
	}


}