package com.etherblood.ethercubes.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Philipp
 */
public class CubesEventbus {
    private final ConcurrentHashMap<Class, List<CubesEventHandler>> handlers = new ConcurrentHashMap<>();
    
    public void fireEvent(Object event) {
        for (CubesEventHandler handler : getList(event.getClass())) {
            handler.handle(event);
        }
    }
    
    public <E> void register(Class<E> eventType, CubesEventHandler<E> handler) {
        getList(eventType).add(handler);
    }

    private <E> List<CubesEventHandler> getList(Class<E> eventType) {
        List<CubesEventHandler> list = handlers.get(eventType);
        if(list == null) {
            list = createListInstance(eventType);
        }
        return list;
    }
    
    private synchronized List<CubesEventHandler> createListInstance(Class eventType) {
        handlers.putIfAbsent(eventType, Collections.synchronizedList(new ArrayList<CubesEventHandler>()));
        return handlers.get(eventType);
    }
}
