/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author ray
 */
public class Flag {

    private final static Flag flag = new Flag();
    public final int n = 3;
    private final Properties p = new Properties();
    private double[] labda;
    private int ngramMinimumShowTimes;

    Flag() {
        try {
            p.load(Flag.class.getClassLoader().getResourceAsStream("pinyin.properties"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        labda = new double[n];
        labda[0] = 1.0 / 6.0;
        labda[1] = 1.0 / 3.0;
        labda[2] = 1.0 / 2.0;
        ngramMinimumShowTimes = Integer.parseInt(p.getProperty("NgramMinimumShowTimes", "1"));
    }

    public static Flag getInstance() {
        return flag;
    }

    public int getNgramMinimumShowTimes() {
        return ngramMinimumShowTimes;
    }

    public double labda(int i) {
        return labda[i];
    }
}
