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
public final class Emission implements ISerialize {

    //observe -> states
    private Map<Integer, Map<Integer, Double>> matrix = new HashMap<Integer, Map<Integer, Double>>(5);
    private int total = 0;

    public Emission() {
    }

    public Emission(Map<Integer, Map<Integer, Integer>> emisMatrix) {
        //state -> observes
        Set<Integer> states = emisMatrix.keySet();
        for (Integer state : states) {
            Map<Integer, Integer> mapO = emisMatrix.get(state);
            int sum = 1;
            Set<Integer> observes = mapO.keySet();
            for (Integer o : observes) {
                sum += mapO.get(o);
            }

            for (Integer o : observes) {
                double prob = (double) mapO.get(o) / (double) sum;
                setProb(o, state, prob);
            }
            total += sum;
        }
    }

    public double getProb(int s, int o) {
        Map<Integer, Double> e = matrix.get(o);
        if (null != e) {
            if (e.containsKey(s)) {
                return e.get(s);
            }
        }

        return 1.0 / (double) total;
    }

    public Set<Integer> getStateProbByObserve(int observe) {
        Map<Integer, Double> map = matrix.get(observe);
        return null != map ? map.keySet() : null;
    }

    public void setProb(int s, int o, double prob) {
        Map<Integer, Double> map = matrix.get(o);
        if (null == map) {
            map = new HashMap<Integer, Double>(1);
            matrix.put(o, map);
        }
        map.put(s, prob);
    }

    @Override
    public void save(SerializeHandler writeHandler) throws IOException {
        writeHandler.serializeInt(total);
        int size = null != matrix ? matrix.size() : 0;
        writeHandler.serializeInt(size);
        for(int key : matrix.keySet()){
            writeHandler.serializeInt(key);
            Map<Integer, Double> row = matrix.get(key);
            writeHandler.serializeMapIntDouble(row);
        }
    }

    @Override
    public void load(SerializeHandler readHandler) throws IOException {
        total = readHandler.deserializeInt();
        int size = readHandler.deserializeInt();
        for(int i = 0; i < size; i++) {
            int key = readHandler.deserializeInt();
            Map<Integer, Double> row = readHandler.deserializeMapIntDouble();
            matrix.put(key, row);
        }
    }
}
