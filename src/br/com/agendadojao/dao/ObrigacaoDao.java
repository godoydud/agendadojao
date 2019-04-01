/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.agendadojao.dao;

import br.com.agendadojao.dao.exceptions.NonexistentEntityException;
import br.com.agendadojao.model.Obrigacao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author renato
 */
public class ObrigacaoDao implements Serializable {

    public ObrigacaoDao() {
        this.emf = Persistence.createEntityManagerFactory("AgendaPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Obrigacao obrigacao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(obrigacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Obrigacao obrigacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            obrigacao = em.merge(obrigacao);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = obrigacao.getId();
                if (findObrigacao(id) == null) {
                    throw new NonexistentEntityException("The obrigacao with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Obrigacao obrigacao;
            try {
                obrigacao = em.getReference(Obrigacao.class, id);
                obrigacao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The obrigacao with id " + id + " no longer exists.", enfe);
            }
            em.remove(obrigacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Obrigacao> findObrigacaoEntities() {
        return findObrigacaoEntities(true, -1, -1);
    }

    public List<Obrigacao> findObrigacaoEntities(int maxResults, int firstResult) {
        return findObrigacaoEntities(false, maxResults, firstResult);
    }

    private List<Obrigacao> findObrigacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Obrigacao.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Obrigacao findObrigacao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Obrigacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getObrigacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Obrigacao> rt = cq.from(Obrigacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
