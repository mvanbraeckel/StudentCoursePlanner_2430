package planner_a1;

import univ.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- Student
 * --> Represents a Student (has a name and student ID number)
 */
public class Student implements Serializable
{
    // ATTRIBUTES
    private String firstName, lastName;
    private int id;
    private PlanOfStudy pos;
    
    // CONSTRUCTORS
    /**
     * Default Constructors - instantiates a Student using default attribute values
     */
    public Student()
    {
        this("Unknown", "Unknown", -1);
    }
    /**
     * Primary Constructor - instantiates a Student with all attribute values except a PlanOfStudy
     * @param first the first name of the Student
     * @param last the last name of the Student
     * @param studentNum the student number of the Student
     */
    public Student(String first, String last, int studentNum)
    {
        firstName = first;
        lastName = last;
        id = studentNum;
        pos = new PlanOfStudy();
    }
    
    /**
     * Full Constructor - instantiates a Student with full attribute values
     * @param first the first name of the Student
     * @param last the last name of the Student
     * @param studentNum the student number of the Student
     * @param thePlan the PlanOfStudy of the Student
     */
    public Student(String first, String last, int studentNum, PlanOfStudy thePlan)
    {
        this(first, last, studentNum);
        pos = thePlan;
    }
    
    // BEHAVIOURS
    
    /**
     * Accessor method - retrieves the first name of the Student
     * @return the first name of the Student
     */
    public String getFirstName()
    {
        return firstName;
    }
    /**
     * Mutator method - sets the first name of the Student
     * @param first the new first name of the Student
     */
    public void setFirstName(String first)
    {
        /*if(first == null) {
            throw new NullPointerException();
        } else {
            firstName = first;
        }*/
        firstName = first;
    }
    
    /**
     * Accessor method - retrieves the last name of the Student
     * @return the last name of the Student
     */
    public String getLastName()
    {
        return lastName;
    }
    /**
     * Mutator method - sets the last name of the Student
     * @param last the new last name of the Student
     */
    public void setLastName(String last)
    {
        /*if(last == null) {
            throw new NullPointerException();
        } else {
            lastName = last;
        }*/
        lastName = last;
    }
    
    /**
     * Accessor method - retrieves the full name of the Student
     * @return the full name of the Student
     */
    public String getFullName()
    {
        return firstName + " " + lastName;
    }
    
    /**
     * Accessor method - retrieves the student number of the Student
     * @return the student number of the Student
     */
    public Integer getStudentNumber()
    {
        return id;
    }
    /**
     * Mutator method - sets the student number of the Student
     * @param studentNum the new student number of the Student
     */
    public void setStudentNumber(Integer studentNum)
    {
        id = studentNum;
    }
    
     /**
     * Accessor method - retrieves the PlanOfStudy of the Student
     * @return the PlanOfStudy of the Student
     */
    public PlanOfStudy getPlan()
    {
        return pos;
    }
    /**
     * Mutator method - sets the PlanOfStudy of the Student
     * @param thePlan the new PlanOfStudy of the Student
     */
    public void setPlan(PlanOfStudy thePlan)
    {
        pos = thePlan;
    }
    
    // =========================================================================
    
