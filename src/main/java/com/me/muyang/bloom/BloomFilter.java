package com.me.muyang.bloom;

import com.me.muyang.hash.MurmurHash;

import java.io.*;
import java.util.BitSet;

/**
 * M = -N*lnP/(ln2)^2
 *
 * K = M/N*ln2
 * Created by xuanda007 on 2017/3/28.
 */
public class BloomFilter {
    private BitSet bitSet = new BitSet();
    private int K;
    private int M;
    public BloomFilter(int m, int k){
        this.M = m;
        this.K = k;
    }

    public long unsigned(int data){
            return data&0x0FFFFFFFFl;
        }



    public void create() throws IOException {
        File file = new File("d:/random.txt");
        FileReader reader = new FileReader(file);
        BufferedReader buffer = new BufferedReader(reader);
        String str = null;
        while((str =  buffer.readLine()) != null) {
            byte[] value = str.getBytes();
            for (int i = 0; i < K; i++) {
                int hash = MurmurHash.hash32(value, value.length, i );
                bitSet.set((int)((unsigned(hash) )/M), true);
            }
        }
    }

    public void test() throws IOException {
        File file = new File("d:/random.txt");
        FileReader reader = new FileReader(file);
        BufferedReader buffer = new BufferedReader(reader);
        String str = null;
        int count = 0;
        while((str =  buffer.readLine()) != null) {
            byte[] value = str.getBytes();
            boolean result = true;
            for (int i = 0; i < K; i++) {
                int hash = MurmurHash.hash32(value, value.length, i );
                result &= bitSet.get((int)(unsigned(hash) / M));
            }
            if(result){
                System.out.println( count + " : str: "+str+" is in bloom files ");
            }
            count++;
        }
    }

    public static void main(String []args) throws IOException {
        BloomFilter bloomFilter = new BloomFilter(3000, 4);
        bloomFilter.create();
        bloomFilter.test();
    }
}
