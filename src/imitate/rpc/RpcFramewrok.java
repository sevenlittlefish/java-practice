package imitate.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

public class RpcFramewrok {

    public static void export(Object service, int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        while(true){
            Socket socket = server.accept();
            new Thread(()->{
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    //方法名
                    String methodName = (String)in.readObject();
                    //参数类型
                    Class<?>[] parameterTypes = (Class<?>[])in.readObject();
                    //参数
                    Object[] args = (Object[])in.readObject();
                    //找到方法
                    Method method = service.getClass().getMethod(methodName, parameterTypes);
                    //调用方法
                    Object result = method.invoke(service, args);
                    //输出结果
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(result);
                } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static <T> T refer(Class<T> interfaceClass, String host, int port) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(host, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                //传递方法名
                out.writeObject(method.getName());
                //传递参数类型
                out.writeObject(method.getParameterTypes());
                //传递参数
                out.writeObject(args);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Object result = in.readObject();
                return result;
            }
        });
    }
}
