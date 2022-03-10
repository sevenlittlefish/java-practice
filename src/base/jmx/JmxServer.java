package base.jmx;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JmxServer {

    public static final String objName = "sevenfish:type=threadPoolExecutor,name=lhr";

    public static final String url = "service:jmx:rmi:///jndi/rmi://localhost:9504/jmxrmi";

    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        //MBeanServer是负责管理MBean的，一般一个JVM只有一个MBeanServer，所有的MBean都要注册到MBeanServer上，并通过MBeanServer对外提供服务
        //一般用ManagementFactory.getPlatformMBeanServer()方法获取当前JVM内的MBeanServer
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        //自定义命名空间:type=自定义类型,name=自定义名称
        ObjectName objectName = new ObjectName(objName);
        //注册，使用JConsole、VisualVM连接查看即可
        mBeanServer.registerMBean(new ThreadPoolInfo(executor), objectName);

        //若需要开启JMX rmi远程连接，则添加如下代码，即可使用JConsole、VisualVM通过ip:port访问，或自定义client访问
        LocateRegistry.createRegistry(9504);
        //url格式：service:jmx:rmi:///jndi/rmi://host:port/jmxrmi
        JMXServiceURL jmxServiceURL = new JMXServiceURL(url);
        JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mBeanServer);
        jmxConnectorServer.start();

        //测试代码
        while (true) {
            executor.execute(() -> {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                int i = random.nextInt(100, 1000);
                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
