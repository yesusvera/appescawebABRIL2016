package br.org.unesco.appesca.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Identidade;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.model.exportacao.FormExportacao;
import br.org.unesco.appesca.model.exportacao.Perg;
import br.org.unesco.appesca.model.exportacao.Q;
import br.org.unesco.appesca.model.exportacao.Resp;
import br.org.unesco.appesca.service.FormularioService;
import br.org.unesco.appesca.service.RespostaService;
import br.org.unesco.appesca.service.UsuarioService;

@Model
@SessionScoped
public class FormularioController implements Serializable {
	private static final long serialVersionUID = 7096314126107579474L;

	@Inject
	private FormularioService formularioService;

	@Inject
	private RespostaService respostaService;

	@Inject
	private UsuarioService usuarioService;

	@Produces
	@Named
	private List<Formulario> listaFormularios;

	private Formulario formulario;
	
	private String titulo;
	
	private String htmlExportacao;

	@Inject
	Identidade identidade;

	private Resposta problemasF1Q72;
	private Resposta solucoesF1Q72;
	private Resposta problemasF2Q60;
	private Resposta solucoesF2Q60;
	private Resposta problemasF3Q74;
	private Resposta solucoesF3Q74;
	private int tipoFormulario;
	
	public String listarFormularios(long tipoFormulario){
		this.tipoFormulario = (int) tipoFormulario;
		listaFormularios = formularioService.listByTipoFormulario((int)tipoFormulario);
		switch (this.tipoFormulario){
	        case 1: 
	                setTitulo("Formulário Camarão Regional");
	                break;
	        case 2:
	        	setTitulo("Formulário Caranguejo");
	                break;
	        case 3:
	        	setTitulo("Formulário Camarão Piticaia e Branco");
	                break;
		}
		return "listaFormularios?faces-redirect=true";
	}
	
	public String getSituacao(Formulario form){
		String ret = "";
		if(form!=null){
			switch (form.getSituacao()){
	        case -1: 
	        	ret = "Devolvido";
	            break;
	        case 1:
	        	ret= "Pendente de Aprovação";
	            break;
	        case 2:	
	        	ret="Finalizado";
	            break;
			}
		}
		return ret;
	}
	
	public String exportar(){
		FormExportacao formExp = new FormExportacao();
		
		for(Formulario form: listaFormularios){
			if(form.getListaQuestoes()!=null){
	            for(Questao questao: form.getListaQuestoes()){
	            	
	            	formExp.getQuestao(questao.getOrdem()).increment();
	            	
	                if(questao.getListaPerguntas()!=null){
	                    for(Pergunta pergunta: questao.getListaPerguntas()){
	                    	formExp.getQuestao(questao.getOrdem()).getPergunta(pergunta.getOrdem()).increment();
	                    	
	                        if(pergunta.getListaRespostas()!=null){
	                            for(Resposta resposta: pergunta.getListaRespostas()){
	                            	formExp.getQuestao(questao.getOrdem()).
	                            			getPergunta(pergunta.getOrdem()).
	                            				getResposta(resposta.getOrdem()).increment();
	                            }
	                        }
	                    }
	                }
	            }
		    }
		}
		System.out.println(formExp);
		
		htmlExportacao = "<table border='1' style='border: 1px solid gray' width='100%'>";
		
		for(Q q: formExp.getMapQuestoes().values()){
			String labelQuestao = "Questão "+ q.getOrdem();
			switch (q.getOrdem()) {
			case -1:
				labelQuestao = "Localização da residência";
				break;
			case 0:
				labelQuestao = "Identificação do entrevistado";
				break;
			default:
				break;
			}
			htmlExportacao += "<tr style='font-size:18px;color: #fff; background: #2d86c1;'><td colspan='2' align='center'><b>"+labelQuestao+"</b></td></tr>";
			
			for(Perg perg: q.getMapPerg().values()){
				htmlExportacao += "<tr style='color: #fff; background: #04B45F;'><td colspan='2' >Pergunta "+perg.getOrdem()+"</td></tr>";
				htmlExportacao += "<tr style='color: #fff; background: gray;'><td><i>Ordem<i/></td><td><i>Quantidade</i></td></tr>";
				for(Resp resp: perg.getMapRespostas().values()){
					htmlExportacao += "<tr><td>Resposta "+resp.getOrdem()+"</td><td>"+resp.getQuantidade()+"</td></tr>";
				}
			}
			htmlExportacao += "</tr>";
		}
	
				
		htmlExportacao += "</table>"; 
		return "formExportacaoPiticaiaEBranco?faces-redirect=true";
	}
	
