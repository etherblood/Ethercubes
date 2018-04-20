package com.etherblood.ethercubes.listutil;

/**
 *
 * @author Philipp
 */
public class IntArrayList {
    private int size = 0;
    private int[] data = new int[8];
    
    public int get(int index) {
        return data[index];
    }
    
    public void add(int value) {
        if(size == data.length) {
            grow();
        }
        data[size++] = value;
    }
    
    public void insertAt(int index, int value) {
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
        int[] nextData = new int[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    public int size() {
        return size;
    }

    public int[] data() {
        return data;
    }
    
}
