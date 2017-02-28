package br.org.unesco.appesca.rest;

import java.util.ArrayList;
import java.util.HashMap;
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

//	@Inject
//	private LocalizacaoUsuarioRepository localizacaoUsuarioRepository;
	
	@Inject
	private UsuarioService usuarioService;


	@POST
	@Path("/inserir")
	@Produces(MediaType.APPLICATION_XML)
	public String inserir(@FormParam("xmlLocalizacao") String xmlLocalizacao, @FormParam("login") String login) {
		System.out.println("Inserindo as localizações");
		XStream xStream = new XStream(new DomDriver());

		HashMap<String, Usuario> mapaUsuarios = new HashMap<>();
		
		try {
			LocalizacaoREST localizacaoREST = (LocalizacaoREST) xStream.fromXML(xmlLocalizacao);
			if(localizacaoREST!=null){
				
				List<LocalizacaoUsuario> lista = localizacaoREST.getListaLocalizacaoUsuario();
				
				if(lista!=null){
					for(LocalizacaoUsuario loc: lista){
							loc.setId(null);
						
							if(loc.getUsuario()==null) continue;
							
							Usuario usuario = mapaUsuarios.get(loc.getUsuario().getLogin());
							
							if(usuario==null){
								usuario = usuarioService.findByLogin(loc.getUsuario().getLogin());
							}
						
							if(usuario!=null){
								mapaUsuarios.put(usuario.getLogin(), usuario);
								loc.setUsuario(usuario);
								
								List<LocalizacaoUsuario> listaBD =  usuario.getListaLocalizacoes();
								if(listaBD==null){
									listaBD = new ArrayList<>();
								}
								
								listaBD.add(loc);
								
								
//								usuario.setListaLocalizacoes(listaBD);
								
								mapaUsuarios.put(usuario.getLogin(), usuario);
							}
					}
					
					for(Usuario usr: mapaUsuarios.values()){
						usuarioService.save(usr);
					}
				}
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