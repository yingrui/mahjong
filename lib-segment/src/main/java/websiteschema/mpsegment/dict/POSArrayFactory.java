/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.dict;

/**
 *
 * @author twer
 */
public class POSArrayFactory {

    private static final POSArray posArray = new POSArray();

    static {
        POS pos = new POS(POSUtil.getPOSString(POSUtil.POS_UNKOWN), 1);
        posArray.add(pos);
    }

    static POSArray getUnknownWordPOSArray() {
        return posArray;
    }
}
