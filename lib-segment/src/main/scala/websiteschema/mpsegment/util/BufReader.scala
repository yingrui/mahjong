package websiteschema.mpsegment.util

trait BufReader {

    def close()

    def read() : Int

    def read(abyte0: Array[Byte]) : Int

    def readByte() : Byte

    def readInt() : Int

    def readIntByte() : Int
}
