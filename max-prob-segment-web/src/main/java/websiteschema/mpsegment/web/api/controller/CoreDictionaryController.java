package websiteschema.mpsegment.web.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import websiteschema.mpsegment.web.api.model.WordItem;
import websiteschema.mpsegment.web.api.model.WordItemDto;
import websiteschema.mpsegment.web.api.service.WordItemService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/dictionary/core")
public class CoreDictionaryController {

    @Autowired
    WordItemService wordItemService;

    @RequestMapping(value = "/pinyin/{pinyin}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getWordHeads(@PathVariable String pinyin, HttpServletRequest request) throws UnsupportedEncodingException {
        List<WordItem> wordItems = wordItemService.findAllByPinyin(pinyin);
        List<String> wordHeads = new ArrayList<String>();
        for (WordItem wordItem : wordItems) {
            wordHeads.add(wordItem.getName());
        }
        return wordHeads;
    }

    @RequestMapping(value = "/heads/{wordHead}", method = RequestMethod.GET)
    @ResponseBody
    public List<WordItemDto> getWordsByHead(@PathVariable String wordHead, HttpServletRequest request) throws UnsupportedEncodingException {
        List<WordItem> wordItems = wordItemService.findAllByWordHead(wordHead);
        List<WordItemDto> words = new ArrayList<WordItemDto>();
        for (WordItem wordItem : wordItems) {
            words.add(wordItem.toDto());
        }
        return words;
    }

}