    // Planner user story #1 -> must read StudentTranscript.csv into a catalog
    /**
     * Reads a file into a (old) transcript (course code, status, grade, semester taken)
     * @param filename the CSV file being read
     */
    public void importData(String filename)
    {
        // declare variables
        boolean eof = false;
        boolean validInput = true;
        String line = "";
        
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            eof = false;
            
            pos.getTranscript().clear(); //reset
            // read the entire file
            while(!eof) {
                line = br.readLine();
                // check if anything is there
                if(line == null) {
                    eof = true;
                } else {
                    validInput = true;
                    String lineSplit[] = line.trim().split(","); //break CSV into parts, clear extra whitespace
                    for(String s : lineSplit) {
                        s = s.trim();
                    }
                    String codeSplit[] = lineSplit[0].trim().split("\\*"); //break code into subject and number, clear extra whitespace
                    for(String s : codeSplit) {
                        s = s.trim();
                    }
                    // load potentially altered course code back
                    if(codeSplit.length > 1) {
                        for(int i = 0; i < codeSplit.length; i++) {
                            if(i == 0) {
                                lineSplit[0] = codeSplit[i];
                            } else {
                                lineSplit[0] += "*" + codeSplit[1];
                            }
                        }
                    }
                    
                    // check course code (length for subject and number, and that the number is a number)
                    if(codeSplit.length != 2 || codeSplit[0].length() < 3 || codeSplit[0].length() > 4 ||
                            codeSplit[1].length() != 4 || !isInteger(codeSplit[1])) {
                        System.out.println("Error: importData(" + filename + "): course code '" + lineSplit[0] + "' is invalid");
                        validInput = false;
                    }
                    // check course status
                    if(!lineSplit[1].equals("Complete") && !lineSplit[1].equals("InProgress") && !lineSplit[1].equals("Planned")) {
                        System.out.println("Error: importData(" + filename + "): course status '" + lineSplit[1] + "' is invalid");
                        validInput = false;
                    }
                    // check grade
                    if(!isDouble(lineSplit[2]) && !lineSplit[2].equals("") && !lineSplit[2].equals("P") && !lineSplit[2].equals("F") &&
                            !lineSplit[2].equals("INC") && !lineSplit[2].equals("MNR")) {
                        System.out.println("Error: importData(" + filename + "): grade '" + lineSplit[2] + "' is invalid");
                        validInput = false;
                    } else if(isDouble(lineSplit[2]) && !lineSplit[2].equals("")){
                        // valid number, but check it's a valid grade
                        if(Double.parseDouble(lineSplit[2]) < 0.0 || Double.parseDouble(lineSplit[2]) > 100.0) {
                            System.out.println("Error: importData(" + filename + "): grade '" + lineSplit[2] + "' must be in range [0,100]");
                            validInput = false;
                        } else if(!lineSplit[1].equals("Complete")) {
                            // make sure that grades are only entered if the course status is Complete
                            System.out.println("Error: importData(" + filename + "): grade can only be set if status is 'Complete'");
                            validInput = false;
                        }
                    }
                    
                    // check semester (must be F,W, B, or S semester character, with 2 digits following)
                    if((lineSplit[3].charAt(0) != 'F' && lineSplit[3].charAt(0) != 'W' &&
                            lineSplit[3].charAt(0) != 'B' && lineSplit[3].charAt(0) != 'S') ||
                            lineSplit[3].length() != 3 || !isInteger(lineSplit[3].substring(1, 3))) {
                        System.out.println("Error: importData(" + filename + "): semester '" + lineSplit[3] + "' is invalid");
                        validInput = false;
                    }
                    
                    // if the input line was good, find it in the catalog
                    if(validInput) {
                        Course found = pos.getDegreeProgram().getCatalog().findCourse(lineSplit[0]);
                        if(found == null) {
                            System.out.println("Error: importData(" + filename + "): course code '" + lineSplit[0] +
                                    "' is not a course at the University of Guelph");
                        } else { //match was found
                             // create a copy, then set attributes before adding to the plan's list of scheduled courses
                             Attempt c = new Attempt(found);
                             c.setAttemptStatus(lineSplit[1]);
                             c.setAttemptGrade(lineSplit[2]);
                             c.setSemesterTaken(lineSplit[3]);
                             pos.getTranscript().add(c);
                        }                       
                    }
                    
                } // end else
            } // end while loop
        } catch (IOException e) {
            System.out.println("Error: IOException: " + e);;
        }
    }
    
    /**
     * File Write method - writes a single PlanOfStudy object to a binary file to save it
     * @param filename the name of the binary file being written to
     */
    public void saveState(String filename)
    {
        try {
            FileOutputStream fout = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            
            //PlanOfStudy thePlan = new PlanOfStudy(degreeProgram, transcript);
            // write the plan's attributes to a binary file
            out.writeObject(this);
            
            // close streams
            out.close();
            fout.close();
            
        } catch (IOException e) {
            System.out.println("Error: IOException -> " + e);
        }
    }
    /**
     * File Read method - reads a single saved PlanOfStudy object from a binary file
     * @param filename the name of the binary file being read from
     */
    public void readSavedState(String filename)
    {
        boolean eof;
        Student line;
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fin);
            eof = false;
            
            // read the entire binary file
            while(!eof) {
                line = (Student) in.readObject();
                // check if anything is there
                if(line == null) {
                    eof = true;
                } else { // cont reading
                    // set this plan's attributes to the read plan's attributes
                    firstName = line.getFirstName();
                    lastName = line.getLastName();
                    id = line.getStudentNumber();
                    pos = line.getPlan();
                    eof = true; // there should only be one list of Courses in the file, so stop
                }
            }
            // close streams
            in.close();
            fin.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error: FileNotFoundException -> " + ex);
        } catch (IOException ex) {
            System.out.println("Error: IOException -> " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: ClassNotFoundException -> " + ex);
        } 
    }
    
    // =========================================================================
    
    /**
     * Retrieves all info about a Student
     * @return a string representation of the Student
     */
    @Override
    public String toString()
    {
        return "First Name: " + firstName + 
                "\nLast Name: " + lastName +
                "\nStudent Number: " + id +
                "\n" + pos.toString();
    }
    
    /**
     * Comparator method -- Compares two Students to each other
     * @param p the other Object being compared
     * @return true if they have the same full name and student number
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof Student)) {
            return false;
        } else {
            Student other = (Student)p;
            return getFullName().equals(other.getFullName()) && id == other.getStudentNumber();
        }
    }
    
    /**
     * Clone method -- Creates an exact copy of a Student
     * @return a full clone of the Student
     */
    @Override
    public Student clone()
    {
        return new Student(firstName, lastName, id, pos);
    }
    
    /**
     * Boolean method - Checks if a string is an integer
     * @param sNum the string form of the number being checked
     * @return true if it successfully parses to an integer
     */
    public boolean isInteger(String sNum) {
        try {
            Integer.parseInt(sNum);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Boolean method - Checks if a string is a double
     * @param sNum the string form of the number being checked
     * @return true if it successfully parses to a double
     */
    public boolean isDouble(String sNum) {
        try {
            Double.parseDouble(sNum);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} // end Student class
