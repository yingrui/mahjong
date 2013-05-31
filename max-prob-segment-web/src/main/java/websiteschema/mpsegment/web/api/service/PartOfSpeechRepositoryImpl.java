package websiteschema.mpsegment.web.api.service;

import org.springframework.stereotype.Service;
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
}
