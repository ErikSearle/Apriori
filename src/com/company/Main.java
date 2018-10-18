package com.company;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
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

        //read the first line of the file denoting the number of transactions. The second line here just jumps us past
        //the rest of the empty space on the first line
        int numTransactions = scan.nextInt();
	    scan.nextLine();

	    //create a transaction database to be filled later. Individual transactions are Treesets to ensure that items
        // are in sorted order. The list of transactions is a linked hash set to maintain order of insertion
        LinkedHashSet<TreeSet<Integer>> transactionData = new LinkedHashSet<>();


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
        long startTime = System.currentTimeMillis();
	    while(scan.hasNextLine()){
	        String line = scan.nextLine();
	        String[] lineData = line.split("\\t");
	        String[] transData = lineData[2].split(" ");
	        TreeSet<Integer> transaction = new TreeSet<>();
	        for(String datum : transData){
	            if(!datum.equals("")){
	                int item = Integer.valueOf(datum);
	                transaction.add(item);
	                TreeSet<Integer> singleton = new TreeSet<>();
	                singleton.add(item);
	                itemSet.add(singleton);
                }
            }
            transactionData.add(transaction);
        }
        System.out.println(System.currentTimeMillis()-startTime + "millis");


        //Convert the itemset to a linked hash set as explained above
        LinkedHashSet<TreeSet<Integer>> linkedItemSet = new LinkedHashSet<>(itemSet);

        //Generate frequent patterns
        LinkedHashSet<TreeSet<Integer>> frequentPatterns =
                Apriori.generateFrequentPatterns(transactionData, linkedItemSet,
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
            return (int) (((float) numTransactions)*(minThreshPercentage/100));
        }
        int minThreshNumber = Integer.valueOf(minThresh);
        if(minThreshNumber < 0 || minThreshNumber > numTransactions){
            System.out.println("Invalid minimum threshold. Threshold must be betwen 0 and " + numTransactions);
            System.exit(1);
        }
        return minThreshNumber;
    }

}