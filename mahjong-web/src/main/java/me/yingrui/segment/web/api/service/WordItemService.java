package me.yingrui.segment.web.api.service;

import me.yingrui.segment.web.api.model.WordItem;
import me.yingrui.segment.web.api.model.dto.WordItemDto;
import org.hibernate.Session;

import java.util.List;

public interface WordItemService {

    public void add(WordItem wordItem);
    public void update(WordItemDto wordItem);
    public WordItem getById(int id);
    public Session session();
    public java.util.Iterator<WordItem> iterator(Session session);
    public void remove(int id);

    public List<WordItem> findAllByPinyin(String pinyin);

    List<WordItem> findAllByWordHead(String pinyin);
}

