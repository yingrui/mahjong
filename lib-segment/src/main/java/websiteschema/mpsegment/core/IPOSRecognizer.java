/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.core;

import websiteschema.mpsegment.graph.IGraph;
import websiteschema.mpsegment.graph.Path;

/**
 *
 * @author twer
 */
public interface IPOSRecognizer {

    public int[] findPOS(Path p, IGraph graph);
    
}
