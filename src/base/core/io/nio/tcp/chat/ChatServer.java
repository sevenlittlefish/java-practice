package base.core.io.nio.tcp.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ChatServer {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(666));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        while(selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isAcceptable()){
//                    ServerSocketChannel ssc = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    //加入群聊
                    ChatHolder.join(socketChannel);
                }else if(selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    if(socketChannel.read(buffer) > 0){
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String content = new String(bytes, StandardCharsets.UTF_8).replace("\r\n","");
                        if(Objects.equals("quit",content)){
                            //退出群聊
                            ChatHolder.quit(socketChannel);
                            selectionKey.cancel();
                            socketChannel.close();
                        }else{
                            //扩散说话
                            ChatHolder.propagate(socketChannel,content);
                        }
                    }
                }
                iterator.remove();
            }
        }
    }

    private static class ChatHolder{
        static final Map<SocketChannel,String> USER_MAP = new ConcurrentHashMap<>();

        /**
         * 加入群聊
         * @param socketChannel
         */
        static void join(SocketChannel socketChannel){
            //有人加入就给他分配一个id
            String userId = "用户"+ ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
            send(socketChannel,"您的id为:"+userId+"\n\r");
            for (SocketChannel channel : USER_MAP.keySet()) {
                send(channel,userId+" 加入了群聊"+"\n\r");
            }
            USER_MAP.put(socketChannel,userId);
        }

        /**
         * 退出群聊
         * @param socketChannel
         */
        static void quit(SocketChannel socketChannel){
            String userId = USER_MAP.get(socketChannel);
            send(socketChannel,"您退出了群聊"+"\n\r");
            for (SocketChannel channel : USER_MAP.keySet()) {
                if(channel != socketChannel){
                    send(channel,userId+" 退出了群聊"+"\n\r");
                }
            }
        }

        /**
         * 扩散说话
         * @param socketChannel
         * @param content
         */
        public static void propagate(SocketChannel socketChannel,String content){
            String userId = USER_MAP.get(socketChannel);
            for (SocketChannel channel : USER_MAP.keySet()) {
                if(channel != socketChannel){
                    send(channel, userId + ": "+content+ "\n\r");
                }
            }
        }

        /**
         * 发送消息
         * @param socketChannel
         * @param content
         * @throws IOException
         */
        private static void send(SocketChannel socketChannel, String content){
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(content.getBytes());
                buffer.flip();
                socketChannel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
