package me.yingrui.segment.web.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import me.yingrui.segment.web.api.model.Concept;
import me.yingrui.segment.web.api.model.dto.ConceptDto;
import me.yingrui.segment.web.api.service.ConceptService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/concept")
public class ConceptController {

    @Autowired
    ConceptService conceptService;

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public ConceptDto getConceptTree() {
        return conceptService.getConceptTree();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<ConceptDto> getAllConcept() {
        List<Concept> concepts = conceptService.list();
        List<ConceptDto> conceptDtos = new ArrayList<ConceptDto>();
        for(Concept concept: concepts) {
            conceptDtos.add(concept.toDto());
        }
        return conceptDtos;
    }

}

