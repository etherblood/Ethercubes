/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk;

/**
 *
 * @author Philipp
 */
public interface Versioned extends VersionedReadonly {
    void setVersion(int version);
    void incVersion();
}
