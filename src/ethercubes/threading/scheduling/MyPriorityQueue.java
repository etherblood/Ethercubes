/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.threading.scheduling;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Philipp
 */
public class MyPriorityQueue<T extends MyTask> {
    private Set<T> tasks = new HashSet<T>();
    
    public T poll(Collection<T> excluded) {
        T best = null;
        int bestPriority = 0;
        for (T task : tasks) {
            int priority = task.getPriority();
            if(priority > bestPriority || best == null) {
                if(!excluded.contains(task)) {
                    best = task;
                    bestPriority = priority;
                }
            }
        }
        return best;
    }
    public boolean add(T task) {
        return tasks.add(task);
    }

    public Set<T> getTasks() {
        return tasks;
    }
}
