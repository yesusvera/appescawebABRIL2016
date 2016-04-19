package br.org.unesco.appesca.rest;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.org.unesco.appesca.data.UsuarioRepository;
import br.org.unesco.appesca.model.Equipe;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.rest.model.RespAutenticacaoREST;

/**
 * 
 * @author yesus
 *
 */
@Path("/usuario")
@RequestScoped
public class UsuarioResourceRESTService extends BaseREST{
	@Inject
	private UsuarioRepository repository;

	@GET
	@Path("/autenticacao")
	@Produces(MediaType.APPLICATION_XML)
	public String authenticate(@QueryParam("login") String login, @QueryParam("senha") String senha) {
		Usuario usr = repository.findByLoginSenha(login, senha);

		if (usr != null) {
			usr.setImagem(null);
			
			if(usr.getListaEquipes()!=null || usr.getListaEquipes().size()>0){
				List<Equipe> listaEquipes = new ArrayList<Equipe>();
				
				for(Equipe e : usr.getListaEquipes()){
					
					if(e.getListaMembrosEquipe()!=null || e.getListaMembrosEquipe().size()>0){
						List<Usuario> listaMembros = new ArrayList<Usuario>();
						for(Usuario membro: e.getListaMembrosEquipe()){
							membro.setListaEquipes(null);
							membro.setListaLocalizacoes(null);
							membro.setImagem(null);
							listaMembros.add(membro);
						}
						e.setListaMembrosEquipe(listaMembros);
					}
					if(e.getId()!=null){
						e.getCoordenador().setListaEquipes(null);
						e.getCoordenador().setListaLocalizacoes(null);
						e.getCoordenador().setImagem(null);
						listaEquipes.add(e);
					}
				}
				usr.setListaEquipes(listaEquipes);
			}
			
			usr.setListaLocalizacoes(null);
		}

		RespAutenticacaoREST rs = new RespAutenticacaoREST(usr, usr == null,
				usr == null ? "Usuário não encontrado!" : "");

		return getXML(rs);
	}

	@GET
	@Path("/imagem")
	@Produces("image/png")
	public Response imageUser(@QueryParam("login") String login, @QueryParam("senha") String senha) {
		Usuario usr = repository.findByLoginSenha(login, senha);
		if (usr == null) {
			return null;
		}
		return Response.ok(new ByteArrayInputStream(usr.getImagem())).build();
	}
}
