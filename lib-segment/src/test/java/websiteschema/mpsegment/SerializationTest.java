/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 * @author ray
 */
public class SerializationTest {

    @Test
    public void should_serialize_a_map_and_deserialize_it() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("张三", 10);
        map.put("李四", 20);
        map.put("王五", 30);
        String filename = "test-map.dat";
        File file = new File(filename);
        try {
            SerializeHandler writeHandler = new SerializeHandler(file, SerializeHandler.MODE_WRITE_ONLY);
            writeHandler.serializeMapStringInt(map);
            writeHandler.close();

            SerializeHandler readHandler = new SerializeHandler(file, SerializeHandler.MODE_READ_ONLY);
            Map<String, Integer> newone = readHandler.deserializeMapStringInt();
            readHandler.close();
            Assert.assertEquals(3, newone.size());
            Assert.assertEquals(newone.get("张三").intValue(), 10);
            Assert.assertEquals(newone.get("李四").intValue(), 20);
            Assert.assertEquals(newone.get("王五").intValue(), 30);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            file.delete();
        }
    }

    @Test
    public void should_serialize_an_array_and_deserialize_it() {
        int[] array = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        String filename = "test-map.dat";
        File file = new File(filename);
        try {
            SerializeHandler writeHandler = new SerializeHandler(file, SerializeHandler.MODE_WRITE_ONLY);
            writeHandler.serializeArrayInt(array);
            writeHandler.close();
            SerializeHandler readHandler = new SerializeHandler(file, SerializeHandler.MODE_READ_ONLY);
            int[] newone = readHandler.deserializeArrayInt();
            readHandler.close();
            Assert.assertArrayEquals(newone, array);
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        } finally {
            file.delete();
        }
    }

    @Test
    public void should_serialize_2d_array_and_deserialize_it() {
        int[][] array = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}};
        String filename = "test-map.dat";
        File file = new File(filename);
        try {
            SerializeHandler writeHandler = new SerializeHandler(file, SerializeHandler.MODE_WRITE_ONLY);
            writeHandler.serialize2DArrayInt(array);
            writeHandler.close();
            SerializeHandler readHandler = new SerializeHandler(file, SerializeHandler.MODE_READ_ONLY);
            int[][] newone = readHandler.deserialize2DArrayInt();
            readHandler.close();
            Assert.assertEquals(newone.length, array.length);
            Assert.assertArrayEquals(newone[0], array[0]);
            Assert.assertArrayEquals(newone[1], array[1]);
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        } finally {
            file.delete();
        }
    }
}
