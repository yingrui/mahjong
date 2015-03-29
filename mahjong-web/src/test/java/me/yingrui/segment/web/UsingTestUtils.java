package me.yingrui.segment.web;

import java.util.UUID;

public class UsingTestUtils {

    public String uniq(String str) {
        return str + UUID.randomUUID();
    }
}
