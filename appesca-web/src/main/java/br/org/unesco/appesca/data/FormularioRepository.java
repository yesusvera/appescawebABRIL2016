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

import br.org.unesco.appesca.model.FormFiltroPesquisa;
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

    /**
     * @param idCoordenadorEquipe - o coordenador neste caso pode ser perfil
     * Coordenador ou Administrador.
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Formulario> listByEquipesCoordenador(int idCoordenadorEquipe, int tipoFormulario) {
        Query query = em.createQuery("SELECT form FROM Formulario form, Usuario usrForm, Equipe equipe "
                + " inner join equipe.listaMembrosEquipe as eqp "
                + " where eqp.coordenador.id = :idCoordenadorEquipe and form.idTipoFormulario = :tipoFormulario"
        );
        query.setParameter("idCoordenadorEquipe", idCoordenadorEquipe);
        query.setParameter("tipoFormulario", tipoFormulario);

        return (List<Formulario>) query.getResultList();
    }

    /**
     * Pesquisa por filtro
     *
     * @param filtro 1.Estado; --> 0,3,1 / 0,3,2 / 0,3,3 2.Município: --> 0,4,1
     * 3.Comunidade --> 0,5,1 4.Pesquisador; 5.Sexo; --> 0,8,1 e 0,8,2 6.Idade
     * do entrevistado; --> 0,7,1 7.Intervalo de Bloco e Questões; 8,Pescador
     * (bloco2, questão 1, opção a.); --> 16,1,1 9. Pescador(a) de
     * camarão/caranguejo (bloco2, questão 1, opção b) --> 16,1,2
     * @param tipoFormulario
     * @return
     */
    @SuppressWarnings("unchecked")

    public List<Formulario> listarPorFiltro(FormFiltroPesquisa filtro, int tipoFormulario) {
        String hqlCabecalho = "SELECT form FROM Formulario form ";

        String hqlInners = " inner join form.listaQuestoes as questao "
                + " inner join questao.listaPerguntas as pergunta "
                + " inner join pergunta.listaRespostas as resposta ";

        String hqlWhereCabecalho = " where form.idTipoFormulario = " + tipoFormulario;

        String hqlWhereCorpo = "";

        boolean precisaInners = false;
        if (filtro.getPesquisador() != null && !filtro.getPesquisador().isEmpty()) {
            hqlCabecalho = "SELECT form FROM Formulario form, Usuario usuario ";
            hqlWhereCorpo += " and usuario.id = form.idUsuario and usuario.nome LIKE '%" + filtro.getPesquisador() + "%' ";
        }
        
        if (filtro.getSituacao() != null && !filtro.getSituacao().isEmpty()) {
            hqlWhereCorpo += " and form.situacao = " + filtro.getSituacao();
        }


        String hqlWhereFiltros = " and ( ";
        if (filtro.getUf() != null && !filtro.getUf().isEmpty()) {
            precisaInners = true;

            hqlWhereFiltros += " ("
                    + "(questao.ordem = 0 and pergunta.ordem = 3 and resposta.ordem = 1 and resposta.texto = '" + filtro.getUf() + "') "
                    + " or (questao.ordem = 0 and pergunta.ordem = 3 and resposta.ordem = 2 and resposta.texto = '" + filtro.getUf() + "') "
                    + " or (questao.ordem = 0 and pergunta.ordem = 3 and resposta.ordem = 3  and resposta.texto = '" + filtro.getUf() + "')"
                    + ")";
        }

        if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty()) {
            if (precisaInners) {
                hqlWhereFiltros += " or ";
            } else {
                precisaInners = true;
            }

            hqlWhereFiltros += " (questao.ordem = 0 "
                    + "and pergunta.ordem = 4 "
                    + "and resposta.ordem = 1 and resposta.texto LIKE '%" + filtro.getMunicipio() + "%')";

        }

        if (filtro.getComunidade() != null && !filtro.getComunidade().isEmpty()) {
            hqlWhereFiltros += " (questao.ordem = 0 "
                    + "and pergunta.ordem = 5 "
                    + "and resposta.ordem = 1 and resposta.texto LIKE '%" + filtro.getComunidade() + "%')";
        }

        if (filtro.getSexo() != null && !filtro.getSexo().isEmpty()) {
             if (precisaInners) {
                hqlWhereFiltros += " or ";
            } else {
                precisaInners = true;
            }
            hqlWhereFiltros += "  ("
                    + "(questao.ordem = 0 and pergunta.ordem = 8 and resposta.ordem = 1 and resposta.texto LIKE '%" + filtro.getSexo() + "%') "
                    + " or (questao.ordem = 0 and pergunta.ordem = 8 and resposta.ordem = 2 and resposta.texto LIKE '%" + filtro.getSexo() + "%') "
                    + ")";
        }

        if (filtro.getIdadeEntrevistado() != null && !filtro.getIdadeEntrevistado().isEmpty()) {
             if (precisaInners) {
                hqlWhereFiltros += " or ";
            } else {
                precisaInners = true;
            }
            hqlWhereFiltros += " (questao.ordem = 0 "
                    + "and pergunta.ordem = 7 "
                    + "and resposta.ordem = 1 and resposta.texto LIKE '%" + filtro.getIdadeEntrevistado() + "%')";
        }

        if (filtro.isPescadorB2Q1Ra()) {
             if (precisaInners) {
                hqlWhereFiltros += " or ";
            } else {
                precisaInners = true;
            }
            hqlWhereFiltros += " (questao.ordem = 16 "
                    + "and pergunta.ordem = 1 "
                    + "and resposta.ordem = 1 and resposta.texto LIKE '%pescador%')";

        }
        if (filtro.isPescadorCamCarB2Rb()) {
             if (precisaInners) {
                hqlWhereFiltros += " or ";
            } else {
                precisaInners = true;
            }
            hqlWhereFiltros += " (questao.ordem = 16 "
                    + "and pergunta.ordem = 1 "
                    + "and resposta.ordem = 2 and resposta.texto LIKE '%pescador%')";

        }
        hqlWhereFiltros += ")";
