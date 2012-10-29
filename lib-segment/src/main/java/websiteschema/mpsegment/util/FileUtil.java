package websiteschema.mpsegment.util;

import java.io.*;

public class FileUtil {

    public static InputStream getResourceAsStream(String resource) throws IOException {
        return FileUtil.class.getClassLoader().getResourceAsStream("websiteschema/mpsegment/" + resource);
    }

    public static boolean saveStringToFile(String s, String s1) {
        boolean flag = false;
        if (!createPathByFileName(s1)) {
            return false;
        }
        try {
            File file = new File(s1);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedoutputstream.write(s.getBytes("GBK"));
            bufferedoutputstream.close();
            flag = true;
        } catch (Exception exception) {
            System.out.println((new StringBuilder("saveStringToFile ERROR!")).append(exception.toString()).toString());
        }
        return flag;
    }

    public static String getStringFromFile(String s) {
        String s2 = "";
        StringBuffer stringbuffer = new StringBuffer();
        try {
            File file = new File(s);
            if (file.exists()) {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
                String s3;
                while ((s3 = bufferedreader.readLine()) != null) {
                    stringbuffer.append(s3);
                    stringbuffer.append("\n");
                }
                bufferedreader.close();
            }
        } catch (Exception exception) {
            System.out.println((new StringBuilder("getStringFromFile error:")).append(exception.toString()).toString());
        }
        String s1 = stringbuffer.toString();
        return s1;
    }

    public static String getStringFromFile(File file) {
        String s1 = "";
        StringBuilder stringbuffer = new StringBuilder();
        try {
            if (file.exists()) {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
                String s2;
                while ((s2 = bufferedreader.readLine()) != null) {
                    stringbuffer.append(s2);
                    stringbuffer.append("\n");
                }
                bufferedreader.close();
            }
        } catch (Exception exception) {
            System.out.println((new StringBuilder("getStringFromFile error:")).append(exception.toString()).toString());
        }
        String s = stringbuffer.toString();
        return s;
    }

    public static boolean createPathByFileName(String s) {
        boolean flag = false;
        File file = new File(s);
        File file1 = file.getParentFile();
        if (file1 != null) {
            if (!file1.exists()) {
                flag = file1.mkdirs();
            } else {
                flag = true;
            }
        }
        return flag;
    }

    public static String getExtension(String s) {
        String s1 = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '.') {
                continue;
            }
            s1 = s.substring(i);
            break;
        }

        return s1;
    }

    public static String removeExtension(String s) {
        String s1 = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '.') {
                continue;
            }
            s1 = s.substring(0, i);
            break;
        }

        return s1;
    }

    public static boolean isFileExist(String s) {
        File file = new File(s);
        return file.exists() && !file.isDirectory();
    }

    public static String execCmd(String s) {
        String s1 = "";
        String s2 = "";
        try {
            Process process = Runtime.getRuntime().exec(s);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s3;
            while ((s3 = bufferedreader.readLine()) != null) {
                if (!s3.trim().equals("")) {
                    if (s1.equals("")) {
                        s1 = s3;
                    } else {
                        s1 = (new StringBuilder(String.valueOf(s1))).append("\n").append(s3).toString();
                    }
                }
            }
            process.waitFor();
        } catch (Exception exception) {
        }
        return s1;
    }

//    public static String[] glob(String s, String s1)
//            throws FileNotFoundException {
//        File file = new File(s);
//        if (!file.isDirectory()) {
//            throw new UserError((new StringBuilder("\"")).append(file.getPath()).append("\" is not a valid directory path.").toString());
//        } else {
//            System.out.println(file.getPath());
//            String as[] = file.list(new c(s1));
//            return as;
//        }
//    }
    public static boolean delete(String s) {
        File file = new File(s);
        if (!file.exists()) {
            System.out.println((new StringBuilder("\u5220\u9664\u6587\u4EF6\u5931\u8D25\uFF1A")).append(s).append("\u6587\u4EF6\u4E0D\u5B58\u5728").toString());
            return false;
        }
        if (file.isFile()) {
            return deleteFile(s);
        } else {
            return deleteDirectory(s);
        }
    }

    public static boolean deleteFile(String s) {
        File file = new File(s);
        if (file.isFile() && file.exists()) {
            file.delete();
            System.out.println((new StringBuilder("\u5220\u9664\u5355\u4E2A\u6587\u4EF6")).append(s).append("\u6210\u529F\uFF01").toString());
            return true;
        } else {
            System.out.println((new StringBuilder("\u5220\u9664\u5355\u4E2A\u6587\u4EF6")).append(s).append("\u5931\u8D25\uFF01").toString());
            return false;
        }
    }

    public static boolean deleteDirectory(String s) {
        if (!s.endsWith(File.separator)) {
            s = (new StringBuilder(String.valueOf(s))).append(File.separator).toString();
        }
        File file = new File(s);
        if (!file.exists() || !file.isDirectory()) {
            System.out.println((new StringBuilder("\u5220\u9664\u76EE\u5F55\u5931\u8D25")).append(s).append("\u76EE\u5F55\u4E0D\u5B58\u5728\uFF01").toString());
            return false;
        }
        boolean flag = true;
        File afile[] = file.listFiles();
        for (int i = 0; i < afile.length; i++) {
            if (afile[i].isFile() ? !(flag = deleteFile(afile[i].getAbsolutePath())) : !(flag = deleteDirectory(afile[i].getAbsolutePath()))) {
                break;
            }
        }

        if (!flag) {
            System.out.println("\u5220\u9664\u76EE\u5F55\u5931\u8D25");
            return false;
        }
        if (file.delete()) {
            System.out.println((new StringBuilder("\u5220\u9664\u76EE\u5F55")).append(s).append("\u6210\u529F\uFF01").toString());
            return true;
        } else {
            System.out.println((new StringBuilder("\u5220\u9664\u76EE\u5F55")).append(s).append("\u5931\u8D25\uFF01").toString());
            return false;
        }
    }
}
