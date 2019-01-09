package planner_a1;

// imports

import univ.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- Bootstrap
 * --> has a main method and can:

  load a list of available courses from a CSV file, replacing any existing representation of those courses
 --> Course List file: Note that prerequisite list will be a colon-delimited list of prerequisite courses
    --> line: CourseCode,CreditWeight,CourseName,PrerequisiteList
    --> eg: CIS*2430,0.5,Object Oriented Programming,CIS*2500
            CIS*2750,0.75,Software Systems Development and Integration,CIS*2520:CIS*2430

  load a list of the required courses for a degree from CSV file, replacing any existing representation of that degree
 --> Required Courses file: file should only be loaded once the Course List has been loaded.
    --> line: represents the required courses for a single degree (DegreeName,RequiredCourse1,RequiredCourse2,etc)
    --> eg: BCG, CIS*1500,CIS*1910,CIS*2430,CIS*2500,CIS*2520,CIS*2750,CIS*2910,CIS*3530

  save the current representation of course and degree information to a file that is used by the planning application
 
 */
public class Bootstrap
{
    /**
     * @param args -the command line arguments
     */
    public static void main(String[] args)
    {
        // declare variables
        Scanner sc = new Scanner(System.in);
        String menuInput = "";
        
        CourseCatalog catalog;
        HashMap<String, ArrayList<String>> degKeyReqCourseList;
        String filename;
        //String majorList[] = {"BCG", "CS", "SEng"};
        String input = "";
        
        // welcome prompt
        System.out.println("Welcome to Bootsrap!");
        
        // continue until user selects to exit
        while(!menuInput.equals("3") && !menuInput.toLowerCase().equals("quit")) {
            System.out.println("\nPlease select the number of the option you would like to complete:\n" +
                    "\t1. Load a new course catalog of courses at the University of Guelph, and then save it to replace the pre-existing catalog\n" +
                    "\t2. Load a list of required courses for each major, and then save it to replace the pre-existing lists\n" +
                    "\t3. Quit\nSelect option: ");
            menuInput = sc.nextLine().trim();
            
            // check input
            if(menuInput.equals("1")) {         // =================== 1 ===================
                System.out.println("Enter the name of the CSV file you wish to be loaded: ");
                filename = sc.nextLine().trim();
                
                catalog = new CourseCatalog();
                catalog.initializeCatalog(filename); // sets the catalog's list of courses to the read list of courses
                // if it worked, it will have a size
                if(catalog.getCatalogSize() > 0) {
                    catalog.saveCatalog(); //writes a binary file to save the loaded catalog
                }
                
            } else if(menuInput.equals("2")) {  // =================== 2 ===================
                System.out.println("Enter the name of the CSV file you wish to be loaded: ");
                filename = sc.nextLine().trim();
                
                // load and store in a hashmap
                degKeyReqCourseList = loadRequiredCourses(filename);
                
                // if it worked, it will have a size
                if(!degKeyReqCourseList.isEmpty() && degKeyReqCourseList.size() > 0) {
                    saveRequiredCourses(degKeyReqCourseList); //writes a binary file to save the loaded catalog
                }
                
            } else if(menuInput.equals("3") || menuInput.toLowerCase().equals("quit")) {
                System.out.println("Good bye!");
            } else {
                // display error msg
                System.out.println("Invalid option: please select one of the options by number [1,5]");
            }
        }
    }
    
    /**
     * File Read method - loads a CSV that contains the list of required courses for each major
     * @param filename the name of the CSV file being read from
     * @return a HashMap with a String key of the Major, and a value of an ArrayList of Strings of required course codes
     */
    public static HashMap<String, ArrayList<String>> loadRequiredCourses(String filename)
    {
        HashMap<String, ArrayList<String>> degKeyReqCourseList = new HashMap<>();
        
        // declare variables
        boolean eof = false;
        boolean validInput = true;
        String line = "";
        String key, value;
        String lineSplit[];
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            eof = false;
            
            // read the entire file
            while(!eof) {
                line = br.readLine();
                // check if anything is there
                if(line == null) {
                    eof = true;
                } else {
                    validInput = true;
                    lineSplit = line.trim().split(",");
                    for(String s : lineSplit) {
                        s = s.trim();
                    }
                    
                    // check major
                    if(lineSplit[0].equals("")) {
                        System.out.println("Error: loadRequiredCourses(" + filename + "): major must exist");
                        validInput = false;
                        continue; // do not add because it's invalid
                    } else {
                        key = lineSplit[0];
                    }
                    
                    // check that course codes are valid
                    String validRequired = ""; // stores a string to be split of all good input prereq courses
                    String codeSplit[];
                    int counter = 0;
                    for(int i = 1; i < lineSplit.length; i++) {
                        codeSplit = lineSplit[i].trim().split("\\*"); //break code into subject and number, clear extra whitespace
                        for(String s : codeSplit) {
                            s = s.trim();
                        }
                        // load potentially altered course code back
                        if(codeSplit.length > 1) {
                            for(int j = 0; j < codeSplit.length; j++) {
                                if(j == 0) {
                                    lineSplit[i] = codeSplit[j];
                                } else {
                                    lineSplit[i] += "*" + codeSplit[j];
                                }
                            }
                        }
                        
                        // now, check course code (length for subject and number)
                        if(codeSplit.length != 2 || codeSplit[0].length() < 3 || codeSplit[0].length() > 4 || 
                                codeSplit[1].length() != 4 || !isInteger(codeSplit[1])) {
                            System.out.println("Error: loadRequiredCourses(" + filename + "): required course code '" + lineSplit[i] + "' is invalid");
                            validInput = false;
                            continue; //do not add as a prerequisite course because invalid course code format
                        }
                        
                        // if reaches here, then the required course is valid, so add it to the storing list
                        if(counter == 0) {
                            validRequired += lineSplit[i];
                        } else {
                            validRequired += ":" + lineSplit[i];
                        }                        
                        counter++; // accumulate since it was a good required course input
                    }
                    String validRequiredSplit[] = validRequired.split(":"); // break the good input required courses into separate course codes
                   
                    // if the input line was good, add it to the HashMap
                    if(validInput) {
                        // create array list to add
                        ArrayList<String> reqCourses = new ArrayList<>();
                        for(String reqCourseCode : validRequiredSplit) {
                            reqCourses.add(reqCourseCode);
                        }
                        degKeyReqCourseList.put(key, reqCourses); // add it to the map
                    }
                    
                } // end else
            } // end while loop
        } catch (IOException e) {
            System.out.println("Error: IOException: " + e);
        }
        return degKeyReqCourseList;
    }
    /**
     * File Write method - writes a binary file that contains the list of required courses for each major
     * @param degKeyReqCourseList the HashMap collection object being written
     */
    public static void saveRequiredCourses(HashMap<String, ArrayList<String>> degKeyReqCourseList)
    {
        try {
            FileOutputStream fout = new FileOutputStream("requiredCourses.bin");
            ObjectOutputStream out = new ObjectOutputStream(fout);
            
            out.writeObject(degKeyReqCourseList);
            
            // close streams
            out.close();
            fout.close();
            
        } catch (IOException e) {
            System.out.println("Error: IOException -> " + e);
        }
    }
    
    // =========================================================================
    
    /**
     * Boolean method - Checks if a string is an integer
     * @param sNum the string form of the number being checked
     * @return true if it successfully parses to an integer
     */
    public static boolean isInteger(String sNum)
    {
        try {
            Integer.parseInt(sNum);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} // end Bootstrap class
