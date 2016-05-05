/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.org.unesco.appesca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.org.unesco.appesca.data.FormularioRepository;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Identidade;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.model.Usuario;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class FormularioService {

    @Inject
    private Logger log;

    @Inject
    private  FormularioRepository formularioRepository;
   

    public Formulario findById(Integer id) {
    	return formularioRepository.findById(id);
    }
    
    public List<Formulario> findListByUsuario(Usuario usuario) {
    	return formularioRepository.findListByUsuario(usuario);
    }
    
    public Formulario findByIdSincronizacao(String idSincronizacao){
    	return formularioRepository.findByIdSincronizacao(idSincronizacao);
    }

    public List<Formulario> listAll() throws Exception {
    	
        return formularioRepository.listAll();
    }
    
    public List<Formulario> listByTipoFormulario(int tipoFormulario){
    	return formularioRepository.listByTipoFormulario(tipoFormulario);
    }
    
    public List<Formulario> listByEquipesCoordenador(int idCoordenadorEquipe, int tipoFormulario){
    	return formularioRepository.listByEquipesCoordenador(idCoordenadorEquipe, tipoFormulario);
    }
    
    public void save(Formulario usr){
    	formularioRepository.save(usr);
    }
    
    public void delete(Formulario formulario){
    	formularioRepository.delete(formulario);
    }
    
    public String getUF(Formulario formulario){
		if( getResposta(0, 3, 1, formulario) != null){
    		return "MA";
    	}else if(getResposta(0, 3, 2, formulario)!=null){
    		return "AP";
    	}else if(getResposta(0, 3, 3, formulario) != null){
    		return "PA";
    	}
		return "";
	}
    
    /**
     * 
	 *	Duas letras do Estado (AP, PA e MA)
	 *	Quatro dígitos (inicia em 0000)
	 *	Sigla do recurso pesqueiro (Exemplo.: CR – Camarão Regional)
     * @param formulario
     * @return
     */
    public String generateIdSincronizacao(Formulario formulario){
    	
    	String idSincronizacao = getUF(formulario);
    	
    	List<Formulario> lista = listByTipoFormulario(formulario.getIdTipoFormulario());
    	idSincronizacao += completaComZeros(lista.size());
    	
    	 switch (formulario.getIdTipoFormulario()){
	         case 1:
	        	 idSincronizacao += "CR"; //CAMARAO REGIONAL
	             break;
	         case 2:
	        	 idSincronizacao += "CN";//CARANGUEJO
	             break;
	         case 3:
	        	 idSincronizacao += "PB";
	             break;
    	 }
    	 System.out.println("IDSINCRONIZACAO->" + idSincronizacao);
    	return idSincronizacao;
    }
    
    private String completaComZeros(long numero){
    	if(numero < 10){
    		return "000"+numero;
    	}else if(numero < 100){
    		return "00" + numero;
    	}else if(numero < 1000){
    		return "000"+ numero;
    	}
    	
    	return ""+numero;
    }
    
    public Resposta getResposta(int ordemQuestao, int ordemPergunta, int ordemResposta, Formulario formulario){
    	Questao questao = getQuestaoPorOrdem(ordemQuestao, formulario);
    	Pergunta pergunta = getPerguntaPorOrdem(ordemPergunta, questao);
    	Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);
    	
    	return resposta;
    }
    
    public String getRespostaTexto(int ordemQuestao, int ordemPergunta, int ordemResposta, Formulario formulario){
    	Questao questao = getQuestaoPorOrdem(ordemQuestao, formulario);
    	Pergunta pergunta = getPerguntaPorOrdem(ordemPergunta, questao);
    	Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);
    	
    	if(resposta != null){
    		return resposta.getTexto();
    	}
    	
    	return "";
    }
    
    public boolean getRespostaBoolean(int ordemQuestao, int ordemPergunta, int ordemResposta, Formulario formulario){
    	Questao questao = getQuestaoPorOrdem(ordemQuestao, formulario);
    	Pergunta pergunta = getPerguntaPorOrdem(ordemPergunta, questao);
    	Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);
    	
    	return resposta!=null;
    }
    
    public Questao getQuestaoPorOrdem(int ordem, Formulario formulario){
    	if(formulario!=null && formulario.getListaQuestoes()!=null && formulario.getListaQuestoes().size() > 0){
    		for(Questao questao: formulario.getListaQuestoes()){
    			if(questao.getOrdem() == ordem){
    				return questao;
    			}
    		}
    	}
    	
    	return null;
    }
    
    public boolean perguntaTemResposta(int ordemQuestao, int ordemPergunta, Formulario formulario){
    	Questao questao = getQuestaoPorOrdem(ordemQuestao, formulario);
    	Pergunta pergunta = getPerguntaPorOrdem(ordemPergunta, questao);
    	
    	if(pergunta!=null && pergunta.getListaRespostas()!=null && !pergunta.getListaRespostas().isEmpty()){
    		return true;
    	}else{
    		return false;
    	}
    }

    public List<Pergunta> getListaPerguntas(int ordemQuestao,  Formulario formulario){
    	Questao questao = getQuestaoPorOrdem(ordemQuestao, formulario);
    	if(questao!=null){
	    	List<Pergunta> lst =questao.getListaPerguntas();
	    	
	    	if(lst!=null){
	    		return lst;
	    	}
    	}
    	return new ArrayList<>();
    	
    }

    public String getRespostaTexto(Pergunta pergunta, int ordemResposta){
    	Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);
    	
    	if(resposta != null){
    		return resposta.getTexto();
    	}
    	
    	return "";
    } 
    public boolean getRespostaBoolean(Pergunta pergunta, int ordemResposta){
    	Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);
    	
    	return resposta!=null;
    } 
    
    public Pergunta getPerguntaPorOrdem(int ordem, Questao questao){
    	if(questao!=null && questao.getListaPerguntas()!=null && questao.getListaPerguntas().size()>0){
    		for(Pergunta pergunta: questao.getListaPerguntas()){
    			if(pergunta.getOrdem()==ordem){
    				return pergunta;
    			}
    		}
    	}
    	
    	return null;
    }
    
    public Resposta getRespostaPorOrdem(int ordem, Pergunta pergunta){
    	if(pergunta!=null && pergunta.getListaRespostas()!=null && pergunta.getListaRespostas().size() > 0 ){
    		for(Resposta resposta: pergunta.getListaRespostas()){
    			if(resposta.getOrdem()== ordem){
    				return resposta;
    			}
    		}
    	}
    	
    	return null;
    }
    
    
    public int getOrdemUltimaQuestao(Formulario formulario){
    	if(formulario==null) return 0;

    	int ordemUltQ = 0;
    	
    	switch (formulario.getIdTipoFormulario()){
        case 1: //camarao
        	ordemUltQ = 73;
                break;
        case 2://caranguejo
        	ordemUltQ = 74;
                break;
        case 3://piticaia
        	ordemUltQ = 61;
                break;
		}
    	return ordemUltQ;
    }
    
    public boolean temDegravacao(Formulario formulario){
//    	Formulario formulario = formularioService.findById(new Integer(idFormulario));
		int ordemUltQ = getOrdemUltimaQuestao(formulario);
		Resposta resp = getResposta(ordemUltQ, 2, 1, formulario);
		
		if(resp!=null && resp.getAudio()!=null){
			return true;
		}else{
			return false;
		}
    }
}
