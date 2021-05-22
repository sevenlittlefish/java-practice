package base.core.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListCompareTest {

    public static void main(String[] args) {
        arrayCompareLink();
    }

    public static void arrayCompareLink(){
        int len = 10000000;
        List<Integer> arrList = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            arrList.add(i);
        }
        List<Integer> linkList = new LinkedList<>(arrList);

        long start = System.currentTimeMillis();
        arrList.add(len/4,777);
        long end = System.currentTimeMillis();
        System.out.println("ArrayList add "+(end-start)+" ms");

        start = System.currentTimeMillis();
        linkList.add(len/4,777);
        end = System.currentTimeMillis();
        System.out.println("LinkedList add "+(end-start)+" ms");

        start = System.currentTimeMillis();
        arrList.get(len/2);
        end = System.currentTimeMillis();
        System.out.println("ArrayList get "+(end-start)+" ms");

        start = System.currentTimeMillis();
        linkList.get(len/2);
        end = System.currentTimeMillis();
        System.out.println("LinkedList get "+(end-start)+" ms");
    }
}
