/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.web.util;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class PojoMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    public static <T> T fromJson(String jsonAsString, Class<T> clazz)
            throws IOException {
        return objectMapper.readValue(jsonAsString, clazz);
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
