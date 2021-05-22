package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 冒泡排序（Bubble Sort，台湾译为：泡沫排序或⽓泡排序）是⼀种简单的排序算法。它重复地⾛
 * 访过要排序的数列，⼀次⽐较两个元素，如果他们的顺序错误就把他们交换过来。⾛访数列的⼯
 * 作是重复地进⾏直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因
 * 为越⼤的元素会经由交换慢慢“浮”到数列的顶端，故名。
 */
public class BubbleSort {

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

    public static void sort(int[] arr){
        //外层循环是排序的趟数
        for (int i = 0; i < arr.length-1; i++) {
            //记录是否发⽣了置换， 0 表示没有发⽣置换、 1 表示发⽣了置换
            int change = 0;
            //内层循环是当前趟数需要⽐较的次数
            for (int j = 0; j < arr.length-1-i; j++) {
                //前⼀位与后⼀位与前⼀位⽐较，如果前⼀位⽐后⼀位要⼤，那么交换
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    //如果进到这⾥⾯了，说明发⽣置换了
                    change = 1;
                }
            }
            //如果⽐较完⼀趟没有发⽣置换，那么说明已经排好序了，不需要再执⾏下去了
            if(change == 0){
                break;
            }
        }
    }
}
