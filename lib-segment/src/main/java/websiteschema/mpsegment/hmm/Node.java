/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.io.IOException;
import websiteschema.mpsegment.util.ISerialize;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 * Graphic Model Node
 * @author ray
 */
public class Node implements ISerialize {

    private int index = -1;
    private String name = null;

    public Node() {
    }
    
    public Node(String name) {
        this.name = name;
    }

    public Node(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void save(SerializeHandler writeHandler) throws IOException {
        writeHandler.serializeString(name);
        writeHandler.serializeInt(index);
    }

    @Override
    public void load(SerializeHandler readHandler) throws IOException {
        name = readHandler.deserializeString();
        index = readHandler.deserializeInt();
    }
}