//        hql += " and questao.ordem in (0, 16) and pergunta.ordem in (1,3,4,5,7,8) and resposta.ordem in (1,2,3,4) ";
//        hql += " and (1=0";
//
//        if (filtro.getComunidade() != null && !filtro.getComunidade().isEmpty()) {
//            hql += " or resposta.texto LIKE '%" + filtro.getComunidade() + "%'";
//        }
//
//        if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty()) {
//            hql += " or resposta.texto LIKE '%" + filtro.getMunicipio() + "%'";
//        }
//        if (filtro.getSexo() != null && !filtro.getSexo().isEmpty()) {
//            hql += " or resposta.texto LIKE '%" + filtro.getSexo() + "%'";
//        }
//      
//        if (filtro.getIdadeEntrevistado() != null && !filtro.getIdadeEntrevistado().isEmpty()) {
//            hql += " or resposta.texto LIKE '%" + filtro.getIdadeEntrevistado() + "%'";
//        }
//        hql += ")";

        if (!precisaInners) {
            hqlInners = "";
            hqlWhereFiltros = "";
        }

        String hqlCompleto = hqlCabecalho + hqlInners + hqlWhereCabecalho + hqlWhereCorpo + hqlWhereFiltros;

        Query query = em.createQuery(hqlCompleto);

        return (List<Formulario>) query.getResultList();
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
    public void save(Formulario formulario) {
//    	beginTransaction();
        if (formulario.getId() == null) {
            em.persist(formulario);
        } else {
            em.merge(formulario);
        }
//    	commitTransaction();
    }

    public void delete(Formulario formulario) {
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
    public List<Formulario> listAll() {
        Query query = em.createQuery("SELECT u FROM Formulario u");
        return (List<Formulario>) query.getResultList();
    }

}
