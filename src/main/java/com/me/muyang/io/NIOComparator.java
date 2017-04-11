package com.me.muyang.io;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 三种方式读写文件性能对比
 * 1， io
 * 2, nio byteBuffer
 * 3, nio MappedByteBuffer
 */
public class NIOComparator {
    private int LENGTH = 4000000;
    public void IOMethod(String TPATH) {
        long start = System.currentTimeMillis();
        try {
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(new File(TPATH))));
            byte[] array = new byte[LENGTH * 4];
            int count = 0;
            for (int i = 0; i < LENGTH; i++) {
//                dos.writeInt(i);//写入 4000000 个整数
                byte[] tmp = int2byte(i);
                for(int j = 0; j < 4; j++){
                    array[count++] = tmp[j];
                }
            }
            dos.write(array, 0, array.length);
            if (dos != null) {
                dos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        start = System.currentTimeMillis();
        try {
            DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(new File(TPATH))));
            byte[] array = new byte[LENGTH * 4];
//            for (int i = 0; i < 4000000; i++) {
//                dis.readInt();
//            }
            dis.read(array);
            for(int i = 0; i < array.length; i=i+4){
                byte2int(array[i], array[i+1], array[i+2], array[i+3]);//将 byte 转为整数
            }
            if (dis != null) {
                dis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public void ByteMethod(String TPATH) {
        long start = System.currentTimeMillis();
        try {
            FileOutputStream fout = new FileOutputStream(new File(TPATH));
            FileChannel fc = fout.getChannel();//得到文件通道
            ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH * 4);//分配 Buffer
            for (int i = 0; i < LENGTH; i++) {
                byteBuffer.put(int2byte(i));//将整数转为数组
            }
            byteBuffer.flip();//准备写
            fc.write(byteBuffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        FileInputStream fin;
        try {
            fin = new FileInputStream(new File(TPATH));
            FileChannel fc = fin.getChannel();//取得文件通道
            ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH * 4);//分配 Buffer
            fc.read(byteBuffer);//读取文件数据
            fc.close();
            byteBuffer.flip();//准备读取数据
            while (byteBuffer.hasRemaining()) {
                byte2int(byteBuffer.get(), byteBuffer.get(), byteBuffer.get(), byteBuffer.get());//将 byte 转为整数
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public void mapMethod(String TPATH) {
        long start = System.currentTimeMillis();
        //将文件直接映射到内存的方法
        try {
            FileChannel fc = new RandomAccessFile(TPATH, "rw").getChannel();
            IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, LENGTH * 4).asIntBuffer();
            for (int i = 0; i < LENGTH; i++) {
                ib.put(i);
            }
            if (fc != null) {
                fc.close();
            }
            clean(ib);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        try {
            FileChannel fc = new FileInputStream(TPATH).getChannel();
            MappedByteBuffer lib = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            lib.asIntBuffer();
            while (lib.hasRemaining()) {
                lib.get();
            }
            if (fc != null) {
                fc.close();
            }
            clean(lib);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);

    }

    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (res & 0xff);//最低位
        targets[2] = (byte) ((res >> 8) & 0xff);//次低位
        targets[1] = (byte) ((res >> 16) & 0xff);//次高位
        targets[0] = (byte) ((res >>> 24));//最高位，无符号右移
        return targets;
    }

    public static int byte2int(byte b1, byte b2, byte b3, byte b4) {
        return ((b1 & 0xff) << 24) | ((b2 & 0xff) << 16) | ((b3 & 0xff) << 8) | (b4 & 0xff);
    }

    public static void main(String[] args) {
        NIOComparator nio = new NIOComparator();
        nio.IOMethod("d:\\1.txt");
        nio.ByteMethod("d:\\2.txt");
        nio.mapMethod("d:\\3.txt");
    }
    public void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

    }
}