package com.company;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class Apriori {

    /**
     *
     * @param transactions The set of transactions from the database to test against (The mountain to mine)
     * @param itemSets The set of items to test against the transaction database (The precious metal)
     * @param minimumThreshold A number indicating the minimum number of times a set from within the item set must
     *                         appear in the transactions to count as a frequentPattern. Must be a discrete number, not a
     *                         a percentage value
     * @return The set of frequent patterns found through recursive analysis
     */
    public static LinkedHashSet<TreeSet<Integer>> generateFrequentPatterns(int[][] transactions,
                                                                           LinkedHashSet<TreeSet<Integer>> itemSets,
                                                                           int minimumThreshold){
        System.out.println("Starting loop");

        LinkedHashSet<TreeSet<Integer>> frequentPatterns = new LinkedHashSet<>();
        /*
        Test each item in the item set against the list of transactions and at the end of run through the transactions
        list, if the number of hits is greater than the minimum threshold, add that set of items to a new item set
        called frequentPatterns
         */
        for(TreeSet<Integer> itemSet : itemSets){
            if(itemSet.first() % 1000 == 0) {
                System.out.println(itemSet.first());
            }
            int count = 0;
            for(int[] transaction: transactions){
                if(transaction[0] > itemSet.first()) break;
                if(transaction[transaction.length - 1] < itemSet.last()) continue;
                if(transactionContainsItemSet(itemSet, transaction)) count++;
            }
            if(count >= minimumThreshold) frequentPatterns.add(itemSet);
        }

        /*
        Generate new candidates based on the frequentPatterns
         */
        LinkedHashSet<TreeSet<Integer>> newCandidates = CandidateGenerator.generate(frequentPatterns);

        /*
        If there are no new candidates, return the frequentPatterns only
         */
        if(newCandidates.isEmpty()) return frequentPatterns;

        /*
        Else recursively call this function with the new set of candidates. Add the frequentPatterns to the set returned
        from the recursive call
         */
        frequentPatterns.addAll(generateFrequentPatterns(transactions, newCandidates, minimumThreshold));
        return frequentPatterns;
    }

    private static boolean transactionContainsItemSet(TreeSet<Integer> itemSet, int[] transaction){
        Iterator<Integer> items = itemSet.iterator();
        int nextLoopStartsAt = 0;
        while(items.hasNext()){
            int item = items.next();
            for(int i=nextLoopStartsAt; i<transaction.length; i++){
                if(transaction[i] == item){
                    nextLoopStartsAt = i+1;
                    break;
                }
                else if(transaction[i] > item) return false;
                else if(i == transaction.length - 1) return false;
            }
            if(items.hasNext() && nextLoopStartsAt >= transaction.length) return false;
        }
        return true;
    }
}
