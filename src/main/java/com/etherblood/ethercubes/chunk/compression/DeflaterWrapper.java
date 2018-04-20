/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author Philipp
 */
public class DeflaterWrapper {
    
  public static byte[] compress(byte[] data) throws IOException {  
   Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);  
   deflater.setInput(data);  
   
   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
       
   deflater.finish();  
   byte[] buffer = new byte[1024];   
   while (!deflater.finished()) {  
    int count = deflater.deflate(buffer); // returns the generated code... index  
    outputStream.write(buffer, 0, count);   
   }  
   outputStream.close();  
   byte[] output = outputStream.toByteArray();  
   
   deflater.end();

//      System.out.println("Original: " + data.length);  
//   System.out.println("Compressed: " + output.length);  
   return output;  
  }  
   
  public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
   Inflater inflater = new Inflater();   
   inflater.setInput(data);  
   
   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
   byte[] buffer = new byte[1024];  
   while (!inflater.finished()) {  
    int count = inflater.inflate(buffer);  
    outputStream.write(buffer, 0, count);  
   }  
   outputStream.close();  
   byte[] output = outputStream.toByteArray();  
   
   inflater.end();
   
//   System.out.println("Original: " + data.length);  
//   System.out.println("Uncompressed: " + output.length);  
   return output;  
  }  
  
  public static void decompress(byte[] data, byte[] destination) throws IOException, DataFormatException {  
   Inflater inflater = new Inflater();   
   inflater.setInput(data);  
    inflater.inflate(destination);
   inflater.end();
  }  

}
