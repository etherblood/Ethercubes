/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.statistics;

/**
 *
 * @author Philipp
 */
public interface TimeStatistics {
    public static final TimeStatistics TIME_STATISTICS = new TimeStatisticsImpl();
    
    void nextFrame();
    long start();
    void end(long start, Object key);
    String displayString();
    void clear();
}
