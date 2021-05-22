package imitate.rpc;

import java.io.IOException;

public class RpcConsumer {

    public static void main(String[] args) throws IOException {
        RpcService service = RpcFramewrok.refer(RpcService.class, "127.0.0.1", 2020);
        System.out.println(service.sayHello("baby"));
    }
}
