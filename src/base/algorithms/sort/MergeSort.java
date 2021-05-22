package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 归并排序（MERGE-SORT）是建⽴在归并操作上的⼀种有效的排序算法,该算法是采⽤分治法
 * （Divide and Conquer）的⼀个⾮常典型的应⽤。将已有序的⼦序列合并，得到完全有序的序
 * 列；即先使每个⼦序列有序，再使⼦序列段间有序。若将两个有序表合并成⼀个有序表，称为⼆
 * 路归并。
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100);
        }
        int[] result = new int[arr.length];
        System.out.println(Arrays.toString(arr));
        recursiveSort(arr, result,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 1.将序列每相邻两个数字进行归并操作，形成floor(n/2)个序列，排序后每个序列包含两个元素
     * 2.将上述序列再次归并，形成floor(n/4)个序列，每个序列包含四个元素
     * 3.重复步骤2，直到所有元素排序完毕
     * @param arr
     * @param result
     * @param left
     * @param right
     */
    private static void recursiveSort(int[] arr, int[] result, int left, int right) {
        if(left >= right) return;
        int middle = (left+right)/2;
        //直到拆分到不可再拆分为止
        recursiveSort(arr, result, left, middle);
        recursiveSort(arr, result, middle+1, right);
        int i = left;
        int j = middle+1;
        int k = left;
        //⽐较这两个数组的值，哪个⼩，就往数组上放
        while(i <= middle && j <= right){
            //谁⽐较⼩，谁将元素放⼊⼤数组中,移动指针，继续⽐较下⼀个
            if(arr[i] < arr[j]){
                result[k] = arr[i];
                i++;
                k++;
            }else{
                result[k] = arr[j];
                j++;
                k++;
            }
        }
        //如果左边的数组还没⽐较完，右边的数都已经完了，那么将左边的数移动到⼤数组中
        while(i <= middle){
            result[k] = arr[i];
            i++;
            k++;
        }
        //如果右边的数组还没⽐较完，左边的数都已经完了，那么将右边的数移动到⼤数组中
        while(j <= right){
            result[k] = arr[j];
            j++;
            k++;
        }
        for (int index = left; index <= right; index++) {
            arr[index] = result[index];
        }
    }
}
