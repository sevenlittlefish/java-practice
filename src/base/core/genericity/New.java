package base.core.genericity;

import java.util.*;

public class New {
    public static <K,V> Map<K,V> map(){
        return new HashMap<>();
    }
    public static <T> List<T> list(){
        return new ArrayList<>();
    }
    public static <T> Set<T> set(){
        return new HashSet<>();
    }
    public static<T> Queue<T> queue(){
        return new LinkedList<>();
    }

    public static void main(String[] args) {
        Map<String,List<String>> map = New.map();
        List<String> list = New.list();
        Set<Long> set = New.set();
        Queue<String> qs = New.queue();
    }
}
