package imitate.rpc;

public class RpcServiceImpl implements RpcService {
    @Override
    public String sayHello(String name) {
        return "Hello everybody,i love my "+name;
    }
}