	public String aprovar() {
		try {
			formulario.setSituacao(2);
			formularioService.save(formulario);
			addMessage("Formulário aprovado e finalizado com sucesso.");
		} catch (Exception e) {
			addMessage("Aconteceu um problema.");
		}
		
		return "";
	}
	
	public String devolver() {
		try {
			formulario.setSituacao(-1);
			formularioService.save(formulario);
			addMessage("Formulário devolvido para correção");
		} catch (Exception e) {
			addMessage("Aconteceu um problema.");
		}
		
		return "";
	}
	
	public String getTitulo() {
		return titulo;
	}



	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}



	public String visualizar(Formulario formulario) {
		this.formulario = formulario;

		switch (formulario.getIdTipoFormulario()) {
		case 1:
			carregarRespostasEditaveisRegional();
			return "formCamaraoRegional?faces-redirect=true";
		
		case 2:
			carregarRespostasEditaveisCaranguejo();
			return "formCaranguejo?faces-redirect=true";
		case 3:
			carregarRespostasEditaveisBranco();
			return "formCamaraoPiticaiaEBranco?faces-redirect=true";
		}
		
		return "";

	}
	
	public Usuario getUsuario(Formulario formulario){
		Usuario usuario = usuarioService.findById(formulario.getIdUsuario());
		return usuario;
	}
	
	public String getUF(Formulario formulario){
		return formularioService.getUF(formulario);
	}

	public String getRespTxt(int ordemQuestao, int ordemPergunta, int ordemResposta){
		return formularioService.getRespostaTexto(ordemQuestao, ordemPergunta, ordemResposta, formulario);
	}
	
	public String getRespTxt(int ordemQuestao, int ordemPergunta, int ordemResposta, Formulario form){
		return formularioService.getRespostaTexto(ordemQuestao, ordemPergunta, ordemResposta, form);
	}
	

	public String getRespTxt(Pergunta pergunta, int ordemResposta) {
		return formularioService.getRespostaTexto(pergunta, ordemResposta);
	}
	
	public boolean getRespBool(int ordemQuestao, int ordemPergunta, int ordemResposta){
		return formularioService.getRespostaBoolean(ordemQuestao, ordemPergunta, ordemResposta, formulario);
	}
	
	public boolean getRespBool(int ordemQuestao, int ordemPergunta, int ordemResposta, Formulario form){
		return formularioService.getRespostaBoolean(ordemQuestao, ordemPergunta, ordemResposta, form);
	}
	

	public boolean getRespBool(Pergunta pergunta, int ordemResposta) {
		return formularioService.getRespostaBoolean(pergunta, ordemResposta);
	}
	 
	public List<Pergunta> getListaPerguntas(int ordemQuestao){
		List<Pergunta> lst = formularioService.getListaPerguntas(ordemQuestao, formulario);
		if(lst!=null){
			return lst;
		}else{
			return new ArrayList<>();
		}
	}
	
	 public boolean perguntaTemResposta(int ordemQuestao, int ordemPergunta){
		 return formularioService.perguntaTemResposta(ordemQuestao, ordemPergunta, formulario);
	 }

	public List<Pergunta> getListaPerguntas(int ordemQuestao, int minOrdem, int maxOrdem){
		
	  	List<Pergunta> lst =  formularioService.getListaPerguntas(ordemQuestao, formulario);
	  	if(lst!=null && lst.size()>0){
	  		List<Pergunta> lstReturn = new ArrayList<>();
	  		for(Pergunta p: lst){
	  			if(p.getOrdem()>=minOrdem && p.getOrdem()<=maxOrdem){
	  				lstReturn.add(p);
	  			}
	  		}
	  		return lstReturn;
	  	}else{
			return new ArrayList<>();
		}
	}

	private void carregarRespostasEditaveisRegional() {
		this.problemasF1Q72 = getResposta("q72_p1_r1");
		this.solucoesF1Q72 = getResposta("q72_p1_r2");

	}

	private void carregarRespostasEditaveisBranco() {
		this.problemasF2Q60 = getResposta("q60_p1_r1");
		this.solucoesF2Q60 = getResposta("q60_p1_r2");
	}

	private void carregarRespostasEditaveisCaranguejo() {
		this.problemasF3Q74 = getResposta("q74_p1_r1");
		this.solucoesF3Q74 = getResposta("q74_p1_r2");
	}

	public List<Formulario> getListaFormularios() {
		return listaFormularios;
	}

	public void setListaFormularios(List<Formulario> listaEntrevistado) {
		this.listaFormularios = listaEntrevistado;
	}

	public FormularioService getFormularioService() {
		return formularioService;
	}

	public void setFormularioService(FormularioService equipeService) {
		this.formularioService = equipeService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public Resposta getResposta(String chave) {
		try {
			String indices[] = chave.split("_");
			String q = indices[0].substring(1);
			String p = indices[1].substring(1);
			String r = indices[2].substring(1);

			Resposta resp = formularioService.getResposta(new Integer(q), new Integer(p), new Integer(r), formulario);
			
			if(resp!=null) return resp;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Resposta();
	}

	public List<Resposta> getListaRespostas(String chave) {
		try {
			String indices[] = chave.split("_");
			String q = indices[0].substring(1);
			String p = indices[1].substring(1);

			for (Questao questao : formulario.getListaQuestoes()) {
				if (q.equals(questao.getOrdem().toString())) {
					for (Pergunta pergunta : questao.getListaPerguntas()) {
						if (p.equals(pergunta.getOrdem().toString())) {
							return pergunta.getListaRespostas();
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Resposta>();
	}

	public void salvarResposta(Resposta resp) {
		try {
			//respostaService.save(resp);
			addMessage("Alteração realizada com sucesso!!");
		} catch (Exception e) {
			addMessage("Erro ao salvar a resposta!!");
		}

	}
	
	public String getHtmlExportacao() {
		return htmlExportacao;
	}

	public void setHtmlExportacao(String htmlExportacao) {
		this.htmlExportacao = htmlExportacao;
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Resposta getProblemasF1Q72() {
		return problemasF1Q72;
	}

	public void setProblemasF1Q72(Resposta problemasF1Q72) {
		this.problemasF1Q72 = problemasF1Q72;
	}

	public Resposta getSolucoesF1Q72() {
		return solucoesF1Q72;
	}

	public void setSolucoesF1Q72(Resposta solucoesF1Q72) {
		this.solucoesF1Q72 = solucoesF1Q72;
	}

	public Resposta getProblemasF2Q60() {
		return problemasF2Q60;
	}

	public void setProblemasF2Q60(Resposta problemasF2Q60) {
		this.problemasF2Q60 = problemasF2Q60;
	}

	public Resposta getSolucoesF2Q60() {
		return solucoesF2Q60;
	}

	public void setSolucoesF2Q60(Resposta solucoesF2Q60) {
		this.solucoesF2Q60 = solucoesF2Q60;
	}

	public Resposta getProblemasF3Q74() {
		return problemasF3Q74;
	}

	public void setProblemasF3Q74(Resposta problemasF3Q74) {
		this.problemasF3Q74 = problemasF3Q74;
	}

	public Resposta getSolucoesF3Q74() {
		return solucoesF3Q74;
	}

	public void setSolucoesF3Q74(Resposta solucoesF3Q74) {
		this.solucoesF3Q74 = solucoesF3Q74;
	}
	
	public MapModel getMap(Formulario form) {
		MapModel map = new DefaultMapModel();
		
		if(form.getLatitude()!=null && form.getLongitude()!=null){
			LatLng coord = new LatLng(form.getLatitude().doubleValue(), form.getLongitude().doubleValue());
			map.addOverlay(new Marker(coord, "Local do formulario", "konyaalti.png", "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
		}
		return map;
	}

	
	public StreamedContent getStream() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the media. Return a real StreamedContent with the media bytes.
//            String id = context.getExternalContext().getRequestParameterMap().get("id");
//            Media media = service.find(Long.valueOf(id));
            return new DefaultStreamedContent(new ByteArrayInputStream(null));
        }
    }

	public int getQtdeMax(Formulario formulario){
		
		if(formulario==null) return 0;

		int retorno = 0;
		
		switch (this.tipoFormulario){
        case 1: //camarao
        	retorno = 75;
                break;
        case 2://caranguejo
        	retorno = 76;
                break;
        case 3://piticaia
        	retorno = 63;
                break;
		}
		
		return retorno;
	}
}
