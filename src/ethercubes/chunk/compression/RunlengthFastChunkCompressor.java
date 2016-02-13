/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk.compression;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RunlengthFastChunkCompressor implements FastChunkCompressor {
    
    public void compress(DataOutputStream stream, byte[] chunk) {
        try {
            _serialize(stream, chunk);
        } catch (IOException ex) {
            Logger.getLogger(RunlengthFastChunkCompressor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    private void _serialize(DataOutputStream stream, byte[] chunk) throws IOException {
        int i = 0, length = chunk.length, start;
        byte value, next = chunk[0];
        while(i < length) {
            value = next;
            start = i;
            do {
                i++;
                if(i >= length) {
                    break;
                }
                next = chunk[i];
            } while(value == next);
            stream.writeInt(i - start);
            stream.writeByte(value);
        }
        stream.flush();
    }

    public void decompress(DataInputStream stream, byte[] chunk) {
        try {
            _deserialize(stream, chunk);
        } catch (IOException ex) {
            Logger.getLogger(RunlengthFastChunkCompressor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    private void _deserialize(DataInputStream stream, byte[] chunk) throws IOException {
        int i = 0, length = chunk.length, num, limit;
        byte value;
        while (i < length) {            
            num = stream.readInt();
            value = stream.readByte();
            limit = i + num;
            Arrays.fill(chunk, i, limit, value);
            i = limit;
        }
    }
    
}
