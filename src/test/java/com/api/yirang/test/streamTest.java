package com.api.yirang.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(JUnit4.class)
public class streamTest {

    @Test
    public void streamFilter(){
        List<Integer> integerList = Arrays.asList(1,2,3,4,5,6,7);
        List<Integer> integerList2 = integerList.stream().filter(x -> x>=3).collect(Collectors.toList());

        System.out.println("List_1: " + integerList);
        System.out.println("List_2: " + integerList2);
    }

    @Test
    public void PQtest(){
        PriorityQueue<Long> longPQ = new PriorityQueue<>(Comparator.comparingLong(e -> e.longValue()));
        longPQ.add(4L);
        System.out.println("longPQ: " + longPQ);
        longPQ.add(5L);
        System.out.println("longPQ: " + longPQ);
        longPQ.add(2L);
        System.out.println("longPQ: " + longPQ);
        longPQ.add(100L);
        System.out.println("longPQ: " + longPQ);
        longPQ.add(200L);
        System.out.println("longPQ: " + longPQ);
        longPQ.add(250L);
        System.out.println("longPQ: " + longPQ);
        longPQ.add(20L);
        System.out.println("longPQ: " + longPQ);

        PriorityQueue<Long> secondPQ = new PriorityQueue<>(longPQ);

        longPQ.addAll(secondPQ);
        System.out.println("longPQ: " + longPQ);
        System.out.println("secondPQ: " + secondPQ);

        while(!longPQ.isEmpty()){
            System.out.println(longPQ.remove());
        }
    }

    @Test
    public void ListTest(){
        final List<Integer> integerList = new ArrayList<>();
        test(integerList);
        System.out.println("List: " + integerList);

    }

    private void test(List list){
        list.add("Good");
        list.add("Bye");
        List<String> str_list = Arrays.asList("시발", "일을 왜해");
        list.addAll(str_list);
    }
}
