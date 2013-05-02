package websiteschema.mpsegment.web.api.service;

import websiteschema.mpsegment.web.api.model.WordItem;

import java.util.List;

public interface WordItemService {

    public void add(WordItem wordItem);
    public WordItem getById(int id);
    public List<WordItem> list();
    public void remove(int id);
}

