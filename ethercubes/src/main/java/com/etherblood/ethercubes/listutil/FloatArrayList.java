package com.etherblood.ethercubes.listutil;

/**
 *
 * @author Philipp
 */
public class FloatArrayList {
    private int size = 0;
    private float[] data = new float[8];
    
    public float get(int index) {
        return data[index];
    }
    
    public void add(float value) {
        if(size == data.length) {
            grow();
        }
        data[size++] = value;
    }
    
    public void removeAt(int index) {
        System.arraycopy(data, index + 1, data, index, --size - index);
    }

    public void clear() {
        size = 0;
    }
    
    private void grow() {
        float[] nextData = new float[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    public int size() {
        return size;
    }

    public float[] data() {
        return data;
    }
    
}
