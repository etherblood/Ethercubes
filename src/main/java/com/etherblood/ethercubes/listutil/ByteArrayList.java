package com.etherblood.ethercubes.listutil;

/**
 *
 * @author Philipp
 */
public class ByteArrayList {
    private int size = 0;
    private byte[] data = new byte[8];
    
    public byte get(int index) {
        return data[index];
    }
    
    public void set(int index, byte value) {
        data[index] = value;
    }
    
    public void add(byte value) {
        if(size == data.length) {
            grow();
        }
        data[size++] = value;
    }
    
    public void insertAt(int index, byte value) {
        if(size == data.length) {
            grow();
        }
        System.arraycopy(data, index, data, index + 1, size++ - index);
        data[index] = value;
    }
    
    public void removeAt(int index) {
        System.arraycopy(data, index + 1, data, index, --size - index);
    }

    public void clear() {
        size = 0;
    }
    
    private void grow() {
        byte[] nextData = new byte[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    public int size() {
        return size;
    }

    public byte[] data() {
        return data;
    }
    
}
