package base.core.io.nio.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;
import java.util.Scanner;

public class UDPClient {
    
    public static void main(String[] args) throws IOException {
        DatagramChannel client = DatagramChannel.open();
        client.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){
            String str = sc.nextLine();
            buffer.put((new Date().toString()+":\n"+str).getBytes());
            buffer.flip();
            client.send(buffer,new InetSocketAddress("127.0.0.1",777));
            buffer.clear();
        }
        sc.close();
    }
}
