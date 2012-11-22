package websiteschema.mpsegment.pinyin;

import java.util.ArrayList;
import java.util.List;

public class Window<T> {

    private int size = 0;
    private List<T> datas = new ArrayList<T>();

    public Window(int size) {
        this.size = size;
    }

    public void clear() {
        datas.clear();
    }

    public void add(T data) {
        datas.add(data);
        if (datas.size() > size) {
            datas.remove(0);
        }
    }

    public T[] toArray(T[] t) {
        return datas.toArray(t);
    }
}
