package base.core.io.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class LargeMappedFile {
    static int length = 0x8FFFFFF;//128M
    public static void main(String[] args) throws IOException {
        MappedByteBuffer mbb = new RandomAccessFile("test.dat", "rw").getChannel()
                //内存映射，提高性能
                .map(FileChannel.MapMode.READ_WRITE, 0, length);
        for (int i = 0; i < length; i++) {
            mbb.put((byte)'x');
        }
        System.out.println("Finished writing");
        for (int i = length/2; i < length/2+6; i++) {
            System.out.println((char)mbb.get(i));
        }
    }
}
