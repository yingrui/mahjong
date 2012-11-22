package websiteschema.mpsegment.pinyin;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {
    final K k;
    V v = null;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public V setValue(V value) {
        v = value;
        return v;
    }


}
