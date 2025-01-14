package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobsRaw;
    //private static ArrayList<HashMap<String, String>> allJobsShallow;
    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);
            String bValue = aValue.toLowerCase();

            //System.out.println("findByColumnAndValue bValue: " + bValue );
            //System.out.println("findByColumnAndValue aValue: " + aValue );

            if (bValue.contains(value)) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        //System.out.println("findByValue column.values(): " + column.values());
        //System.out.println("findByValue column: " + column);
        //System.out.println("findByValue value: " + value);

        for (HashMap<String, String> row : allJobs) {
            //System.out.println("findByValue row: " + row);

            Integer k = 0;
            //for (String key : column.values()){
            for (String key : row.values()){
                key = key.toLowerCase();
                //System.out.println("findByValue key: " + key);
                //System.out.println("k: " + k);

                if(key.contains(value)) {

//                    String aValue = row.get(column);
//                    String bValue = aValue.toLowerCase();
//
//                    System.out.println("findByValue bValue: " + bValue );
//                    System.out.println("findByValue aValue: " + aValue );
//
//
//                    if (bValue.contains(value))
                    jobs.add(row);
                    break;

                } else {
                    //System.out.println("key: " + key + "    doesn't contain  value: " + value);
                    //System.out.println("*************\n");
                }
                k++;
            }
        }

        return jobs;
    }


    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobsRaw = new ArrayList<>();



            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }
            // added a shallow clone for Bonus 2  see line 24/25  157   169/170
                allJobsRaw.add(newJob);
                allJobs = (ArrayList<HashMap<String, String>>) allJobsRaw.clone();


                //allJobsShallow = (ArrayList<HashMap<String, String>>) allJobsRaw.clone();

            // adding a deep clone for Bonus 3
            //  https://howtodoinjava.com/java/collections/arraylist/arraylist-clone-deep-copy/

//                allJobs = new ArrayList<HashMap<String, String>>;
//                Iterator<ArrayList<HashMap<String, String>>> iterator = allJobsRaw.iterator();
//                while(iterator.hasNext()){
//                    allJobs.add((JobData) iterator.next().clone());
//                }



                //allJobsLower.add(newJob);
            }


            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }

    }

}
