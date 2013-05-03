package websiteschema.mpsegment.web.api.service;

import websiteschema.mpsegment.web.api.model.Concept;

import java.util.List;

public interface ConceptService {

    public void add(Concept concept);
    public Concept getById(int id);
    public List<Concept> list();
    public void remove(int id);
}
