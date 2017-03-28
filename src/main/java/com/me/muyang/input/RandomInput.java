package com.me.muyang.input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by xuanda007 on 2017/3/28.
 */
public class RandomInput {
    public static void random(int count) throws IOException {
        File file = new File("D:\\random.txt");
        if(!file.exists()) file.createNewFile();
        FileWriter writer = null;
        try{
            writer  = new FileWriter(file);
            Random random = new Random();
            BufferedWriter buffer = new BufferedWriter(writer);
            for(int i = 0; i < count; i++){
                long value = random.nextLong();
                buffer.write(String.valueOf(value));
                buffer.newLine();
            }
            buffer.flush();
            writer.flush();
            buffer.close();
            writer.close();
        }catch(Exception e){
            writer.close();
        }

    }

    public static void main(String [] args) throws IOException {
        random(100);
    }
}
