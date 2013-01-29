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

import java.util.List;

@Controller
@RequestMapping(value = "/segment")
public class SegmentController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<WordAtom> get(String sentence) {
        System.out.println(sentence);
        SegmentWorker worker = SegmentEngine.getInstance().getSegmentWorker();
        SegmentResult result = worker.segment(sentence);
        System.out.println(result);
        return result.getWordAtoms();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public List<WordAtom> post(@RequestBody String sentence) {
        System.out.println(sentence);
        SegmentWorker worker = SegmentEngine.getInstance().getSegmentWorker();
        SegmentResult result = worker.segment(sentence);
        System.out.println(result);
        return result.getWordAtoms();
    }
}
