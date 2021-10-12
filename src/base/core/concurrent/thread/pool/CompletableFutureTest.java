package base.core.concurrent.thread.pool;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

/**
 * 默认情况下 CompletableFuture 会使用公共的 ForkJoinPool 线程池，这个线程池默认创建的线程数是 CPU 的核数
 * （也可以通过 JVM option:-Djava.util.concurrent.ForkJoinPool.common.parallelism 来设置 ForkJoinPool 线程池的线程数）。
 * 如果所有 CompletableFuture 共享一个线程池，那么一旦有任务执行一些很慢的 I/O 操作，就会导致线程池中所有线程都阻塞在 I/O 操作上，
 * 从而造成线程饥饿，进而影响整个系统的性能。所以，强烈建议你要根据不同的业务类型创建不同的线程池，以避免互相干扰。
 *
 * 1、创建CompletableFuture对象：runAsync(Runnable runnable)、runAsync(Runnable runnable, Executor executor)、supplyAsync(Supplier supplier)、supplyAsync(Supplier supplier, Executor executor)
 * 2、描述串行关系：thenApply、thenAccept、thenRun、thenCompose
 * 3、描述and关系：thenCombine、thenAcceptBoth、runAfterBoth
 * 4、描述or关系：applyToEither、acceptEither、runAfterEither
 * 5、异常处理：exceptionally() 的使用非常类似于 try{}catch{}中的 catch{}，但是由于支持链式编程方式，所以相对更简单。
 * 既然有 try{}catch{}，那就一定还有 try{}finally{}，whenComplete() 和 handle() 系列方法就类似于
 * try{}finally{}中的 finally{}，无论是否发生异常都会执行 whenComplete() 中的回调函数 consumer 和 handle() 中的回调函数 fn。
 * whenComplete() 和 handle() 的区别在于 whenComplete() 不支持返回结果，而 handle() 是支持返回结果的
 */
public class CompletableFutureTest {

    private static void sleepRandom(){
        Random random = new Random();
        int time = random.nextInt(10)+1;
        try {
            Thread.sleep(time*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sleepEnough(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String delayedUpperCase(String s) {
        sleepRandom();
        return Objects.isNull(s) ? "" : s.toUpperCase();
    }

    private static String delayedLowerCase(String s) {
        sleepRandom();
        return Objects.isNull(s) ? "" : s.toLowerCase();
    }

    private static boolean isUpperCase(String res) {
        return Objects.equals(res,res.toUpperCase());
    }

    private static boolean isLowerCase(String res) {
        return Objects.equals(res,res.toLowerCase());
    }

    public static void main(String[] args) {
        supplyAsyncExample();
    }

    /**
     * 创建一个完成的CompletableFuture
     */
    public static void completedFutureExample(){
        CompletableFuture cf = CompletableFuture.completedFuture("message");
        System.out.println(cf.isDone());
        System.out.println(Objects.equals("message",cf.getNow(null)));
    }

    /**
     * 运行一个简单的异步阶段
     */
    public static void runAsyncExample(){
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            System.out.println("Is daemon ? "+Thread.currentThread().isDaemon());
            sleepRandom();
        });
        System.out.println(cf.isDone());
        sleepEnough();
        System.out.println(cf.isDone());
    }

    /**
     * 运行一个带返回值的异步方法
     */
    public static void supplyAsyncExample(){
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            sleepRandom();
            return "Hello world";
        });
        System.out.println(cf.isDone());
        cf.join();
        System.out.println(cf.getNow(null));
    }

    /**
     * 在前一个阶段上应用函数
     */
    public static void thenApplyExample(){
        CompletableFuture cf = CompletableFuture.completedFuture("message").thenApply(s -> {
            System.out.println("Is daemon ? "+Thread.currentThread().isDaemon());
            return s.toUpperCase();
        });
        System.out.println(Objects.equals("MESSAGE", cf.getNow(null)));
    }

    /**
     * 在前一个阶段上异步应用函数
     */
    public static void thenApplyAsyncExample(){
        CompletableFuture cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            System.out.println("Is daemon ? "+Thread.currentThread().isDaemon());
            sleepRandom();
            return s.toUpperCase();
        });
        System.out.println((cf.getNow(null)));
        System.out.println(Objects.equals("MESSAGE", cf.join()));
    }

    /**
     * 使用定制的Executor在前一个阶段上异步应用函数
     */
    static void thenApplyAsyncWithExecutorExample() {
        ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
            int count = 1;

            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "custom-executor-" + count++);
            }
        });
        CompletableFuture cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName().startsWith("custom-executor-"));
            System.out.println("Is daemon ? "+Thread.currentThread().isDaemon());
            sleepRandom();
            return s.toUpperCase();
        }, executor);
        System.out.println(cf.getNow(null));
        System.out.println(Objects.equals("MESSAGE", cf.join()));
    }

    /**
     * 消费前一阶段的结果
     */
    public static void thenAcceptExample(){
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture("thenAccept message").thenAccept(result::append);
        System.out.println(result.length() > 0 ? result : "Empty result");
    }

    /**
     * 异步地消费迁移阶段的结果
     */
    public static void thenAcceptAsyncExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture cf = CompletableFuture.completedFuture("thenAcceptAsync message")
                .thenAcceptAsync(result::append);
