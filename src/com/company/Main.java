package com.company;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
	    String inputFile = args[0];
	    String minimumThreshold = args[1];
	    Scanner scan = null;

	    try {
            scan = new Scanner(new File(inputFile));
        } catch(IOException e){
	        System.out.println("Unable to read from file");
	        System.exit(1);
        }
        int numTransactions = scan.nextInt();
	    int numItems = scan.nextInt();

        LinkedHashSet<TreeSet<Integer>> transactionData = new LinkedHashSet<>();
	    while(scan.hasNextLine()){
	        String line = scan.nextLine();
	        String[] lineData = line.split(" ");
	        TreeSet<Integer> transaction = new TreeSet<>();
	        for(String datum : lineData){
	            if(!datum.equals("")) transaction.add(Integer.valueOf(datum));
            }
            transactionData.add(transaction);
        }

        LinkedHashSet<TreeSet<Integer>> itemSet = new LinkedHashSet<>();
	    for(int i=0; i<numItems; i++){
	        TreeSet<Integer> item = new TreeSet<>();
	        item.add(i);
	        itemSet.add(item);
        }

        LinkedHashSet<TreeSet<Integer>> frequentPatterns =
                Apriori.generateFrequentPatterns(transactionData, itemSet,
                        processMinThreshold(minimumThreshold, numTransactions));

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
