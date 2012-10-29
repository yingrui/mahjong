package websiteschema.mpsegment.dict;

import java.io.Serializable;

public class POS implements Serializable, Comparable {

    public POS(String name, int count) {
        this.name = name;
        this.count = count;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj != null && (obj instanceof POS)) {
            return name.compareTo(((POS) obj).name);
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof POS)) {
            return name.equals(((POS) obj).name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append((new StringBuilder("\u8BCD\u6027\u540D\u79F0=")).append(name).append("\n").toString());
        stringbuffer.append((new StringBuilder("\u51FA\u73B0\u6B21\u6570=")).append(count).append("\n").toString());
        return stringbuffer.toString();
    }

    public String toDBFString() {
        StringBuilder stringbuffer = new StringBuilder();
        stringbuffer.append((new StringBuilder(" ")).append(name).toString());
        stringbuffer.append((new StringBuilder(" ")).append(count).toString());
        return stringbuffer.toString();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int i) {
        count = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }
    private String name;
    private int count;
}
