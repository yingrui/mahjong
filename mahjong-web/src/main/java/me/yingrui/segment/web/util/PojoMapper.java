/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.yingrui.segment.web.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class PojoMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    public static <T> T fromJson(String jsonAsString, Class<T> clazz)
            throws IOException {
        return objectMapper.readValue(jsonAsString, clazz);
    }

    public static <T> List<T> fromJsonArray(String jsonAsString, Class<T> clazz) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonAsString);
        List<T> list = new ArrayList<T>();
        for (JsonNode node : rootNode) {
            T ele = objectMapper.treeToValue(node, clazz);
            list.add(ele);
        }
        return list;
    }

    public static <T> T fromJson(FileReader fr, Class<T> clazz)
            throws IOException {
        return objectMapper.readValue(fr, clazz);
    }

    public static String toJson(Object obj)
            throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(writer);
        objectMapper.writeValue(jsonGenerator, obj);
        return writer.toString();
    }

    public static String toJson(Object obj, boolean prettyPrint)
            throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator jg = jsonFactory.createJsonGenerator(writer);
        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }
        objectMapper.writeValue(jg, obj);
        return writer.toString();
    }

    public static String toJson(Object obj, PrettyPrinter prettyPrint)
            throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator jg = jsonFactory.createJsonGenerator(writer);
        if (null != prettyPrint) {
            jg.setPrettyPrinter(prettyPrint);
        }
        objectMapper.writeValue(jg, obj);
        return writer.toString();
    }

    public static void toJson(Object obj, Writer writer, boolean prettyPrint)
            throws IOException {
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(writer);
        if (prettyPrint) {
            jsonGenerator.useDefaultPrettyPrinter();
        }
        objectMapper.writeValue(jsonGenerator, obj);
    }
}
