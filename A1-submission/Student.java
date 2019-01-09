//package planner;

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
    
    // CONSTRUCTORS
    /**
     * Default Constructors - instantiates a Student using default attribute values
     */
    public Student()
    {
        this("Unknown", "Unknown", -1);
    }
    /**
     * Primary Constructor - instantiates a Student with full attribute values
     * @param first the first name of the Student
     * @param last the last name of the Student
     * @param studentNum the student number of the Student
     */
    public Student(String first, String last, int studentNum)
    {
        firstName = first;
        lastName = last;
        id = studentNum;
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
                "\nStudent Number: " + id;
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
        return new Student(firstName, lastName, id);
    }
} // end Student class
