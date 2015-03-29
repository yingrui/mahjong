package me.yingrui.segment.web.api.service;

import org.junit.Assert;
import org.junit.Test;
import me.yingrui.segment.web.api.model.dto.WordDto;

import java.util.HashMap;
import java.util.List;

public class SegmentServiceTest {

    @Test
    public void should_return_segment_result() {
        SegmentService segmentService = new SegmentService();
        List<WordDto> result = segmentService.segment("舌尖上的中国", new HashMap<String,String>());

        Assert.assertEquals("舌尖/N", result.get(0).toString());
        Assert.assertEquals("上/F", result.get(1).toString());
        Assert.assertEquals("的/U", result.get(2).toString());
        Assert.assertEquals("中国/NS", result.get(3).toString());
    }

}
