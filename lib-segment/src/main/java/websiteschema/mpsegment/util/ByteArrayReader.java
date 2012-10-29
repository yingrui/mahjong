package websiteschema.mpsegment.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayReader
        implements BufReader {

    DataInputStream input = null;

    public ByteArrayReader(InputStream stream) {
        input = new DataInputStream(stream);
    }

    @Override
    public void close()
            throws IOException {
        input.close();
        input = null;
    }

    @Override
    public int read()
            throws IOException {
        return input.readByte();
    }

    @Override
    public byte readByte()
            throws IOException {
        return input.readByte();
    }

    @Override
    public int readIntByte()
            throws IOException {
        return read();
    }

    @Override
    public int readInt()
            throws IOException {
        return input.readInt();
    }

    @Override
    public int read(byte abyte0[])
            throws IOException {
        return input.read(abyte0);
    }
}
