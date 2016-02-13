package ethercubes.listutil;

/**
 *
 * @author Philipp
 */
public class ShortArrayList {
    private int size = 0;
    private short[] data = new short[8];
    
    public short get(int index) {
        return data[index];
    }
    
    public void add(short value) {
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
        short[] nextData = new short[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    public int size() {
        return size;
    }

    public short[] data() {
        return data;
    }
    
}
