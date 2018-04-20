package ethercubes.pagination.services;

import ethercubes.context.Autowire;
import ethercubes.events.CubesEventHandler;
import ethercubes.events.CubesEventbus;
import ethercubes.pagination.TaskExecutor;
import ethercubes.pagination.events.FinishTasksRequest;
import javax.annotation.PostConstruct;

/**
 *
 * @author Philipp
 */
public class TaskExecutorService {
    @Autowire
    private CubesEventbus eventbus;
    @Autowire
    private TaskExecutor taskExecutor;
    
    @PostConstruct
    public void init() {
        eventbus.register(FinishTasksRequest.class, new CubesEventHandler<FinishTasksRequest>() {
            @Override
            public void handle(FinishTasksRequest event) {
                taskExecutor.blockUntilFinished();
            }
        });
    }
}
