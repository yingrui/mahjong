package me.yingrui.segment.web;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import me.yingrui.segment.web.api.model.PartOfSpeech;
import me.yingrui.segment.web.api.service.PartOfSpeechRepository;

public class UsingFixtures extends UsingTestUtils {

    @Autowired
    protected PartOfSpeechRepository partOfSpeechRepository;

    protected PartOfSpeech posN;
    protected PartOfSpeech posT;
    protected PartOfSpeech posV;
    protected PartOfSpeech posA;
    protected PartOfSpeech posUN;

    @Before
    public void initPartOfSpeech() {
        posN = addPartOfSpeech(1, "名词", "N");
        posT = addPartOfSpeech(2, "时间词", "T");
        posV = addPartOfSpeech(9, "动词", "V");
        posA = addPartOfSpeech(10, "形容词", "A");
        posUN = addPartOfSpeech(44, "未登录词", "UN");
    }

    private PartOfSpeech addPartOfSpeech(int id, String note, String pos) {
        PartOfSpeech partOfSpeech = partOfSpeechRepository.get(id);
        if(partOfSpeech != null) return partOfSpeech;

        partOfSpeech = new PartOfSpeech();
        partOfSpeech.setId(id);
        partOfSpeech.setNote(note);
        partOfSpeech.setName(pos);
        partOfSpeechRepository.save(partOfSpeech);
        return partOfSpeech;
    }
}
