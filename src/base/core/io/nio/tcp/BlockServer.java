package base.core.io.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BlockServer {

    public static void main(String[] args) throws IOException {
        //1.获取通道
        ServerSocketChannel server = ServerSocketChannel.open();
        //2.得到文件通道，将客户端传递过来的文件写到本地项目下(写模式、没有则创建)
        FileChannel fileChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        //3. 绑定链接
        server.bind(new InetSocketAddress(567));
        //4. 获取客户端的连接(阻塞的)
        SocketChannel client = server.accept();
        //5. 要使用NIO，有了Channel，就必然要有Buffer，Buffer是与数据打交道的
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //6. 将客户端传递过来的文件保存在本地中
        while(client.read(buffer) != -1){
            //转换为读模式
            buffer.flip();
            fileChannel.write(buffer);
            //读完切换成写模式，能让管道继续读取文件的数据
            buffer.clear();
        }

        //通知客户端成功接收到了数据
        buffer.put("file accept success!".getBytes());
//        buffer.put("file accept success UTF-16BE!".getBytes("UTF-16BE"));
        buffer.flip();
        client.write(buffer);
        buffer.clear();

        fileChannel.close();
        client.close();
        server.close();
    }
}
