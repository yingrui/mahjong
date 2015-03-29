package me.yingrui.segment.web.api.service;

import me.yingrui.segment.web.api.model.PartOfSpeech;

public interface PartOfSpeechRepository {

    public PartOfSpeech get(int id);

    public void save(PartOfSpeech pos);

}

