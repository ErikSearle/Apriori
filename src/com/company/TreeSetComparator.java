package com.company;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class TreeSetComparator implements Comparator<TreeSet<Integer>> {

    public TreeSetComparator(){

    }

    @Override
    public int compare(TreeSet<Integer> integers, TreeSet<Integer> t1) {
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
