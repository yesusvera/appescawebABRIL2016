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

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Status;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Usuario;

@ApplicationScoped
public class FormularioRepository {

    @Inject
    private EntityManager em;
    
    @Resource
    private UserTransaction ut;

    public Formulario findById(Integer id) {
        return em.find(Formulario.class, id);
    }
    

    public Formulario findByIdSincronizacao(String idSincronizacao) {
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Formulario> criteria = cb.createQuery(Formulario.class);
        Root<Formulario> formulario = criteria.from(Formulario.class);
        criteria.select(formulario).where(cb.equal(formulario.get("idSincronizacao"), idSincronizacao));
        
        return em.createQuery(criteria).getSingleResult();
    }
    
    public List<Formulario> listByTipoFormulario(int tipoFormulario) {
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Formulario> criteria = cb.createQuery(Formulario.class);
        Root<Formulario> formulario = criteria.from(Formulario.class);
        criteria.select(formulario).where(cb.equal(formulario.get("idTipoFormulario"), tipoFormulario));
        return em.createQuery(criteria).getResultList();
    }
    
    
    public List<Formulario> findListByUsuario(Usuario usuario) {
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Formulario> criteria = cb.createQuery(Formulario.class);
        Root<Formulario> formulario = criteria.from(Formulario.class);
        criteria.select(formulario).where(cb.equal(formulario.get("idUsuario"), usuario.getId()));
        return em.createQuery(criteria).getResultList();
        
    }
    
    @Transactional
    public void save(Formulario formulario){
//    	beginTransaction();
    	if(formulario.getId()==null){
    		em.persist(formulario);
    	}else{
    		em.merge(formulario);
    	}
//    	commitTransaction();
    }
    
    public void delete(Formulario formulario){
    	em.remove(em.contains(formulario) ? formulario : em.merge(formulario));
    }
    
    protected void beginTransaction() {
        try {
            if (ut.getStatus() != Status.STATUS_ACTIVE) {
                ut.begin();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected void commitTransaction() {
        try {
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                ut.commit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void rollbackTransaction() {
        try {
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                ut.rollback();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    @SuppressWarnings("unchecked")
	public List<Formulario> listAll(){
    	 Query query = em.createQuery("SELECT u FROM Formulario u");
    	 return (List<Formulario>) query.getResultList();
    }

}
