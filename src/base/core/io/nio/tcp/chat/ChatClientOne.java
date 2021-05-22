package base.core.io.nio.tcp.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChatClientOne {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 666));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        receiveServerMsg(selector);
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){
            String content = sc.nextLine();
            buffer.put(content.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
    }

    private static void receiveServerMsg(Selector selector){
        new Thread(()->{
            try {
                while (selector.select() > 0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        if(selectionKey.isReadable()){
                            SocketChannel channel = (SocketChannel)selectionKey.channel();
                            ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
                            int len = 0;
                            while((len = channel.read(responseBuffer)) > 0){
                                responseBuffer.flip();
                                System.out.print(new String(responseBuffer.array(),0,len));
                            }
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
