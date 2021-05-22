package base.generatecode;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

public class GenerateUniqueCode {

    private static ExecutorService threadpool = Executors.newCachedThreadPool();
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));
    private static List<String> list = new CopyOnWriteArrayList<>();
    private static CountDownLatch countDown = null;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        //主线程等待
        int threadNum = 100;
        countDown = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            //threadpool.execute(new GenerateCodeTask());
            executor.execute(new GenerateCodeTask());
        }
        threadpool.shutdown();
        executor.shutdown();
        try {
            Thread.sleep(1000);
            countDown.await();
            System.out.println("生成完毕");
            Set<String> set = new HashSet<>(list);
            System.out.println(list.size() == set.size() ? "无重复数据，list："+list.size() : "有重复数据，list："+list.size()+"，set："+set.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("耗时："+(System.currentTimeMillis() - start)+" ms");
    }

    private static class GenerateCodeTask implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                list.add(getGUID());
            }
            System.out.println(Thread.currentThread().getName()+" 线程生码完成");
            countDown.countDown();
        }
    }

    //16位唯一串
    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(2);
            switch (type){
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char)(rd.nextInt(26)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char)(rd.nextInt(26)+97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    //8位唯一串（经测试chars36在生成一千万数据下有几十条重复，chars62暂时没测出问题）
    public static String[] chars36 = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    public static String[] chars62 = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    public static String generateInviteCode(){
        StringBuilder shortBuilder = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            // 如果是 chars62,则是x%62
            shortBuilder.append(chars36[x % 36]);
        }
        return shortBuilder.toString();
    }
}
