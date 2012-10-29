/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author ray
 */
public class ViterbiTest {

    @Test
    public void should_return_status_333332_with_giving_observes_THTHTH() {
        Viterbi viterbi = new Viterbi();
        TrieNodeSortor sortor = new TrieNodeBinarySort();

        viterbi.setSortor(sortor);

        initTestData(viterbi);
        viterbi.setN(2);

        List<String> o = Arrays.asList(new String[]{"T", "H", "T", "H", "T", "H"});
        try {
            List<Node> s;
            s = viterbi.calculateWithLog(o);
            StringBuilder sb = new StringBuilder();
            for (Node state : s) {
                System.out.print(state.getName() + " ");
                sb.append(state.getName()).append(" ");
            }
            assert (sb.toString().trim().equals("three three three three three two"));
        } catch (ObserveListException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void should_handle_unknown_State() {
        List<String> o = Arrays.asList(new String[]{"A", "H"});
        Viterbi viterbi = new Viterbi();
        TrieNodeSortor sortor = new TrieNodeBinarySort();

        viterbi.setSortor(sortor);

        initTestData(viterbi);
        viterbi.setN(2);

        try {
            List<Node> s;
            s = viterbi.calculateWithLog(o);
            StringBuilder sb = new StringBuilder();
            for (Node state : s) {
                System.out.print(state.getName() + " ");
                sb.append(state.getName()).append(" ");
            }
            Assert.fail("should throw ObserveListException.");
        } catch (ObserveListException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void initTestData(Viterbi v) {
        Node s1 = new Node("one");
        v.getStateBank().add(s1);
        Node s2 = new Node("two");
        v.getStateBank().add(s2);
        Node s3 = new Node("three");
        v.getStateBank().add(s3);

        Node o1 = new Node("H");
        v.getObserveBank().add(o1);
        Node o2 = new Node("T");
        v.getObserveBank().add(o2);

        //transition
        //   s1  s2  s3
        //s1 0.8 0.1 0.1
        //s2 0.1 0.8 0.1
        //s3 0.1 0.1 0.8
        v.getTran().setStateBank(v.getStateBank());
        v.getTran().setProb(s1.getIndex(), s1.getIndex(), 0.3);
        v.getTran().setProb(s1.getIndex(), s2.getIndex(), 0.3);
        v.getTran().setProb(s1.getIndex(), s3.getIndex(), 0.4);
        v.getTran().setProb(s2.getIndex(), s1.getIndex(), 0.2);
        v.getTran().setProb(s2.getIndex(), s2.getIndex(), 0.6);
        v.getTran().setProb(s2.getIndex(), s3.getIndex(), 0.2);
        v.getTran().setProb(s3.getIndex(), s1.getIndex(), 0.2);
        v.getTran().setProb(s3.getIndex(), s2.getIndex(), 0.2);
        v.getTran().setProb(s3.getIndex(), s3.getIndex(), 0.6);
        v.getTran().getRoot().printTreeNode("");
        //emission
        //   o1  o2
        //s1 0.5 0.5
        //s2 0.8 0.2
        //s3 0.2 0.8
        v.getE().setProb(s1.getIndex(), o1.getIndex(), 0.5);
        v.getE().setProb(s1.getIndex(), o2.getIndex(), 0.5);
        v.getE().setProb(s2.getIndex(), o1.getIndex(), 0.8);
        v.getE().setProb(s2.getIndex(), o2.getIndex(), 0.2);
        v.getE().setProb(s3.getIndex(), o1.getIndex(), 0.2);
        v.getE().setProb(s3.getIndex(), o2.getIndex(), 0.8);

        //Pi = [0.2 0.3 0.5]
        v.getPi().setPi(s1.getIndex(), 0.2);
        v.getPi().setPi(s2.getIndex(), 0.4);
        v.getPi().setPi(s3.getIndex(), 0.4);
    }
}
