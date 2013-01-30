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
import java.util.List;

@Controller
@RequestMapping(value = "/segment")
public class SegmentController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<WordAtom> get(HttpServletRequest request) throws UnsupportedEncodingException {
        String sentence = getSentence(request);
        return segment(sentence);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public List<WordAtom> post(@RequestBody String sentence) {
        return segment(sentence);
    }

    private String getSentence(HttpServletRequest request) throws UnsupportedEncodingException {
        String sentence = "";
        String queryString = java.net.URLDecoder.decode(request.getQueryString(), "UTF-8");
        String[] strings = queryString.split("&");
        for(String str : strings) {
            if(str.startsWith("sentence=")) {
                sentence = str.substring(9);
                break;
            }
        }
        return sentence;
    }

    private List<WordAtom> segment(String sentence) {
        SegmentWorker worker = SegmentEngine.getInstance().getSegmentWorker();
        SegmentResult result = worker.segment(sentence);
        return result.getWordAtoms();
    }
}
