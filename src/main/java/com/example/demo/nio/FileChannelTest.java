package com.example.demo.nio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * @Description TODO
 * @Author zhuwei
 * @Date 2021/3/10 20:55
 */
public class FileChannelTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        //final int FILE_SIZE = 1024 * 1024 * 1024;
        final int FILE_SIZE = 1024;
        final int FILE_SIZE_HALF = FILE_SIZE / 20 * 10;//确保可被10整除
        File f = new File("D:\\tmp.log");
        if(!f.exists()) {
            f.getParentFile().mkdir();
            FileWriter fw = new FileWriter(f);
            final char[] cs = new char[1];
            for(int i=0;i<FILE_SIZE;i++) {
                cs[0]=(char)(0x30+(i%10));
                fw.write(cs);
            }
            fw.close();
        }

        RandomAccessFile raf = new RandomAccessFile(f,"rw");
        raf.seek(FILE_SIZE_HALF);
        long c = 0;
        long b = System.currentTimeMillis();
        for(int x=0;x<FILE_SIZE_HALF;x++) {
            c += raf.readByte()-0x30;
        }
        b = System.currentTimeMillis() - b;
        System.out.println(String.format("直接读%s大小的物理文件，耗时%s毫秒。",FILE_SIZE_HALF,b,c));


        final Random rnd = new Random();
        FileChannel channel = raf.getChannel();
        for(int i=0;i<100;i++) {
            MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, FILE_SIZE_HALF, FILE_SIZE_HALF);

            int index = rnd.nextInt(FILE_SIZE_HALF);
            c = 0;
            b = System.currentTimeMillis();
            for(int x=0;x<FILE_SIZE_HALF;x++) {
                c+=buf.get(x)-0x30;
            }
            b = System.currentTimeMillis() -b;
            System.out.println(String.format("%s:读（%s:%s）=%s(和：%s,耗时：%sms)",i,index,index%10,buf.get(index)-0x30,c,b));
            Thread.sleep(1000);
        }
        raf.close();
    }
}
