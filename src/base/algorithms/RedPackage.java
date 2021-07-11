package base.algorithms;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

/**
 * 给定100元，10个人来抢红包，10个人随机得到一些金额，请编写一个红包算法，
 * 入参为金额与人数，出参为金额的随机数组，约束条件为最大红包不能超过总金额的90%
 */
public class RedPackage {
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            System.out.println(Arrays.asList(distribute(new BigDecimal(100),10)));
        }
    }

    /**
     * 算法细节：
     * 1.初始化一个数组长度为人数的数组且每个数组中分配0.01元，计算出剩余金额
     * 2.用剩余的金额（A）生成随机金额（B），B不大于单个人所能接受的最大金额值（C），再将A重新赋值A-=B
     * 3.随机取数组中的一个下标，将B累加到当前下标的金额值中
     * 4.若累加的金额值大于C，则回到步骤3重新选取另外一个下标进行累加
     * 5.若A大于0则继续回到步骤2执行
     * 5.最后返回数组
     * @param totalMoney
     * @param num
     * @return
     */
    public static BigDecimal[] distribute(BigDecimal totalMoney,int num){
        //步骤1
        BigDecimal[] arr = new BigDecimal[num];
        BigDecimal baseMoney = new BigDecimal("0.01");
        Arrays.fill(arr, baseMoney);

        BigDecimal minTotalMoney = baseMoney.multiply(new BigDecimal(num));
        BigDecimal maxMoney = totalMoney.multiply(new BigDecimal("0.9"));
        BigDecimal buildMaxMoney = maxMoney.subtract(baseMoney);
        if (totalMoney.compareTo(minTotalMoney) < 0)
            throw new RuntimeException("每人分得金额必须大于0.01元");

        Random random = new Random();
        BigDecimal remainMoney = totalMoney.subtract(minTotalMoney);

        while(remainMoney.compareTo(BigDecimal.ZERO) > 0){
            //步骤2
            BigDecimal addMoney = buildMoney(remainMoney, buildMaxMoney);
            remainMoney = remainMoney.subtract(addMoney);
            //步骤3、4
            while (true){
                int index = random.nextInt(num);
                BigDecimal resultMoney = arr[index].add(addMoney);
                if (resultMoney.compareTo(maxMoney) <= 0){
                    arr[index] = resultMoney;
                    break;
                }
            }
            //可替代步骤3、4，使红包金额分散得更加均匀
            /*while (addMoney.compareTo(BigDecimal.ZERO) > 0){
                BigDecimal realAddMoney = buildMoney(addMoney, addMoney);
                int index = random.nextInt(num);
                BigDecimal resultMoney = arr[index].add(realAddMoney);
                if (resultMoney.compareTo(maxMoney) <= 0){
                    arr[index] = resultMoney;
                    addMoney = addMoney.subtract(realAddMoney);
                }
            }*/
        }
        //校验算法是否正确
        validate(arr, totalMoney, maxMoney);
        return arr;
    }

    public static BigDecimal buildMoney(BigDecimal curMoney,BigDecimal maxMoney){
        curMoney = curMoney.compareTo(maxMoney) > 0 ? maxMoney : curMoney;
        Random random = new Random();
        int val = random.nextInt(curMoney.multiply(new BigDecimal(100)).intValue())+1;
        return new BigDecimal(val).divide(new BigDecimal(100));
    }

    private static void validate(BigDecimal[] arr, BigDecimal totalMoney, BigDecimal maxMoney) {
        BigDecimal resultMoney = BigDecimal.ZERO;
        for (BigDecimal money : arr) {
            if (money.compareTo(maxMoney) > 0){
                System.out.println("======================");
                System.out.println(Arrays.asList(arr));
                throw new RuntimeException("算法有误，单个用户最大金额："+money+"元超过阈值："+money+"元");
            }
            resultMoney = resultMoney.add(money);
        }
        if (resultMoney.compareTo(totalMoney) != 0){
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(Arrays.asList(arr));
            throw new RuntimeException("算法有误，发放总金额："+resultMoney+"元与初始总金额"+totalMoney+"元不匹配");
        }
    }
}
