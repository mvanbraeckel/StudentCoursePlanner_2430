//package planner;

// imports
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- (abstract) GeneralDegree (extends Degree)
 * --> Represents an abstract GeneralDegree at the University of Guelph 
 *      (is a subclass of Degree, and also has number of credits required)
 */
abstract public class GeneralDegree extends Degree implements Serializable
{
    // ATTRIBUTES
    private static final double CREDITS_REQUIRED = 15.00;
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates an GeneralDegree with default values
     */
    public GeneralDegree()
    {
        super();
    }
    /**
     * Full Constructor - instantiates an GeneralDegree with full attributes
     * @param title the title of the GeneralDegree
     * @param reqCourses the list of required Courses for the GeneralDegree
     * @param catalog the CourseCatalog containing a list of all courses at the University of Guelph
     */
    public GeneralDegree(String title, ArrayList<Course> reqCourses, CourseCatalog catalog)
    {
        super(title, reqCourses, catalog);
    }
    
    // BEHAVIOURS
    /**
     * Abstract Accessor method - Retrieves the Major of the Concrete Degree
     * @return the Major of the Concrete Degree
     */
    @Override
    abstract public String getMajor();
    
    /**
     * Accessor method - Retrieves the required number of credits to graduate
     * @return the number of credits to graduate
     */
    @Override
    public double getCreditsRequired() {
        return CREDITS_REQUIRED;
    }
    
    /**
     * Mutator method - Sets the title of the GeneralDegree
     * @param title the new title of the GeneralDegree
     */
    @Override
    public void setDegreeTitle(String title)
    {
        super.setDegreeTitle(title + " (General)");
    }
    
    // =========================================================================
    
    /**
     * Retrieves all info about an GeneralDegree
     * @return a string representation of the GeneralDegree's information
     */
    @Override
    public String toString()
    {
        return super.toString() + "\nCredits Required: " + CREDITS_REQUIRED;
    }
    
    /**
     * Comparator method -- Compares two GeneralDegrees to each other
     * @param p the other Object being compared
     * @return true if they have the same attributes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof GeneralDegree)) {
            return false;
        } else {
            return super.equals((Degree)p); //?? not sure about casting
        }
    }
    
} // end (abstract) GeneralDegree subclass
