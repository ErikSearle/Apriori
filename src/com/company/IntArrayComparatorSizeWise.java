package com.company;

import java.util.Comparator;

public class IntArrayComparatorSizeWise implements Comparator<int[]> {

    public IntArrayComparatorSizeWise(){}

    @Override
    public int compare(int[] ints, int[] t1) {
        if(ints.length > t1.length) return 1;
        if(t1.length > ints.length) return -1;

        for(int i=0; i<ints.length; i++){
            if(ints[i] > t1[i]) return 1;
            else if(ints[i] < t1[i]) return -1;
        }
        return 0;
    }
}
