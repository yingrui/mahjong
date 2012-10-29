/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.hmm;

/**
 *
 * @author ray
 */
public class ObserveListException extends Exception {

    String detail = "";

    /**
     *
     */
    public ObserveListException() {
    }

    /**
     *
     */
    public ObserveListException(String d) {
        this.detail = d;
    }

    @Override
    public String getMessage() {
        return detail;
    }

    @Override
    public String toString() {
        return "Observe List Exception : " + detail;
    }
}