//        cf.join();
        System.out.println(result.length() > 0 ? result : "Empty result");
    }

    /**
     * 在两个完成的阶段其中之一上应用函数
     */
    public static void applyToEitherExample() {
        String original1 = "Message1";
        String original2 = "Message2";
        CompletableFuture cf1 = CompletableFuture.completedFuture(original1)
                .thenApplyAsync(CompletableFutureTest::delayedUpperCase);
        CompletableFuture cf2 = cf1.applyToEither(
                CompletableFuture.completedFuture(original2).thenApplyAsync(CompletableFutureTest::delayedLowerCase),
                s -> s + " from applyToEither");
        System.out.println(cf2.join());
    }

    /**
     * 在两个完成的阶段其中之一上调用消费函数
     */
    public static void acceptEitherExample() {
        String original1 = "Message1";
        String original2 = "Message2";
        StringBuilder result = new StringBuilder();
        CompletableFuture cf = CompletableFuture.completedFuture(original1)
                .thenApplyAsync(CompletableFutureTest::delayedUpperCase)
                .acceptEither(
                        CompletableFuture.completedFuture(original2).thenApplyAsync(CompletableFutureTest::delayedLowerCase),
                        s -> result.append(s).append("acceptEither"));
        cf.join();
        System.out.println(result.toString());
    }

    /**
     * 在两个阶段都执行完后运行一个 Runnable
     */
    public static void runAfterBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original)
                .thenApply(String::toUpperCase)
                .runAfterBoth(
                        CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                        () -> result.append("done"));
        System.out.println(result);
    }

    /**
     * 使用BiConsumer处理两个阶段的结果
     */
    public static void thenAcceptBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase)
                .thenAcceptBoth(
                        CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                        (s1, s2) -> result.append(s1 + s2));
        System.out.println(Objects.equals("MESSAGEmessage", result.toString()));
    }

    /**
     * 使用BiFunction处理两个阶段的结果
     */
    public static void thenCombineExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(CompletableFutureTest::delayedUpperCase)
                .thenCombine(
                        CompletableFuture.completedFuture(original).thenApply(CompletableFutureTest::delayedLowerCase),
                        (s1, s2) -> s1 + s2);
        System.out.println(Objects.equals("MESSAGEmessage", cf.getNow(null)));
    }

    /**
     * 异步使用BiFunction处理两个阶段的结果
     */
    public static void thenCombineAsyncExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(CompletableFutureTest::delayedUpperCase)
                .thenCombine(
                        CompletableFuture.completedFuture(original).thenApplyAsync(CompletableFutureTest::delayedLowerCase),
                        (s1, s2) -> s1 + s2);
        System.out.println(Objects.equals("MESSAGEmessage", cf.join()));
    }

    /**
     * 组合 CompletableFuture
     */
    public static void thenComposeExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original)
                .thenApply(CompletableFutureTest::delayedUpperCase)
                .thenCompose(upper -> CompletableFuture.completedFuture(original)
                        .thenApply(CompletableFutureTest::delayedLowerCase)
                        .thenApply(s -> upper + s));
        System.out.println(Objects.equals("MESSAGEmessage", cf.join()));
    }

    /**
     * 当几个阶段中的一个完成，创建一个完成的阶段
     */
    public static void anyOfExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(CompletableFutureTest::delayedUpperCase))
                .collect(Collectors.toList());
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((res, th) -> {
                    if (th == null) {
                        System.out.println(isUpperCase((String) res));
                        result.append(res);
                    }
                });
        System.out.println(result);
    }

    /**
     * 当几个异步阶段中的一个完成，创建一个完成的阶段
     */
    public static void anyOfAsyncExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableFutureTest::delayedUpperCase))
                .collect(Collectors.toList());
        CompletableFuture<Object> cf = CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((res, th) -> {
                    if (th == null) {
                        System.out.println(isUpperCase((String) res));
                        result.append(res);
                    }
                });
        cf.join();
        System.out.println(result);
    }

    /**
     * 当所有的阶段都完成后创建一个阶段
     */
    public static void allOfExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(CompletableFutureTest::delayedUpperCase))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((v, th) -> {
                    futures.forEach(cf -> System.out.println(isUpperCase((String) cf.getNow(null))));
                    result.append("done");
                });
        System.out.println(result);
    }

    /**
     * 当所有的阶段都完成后异步地创建一个阶段
     */
    public static void allOfAsyncExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableFutureTest::delayedUpperCase))
                .collect(Collectors.toList());
        CompletableFuture allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((v, th) -> {
                    futures.forEach(cf -> System.out.println(isUpperCase((String) cf.getNow(null))));
                    result.append("done");
                });
        allOf.join();
        System.out.println(result);
    }
}
