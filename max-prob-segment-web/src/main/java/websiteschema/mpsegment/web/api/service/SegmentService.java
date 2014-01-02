package websiteschema.mpsegment.web.api.service;

import org.springframework.stereotype.Service;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.core.WordAtom;
import websiteschema.mpsegment.web.api.model.dto.WordDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SegmentService {

    public List<WordDto> segment(String sentence, Map<String, String> params) {
        SegmentWorker worker = SegmentWorker.apply(params);
        SegmentResult result = worker.segment(sentence);
        List<WordDto> words = new ArrayList<WordDto>(result.length());
        for(WordAtom wordAtom : result.getWordAtoms()) {
            words.add(new WordDto(wordAtom));
        }
        return words;
    }
}
