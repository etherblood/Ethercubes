/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.tests;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class PriorityCache<K, T> {
    private final int limit;
    private final LinkedHashMap<K, T> map = new LinkedHashMap<>();

    public PriorityCache(int limit) {
        this.limit = limit;
    }
    
    public synchronized void put(K key, T value) {
        if(map.size() == limit) {
            Iterator<Map.Entry<K, T>> iterator = map.entrySet().iterator();
            iterator.next();
            iterator.remove();
        }
        map.put(key, value);
    }
    
    public synchronized T get(K key) {
        T value = map.remove(key);
        if(value != null) {
            map.put(key, value);
        }
        return value;
    }
    
}
