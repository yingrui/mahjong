package websiteschema.mpsegment.util;

import java.io.*;

public class FileUtil {

    public static InputStream getResourceAsStream(String resource) throws IOException {
        return FileUtil.class.getClassLoader().getResourceAsStream("websiteschema/mpsegment/" + resource);
    }

}
