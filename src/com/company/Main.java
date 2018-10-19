package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        System.out.println("Program started");
        long startTime = System.currentTimeMillis();

	    String inputFile = args[0];
	    String minimumThreshold = args[1];
	    BufferedReader fileReader = null;

	    //Open the file
	    try {
            fileReader = new BufferedReader(new FileReader(new File(inputFile)));
        } catch(IOException e){
	        System.out.println("Unable to read from file");
	        System.exit(1);
        }

        //read the first line of the file denoting the number of transactions.
        int numTransactions = 0;
        try {
            numTransactions = Integer.valueOf(fileReader.readLine());
        } catch(IOException e){
            System.out.println("Unable to read from file");
            System.exit(1);
        }

	    //create a transaction database to be filled later.
        LinkedList<LinkedHashSet<Integer>> transactions = new LinkedList<>();


        //Potential itemsets are first initialized as a treeset of tree sets. This is to ensure that we only test for
        //items that actually exist in our database, and everything begins in sorted order. After this initialization,
        // we will convert the outer treeset to a linked hashset because the apriori algorithm will always maintain
        // sorted order and a linkedhashset will be faster than a treeset for insertions. The following comparator
        // allows us to sort treesets based on their items. It is not a complete comparator that could be used
        // elsewhere, but is perfectly sufficient to our needs.
//        TreeSet<TreeSet<Integer>> itemSet = new TreeSet<>(new Comparator<TreeSet<Integer>>() {
//            @Override
//            public int compare(TreeSet<Integer> t0, TreeSet<Integer> t1) {
//
//            }
//        });


        //Read the file and put transactions into the transaction set. Simultaneously put items into the item set.
        try {
            String line = fileReader.readLine();
            while (line != null) {
                String[] splitLine = line.split("\\s+");
                LinkedHashSet<Integer> trans = new LinkedHashSet<>();
                for(int i=2; i<splitLine.length; i++){
                    trans.add(Integer.valueOf(splitLine[i]));
                }
                transactions.add(trans);
                line = fileReader.readLine();
            }
        } catch(IOException e){
            System.out.println("Unable to read from file");
            System.exit(1);
        }


        System.out.println((System.currentTimeMillis()-startTime));
	    //Sort the transactions
	    transactions.sort(new LinkedHashSetComparator());
	    System.out.println("File read and sorted after " + (System.currentTimeMillis()-startTime) + " millis");

	    TreeMap<int[], Integer> frequentPatterns = Apriori.generateFrequentPatterns(transactions, processMinThreshold(minimumThreshold, numTransactions));
//
//        //Generate frequent patterns
//        LinkedHashSet<TreeSet<Integer>> frequentPatterns =
//                Apriori.generateFrequentPatterns(transactions, linkedItemSet,
//                        processMinThreshold(minimumThreshold, numTransactions));
//
//        //Print frequent patterns
//        frequentPatterns.forEach(new Consumer<TreeSet<Integer>>() {
//            @Override
//            public void accept(TreeSet<Integer> integers) {
//                StringBuilder sb = new StringBuilder();
//                for(Integer integer : integers){
//                    sb.append(integer).append(" ");
//                }
//                System.out.println(sb.toString());
//            }
//        });
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