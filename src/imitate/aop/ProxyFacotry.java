package imitate.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyFacotry {

    private static Object target;
    private static Aop aop;

    public static Object getProxyInstance(Object _target, Aop _aop){
        target = _target;
        aop = _aop;

        return java.lang.reflect.Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                aop.begin();
                Object result = method.invoke(target, args);
                aop.end();
                return result;
            }
        });
    }
}
