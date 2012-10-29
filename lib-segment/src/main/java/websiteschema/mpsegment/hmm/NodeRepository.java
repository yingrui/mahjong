/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.io.IOException;
import java.util.*;
import websiteschema.mpsegment.util.ISerialize;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 *
 * @author ray
 */
public class NodeRepository implements ISerialize {

    List<Node> repo = null;
    Map<String, Integer> indexMap = null;

    public NodeRepository() {
        repo = new ArrayList<Node>();
        indexMap = new HashMap<String, Integer>();
    }

    public Node add(Node node) {
        String name = node.getName();
        if (!indexMap.containsKey(name)) {
            int index = repo.size();
            node.setIndex(index);
            repo.add(node);
            indexMap.put(name, index);
            return node;
        } else {
            return repo.get(indexMap.get(name));
        }
    }

    public Node get(String name) {
        if (indexMap.containsKey(name)) {
            int index = indexMap.get(name);
            return repo.get(index);
        } else {
            return null;
        }
    }

    public Node get(int index) {
        if (repo.size() > index) {
            return repo.get(index);
        } else {
            return null;
        }
    }

    public Set<String> keySet() {
        return indexMap.keySet();
    }

    @Override
    public void save(SerializeHandler writeHandler) throws IOException {
        int length = null != repo ? repo.size() : 0;
        writeHandler.serializeInt(length);
        for(int i = 0; i < length; i++) {
            Node node = repo.get(i);
            node.save(writeHandler);
        }
    }

    @Override
    public void load(SerializeHandler readHandler) throws IOException {
        int length = readHandler.deserializeInt();
        if(length > 0) {
            for(int i = 0; i < length; i++) {
                Node node = new Node();
                node.load(readHandler);
                repo.add(node);
                indexMap.put(node.getName(), node.getIndex());
            }
        }
    }
}
