package base.core.io.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class NoBlockServer {

    public static void main(String[] args) throws IOException {
        //1.获取通道
        ServerSocketChannel server = ServerSocketChannel.open();
        //2.切换为非阻塞模式
        server.configureBlocking(false);
        //3.绑定链接
        server.bind(new InetSocketAddress(567));
        //4.获取选择器
        Selector selector = Selector.open();
        //5.将通道注册到选择器上，指定接收“监听通道”事件
        server.register(selector, SelectionKey.OP_ACCEPT);
        //6.轮询地获取选择器上已“就绪”的事件--->只要select()>0，说明已就绪
        while(selector.select() > 0){
            // 7.获取当前选择器所有注册的“选择键”(已就绪的监听事件)
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            // 8.获取已“就绪”的事件，(不同的事件做不同的事)
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //接收事件就绪
                if(selectionKey.isAcceptable()){
                    //9.1获取客户端连接
                    SocketChannel client = server.accept();
                    //9.2切换为非阻塞模式
                    client.configureBlocking(false);
                    //9.3注册到选择器上-->拿到客户端的连接为了读取通道的数据(监听读就绪事件)
                    client.register(selector, SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    //10.1获取当前选择器读就绪状态的通道
                    SocketChannel client = (SocketChannel)selectionKey.channel();
                    //10.2创建缓冲区读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //10.3得到文件通道，将客户端传递过来的文件写到本地项目下(写模式、没有则创建)
                    FileChannel fileChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                    while(client.read(buffer) > 0){//-1：客户端主动关闭channel，0：buffer已满或者没有数据可读或者网卡资源被其他socket占用，>0：正常的读取数据的长度
                        //转换为读模式
                        buffer.flip();
                        fileChannel.write(buffer);
                        //读完切换成写模式，能让管道继续读取文件的数据
                        buffer.clear();
                    }
                    //通知NoBlockClient2接收成功
                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                    writeBuffer.put("The fill is accept,thanks!".getBytes());
                    writeBuffer.flip();
                    client.write(writeBuffer);
                }
                //11.取消选择键(已经处理过的事件，就应该取消掉了)
                iterator.remove();
            }
        }
    }
}
