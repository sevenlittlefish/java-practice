package base.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

public class TestSort {

    public static void main(String[] args) {
        int[] arr = new int[11];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr) {
        for (int incr = arr.length/2; incr > 0; incr/=2) {
            for (int i = incr; i < arr.length; i++) {
                for (int j = i-incr; j >= 0; j--) {
                    if (arr[j] > arr[j+incr]){
                        int temp = arr[j];
                        arr[j] = arr[j+incr];
                        arr[j+incr] = temp;
                    }
                }
            }
        }
    }

    private static void initHeap(int[] arr){
        for (int i = arr.length-1; i >= 0; i--) {
            heapify(arr,i,arr.length);
        }
    }

    private static void heapify(int[] arr,int index,int end){
        int left = 2*index+1;
        int right = 2*index+2;
        int max = left;
        if (right < end && arr[left] < arr[right])
            max = right;
        if (left < end && arr[max] > arr[index]){
            int temp = arr[max];
            arr[max] = arr[index];
            arr[index] = temp;
            heapify(arr, max, end);
        }
    }
}
