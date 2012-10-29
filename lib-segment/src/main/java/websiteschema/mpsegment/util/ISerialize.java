/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.util;

import java.io.IOException;

/**
 *
 * @author twer
 */
public interface ISerialize {

    public void save(SerializeHandler writeHandler) throws IOException;
    
    public void load(SerializeHandler readHandler) throws IOException;
    
}
