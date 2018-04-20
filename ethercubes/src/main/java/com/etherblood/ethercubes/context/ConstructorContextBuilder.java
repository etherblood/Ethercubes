package com.etherblood.ethercubes.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Philipp
 */
public class ConstructorContextBuilder {
    private final List<Constructor> constructors = new ArrayList<>();
    
    public void register(Constructor constructor) {
        constructors.add(constructor);
    }
    
    public CubesContext build() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<Constructor> open = new HashSet<>(constructors);
        List<Object> beans = new ArrayList<>(open.size());
        int prevOpenSize = -1;
        while(prevOpenSize != open.size()) {
            prevOpenSize = open.size();
            constructBeansIteration(open, beans);
        }
        if(!open.isEmpty()) {
            throw new AssertionError();
        }
        CubesContext context = new CubesContext();
        context.setBeans(beans);
        return context;
    }

    private void constructBeansIteration(Set<Constructor> open, List<Object> beans) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Iterator<Constructor> it = open.iterator();
        while(it.hasNext()) {
            Constructor constructor = it.next();
            Object[] params = findParameters(constructor, beans);
            if(params != null) {
                beans.add(constructor.newInstance(params));
                it.remove();
            }
        }
    }

    private Object[] findParameters(Constructor constructor, List<Object> beans) {
        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < params.length; i++) {
            Object param = findParameter(parameterTypes[i], beans);
            if(param == null) {
                return null;
            }
            params[i] = param;
        }
        return params;
    }

    private Object findParameter(Class<?> paramType, List<Object> beans) throws AssertionError {
        Object param = null;
        for (Object bean : beans) {
            if(paramType.isInstance(bean)) {
                if(param != null) {
                    throw new AssertionError();
                }
                param = bean;
            }
        }
        return param;
    }
}
