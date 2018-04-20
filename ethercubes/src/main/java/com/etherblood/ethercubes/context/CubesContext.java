package com.etherblood.ethercubes.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.PreDestroy;

/**
 *
 * @author Philipp
 */
public class CubesContext {

    private final List<Object> beans = new ArrayList<>();
    
    void setBeans(Collection<Object> beans) {
        this.beans.addAll(beans);
    }

    public <T> T getBean(Class<T> beanClass) {
        return extractResult(getBeans(beanClass), beanClass);
    }

    public <T> List<T> getBeans(Class<T> fieldClass) {
        ArrayList<T> result = new ArrayList<>();
        for (Object wireCandidate : beans) {
            if (fieldClass.isInstance(wireCandidate)) {
                result.add((T) wireCandidate);
            }
        }
        return result;
    }

    private <T> T extractResult(List<T> results, Class<T> fieldClass) throws IllegalStateException {
        if (results.size() != 1) {
            if (results.isEmpty()) {
                throw new IllegalStateException("no bean found for " + fieldClass.getName());
            }
            throw new IllegalStateException("multiple beans found for " + fieldClass.getName() + ": " + Arrays.toString(results.toArray()));
        }
        return results.get(0);
    }

    public void destroy() {
        for (Object bean : beans) {
            Class clazz = bean.getClass();
            while (clazz != Object.class) {
                for (Method declaredMethod : clazz.getDeclaredMethods()) {
                    if (declaredMethod.isAnnotationPresent(PreDestroy.class)) {
                        try {
                            declaredMethod.setAccessible(true);
                            declaredMethod.invoke(bean);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            System.err.println(ex);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
        beans.clear();
    }
}
