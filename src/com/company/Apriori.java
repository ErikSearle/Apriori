package com.company;

import java.util.*;

public class Apriori {

    public static TreeMap<int[], Integer> generateFrequentPatterns(LinkedList<TreeSet<Integer>> transactions, int minimumThreshold){
        TreeMap<int[], Integer> singletons = new TreeMap<>(new IntArrayComparatorItemWise());
        for(TreeSet<Integer> transaction: transactions){
            for(Integer item: transaction){
                int[] itemSet = new int[1];
                itemSet[0] = item;
                Integer currentVal = singletons.put(itemSet, 1);
                if(currentVal != null) singletons.put(itemSet, ++currentVal);
            }
        }
        pruneInfrequentSets(singletons, minimumThreshold);
        singletons.putAll(generateFrequentPatterns(transactions, CandidateGenerator.generate(singletons), minimumThreshold));
        return singletons;
    }

    /**
     *
     * @param transactions The set of transactions from the database to test against (The mountain to mine)
     * @param itemSets The set of items to test against the transaction database (The precious metal)
     * @param minimumThreshold A number indicating the minimum number of times a set from within the item set must
     *                         appear in the transactions to count as a frequentPattern. Must be a discrete number, not a
     *                         a percentage value
     * @return The set of frequent patterns found through recursive analysis
     */
    private static Map<int[], Integer> generateFrequentPatterns(LinkedList<TreeSet<Integer>> transactions,
                                                                           TreeMap<int[], Integer> itemSets,
                                                                           int minimumThreshold){

        TreeMap<int[], Integer> frequentPatterns = new TreeMap<>(new IntArrayComparatorItemWise());
        /*
        Test each item in the item set against the list of transactions and at the end of run through the transactions
        list, if the number of hits is greater than the minimum threshold, add that set of items to a new item set
        called frequentPatterns
         */
        for(int[] itemSet: itemSets.keySet()){
            int count = 0;
            for(TreeSet<Integer> transaction: transactions){
                if(transaction.first() > itemSet[0]) break;
                if(transaction.last() < itemSet[itemSet.length-1]) continue;
                for(int i=0; i<itemSet.length; i++){
                    if(!transaction.contains(itemSet[i])) break;
                    if(i == itemSet.length-1) count++;
                }
            }
            if(count > minimumThreshold) frequentPatterns.put(itemSet, count);
        }

        /*
        Generate new candidates based on the frequentPatterns
         */
        TreeMap<int[], Integer> newCandidates = CandidateGenerator.generate(frequentPatterns);

        /*
        If there are no new candidates, return the frequentPatterns only
         */
        if(newCandidates.isEmpty()) return frequentPatterns;

        /*
        Else recursively call this function with the new set of candidates. Add the frequentPatterns to the set returned
        from the recursive call
         */
        frequentPatterns.putAll(generateFrequentPatterns(transactions, newCandidates, minimumThreshold));
        return frequentPatterns;
    }


    private static void pruneInfrequentSets(TreeMap<int[], Integer> tree, int minThresh){
        ArrayList<int[]> keysToRemove = new ArrayList<>();
        for(int[] key: tree.keySet()){
            if(tree.get(key) < minThresh) keysToRemove.add(key);
        }
        for(int[] key: keysToRemove) tree.remove(key);
    }
}
