package com.company;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class LinkedHashSetComparator implements Comparator<LinkedHashSet<Integer>> {

    public LinkedHashSetComparator(){

    }

    @Override
    public int compare(LinkedHashSet<Integer> integers, LinkedHashSet<Integer> t1) {
        Iterator<Integer> firstIterator = integers.iterator();
        Iterator<Integer> secondIterator = t1.iterator();
        while(firstIterator.hasNext() && secondIterator.hasNext()){
            int firstNext = firstIterator.next();
            int secondNext = secondIterator.next();
            if(firstNext > secondNext) return 1;
            else if(firstNext < secondNext) return -1;
        }
        if(firstIterator.hasNext())return 1;
        if(secondIterator.hasNext())return -1;
        return 0;
    }
}
