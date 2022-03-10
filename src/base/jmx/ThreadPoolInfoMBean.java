package base.jmx;

/**
 * 接口命名规则：类名+MBean
 */
public interface ThreadPoolInfoMBean {

    boolean shutdown();

    boolean terminated();

    boolean terminating();

    int activeCount();

    long completedTaskCount();

    int corePoolSize();

    int largestPoolSize();

    int maximumPoolSize();

    int poolSize();

    long taskCount();
}
