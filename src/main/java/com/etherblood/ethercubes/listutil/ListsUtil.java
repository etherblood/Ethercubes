package com.etherblood.ethercubes.listutil;

/**
 *
 * @author Philipp
 */
public class ListsUtil {
    public static void sort(int[] keys, byte[] values, int length) {
        for (int i = 0; i < length - 1; i++) {
            int smallest = i;
            for (int j = i + 1; j < length; j++) {
                if(keys[j] < keys[smallest]) {
                    smallest = j;
                }
            }
            swap(keys, i, smallest);
            swap(values, i, smallest);
        }
    }
    
    public static void swap(int[] keys, int i, int j) {
        int tmp = keys[i];
        keys[i] = keys[j];
        keys[j] = tmp;
    }
    public static void swap(byte[] keys, int i, int j) {
        byte tmp = keys[i];
        keys[i] = keys[j];
        keys[j] = tmp;
    }
}
