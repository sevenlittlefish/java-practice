package base.jmx;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxClient {

    public static void main(String[] args) throws Exception {
        JMXServiceURL jmxServiceURL = new JMXServiceURL(JmxServer.url);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL);
        MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();

        ThreadPoolInfoMBean proxy = MBeanServerInvocationHandler.newProxyInstance(connection, new ObjectName(JmxServer.objName), ThreadPoolInfoMBean.class, false);
        while (true) {
            System.out.println("shutdown:" + proxy.shutdown());
            System.out.println("terminated:" + proxy.terminated());
            System.out.println("terminating:" + proxy.terminating());
            System.out.println("corePoolSize:" + proxy.corePoolSize());
            System.out.println("maximumPoolSize:" + proxy.maximumPoolSize());
            System.out.println("largestPoolSize:" + proxy.largestPoolSize());
            System.out.println("poolSize:" + proxy.poolSize());
            System.out.println("activeCount:" + proxy.activeCount());
            System.out.println("taskCount:" + proxy.taskCount());
            System.out.println("completedTaskCount:" + proxy.completedTaskCount());
            System.out.println("====================================");
            Thread.sleep(1000);
        }
    }
}
