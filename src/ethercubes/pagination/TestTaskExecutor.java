package ethercubes.pagination;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTaskExecutor {
    
    public static void main(String... args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        TaskExecutor testTaskExecutorImpl = new TaskExecutor(executor);
        ArrayList<Runnable> tasks = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) {
            final int value = i;
            tasks.add(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 + 10 * value);
                    System.out.println(value + " a " + System.currentTimeMillis());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        }
        testTaskExecutorImpl.submitTasks(tasks);
        testTaskExecutorImpl.blockUntilFinished();
        System.out.println("b " + System.currentTimeMillis());
        executor.shutdown();
    }
}
