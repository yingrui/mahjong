package websiteschema.mpsegment.web.api.service;

import websiteschema.mpsegment.web.api.model.Concept;
import websiteschema.mpsegment.web.api.model.dto.ConceptDto;

import java.util.List;

public interface ConceptService {

    public void add(Concept concept);
    public Concept getById(int id);
    public List<Concept> list();
    public ConceptDto getConceptTree();
    public void remove(int id);
    public List<Concept> getChildren(int id);

    public Concept getByName(String name);
}
