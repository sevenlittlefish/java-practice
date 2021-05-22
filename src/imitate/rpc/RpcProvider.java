package imitate.rpc;

import java.io.IOException;

public class RpcProvider {

    public static void main(String[] args) throws IOException {
        RpcService service = new RpcServiceImpl();
        RpcFramewrok.export(service,2020);
    }
}
