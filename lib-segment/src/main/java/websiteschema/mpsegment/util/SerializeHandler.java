/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author ray
 */
public class SerializeHandler {

    DataInputStream input;
    DataOutputStream output;
    public final static int MODE_READ_ONLY = 0;
    public final static int MODE_WRITE_ONLY = 1;

    public SerializeHandler(File f, int mode) throws IOException {
        if (!f.exists()) {
            f.createNewFile();
        }
        if (mode == MODE_WRITE_ONLY) {
            output = new DataOutputStream(new FileOutputStream(f));
        } else if (mode == MODE_READ_ONLY) {
            input = new DataInputStream(new FileInputStream(f));
        }
    }

    public SerializeHandler(DataInputStream input) {
        this.input = input;
    }

    public SerializeHandler(DataOutputStream output) {
        this.output = output;
    }

    public void close() throws IOException {
        if (null != input) {
            input.close();
        }
        if (null != output) {
            output.close();
        }
    }

    public void serializeMapStringInt(Map<String, Integer> map) throws IOException {
        if (null != map) {
            output.writeInt(map.size());
            if (!map.isEmpty()) {
                for (String key : map.keySet()) {
                    int value = map.get(key);
                    output.writeUTF(key);
                    output.writeInt(value);
                }
            }
            output.flush();
        }
    }
    
    public void serializeMapIntDouble(Map<Integer, Double> map) throws IOException {
        if (null != map) {
            output.writeInt(map.size());
            if (!map.isEmpty()) {
                for (Integer key : map.keySet()) {
                    double value = map.get(key);
                    output.writeInt(key);
                    output.writeDouble(value);
                }
            }
            output.flush();
        }
    }

    public Map<String, Integer> deserializeMapStringInt() throws IOException {
        Map<String, Integer> map = null;
        int size = input.readInt();
        if (size > 0) {
            map = new HashMap<String, Integer>(size);
            for (int i = 0; i < size; i++) {
                String key = input.readUTF();
                int value = input.readInt();
                map.put(key, value);
            }
        } else {
            map = new HashMap<String, Integer>();
        }
        return map;
    }
    
    public Map<Integer, Double> deserializeMapIntDouble() throws IOException {
        Map<Integer, Double> map = null;
        int size = input.readInt();
        if (size > 0) {
            map = new HashMap<Integer, Double>(size);
            for (int i = 0; i < size; i++) {
                int key = input.readInt();
                double value = input.readDouble();
                map.put(key, value);
            }
        } else {
            map = new HashMap<Integer, Double>();
        }
        return map;
    }

    public void serializeArrayInt(int[] array) throws IOException {
        if (null != array) {
            output.writeInt(array.length);
            for (int i : array) {
                output.writeInt(i);
            }
            output.flush();
        }
    }

    public int[] deserializeArrayInt() throws IOException {
        int size = input.readInt();
        int[] array = new int[size];
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int value = input.readInt();
                array[i] = value;
            }
        }
        return array;
    }

    public void serializeArrayDouble(double[] array) throws IOException {
        if (null != array) {
            output.writeInt(array.length);
            for (double i : array) {
                output.writeDouble(i);
            }
            output.flush();
        }
    }

    public double[] deserializeArrayDouble() throws IOException {
        int size = input.readInt();
        double[] array = new double[size];
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                double value = input.readDouble();
                array[i] = value;
            }
        }
        return array;
    }

    public void serialize2DArrayInt(int[][] array) throws IOException {
        if (null != array) {
            output.writeInt(array.length);
            for (int[] a : array) {
                output.writeInt(a.length);
                for (int i : a) {
                    output.writeInt(i);
                }
            }
            output.flush();
        }
    }

    public int[][] deserialize2DArrayInt() throws IOException {
        int size = input.readInt();
        int[][] array = new int[size][];
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int length = input.readInt();
                array[i] = new int[length];
                for (int j = 0; j < length; j++) {
                    array[i][j] = input.readInt();
                }
            }
        }
        return array;
    }

    public void serializeDouble(double d) throws IOException {
        output.writeDouble(d);
        output.flush();
    }

    public double deserializeDouble() throws IOException {
        return input.readDouble();
    }

    public void serializeInt(int i) throws IOException {
        output.writeInt(i);
        output.flush();
    }

    public int deserializeInt() throws IOException {
        return input.readInt();
    }
    
    public void serializeString(String str) throws IOException {
        output.writeUTF(str);
        output.flush();
    }
    
    public String deserializeString() throws IOException {
        return input.readUTF();
    }
}
