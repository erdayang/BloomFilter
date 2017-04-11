package com.me.muyang.input;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by yangxianda on 2017/3/26.
 */
public class BigFileHash {
    private File file;

    public BigFileHash(){}
    public BigFileHash(String path) throws IOException {
        file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
    }
    public void random(long limit) throws IOException {
        FileWriter writer = new FileWriter(file);
        Random random = new Random();
        for(long i = 0; i < limit; i++){
            long a = random.nextLong();

            writer.write(String.valueOf(a)+"\n");
            writer.flush();
        }

        if(writer != null ){
            writer.close();
        }
    }

    public void test() throws IOException {
        BigFileHash input = new BigFileHash("D:/random.txt");
        input.random(2l<<32);
    }

    /** 掩码，用于取余 */
    private long mask = (2l << 10) - 1;

    public void hash(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while((line = reader.readLine()) != null){
            long hash = Long.parseLong(line) & mask;
            toHashFile(String.valueOf(hash), line);
        }
        reader.close();
        close();
    }

    private Map<String, BufferedWriter> fileMap = new HashMap<String, BufferedWriter>(1024);
    public void toHashFile(final String fileName, String value) throws IOException {
        if(!fileMap.containsKey(fileName)){
            File file = new File(fileName);
            if(!file.exists()) file.createNewFile();
            BufferedWriter reader = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:/test/sub/" + file)));
            fileMap.put(fileName, reader);
            System.out.println("create new file； " + fileName);
        }
        fileMap.get(fileName).write(value+"\n");
    }
    public void close() throws IOException {
        for(BufferedWriter writer : fileMap.values()){
            if(null != writer){
                writer.close();
            }
        }
    }

    public static void main(String [] args) throws IOException {
        BigFileHash randomInput = new BigFileHash();
        randomInput.hash("d:/test/random.txt");
    }
}
