package base.core.concurrent.thread.pool.custom;

public class DiscardRejectPolicy implements RejectPolicy {

    @Override
    public void reject(Runnable task, MyThreadPoolExecutor executor) {
        //do nothing
        System.out.println("discard one task");
    }
}
