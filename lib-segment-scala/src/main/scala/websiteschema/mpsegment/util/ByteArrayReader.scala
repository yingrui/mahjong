package websiteschema.mpsegment.util;

import java.io.{InputStream, DataInputStream}

class ByteArrayReader(stream:InputStream)
        extends BufReader {

    val input = new DataInputStream(stream);

    override def close(): Unit = input.close()

    override def read(): Int = input.readByte()

    override def readByte():Byte = input.readByte()

    override def readIntByte(): Int = read()

    override def readInt(): Int =  input.readInt()

    override def read(bytes: Array[Byte]): Int = input.read(bytes)
}
