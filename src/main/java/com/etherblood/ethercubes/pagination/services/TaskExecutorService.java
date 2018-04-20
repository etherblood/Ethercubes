package com.etherblood.ethercubes.pagination.services;

import com.etherblood.ethercubes.context.Autowire;
import com.etherblood.ethercubes.events.CubesEventHandler;
import com.etherblood.ethercubes.events.CubesEventbus;
import com.etherblood.ethercubes.pagination.TaskExecutor;
import com.etherblood.ethercubes.pagination.events.FinishTasksRequest;
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
