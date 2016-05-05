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

import br.org.unesco.appesca.data.EquipeRepository;
import br.org.unesco.appesca.enums.PerfilEnum;
import br.org.unesco.appesca.model.Equipe;
import br.org.unesco.appesca.model.Usuario;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class EquipeService {

    @Inject
    private Logger log;

    @Inject
    private EquipeRepository equipeRepository;


    public List<Equipe> listarPorPerfil(Usuario usuario) throws Exception{
    	if(usuario==null){
    		return null;
    	}
    	
    	if(usuario.getPerfil().getValor() == PerfilEnum.DEGRAVADOR.getValor() 
    			|| usuario.getPerfil().getValor() == PerfilEnum.PESQUISADOR.getValor()){
    		return null;
    	}else if(usuario.getPerfil().getValor() == PerfilEnum.COORDENADOR.getValor()){
    		List<Equipe> lst = listAll();
    		List<Equipe> listaRetorno = new ArrayList<>();
    		if(lst!=null && !lst.isEmpty()){
    			for(Equipe equipe: lst){
    				if(equipe.getCoordenador().getId().intValue() == usuario.getId().intValue()){
    					listaRetorno.add(equipe);
    				}
    			}
    		}
    		return listaRetorno;
    	}else if(usuario.getPerfil().getValor() == PerfilEnum.ADMINISTRADOR.getValor()){
    		return listAll();
    	}
    	return null;
    }
    
    public List<Equipe> listAll() throws Exception {
        return equipeRepository.listAll();
    }
    
    public void save(Equipe usr){
    	equipeRepository.save(usr);
    }
}
