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
import br.org.unesco.appesca.model.LocalizacaoUsuario;
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
	
	@GET
	@Path("/csvGeoreferenciamentoTodos")
	@Produces("text/csv")
	public Response exportarGeoCSVAll() {
		
		List<Usuario> listaUsuarios = repository.listAll();
		
		String conteudoCSV = "DATAHORA;LAT;LONG;USUARIO;TIPOCAPTURA";
		
		for(Usuario usr: listaUsuarios){
			for(LocalizacaoUsuario loc : usr.getListaLocalizacoes()){
				conteudoCSV = concatLinhaCsv(conteudoCSV, usr, loc);
			}
		}
		
		return Response.ok(conteudoCSV).header("Content-Disposition", "attachment; filename=geoReferenciamentoTodosUsuarios.csv").build();
	}

	private String concatLinhaCsv(String conteudoCSV, Usuario usr, LocalizacaoUsuario loc) {
		String lat = (loc.getLatitude()!=null?loc.getLatitude().doubleValue():"") + "";
		String longit = (loc.getLongitude()!=null?loc.getLongitude().doubleValue():"")+"";

		lat = lat.replace(".", ",");
		longit = longit.replace(".", ",");
		
		conteudoCSV = conteudoCSV.concat("\n"+loc.getData() + " " + loc.getHora())
			.concat(";")
			.concat(lat)
			.concat(";")
			.concat(longit)
			.concat(";")
			.concat(usr.getNome()+"")
			.concat(";")
			.concat(loc.getProvided()!=null?loc.getProvided():""+"");
		return conteudoCSV;
	}
	
	@GET
	@Path("/csvExportacaoPorUsuario")
	@Produces("text/csv")
	public Response exportarGeoCSVAll(@QueryParam("id") String id) {
		
		Usuario usuario = repository.findById(new Integer(id));
		
		String conteudoCSV = "DATAHORA;LAT;LONG;USUARIO;TIPOCAPTURA";
		
		for(LocalizacaoUsuario loc : usuario.getListaLocalizacoes()){
			conteudoCSV = concatLinhaCsv(conteudoCSV, usuario, loc);
		}
		
		return Response.ok(conteudoCSV).header("Content-Disposition", "attachment; filename=geoReferenciamentoPorUsuario.csv").build();
	}
}
