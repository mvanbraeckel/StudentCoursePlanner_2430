package univ;

// imports
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- (abstract) HonoursDegree (extends Degree)
 * --> Represents an abstract HonoursDegree at the University of Guelph 
 *      (is a subclass of Degree, and also has number of credits required)
 */
abstract public class HonoursDegree extends Degree implements Serializable
{
    // ATTRIBUTES
    private static final double CREDITS_REQUIRED = 20.00;
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates an HonoursDegree with default values
     */
    public HonoursDegree()
    {
        super();
    }
    /**
     * Full Constructor - instantiates an HonoursDegree with full attributes
     * @param title the title of the HonoursDegree
     * @param reqCourses the list of required Courses for the HonoursDegree
     * @param catalog the CourseCatalog containing a list of all courses at the University of Guelph
     */
    public HonoursDegree(String title, ArrayList<Course> reqCourses, CourseCatalog catalog)
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
     * Mutator method - Sets the title of the HonoursDegree
     * @param title the new title of the HonoursDegree
     */
    @Override
    public void setDegreeTitle(String title)
    {
        super.setDegreeTitle(title + " (Honours)");
    }
    
    // =========================================================================
    
    /**
     * Retrieves all info about an HonoursDegree
     * @return a string representation of the HonoursDegree's information
     */
    @Override
    public String toString()
    {
        return super.toString() + "\nTotal Credits Required:\t" + CREDITS_REQUIRED;
    }
    
    /**
     * Comparator method -- Compares two HonoursDegrees to each other
     * @param p the other Object being compared
     * @return true if they have the same attributes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof HonoursDegree)) {
            return false;
        } else {
            return super.equals((Degree)p); //?? not sure about casting
        }
    }
    
} // end (abstract) HonoursDegree subclass
