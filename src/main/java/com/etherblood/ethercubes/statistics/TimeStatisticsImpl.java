/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.statistics;

import com.etherblood.ethercubes.Util;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Philipp
 */
public class TimeStatisticsImpl implements TimeStatistics {
    private ConcurrentHashMap<Object, ValueStatistic> map = new ConcurrentHashMap<Object, ValueStatistic>();
    private long numFrames = 0;
    
    @Override
    public void nextFrame() {
        numFrames++;
    }
    
    private ValueStatistic getOrCreate(Object key) {
        ValueStatistic value = map.get(key);
        if(value == null) {
            value = new ValueStatistic();
            map.put(key, value);
        }
        return value;
    }
    
    @Override
    public long start() {
        return -System.nanoTime();
    }
    
    @Override
    public void end(long start, Object key) {
        getOrCreate(key).addValue(System.nanoTime() + start);
    }
    
    @Override
    public void clear() {
        map.clear();
        numFrames = 0;
    }
    
    @Override
    public String displayString() {
        String result = "";
        ArrayList<Object> keys = new ArrayList<>(map.keySet());
        ArrayList<ValueStatistic> values = new ArrayList<>();
        for (Object key : keys) {
            values.add(map.put(key, new ValueStatistic()));
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        
        ArrayList<Entry<Object, ValueStatistic>> list = new ArrayList<Entry<Object, ValueStatistic>>();
        for (int i = 0; i < keys.size(); i++) {
            list.add(new AbstractMap.SimpleEntry<>(keys.get(i), values.get(i)));
        }
        
        Collections.sort(list, new Comparator<Entry<Object, ValueStatistic>>() {
            @Override
            public int compare(Entry<Object, ValueStatistic> o1, Entry<Object, ValueStatistic> o2) {
                long diff = o2.getValue().getTotal() - o1.getValue().getTotal();
                return diff < 0? -1: diff > 0? 1: 0;
            }
        });
        for (Entry<Object, ValueStatistic> entry : list) {
            ValueStatistic stats = entry.getValue();
            result += entry.getKey();
            result += ": " + Util.humanReadableNanos(stats.getTotal());
            result += ", avg:" + Util.humanReadableNanos(stats.getTotal() / stats.getCount());
            if(numFrames != 0) {
                result += ", frame: " + Util.humanReadableNanos(stats.getTotal() / numFrames);
            }
            result += System.lineSeparator();
        }
        
        for (Entry<Object, ValueStatistic> entry : list) {
            map.get(entry.getKey()).addTotalAndCount(entry.getValue().getTotal(), entry.getValue().getCount());
        }
        
        return result;
    }
}
