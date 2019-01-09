package planner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JOptionPane;

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
        if(first == null) {
            throw new NullPointerException();
        } else {
            firstName = first;
        }
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
        if(last == null) {
            throw new NullPointerException();
        } else {
            lastName = last;
        }
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
    
    /**
     * File Write method - writes the Student to a binary file to save it
     * @param filename the name of the binary file being written to
     */
    public void saveState(String filename)
    {
        try {
            FileOutputStream fout = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            
            out.writeObject(this);
            
            // close streams
            out.close();
            fout.close();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error - saveState(" + filename + "): IOException -> " + ex,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * File Read method - reads a single saved Student object from a binary file
     * @param filename the name of the binary file being read from
     * @return true if it worked properly
     */
    public boolean readSavedState(String filename)
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
            JOptionPane.showMessageDialog(null, "Error - readSavedState(" + filename + "): You do not have a saved plan",
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error - readSavedState(" + filename + "): IOException -> " + ex,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error - readSavedState(" + filename + "): ClassNotFoundException -> " + ex,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    // =========================================================================
    
    /**
     * Retrieves all info about a Student
     * @return a string representation of the Student
     */
    @Override
    public String toString()
    {
        return "First Name:\t\t" + firstName + 
                "\nLast Name:\t\t" + lastName +
                "\nStudent Number:\t\t" + id +
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
