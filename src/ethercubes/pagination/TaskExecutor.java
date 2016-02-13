package ethercubes.pagination;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Philipp
 */
public class TaskExecutor {
    private final ExecutorService executor;
    private final AtomicInteger remainingTasks = new AtomicInteger(0);

    public TaskExecutor(ExecutorService executor) {
        this.executor = executor;
    }
    
    public void submitTasks(Collection<Runnable> tasks) {
        synchronized(this) {
            remainingTasks.addAndGet(tasks.size());
            for (Runnable task : tasks) {
                executor.execute(runnable(task));
            }
        }
    }
    
    private Runnable runnable(final Runnable task) {
        return new Runnable() {
            @Override
            public void run() {
                task.run();
                onTaskFinished();
            }
        };
    }
    
    private void onTaskFinished() {
        if(remainingTasks.decrementAndGet() == 0) {
            synchronized(this) {
                notify();
            }
        }
    }
    
    public void blockUntilFinished() {
        synchronized(this) {
            if(remainingTasks.get() == 0) {
                return;
            }
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
