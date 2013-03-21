package websiteschema.mpsegment.util

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

class SerializationTest {

    @Test
    def should_serialize_a_map_and_deserialize_it() {
        var map = scala.collection.mutable.Map[String,Int]()
        map.put("张三", 10);
        map.put("李四", 20);
        map.put("王五", 30);
        var filename = "test-map.dat"
        var file = new File(filename)
        try {
            var writeHandler = SerializeHandler(file, SerializeHandler.MODE_WRITE_ONLY)
            writeHandler.serializeMapStringInt(map);
            writeHandler.close();

            var readHandler = SerializeHandler(file, SerializeHandler.MODE_READ_ONLY)
            var newone = readHandler.deserializeMapStringInt()
            readHandler.close();
            Assert.assertEquals(3, newone.size);
            Assert.assertEquals(newone("张三"), 10);
            Assert.assertEquals(newone("李四"), 20);
            Assert.assertEquals(newone("王五"), 30);
        } catch {
          case _: Throwable =>
            Assert.fail();
        } finally {
            file.delete();
        }
    }

    @Test
    def should_serialize_an_array_and_deserialize_it() {
        var array = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
        var filename = "test-map.dat"
        var file = new File(filename)
        try {
            var writeHandler = SerializeHandler(file, SerializeHandler.MODE_WRITE_ONLY)
            writeHandler.serializeArrayInt(array.toArray);
            writeHandler.close();
            var readHandler = SerializeHandler(file, SerializeHandler.MODE_READ_ONLY)
            var newone = readHandler.deserializeArrayInt()
            readHandler.close();
            Assert.assertArrayEquals(newone, array.toArray);
        } catch {
          case _: Throwable =>
            Assert.fail();
        } finally {
            file.delete();
        }
    }

    @Test
    def should_serialize_2d_array_and_deserialize_it() {
        var array = new Array[Array[Int]](2)
        array(0) = List(1,2,3,4).toArray
        array(1) = List(5,6,7,8).toArray
        var filename = "test-map.dat"
        var file = new File(filename)
        try {
            var writeHandler = SerializeHandler(file, SerializeHandler.MODE_WRITE_ONLY)
            writeHandler.serialize2DArrayInt(array);
            writeHandler.close();
            var readHandler = SerializeHandler(file, SerializeHandler.MODE_READ_ONLY)
            var newone = readHandler.deserialize2DArrayInt()
            readHandler.close();
            Assert.assertEquals(newone.length, array.length);
            Assert.assertArrayEquals(newone(0), array(0).toArray);
            Assert.assertArrayEquals(newone(1), array(1).toArray);
        } catch {
          case _: Throwable =>
            Assert.fail();
        } finally {
            file.delete();
        }
    }
}
