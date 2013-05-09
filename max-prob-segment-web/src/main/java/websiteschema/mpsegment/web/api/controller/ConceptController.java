package websiteschema.mpsegment.web.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import websiteschema.mpsegment.web.api.model.dto.ConceptDto;
import websiteschema.mpsegment.web.api.service.ConceptService;

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

}

