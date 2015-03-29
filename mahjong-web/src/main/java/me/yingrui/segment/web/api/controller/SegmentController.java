package me.yingrui.segment.web.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import me.yingrui.segment.web.api.service.SegmentService;
import me.yingrui.segment.web.api.model.dto.WordDto;
import me.yingrui.segment.web.util.PojoMapper;
import me.yingrui.segment.web.util.RequestHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/segment")
public class SegmentController {

    @Autowired
    SegmentService segmentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<WordDto> get(HttpServletRequest request) throws UnsupportedEncodingException {
        RequestHelper requestHelper = new RequestHelper(request);
        Map<String, String> params = requestHelper.getParams();
        String sentence = params.get("sentence");
        return segmentService.segment(sentence, params);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void post(@RequestBody String sentence, HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestHelper requestHelper = new RequestHelper(request);
        Map<String, String> params = requestHelper.getParams();
        String contentType = request.getContentType();
        String result = SegmentByContentType(sentence, params, contentType);
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.append(result);
    }

    private String SegmentByContentType(String sentence, Map<String, String> params, String contentType) throws IOException {
        String result;
        if (contentType.contains("application/vnd.ideaboardy")) {
            result = segmentForIdeaBoardy(sentence, params);
        } else {
            result = segmentSentence(sentence, params);
        }
        return result;
    }

    private String segmentForIdeaBoardy(String sentence, Map<String, String> params) throws IOException {
        String result;List<Map> array = PojoMapper.fromJson(sentence, List.class);
        for(Map obj : array) {
            String content = (String)obj.get("content");
            List<WordDto> words = segmentService.segment(content, params);
            obj.put("segmentResult", words);
        }
        result = PojoMapper.toJson(array);
        return result;
    }

    private String segmentSentence(String sentence, Map<String, String> params) throws IOException {
        String result;List<WordDto> words = segmentService.segment(sentence, params);
        result = PojoMapper.toJson(words);
        return result;
    }
}

