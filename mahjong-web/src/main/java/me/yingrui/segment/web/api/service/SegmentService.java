package me.yingrui.segment.web.api.service;

import org.springframework.stereotype.Service;
import me.yingrui.segment.core.SegmentResult;
import me.yingrui.segment.core.SegmentWorker;
import me.yingrui.segment.core.SegmentWorkerBuilder;
import me.yingrui.segment.core.Word;
import me.yingrui.segment.web.api.model.dto.WordDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SegmentService {

    public List<WordDto> segment(String sentence, Map<String, String> params) {
        SegmentWorker worker = SegmentWorkerBuilder.build(params);
        SegmentResult result = worker.segment(sentence);
        List<WordDto> words = new ArrayList<WordDto>(result.length());
        for(Word word : result.getWords()) {
            words.add(new WordDto(word));
        }
        return words;
    }
}
