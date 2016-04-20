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
package br.org.unesco.appesca.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.unesco.appesca.model.LocalizacaoUsuario;

@ApplicationScoped
public class LocalizacaoUsuarioRepository {

    @Inject
    private EntityManager em;

    public LocalizacaoUsuario findById(Integer id) {
        return em.find(LocalizacaoUsuario.class, id);
    }
    
    @Transactional
    public void save(LocalizacaoUsuario localizacao){
    	if(localizacao.getId()==null){
    		em.persist(localizacao);
    	}else{
    		em.merge(localizacao);
    	}
    }
}
