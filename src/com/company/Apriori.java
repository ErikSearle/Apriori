package com.company;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class Apriori {

    /**
     *
     * @param transactions The set of transactions from the database to test against (The mountain to mine)
     * @param itemSet The set of items to test against the transaction database (The precious metal)
     * @param minimumThreshold A number indicating the minimum number of times a set from within the item set must
     *                         appear in the transactions to count as a frequentPattern. Must be a discrete number, not a
     *                         a percentage value
     * @return The set of frequent patterns found through recursive analysis
     */
    public static LinkedHashSet<TreeSet<Integer>> generateFrequentPatterns(LinkedHashSet<TreeSet<Integer>> transactions,
                                                                           LinkedHashSet<TreeSet<Integer>> itemSet,
                                                                           int minimumThreshold){
        LinkedHashSet<TreeSet<Integer>> frequentPatterns = new LinkedHashSet<>();
        /*
        Test each item in the item set against the list of transactions and at the end of run through the transactions
        list, if the number of hits is greater than the minimum threshold, add that set of items to a new item set
        called frequentPatterns
         */
        for(TreeSet<Integer> item : itemSet){
            int count = 0;
            for(TreeSet<Integer> transaction : transactions){
                if(transaction.containsAll(item)) count++;
            }
            if(count > minimumThreshold) frequentPatterns.add(item);
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
}
