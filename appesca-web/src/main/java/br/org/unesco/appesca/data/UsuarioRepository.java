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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import br.org.unesco.appesca.model.Usuario;

@ApplicationScoped
public class UsuarioRepository {

    @Inject
    private EntityManager em;

    public Usuario findById(Integer id) {
        return em.find(Usuario.class, id);
    }
  
    @Transactional
    public void save(Usuario usr){
    	if(usr.getId()==null){
    		em.persist(usr);
    	}else{
    		em.merge(usr);
    	}
    }
    
 
    
    @SuppressWarnings("unchecked")
	public List<Usuario> listAll(){
    	 Query query = em.createQuery("SELECT u FROM Usuario u");
    	 return (List<Usuario>) query.getResultList();
    }
    
    //Pode logar com o campo login ou email!
    public Usuario findByLoginSenha(String loginOuEmail, String senha) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
        Root<Usuario> usuario = criteria.from(Usuario.class);
        criteria.select(usuario).where(
        		cb.or(
	        		cb.and(cb.equal(usuario.get("login"), loginOuEmail),
	        				cb.equal(usuario.get("senha"), senha)), 
	        		cb.and(cb.equal(usuario.get("email"), loginOuEmail),
	        				cb.equal(usuario.get("senha"), senha))
    			)
        );
        try{
        	return em.createQuery(criteria).getSingleResult();
        }catch(javax.persistence.NoResultException nr){
        	return null;
        }
    }
    
 
    public Usuario findByLogin(String loginOuEmail) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = cb.createQuery(Usuario.class);
        Root<Usuario> usuario = criteria.from(Usuario.class);
        criteria.select(usuario).where(
        		cb.or(
	        		cb.equal(usuario.get("login"), loginOuEmail), 
	        		cb.equal(usuario.get("email"), loginOuEmail)
    			)
        );
        try{
        	return em.createQuery(criteria).getSingleResult();
        }catch(javax.persistence.NoResultException nr){
        	return null;
        }
    }

}
