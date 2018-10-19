package com.company;

import java.util.Comparator;


public class IntArrayComparator implements Comparator<int[]> {

    public IntArrayComparator(){}

    @Override
    public int compare(int[] ints, int[] t1) {
        for(int i=0; i<ints.length && i<t1.length; i++){
            if(ints[i] > t1[i]) return 1;
            else if(ints[i] < t1[i]) return -1;
        }
        if(t1.length > ints.length) return -1;
        else if(ints.length > t1.length) return 1;
        else return 0;
    }
}
