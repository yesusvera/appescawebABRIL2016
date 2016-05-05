package br.org.unesco.appesca.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.org.unesco.appesca.model.Identidade;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.service.LoginService;

@Model
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = 7096314126107579474L;

	@Inject
    private FacesContext facesContext;

    @Inject
    private LoginService loginService;
    
    @Inject FormularioController formularioController;

    @Produces
    @Named
    private Usuario usuario;
    
    @Inject Identidade identidade;

    @PostConstruct
    public void inicializaNovoUsuario() {
    	usuario = new Usuario();
    }

    public String logout() throws Exception{
    	 ((HttpSession)facesContext.getExternalContext().getSession(false)).invalidate();
    	 return "listaFormularios?faces-redirect=true";
    }
    
    public String login() throws Exception {
        try {

        	Usuario usrTmp = loginService.autenticar(usuario.getLogin(), usuario.getSenha());
        	
        	if(usrTmp!=null){
        		identidade.setUsuarioLogado(usrTmp);
        		
        		FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Autenticado", "Seja bem vindo.");
	            facesContext.addMessage(null, m);
	            
	            
	            formularioController.listarFormularios(1);
	            return "/admin/index?faces-redirect=true";
        	}else{
        		FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos", "Usuário ou senha inválidos.");
 	            facesContext.addMessage(null, m);
        	}
        	
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro ao fazer o login.");
            facesContext.addMessage(null, m);
        }
        
        inicializaNovoUsuario();
        
        return "";
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Erro ao efetuar login.";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
