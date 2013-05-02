package websiteschema.mpsegment.web.api.service;

import websiteschema.mpsegment.web.api.model.Concept;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: twer
 * Date: 5/2/13
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ConceptService {

    public void add(Concept concept);
    public Concept getById(int id);
    public List<Concept> list();
    public void remove(int id);
}
