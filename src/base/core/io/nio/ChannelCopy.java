package base.core.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelCopy {

    public static void main(String[] args) throws IOException {
        FileChannel in = new FileInputStream("test.txt").getChannel(),
                out = new FileOutputStream("data.txt").getChannel();
        //No.1
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(in.read(buffer) != -1){
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
        //NO.2
//        in.transferTo(0,in.size(),out);
        //or
//        out.transferFrom(in,0,in.size());
    }
}
