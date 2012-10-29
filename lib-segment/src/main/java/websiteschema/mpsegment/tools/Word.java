/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.mpsegment.tools;

import java.util.*;

/**
 *
 * @author gxm
 */
public class Word {

    public String wordName = "";
    public Map<Integer,Integer> pos2freq = new HashMap<Integer,Integer>();

    public void addPOSAndFreq(int pos, int freq)
    {
        if(!pos2freq.containsKey(pos)){
            pos2freq.put(pos, freq);
        }
    }
}
