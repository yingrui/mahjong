package websiteschema.mpsegment.web.api.service;

import websiteschema.mpsegment.web.api.model.WordItem;
import websiteschema.mpsegment.web.api.model.dto.WordItemDto;

import java.util.List;

public interface WordItemService {

    public void add(WordItem wordItem);
    public void update(WordItemDto wordItem);
    public WordItem getById(int id);
    public List<WordItem> list();
    public void remove(int id);

    public List<WordItem> findAllByPinyin(String pinyin);

    List<WordItem> findAllByWordHead(String pinyin);
}

