package base.core.io.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class NoBlockClient2 {

    public static void main(String[] args) throws IOException {
        //获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 567));
        //切换为非阻塞模式
        socketChannel.configureBlocking(false);
        //获取选择器
        Selector selector = Selector.open();
        //将通道注册到选择器上
        socketChannel.register(selector, SelectionKey.OP_READ);
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
        //轮训地获取选择器上已“就绪”的事件--->只要select()>0，说明已就绪
        while(selector.select() > 0){
            //获取当前选择器所有注册的“选择键”(已就绪的监听事件)
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            //获取已“就绪”的事件，(不同的事件做不同的事)
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //读事件就绪
                if(selectionKey.isReadable()){
                    //得到对应的通道
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while((len = channel.read(responseBuffer)) > 0){//此处应该注意，区别于文件io条件是：!=-1，网络io条件是：>0
                        responseBuffer.flip();
                        System.out.println(new String(responseBuffer.array(),0,len));
                    }
                }
                //取消选择键(已经处理过的事件，就应该取消掉了)
                iterator.remove();
            }
        }
    }
}
