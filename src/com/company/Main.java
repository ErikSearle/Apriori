package com.company;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        System.out.println("Program started");
        long startTime = System.currentTimeMillis();

	    String inputFile = args[0];
	    String minimumThreshold = args[1];
	    Scanner scan = null;

	    //Open the file
	    try {
            scan = new Scanner(new File(inputFile));
        } catch(IOException e){
	        System.out.println("Unable to read from file");
	        System.exit(1);
        }

        //read the first line of the file denoting the number of transactions.
        int numTransactions = scan.nextInt();

	    //create a transaction database to be filled later.
        int[][] transactions = new int[numTransactions][];


        //Potential itemsets are first initialized as a treeset of tree sets. This is to ensure that we only test for
        //items that actually exist in our database, and everything begins in sorted order. After this initialization,
        // we will convert the outer treeset to a linked hashset because the apriori algorithm will always maintain
        // sorted order and a linkedhashset will be faster than a treeset for insertions. The following comparator
        // allows us to sort treesets based on their items. It is not a complete comparator that could be used
        // elsewhere, but is perfectly sufficient to our needs.
        TreeSet<TreeSet<Integer>> itemSet = new TreeSet<>(new Comparator<TreeSet<Integer>>() {
            @Override
            public int compare(TreeSet<Integer> t0, TreeSet<Integer> t1) {
                Iterator<Integer> t0Iterator = t0.iterator();
                Iterator<Integer> t1Iterator = t1.iterator();
                while(t0Iterator.hasNext() && t1Iterator.hasNext()){
                    int t0Item = t0Iterator.next();
                    int t1Item = t1Iterator.next();
                    if(t0Item > t1Item) return 1;
                    else if(t0Item < t1Item) return -1;
                }
                return 0;
            }
        });


        //Read the file and put transactions into the transaction set. Simultaneously put items into the item set.
	    while(scan.hasNext()){
	        int transNumber = scan.nextInt() - 1;
	        int numItems = scan.nextInt();
	        transactions[transNumber] = new int[numItems];
	        for(int i=0; i<numItems; i++){
	            transactions[transNumber][i] = scan.nextInt();
	            TreeSet<Integer> potentialItem = new TreeSet<>();
	            potentialItem.add(transactions[transNumber][i]);
	            itemSet.add(potentialItem);
            }
        }


	    //Sort the transactions
	    Arrays.sort(transactions, new Comparator<int[]>() {
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
        });
	    System.out.println("File read and sorted after " + (System.currentTimeMillis()-startTime) + " millis");

        //Convert the itemset to a linked hash set as explained above
        LinkedHashSet<TreeSet<Integer>> linkedItemSet = new LinkedHashSet<>(itemSet);

        //Generate frequent patterns
        LinkedHashSet<TreeSet<Integer>> frequentPatterns =
                Apriori.generateFrequentPatterns(transactions, linkedItemSet,
                        processMinThreshold(minimumThreshold, numTransactions));

        //Print frequent patterns
        frequentPatterns.forEach(new Consumer<TreeSet<Integer>>() {
            @Override
            public void accept(TreeSet<Integer> integers) {
                StringBuilder sb = new StringBuilder();
                for(Integer integer : integers){
                    sb.append(integer).append(" ");
                }
                System.out.println(sb.toString());
            }
        });
    }

    private static int processMinThreshold(String minThresh, int numTransactions){
        if(minThresh.charAt(minThresh.length()-1) == '%'){
            String minThreshNumbersOnly = minThresh.substring(0, minThresh.length()-1);
            float minThreshPercentage = Float.valueOf(minThreshNumbersOnly);
            if(minThreshPercentage > 100 || minThreshPercentage < 0){
                System.out.println("Invalid percentage. Percentage must be between 0 and 100.");
                System.exit(1);
            }
            return (int) Math.ceil(((float) numTransactions)*(minThreshPercentage/100));
        }
        int minThreshNumber = Integer.valueOf(minThresh);
        if(minThreshNumber < 0 || minThreshNumber > numTransactions){
            System.out.println("Invalid minimum threshold. Threshold must be betwen 0 and " + numTransactions);
            System.exit(1);
        }
        System.out.println(minThreshNumber);
        return minThreshNumber;
    }

}