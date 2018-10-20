package com.company;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
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

	    TreeMap<Integer[], Integer> frequentPatterns = Apriori.generateFrequentPatterns(transactions, processMinThreshold(minimumThreshold, numTransactions));
	    TreeMap<Integer[], Integer> reSortedPatterns = new TreeMap<>(new IntArrayComparatorSizeWise());
	    reSortedPatterns.putAll(frequentPatterns);

	    System.out.println("|FPs| = " + reSortedPatterns.size());
	    System.out.println("Frequent patterns found in " + (System.currentTimeMillis()-startTime) + " millis");


        //Print frequent patterns to file
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File("MiningResult.txt"));
        } catch (IOException e){
            System.out.println("Unable to open output file");
            System.exit(1);
        }
        try {
            writer.write("|FPs| = " + reSortedPatterns.size() + "\n");
            for (Integer[] fp : reSortedPatterns.keySet()) {
                for (int i : fp) writer.write(i + " ");
                writer.write(": " + reSortedPatterns.get(fp) + "\n");
            }
            writer.close();
        } catch(IOException e){
            System.out.println("Unable to write to output file");
            System.exit(1);
        }
    }

    /**
     * A helper method to determine the min threshold. If teh min threshold is passed as a percentage, it determines
     * what the corresponding int is. Otherwise it just returns the original value
     * @param minThresh The String passed in from teh command line
     * @param numTransactions The total number of transactions
     * @return the integer denoting the minimum support threshold
     */
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
        return minThreshNumber;
    }

}