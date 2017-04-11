package com.me.muyang.input;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by yangxianda on 2017/4/1.
 */
public class LoadFile {
    public static void test(String fileName) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r");
        MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, randomAccessFile.length());
        byte[] data = new byte[100];
        while(mappedByteBuffer.hasRemaining()){
            int remaining = data.length;
            if(mappedByteBuffer.remaining() < remaining){
                remaining = mappedByteBuffer.remaining();
                mappedByteBuffer.get(data, 0, remaining);
            }
        }
    }

    public static void main(String [] args) throws IOException {
        test("");
    }
}
