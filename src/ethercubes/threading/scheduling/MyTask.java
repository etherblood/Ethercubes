/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.threading.scheduling;

/**
 *
 * @author Philipp
 */
public interface MyTask extends Runnable {
    int getPriority();
    boolean tryFinish();
}
