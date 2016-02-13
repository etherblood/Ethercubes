/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

/**
 *
 * @author Philipp
 */
public class Util {
    
    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) {
            return bytes + "B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "kMGTPE".charAt(exp-1) + "";
        return String.format("%.1f%sB", bytes / Math.pow(unit, exp), pre);
    }
    
    public static String humanReadableNanos(long nanos) {
        int count = 0;
        while(nanos > 10000 && count < 3) {
            nanos /= 1000;
            count++;
        }
        if(count == 3) {
            return nanos + "s";
        }
        return nanos + ("nμm".charAt(count) + "") + "s";
    }
    
}
