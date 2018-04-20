/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.listutil;

/**
 *
 * @author Philipp
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        for (int i = 0; i < 256; i++) {
            System.out.println((float)i % 16f);
        }
//        IntArrayList list = new IntArrayList();
//        for (int i = 0; i < 10; i++) {
//            list.add(i);
//        }
//        list.insertAt(5, 15);
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }
    }
}
