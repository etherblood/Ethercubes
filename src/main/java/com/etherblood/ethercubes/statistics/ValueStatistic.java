/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.statistics;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Philipp
 */
public class ValueStatistic {
    private AtomicLong count = new AtomicLong(0), total = new AtomicLong(0);

    public void addValue(long nanos) {
        total.addAndGet(nanos);
        count.incrementAndGet();
    }
    
    void addTotalAndCount(long total, long count) {
        this.total.addAndGet(total);
        this.count.addAndGet(count);
    }
    
    public long getCount() {
        return count.get();
    }
    
    public long getTotal() {
        return total.get();
    }
}
