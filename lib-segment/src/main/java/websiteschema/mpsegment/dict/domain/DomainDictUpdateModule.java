/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.dict.domain;

/**
 *
 * @author ray
 */
public class DomainDictUpdateModule {

    public void update() throws Exception {
        DomainDictFactory.getInstance().buildDictionary();
    }
}
