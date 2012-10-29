/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import websiteschema.mpsegment.util.ISerialize;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 *
 * @author ray
 */
public class Pi implements ISerialize {

    private Map<Integer, Double> pi = new HashMap<Integer, Double>();
    private int total = 1;

    public Pi() {
    }

    public Pi(Map<Integer, Integer> pii) {
        init(pii);
    }
    
    private void init(Map<Integer, Integer> pii) {
        Set<Integer> keySet = pii.keySet();
        for (Integer i : keySet) {
            total += pii.get(i);
        }

        for (Integer i : keySet) {
            setPi(i, (double) pii.get(i) / (double) (total));
        }
    }

    public double getPi(int index) {
        if (pi.containsKey(index)) {
            return pi.get(index);
        } else {
            return 1.0 / (double) total;
        }
    }

    public void setPi(int index, double prob) {
        pi.put(index, prob);
    }

    @Override
    public void save(SerializeHandler writeHandler) throws IOException {
        writeHandler.serializeInt(total);
        writeHandler.serializeMapIntDouble(pi);
    }

    @Override
    public void load(SerializeHandler readHandler) throws IOException {
        total = readHandler.deserializeInt();
        pi = readHandler.deserializeMapIntDouble();
    }
}
