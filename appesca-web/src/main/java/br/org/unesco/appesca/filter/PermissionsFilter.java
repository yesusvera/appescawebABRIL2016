package br.org.unesco.appesca.filter;

import java.io.IOException;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.org.unesco.appesca.model.Identidade;

@Model
@WebFilter(urlPatterns = { "/admin/*" }, description = "Session Checker Filter")
public class PermissionsFilter implements Filter {

	@Inject
	Identidade identidade;
	
//	@Inject
//    private FacesContext facesContext;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (identidade == null || identidade.getUsuarioLogado() == null){
//			request.getSession().invalidate();
            
			response.sendRedirect(request.getContextPath() + "/login.jsf?faces-redirect=true");
		}else{
			chain.doFilter(req, res);
		}
	}

	@Override
	public void destroy() {
//		config.getServletContext().log("Destroying SessionCheckerFilter");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}
