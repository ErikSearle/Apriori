package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
        LinkedList<TreeSet<Integer>> transactions = new LinkedList<>();


        //Read the file and put transactions into the transaction set
        try {
            String line = fileReader.readLine();
            while (line != null) {
                String[] splitLine = line.split("\\s+");
                TreeSet<Integer> trans = new TreeSet<>();
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

	    //Sort the transactions
	    transactions.sort(new TreeSetComparator());
	    System.out.println("File read and sorted after " + (System.currentTimeMillis()-startTime) + " millis");

	    TreeMap<int[], Integer> frequentPatterns = Apriori.generateFrequentPatterns(transactions, processMinThreshold(minimumThreshold, numTransactions));
	    TreeMap<int[], Integer> reSortedPatterns = new TreeMap<>(new IntArrayComparatorSizeWise());
	    reSortedPatterns.putAll(frequentPatterns);

	    System.out.println("Frequent patterns found in " + (System.currentTimeMillis()-startTime) + " millis");
        //Print frequent patterns
        for(int[] fp: reSortedPatterns.keySet()){
            for(int i: fp) System.out.print(i + " ");
            System.out.print("\n");
        }
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