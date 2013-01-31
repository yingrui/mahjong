package websiteschema.mpsegment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.core.WordAtom;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/segment")
public class SegmentController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<WordDto> get(HttpServletRequest request) throws UnsupportedEncodingException {
        RequestHelper requestHelper = new RequestHelper(request);
        Map<String,String> params = requestHelper.getParams();
        String sentence = params.get("sentence");
        return segment(sentence, params);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public List<WordDto> post(@RequestBody String sentence, HttpServletRequest request) throws UnsupportedEncodingException {
        RequestHelper requestHelper = new RequestHelper(request);
        Map<String,String> params = requestHelper.getParams();
        return segment(sentence, params);
    }

    private List<WordDto> segment(String sentence, Map<String, String> params) {
        SegmentWorker worker = SegmentEngine.getInstance().getSegmentWorker(params);
        SegmentResult result = worker.segment(sentence);
        List<WordDto> words = new ArrayList<WordDto>(result.length());
        for(WordAtom wordAtom : result.getWordAtoms()) {
            words.add(new WordDto(wordAtom));
        }
        return words;
    }
}

