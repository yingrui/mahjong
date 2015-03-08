package websiteschema.mpsegment.web.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.mpsegment.web.api.model.PartOfSpeech;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PartOfSpeechRepositoryImpl implements PartOfSpeechRepository {

    @PersistenceContext
    EntityManager em;

    public PartOfSpeech get(int id) {
        return em.find(PartOfSpeech.class, id);
    }

    @Override
    @Transactional
    public void save(PartOfSpeech pos) {
        em.persist(pos);
    }
}
