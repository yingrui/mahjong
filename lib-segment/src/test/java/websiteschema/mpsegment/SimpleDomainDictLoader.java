/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment;

import websiteschema.mpsegment.dict.DictionaryException;
import websiteschema.mpsegment.dict.domain.DomainDictLoader;
import websiteschema.mpsegment.dict.domain.DomainDictionary;

public class SimpleDomainDictLoader implements DomainDictLoader {

    @Override
    public void load(DomainDictionary dict) {
        try {
            dict.pushWord("PC机", null, "N", 5, 10001);
            dict.pushWord("个人电脑", "PC机", "N", 5, 10001);
        } catch (DictionaryException e) {
            e.printStackTrace();
        }
    }
}
