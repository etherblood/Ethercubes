/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.threading.scheduling;

import ethercubes.chunk.BlockChunk;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Philipp
 */
public class Scheduler<C extends BlockChunk> {
    private final ExecutorService executor;
    private final int activeLimit;
    private final List<PriorityTask<C>> active;
    private final MyPriorityQueue<PriorityTask<C>> queue;

    public Scheduler(int activeLimit) {
        this.activeLimit = activeLimit;
        active = new ArrayList<PriorityTask<C>>(activeLimit);
        queue = new MyPriorityQueue();
        executor = Executors.newFixedThreadPool(activeLimit);
    }
    
    public void submit(PriorityTask<C> task) {
        queue.add(task);
    }
    
    public void update() {
        for (int i = active.size() - 1; i >= 0; i--) {
            if(active.get(i).tryFinish()) {
                active.remove(i);
            }
        }
        int count = activeLimit - active.size();
        for (int i = 0; i < count && activateNext(); i++) {
        }
    }
    
    public void shutdown() {
        executor.shutdown();
    }
    
    private boolean activateNext() {
        PriorityTask<C> next = queue.poll(active);
        if(next == null) {
            return false;
        }
        active.add(next);
        executor.submit(next);
        return true;
    }

    public MyPriorityQueue<PriorityTask<C>> getQueue() {
        return queue;
    }
    
}
