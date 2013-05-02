package websiteschema.mpsegment.web.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.api.model.Concept;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class ConceptServiceImpl implements ConceptService {

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public void add(Concept concept) {
        em.persist(concept);
    }

    @Override
    public Concept getById(int id) {
        return em.find(Concept.class, id);
    }

    @Override
    public List<Concept> list() {
        CriteriaQuery<Concept> c = em.getCriteriaBuilder().createQuery(Concept.class);
        c.from(Concept.class);
        return em.createQuery(c).getResultList();
    }

    @Override
    public void remove(int id) {
        Concept concept = getById(id);
        if (null != concept) {
            em.remove(concept);
        }
    }
}
