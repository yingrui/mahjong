// Source File Name:   BufReader.java
package websiteschema.mpsegment.util;

import java.io.IOException;

public interface BufReader {

    public void close() throws IOException;

    public int read() throws IOException;

    public int read(byte abyte0[]) throws IOException;

    public byte readByte() throws IOException;

    public int readInt() throws IOException;

    public int readIntByte() throws IOException;
}
