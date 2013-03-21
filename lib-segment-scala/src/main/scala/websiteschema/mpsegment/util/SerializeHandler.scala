package websiteschema.mpsegment.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream

import scala.collection.mutable.Map

object SerializeHandler {
  val MODE_READ_ONLY = 0;
  val MODE_WRITE_ONLY = 1;

  def apply(f: File, mode: Int): SerializeHandler = {
    if (!f.exists()) f.createNewFile()

    if (mode == MODE_WRITE_ONLY) {
      return new SerializeHandler(null, new DataOutputStream(new FileOutputStream(f)))
    }
    if (mode == MODE_READ_ONLY){
      return new SerializeHandler(new DataInputStream(new FileInputStream(f)), null)
    }

    return null
  }

  def apply(input: DataInputStream) = new SerializeHandler(input, null)

  def apply(output: DataOutputStream) = new SerializeHandler(null, output)
}

class SerializeHandler(input: DataInputStream, output: DataOutputStream) {

    def close() {
        if (null != input) {
            input.close();
        }
        if (null != output) {
            output.close();
        }
    }

    def serializeMapStringInt(map: Map[String,Int]) {
        if (null != map) {
            output.writeInt(map.size);
            if (!map.isEmpty) {
                for (key <- map.keys) {
                    val value = map(key)
                    output.writeUTF(key);
                    output.writeInt(value);
                }
            }
            output.flush();
        }
    }

    def serializeMapIntDouble(map: Map[Int,Double]) {
        if (null != map) {
            output.writeInt(map.size);
            if (!map.isEmpty) {
                for (key <- map.keys) {
                    val value = map(key)
                    output.writeInt(key);
                    output.writeDouble(value);
                }
            }
            output.flush();
        }
    }

    def deserializeMapStringInt():Map[String,Int] = {
        var size = input.readInt()
        if (size > 0) {
            val map = Map[String,Int]();
            for (i <- 0 until size) {
                var key = input.readUTF()
                var value = input.readInt()
                map += (key -> value);
            }
          map
        } else {
            Map[String,Int]();
        }
    }

    def deserializeMapIntDouble(): Map[Int,Double] = {
        var size = input.readInt()
        if (size > 0) {
            val map = Map[Int,Double]();
            for (i <- 0 until size) {
                var key = input.readInt()
                var value = input.readDouble()
                map += (key -> value);
            }
          map
        } else {
            Map[Int,Double]();
        }
    }

    def serializeArrayInt(array: Array[Int]) {
        if (null != array) {
            output.writeInt(array.length);
            for (i <- array) {
                output.writeInt(i);
            }
            output.flush();
        }
    }

    def deserializeArrayInt() : Array[Int] = {
        var size = input.readInt()
        var array = new Array[Int](size)
        if (size > 0) {
            for (i <- 0 until size) {
                var value = input.readInt()
                array(i) = value;
            }
        }
        return array;
    }

    def serializeArrayDouble(array: Array[Double]) {
        if (null != array) {
            output.writeInt(array.length);
            for (i <- array) {
                output.writeDouble(i);
            }
            output.flush();
        }
    }

    def deserializeArrayDouble() : Array[Double] = {
        val size = input.readInt()
        val array = new Array[Double](size)
        if (size > 0) {
            for (i <- 0 until size) {
                val value = input.readDouble()
                array(i) = value;
            }
        }
        return array;
    }

    def serialize2DArrayInt(array: Array[Array[Int]]) {
        if (null != array) {
            output.writeInt(array.length);
            for (a <- array) {
                output.writeInt(a.length);
                for (i <- a) {
                    output.writeInt(i);
                }
            }
            output.flush();
        }
    }

    def deserialize2DArrayInt() : Array[Array[Int]] = {
        var size = input.readInt()
        var array = new Array[Array[Int]](size)
        if (size > 0) {
            for (i <- 0 until size) {
                var length = input.readInt()
                array(i) = new Array[Int](length);
                for (j <- 0 until length) {
                    array(i)(j) = input.readInt();
                }
            }
        }
        return array;
    }

    def serializeDouble(d: Double) {
        output.writeDouble(d);
        output.flush();
    }

    def deserializeDouble() : Double = {
        return input.readDouble();
    }

    def serializeInt(i: Int) {
        output.writeInt(i);
        output.flush();
    }

    def deserializeInt() : Int = {
        return input.readInt();
    }

    def serializeString(str: String) {
        output.writeUTF(str);
        output.flush();
    }

    def deserializeString() : String = {
        return input.readUTF();
    }
}
