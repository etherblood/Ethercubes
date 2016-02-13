package ethercubes.listutil;

/**
 *
 * @author Philipp
 */
public class BoolArrayList {
    private int size = 0;
    private boolean[] data = new boolean[8];
    
    public boolean get(int index) {
        return data[index];
    }
    
    public void set(int index, boolean value) {
        data[index] = value;
    }
    
    public void add(boolean value) {
        if(size == data.length) {
            grow();
        }
        data[size++] = value;
    }
    
    public void insertAt(int index, boolean value) {
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
        boolean[] nextData = new boolean[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    public int size() {
        return size;
    }

    public boolean[] data() {
        return data;
    }
    
}
