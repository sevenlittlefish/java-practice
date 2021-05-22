package base.core.io.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NoBlockClient {

    public static void main(String[] args) throws IOException {
        //获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 567));
        //切换为非阻塞模式
        socketChannel.configureBlocking(false);
        //读取文件，也可用new FileInputStream("path").getChannel();
        FileChannel fileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        //创建缓冲区，用于存储channel的数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(fileChannel.read(buffer) != -1){
            //转换为读模式
            buffer.flip();
            socketChannel.write(buffer);
            //读完切换为写模式，让管道继续读取文件
            buffer.clear();
        }
        fileChannel.close();
        socketChannel.close();
    }
}
