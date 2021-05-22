package base.core.io.nio.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class UDPServer {

    public static void main(String[] args) throws IOException {
        DatagramChannel server = DatagramChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress("127.0.0.1",777));
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_READ);
        while(selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isReadable()){
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    server.receive(buffer);
                    buffer.flip();
                    System.out.println(new String(buffer.array(),0,buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }
    }
}
