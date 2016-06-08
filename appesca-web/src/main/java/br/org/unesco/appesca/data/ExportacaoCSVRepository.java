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

import br.org.unesco.appesca.model.ExportacaoCSV;

@ApplicationScoped
public class ExportacaoCSVRepository {

    @Inject
    private EntityManager em;

    public ExportacaoCSV findById(Integer id) {
        return em.find(ExportacaoCSV.class, id);
    }
  
    @Transactional
    public void save(ExportacaoCSV expCSV){
    	if(expCSV.getId()==null){
    		em.persist(expCSV);
    	}else{
    		em.merge(expCSV);
    	}
    }
    
    @SuppressWarnings("unchecked")
	public List<ExportacaoCSV> listAll(){
    	 Query query = em.createQuery("SELECT exp FROM ExportacaoCSV exp");
    	 return (List<ExportacaoCSV>) query.getResultList();
    }
 
    @SuppressWarnings("unchecked")
	public List<ExportacaoCSV> listByIdTipoFormulario(int idTipoFormulario){
    	 Query query = em.createQuery("SELECT exp FROM ExportacaoCSV exp where exp.idTipoFormulario = :idTipoFormulario");
    	 query.setParameter("idTipoFormulario", idTipoFormulario);
    	 return (List<ExportacaoCSV>) query.getResultList();
    }
    

    public ExportacaoCSV findByIdFormulario(Integer idFormulario) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExportacaoCSV> criteria = cb.createQuery(ExportacaoCSV.class);
        Root<ExportacaoCSV> exportacaoCSV = criteria.from(ExportacaoCSV.class);
        criteria.select(exportacaoCSV).where(
	        		cb.equal(exportacaoCSV.get("idFormulario"), idFormulario)
        );
        try{
        	return em.createQuery(criteria).getSingleResult();
        }catch(javax.persistence.NoResultException nr){
        	return null;
        }
    }
}
