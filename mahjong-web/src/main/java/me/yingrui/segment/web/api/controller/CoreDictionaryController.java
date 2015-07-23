package me.yingrui.segment.web.api.controller;

import me.yingrui.segment.web.api.model.Concept;
import me.yingrui.segment.web.api.model.WordFreq;
import me.yingrui.segment.web.api.model.WordItem;
import me.yingrui.segment.web.api.model.dto.WordItemDto;
import me.yingrui.segment.web.api.service.WordItemService;
import me.yingrui.segment.web.exception.NotFoundException;
import me.yingrui.segment.web.util.PojoMapper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping(value = "/dictionary/core")
public class CoreDictionaryController {

    @Autowired
    WordItemService wordItemService;

    @RequestMapping(value = "/pinyin/{pinyin}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getWordHeads(@PathVariable String pinyin) throws UnsupportedEncodingException {
        List<WordItem> wordItems = wordItemService.findAllByPinyin(pinyin);
        List<String> wordHeads = new ArrayList<String>();
        for (WordItem wordItem : wordItems) {
            wordHeads.add(wordItem.getName());
        }
        return wordHeads;
    }

    @RequestMapping(value = "/heads/{wordHead}", method = RequestMethod.GET)
    @ResponseBody
    public List<WordItemDto> getWordsByHead(@PathVariable String wordHead) throws UnsupportedEncodingException {
        List<WordItem> wordItems = wordItemService.findAllByWordHead(wordHead);
        List<WordItemDto> words = new ArrayList<WordItemDto>();
        for (WordItem wordItem : wordItems) {
            words.add(wordItem.toDto());
        }
        return words;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public void get(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        Session session = wordItemService.session();
        session.beginTransaction();
        Iterator<WordItem> words = wordItemService.iterator(session);

        if(!words.hasNext()) {
            writer.write("[]");
        } else {
            writer.write("[");
            do {
                String wordString = PojoMapper.toJson(simplify(words.next()));
                writer.write(wordString);
                if(words.hasNext())
                    writer.write(",\n");
                else
                    break;
            } while(true);
            writer.write("]");
        }
        session.close();
        response.setContentType("application/json; charset=utf-8");
    }

    private Map<String, Object> simplify(WordItem word) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("word", word.getName());
        map.put("domainType", word.getType().getValue());

        Map<String, Integer> posTable = new LinkedHashMap<String, Integer>();
        for (WordFreq pos: word.getWordFreqSet()) {
            posTable.put(pos.getPartOfSpeech().getName(), pos.getFreq());
        }
        map.put("POSTable", posTable);

        if(!word.getConceptSet().isEmpty()) {
            List<String> concepts = new ArrayList<String>();
            for (Concept concept: word.getConceptSet()) {
                concepts.add(concept.getName());
            }
            map.put("concepts", concepts);
        }

        return map;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/words/{wordId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateWord(@PathVariable int wordId, @RequestBody WordItemDto updateRequestDto) throws UnsupportedEncodingException {
        WordItem wordItem = wordItemService.getById(wordId);
        if(null == wordItem) {
            throw new NotFoundException("Can not find specified word with id: " + wordId);
        }
        updateRequestDto.id = wordId; // confirm word id
        wordItemService.update(updateRequestDto);
    }

}

