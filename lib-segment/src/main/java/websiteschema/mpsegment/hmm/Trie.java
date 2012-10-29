/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import websiteschema.mpsegment.util.ISerialize;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 *
 * @author ray
 */
public class Trie implements ISerialize {

    int key = -1;
    int count = 0;
    double prob = 0.0;
    Trie descendant[] = null;
    private static TrieNodeSortor sortor = new TrieNodeBinarySort();

    public static void setTreeNodeSorter(TrieNodeSortor trieNodeSortor) {
        sortor = trieNodeSortor;
    }
    
    public int getKey() {
        return key;
    }

    public void buildIndex(int c) {
        prob = (double) count / (double) (c + 1.0);
        if (null != descendant) {
            for (Trie node : descendant) {
                node.buildIndex(count);
            }
            sortor.sort(descendant);
        }
    }

    public Trie insert(int[] ngram) {
        count++;
        if (ngram.length > 0) {
            int k = ngram[0];
            Trie n = null != descendant ? binarySearch(descendant, descendant.length, k) : null;
            if (null == n) {
                n = new Trie();
                n.key = k;
                add(n);
                descendant = sortor.sort(descendant);
            }

            int rec[] = new int[ngram.length - 1];
            for (int i = 1; i < ngram.length; i++) {
                rec[i - 1] = ngram[i];
            }
            return n.insert(rec);
        } else {
            return this;
        }
    }

    protected void add(Trie e) {
        int i = 0;
        if (null == descendant) {
            descendant = new Trie[1];
        } else {
            Trie[] tmp = new Trie[descendant.length + 1];
            System.arraycopy(descendant, 0, tmp, 0, descendant.length);
            i = descendant.length;
            descendant = tmp;
        }
        descendant[i] = e;
    }

    public Trie searchNode(int[] ngram) {
        int k = ngram[0];
        Trie n = searchNode(k);
        if (null != n && ngram.length > 1) {
            int rec[] = (int[]) new int[ngram.length - 1];
            for (int i = 1; i < ngram.length; i++) {
                rec[i - 1] = ngram[i];
            }
            return n.searchNode(rec);
        }
        return n;
    }

    public Trie searchNode(int k) {
        return null != descendant ? binarySearch(descendant, descendant.length, k) : null;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public Trie binarySearch(Trie[] list, int listLength, int searchItem) {
        if (null == list) {
            return null;
        }
        int first = 0;
        int last = listLength - 1;
        int mid = -1;

        boolean found = false;
        while (first <= last && !found) {
            mid = (first + last) / 2;

            int i = list[mid].key - searchItem;

            if (i == 0) {
                found = true;
            } else {
                if (i > 0) {
                    last = mid - 1;
                } else {
                    first = mid + 1;
                }
            }
        }

        if (found) {
            return list[mid];
        } else {
            return null;
        }
    }

    public void printTreeNode(String indent) {
        System.out.println(indent + key + " - " + count + " - " + prob);
        if (null != descendant) {
            for (Trie node : descendant) {
                node.printTreeNode(indent + "  ");
            }
        }
    }


    public long getNumberOfNodeWhichCountLt(int lt) {
        long c = count < lt ? 1 : 0;

        if (null != descendant) {
            for (Trie node : descendant) {
                c += node.getNumberOfNodeWhichCountLt(lt);
            }
        }

        return c;
    }

    public void cutCountLowerThan(int lt) {
        if (lt == 1) {
            return;
        }
        if (null != descendant) {
            List<Trie> l = new LinkedList<Trie>();
            for (int i = 0; i < descendant.length; i++) {
                Trie node = descendant[i];
                if (node.getCount() >= lt) {
                    l.add(node);
                    node.cutCountLowerThan(lt);
                }
            }

            descendant = l.toArray(new Trie[0]);
        }
    }
    
    @Override
    public void save(SerializeHandler writeHandler) throws IOException {
        writeHandler.serializeInt(key);
        writeHandler.serializeInt(count);
        writeHandler.serializeDouble(prob);
        if(null != descendant) {
            writeHandler.serializeInt(descendant.length);
            for(Trie child : descendant) {
                child.save(writeHandler);
            }
        } else {
            writeHandler.serializeInt(0);
        }
    }
    
    @Override
    public void load(SerializeHandler readHandler) throws IOException {
        key = readHandler.deserializeInt();
        count = readHandler.deserializeInt();
        prob = readHandler.deserializeDouble();
        int numberOfDescendant = readHandler.deserializeInt();
        if(numberOfDescendant > 0) {
            descendant = new Trie[numberOfDescendant];
            for(int i = 0; i < numberOfDescendant; i++) {
                Trie child = new Trie();
                child.load(readHandler);
                descendant[i] = child;
            }
        }
    }
}
