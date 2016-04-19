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

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.org.unesco.appesca.model.Equipe;

@ApplicationScoped
public class EquipeRepository {

    @Inject
//    @PersistenceContext(type=PersistenceContextType.TRANSACTION)
    private EntityManager em;

    public Equipe findById(Long id) {
        return em.find(Equipe.class, id);
    }
    
    
    public void save(Equipe equipe){
//    	if(equipe.getId()==null){
//    		em.persist(equipe);
//    	}else{
    		em.merge(equipe);
//    	}
    }
    
    @SuppressWarnings("unchecked")
	public List<Equipe> listAll(){
    	 Query query = em.createQuery("SELECT e FROM Equipe e");
    	 return (List<Equipe>) query.getResultList();
    }
    

}
