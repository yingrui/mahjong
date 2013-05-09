package websiteschema.mpsegment.web.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.api.model.Concept;
import websiteschema.mpsegment.web.api.model.dto.ConceptDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Concept> c = cb.createQuery(Concept.class);
        Root<Concept> from = c.from(Concept.class);
        c.orderBy(cb.asc(from.get("id")));
        return em.createQuery(c).getResultList();
    }

    public ConceptDto getConceptTree() {
        return getConceptTreeRoot();
    }

    @Override
    @Transactional
    public void remove(int id) {
        Concept concept = getById(id);
        if (null != concept) {
            List<Concept> children = getChildren(concept.getId());
            if(null!= children&&!children.isEmpty()) {
                for(Concept child : children) {
                    remove(child.getId());
                }
            }
            em.remove(concept);
        }
    }

    public List<Concept> getChildren(int id) {
        return em.createQuery(
                "SELECT c From Concept c " +
                        "LEFT OUTER JOIN FETCH c.partOfSpeech partOfSpeech " +
                        "WHERE c.parent.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    public ConceptDto getConceptTreeRoot() {
        List<Concept> list = list();
        Map<String, ConceptDto> indexOfConcepts = new LinkedHashMap<String, ConceptDto>(list.size());
        for (Concept concept : list) {
            ConceptDto dto = concept.toDto();
            indexOfConcepts.put(concept.getName(), dto);
        }
        ConceptDto root = new ConceptDto();
        root.name = "root";
        root.children = new ArrayList<ConceptDto>();

        for(String conceptName : indexOfConcepts.keySet()) {
            ConceptDto dto = indexOfConcepts.get(conceptName);
            ConceptDto parent = indexOfConcepts.get(dto.parent);
            if(null != parent) {
                parent.children.add(dto);
            } else {
                root.children.add(dto);
            }
        }
        return root;
    }
}
