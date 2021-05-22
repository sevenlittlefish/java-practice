package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 基数排序（桶排序）：生成一个二维数组，纵列表示数组数组长度，横列为0~9十列，第一趟比较数组中个位数大小分派到不同列中再回收形成个位有序，
 * 第二趟比较十位数大小分派到不同列中再回收形成十位数有序，以此类推，最后形成有序序列
 */
public class BinSort {

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        int max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if(max < arr[i]){
                max = arr[i];
            }
        }
        for (int i = 1; max/i > 0; i*=10) {
            //定义一个二维数组
            int[][] twoArr = new int[arr.length][10];
            for (int j = 0; j < arr.length; j++) {
                //找到当前位数对应的下标
                int index = arr[j]/i%10;
                twoArr[j][index] = arr[j];
            }
            //将二维数组数据回收到一维数组中
            int k = 0;
            for (int m = 0; m < 10; m++) {
                for (int n = 0; n < arr.length; n++) {
                    if(twoArr[n][m] != 0){
                        arr[k++] = twoArr[n][m];
                    }
                }
            }
        }
    }
}
