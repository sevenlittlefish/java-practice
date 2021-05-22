package base.core.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class GetChannel {

    public static void main(String[] args) throws IOException {
        String file = "data.txt";
        //write file
        FileChannel fc = new FileOutputStream(file).getChannel();
        fc.write(ByteBuffer.wrap("Some text ".getBytes()));
        fc.close();
        //add to end of file
        fc = new RandomAccessFile(file,"rw").getChannel();
        fc.position(fc.size());//move to the end
        fc.write(ByteBuffer.wrap("Some more ".getBytes()));
        fc.close();
        //read file
        fc = new FileInputStream(file).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        fc.read(buffer);
        buffer.flip();
        while(buffer.hasRemaining())
            System.out.print((char)buffer.get());
    }
}
