package com.me.muyang.input;

import java.io.*;
import java.util.BitSet;

/**
 * Created by yangxianda on 2017/4/6.
 */
public class BitSort {
    private BitSet bits;
    private File file;
    private String path;
    private int size = 0;
    public BitSort(String input, String output){
        this.path = input;
    }

    public void init(){
        file = new File(path);
        long fileSize = file.length();  // 文件的大小
        size = (int)(fileSize / 64); // 得到 行数
        bits = BitSet.valueOf(new long[size]); // 创建位图
    }

    public void sort() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while((line = reader.readLine()) != null){
            long hash = Long.parseLong(line);
         //   bits.set(hash);
         //   toHashFile(String.valueOf(hash), line);
        }
        reader.close();
    }

    public static void main(String [] args){

    }
}
