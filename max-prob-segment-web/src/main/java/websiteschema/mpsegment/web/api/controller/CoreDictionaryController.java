package websiteschema.mpsegment.web.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import websiteschema.mpsegment.web.api.model.WordItemDto;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/dictionary/core")
public class CoreDictionaryController {

    @RequestMapping(value = "/index/{wordIndex}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getWordHeads(@PathVariable String wordIndex, HttpServletRequest request) throws UnsupportedEncodingException {
        List<String> wordHeads = new ArrayList<String>();
        wordHeads.add("啊");
        wordHeads.add("阿");
        return wordHeads;
    }

    @RequestMapping(value = "/heads/{wordHead}", method = RequestMethod.GET)
    @ResponseBody
    public List<WordItemDto> getWordsByHead(@PathVariable String wordHead, HttpServletRequest request) throws UnsupportedEncodingException {
        List<WordItemDto> words = new ArrayList<WordItemDto>();
        words.add(new WordItemDto("啊"));
        words.add(new WordItemDto("阿猫"));
        words.add(new WordItemDto("阿狗"));
        return words;
    }

}

