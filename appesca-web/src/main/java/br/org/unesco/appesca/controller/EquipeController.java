package br.org.unesco.appesca.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.DragDropEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.org.unesco.appesca.model.Equipe;
import br.org.unesco.appesca.model.Identidade;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.service.EquipeService;
import br.org.unesco.appesca.service.UsuarioService;

@Model
@SessionScoped
public class EquipeController implements Serializable {
	private static final long serialVersionUID = 7096314126107579474L;

	@Inject
    private FacesContext facesContext;

    @Inject
    private EquipeService equipeService;
    
    @Inject
    private UsuarioService usuarioService;

    @Produces
    @Named
    private List<Equipe> listaEquipes;
    
    private Equipe equipe = new Equipe();
    
    @Inject Identidade identidade;
    
    private List<Usuario> listaUsuarios;
    
    private List<Usuario> usuariosEscolhidos;
     
    private Usuario usuarioSelecionado;
    
    private String ufFiltro;
    

    @PostConstruct
    public void inicializaNovaEquipe() {
    	try {
			listaEquipes = equipeService.listAll();
			usuariosEscolhidos = new ArrayList<>();
			equipe = new Equipe();
			pegaUFCoordenador();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void pegaUFCoordenador() {
		if(identidade.getUsuarioLogado()!=null && identidade.getUsuarioLogado().getUf()!=null){
			ufFiltro = identidade.getUsuarioLogado().getUf();
		}
	}
    
    public void onUsuarioDrop(DragDropEvent ddEvent) {
        Usuario usr = ((Usuario) ddEvent.getData());
  
        usuariosEscolhidos.add(usr);
        listaUsuarios.remove(usr);
    }
    
    public void removerSelecionado(Usuario usrTemp) {
  
        usuariosEscolhidos.remove(usrTemp);
        listaUsuarios.add(usrTemp);
    }

    public String salvar() throws Exception{
    	equipe.setListaMembrosEquipe(usuariosEscolhidos);
    	equipe.setCoordenador(identidade.getUsuarioLogado());
    	
    	equipeService.save(equipe);
    	
    	usuariosEscolhidos = new ArrayList<>();
    	listaUsuarios = new ArrayList<>();
    	listaEquipes = equipeService.listAll();
    	return "equipes";
    }
    
    public StreamedContent getDynamicImage() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("image_id");
		if (id != null && listaUsuarios!=null && !listaUsuarios.isEmpty()) {
			for (Usuario usrTmp : listaUsuarios) {
				if (usrTmp.getId().intValue() == Integer.valueOf(id)) {
					return usrTmp.getImageGraphics();
				}
			}
		}
		id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("image_selected_id");
		if (id != null && listaUsuarios!=null && !listaUsuarios.isEmpty()) {
			for (Usuario usrTmp : listaUsuarios) {
				if (usrTmp.getId().intValue() == Integer.valueOf(id)) {
					return usrTmp.getImageGraphics();
				}
			}
		}
		 return new DefaultStreamedContent();
	 }

    public String cadastrarEquipe(){
    	equipe = new Equipe();
    	equipe.setData(new Date());
    	
    	try {
			listaUsuarios = usuarioService.listAll();
			usuariosEscolhidos = new ArrayList<>();
			pegaUFCoordenador();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "cadastrarEquipe";
    }
    
    public String editar(Equipe equipe){
    	this.equipe = equipe;
    	try {
			listaUsuarios = usuarioService.listAll();
			usuariosEscolhidos =equipe.getListaMembrosEquipe();
			pegaUFCoordenador();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "cadastrarEquipe";
    }
    
	public List<Equipe> getListaEquipes() {
		return listaEquipes;
	}

	public void setListaEquipes(List<Equipe> listaEquipes) {
		this.listaEquipes = listaEquipes;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public EquipeService getEquipeService() {
		return equipeService;
	}

	public void setEquipeService(EquipeService equipeService) {
		this.equipeService = equipeService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public List<Usuario> getListaUsuarios() {
		//FILTRANDO POR UF.
		if(listaUsuarios!=null && ufFiltro!=null){
			List<Usuario> listaTmp = new ArrayList<>();
			for(Usuario usrTmp : listaUsuarios){
				if(usrTmp.getUf().equalsIgnoreCase(ufFiltro)){
					listaTmp.add(usrTmp);
				}
			}
			return listaTmp;
		}
		
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Usuario> getUsuariosEscolhidos() {
		return usuariosEscolhidos;
	}

	public void setUsuariosEscolhidos(List<Usuario> usuariosEscolhidos) {
		this.usuariosEscolhidos = usuariosEscolhidos;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public String getUfFiltro() {
		return ufFiltro;
	}

	public void setUfFiltro(String ufFiltro) {
		this.ufFiltro = ufFiltro;
	}
}
