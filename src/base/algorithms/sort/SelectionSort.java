package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 选择排序(Selection sort)是⼀种简单直观的排序算法。它的⼯作原理是每⼀次从待排序的数据元
 * 素中选出最⼩(或最⼤)的⼀个元素，存放在序列的起始(末尾)位置，直到全部待排序的数据元素排
 * 完。选择排序是不稳定的排序⽅法（⽐如序列[5， 5， 3]第⼀次就将第⼀个[5]与[3]交换，导致第
 * ⼀个5挪动到第⼆个5后⾯）。
 */
public class SelectionSort {

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
        for (int i = 0; i < arr.length-1; i++) {
            //新的趟数、将⻆标重新赋值为0
            int index = 0;
            //内层循环控制遍历数组的个数并得到最⼤数的⻆标
            for (int j = 0; j < arr.length-i; j++) {
                if(arr[j] > arr[index]){
                    index = j;
                }
            }
            int temp = arr[index];
            arr[index] = arr[arr.length-i-1];
            arr[arr.length-i-1] = temp;
        }
    }
}
