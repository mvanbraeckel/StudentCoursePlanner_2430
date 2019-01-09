package univ;

// imports
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import planner_a1.*;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- (abstract) Degree
 * --> Represents an abstract Degree at the University of Guelph 
 *      (has a title, list of required courses, a catalog of all courses at UoG, number of credits earned)
 */
abstract public class Degree implements Serializable
{
    // ATTRIBUTES
    private String degreeTitle;
    private ArrayList<Course> requiredCourses;
    private CourseCatalog fullCourseList;
    //private double creditsRequired; in Honours or General
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates a Degree with default values
     */
    public Degree()
    {
        degreeTitle = "Unknown";
        requiredCourses = new ArrayList<>();
        fullCourseList = new CourseCatalog();
    }
    /**
     * Full Constructor - instantiates a Degree with full attributes
     * @param title the title of the Degree
     * @param reqCourses the list of required Courses for the Degree
     * @param catalog the CourseCatalog containing a list of all courses at the University of Guelph
     */
    public Degree(String title, ArrayList<Course> reqCourses, CourseCatalog catalog)
    {
        this();
        degreeTitle = title;
        for(Course c : reqCourses) {
            requiredCourses.add(new Course(c));
        }
        fullCourseList = catalog;
    }
    
    // BEHAVIOURS    
    /**
     * Accessor method - Retrieves the title of the Degree
     * @return the title of the Degree
     */
    public String getDegreeTitle()
    {
        return degreeTitle;
    }
    /**
     * Mutator method - Sets the title of the Degree
     * @param title the new title of the Degree
     */
    public void setDegreeTitle(String title)
    {
        degreeTitle = title;
    }
    
    /**
     * Accessor method - Retrieves the list of required courses for the Degree
     * @return the list of required courses for the Degree
     */
    public ArrayList<Course> getRequiredCourses() 
    {
        return requiredCourses;
    }
    /**
     * Mutator method - Sets the list of required courses for the Degree
     * @param listOfRequiredCourseCodes the new list of required courses for the Degree
     */
    public void setRequiredCourses(ArrayList<String> listOfRequiredCourseCodes)
    {
        // loop through CourseCatalog to find matching course code, add that Course object to the requiredCourses list
        Course found;
        for(String reqCourse : listOfRequiredCourseCodes) {
            found = fullCourseList.findCourse(reqCourse);   // searches for and obtains the Course with the matching course code
            if(found != null) {
                requiredCourses.add(new Course(found));     // then adds a copy to the list of required courses (as long as it finds a match)
            } else {
                System.out.println("Error: did not find course code '" + reqCourse + "' in the Course Catalog");
            }
        }
    }
    
    /**
     * Accessor method - Retrieves the CourseCatalog of all courses
     * @return the CourseCatalog for UoG
     */
    public CourseCatalog getCatalog()
    {
        return fullCourseList;
    }
        
    /**
     * Action accessor method - retrieves a list of required courses that still need to be completed
     * @param thePlan the user's PlanOfStudy that contains their course list
     * @return an ArrayList of Courses that still need to be completed
     */
    public ArrayList<Course> remainingRequiredCourses(PlanOfStudy thePlan)
    {
        ArrayList<Course> remaining = new ArrayList<>();
        // loop through the requiredCourses list, checking that each course has been completed in the PlanOfStudy
        Attempt found;
        for(Course req : requiredCourses) {
            found = thePlan.findAttempt(req.getCourseCode());
            if(found == null || found.getAttemptStatus().toLowerCase().charAt(0) != 'c') {
                remaining.add(req); // does not exist in the PlanOfStudy or exists, but is not completed yet
            }
        }
        return remaining;
    }
    
    /**
     * Loads the list of required courses from a binary file then sets it here according to the matching major
     * @param filename the name of the binary file to be read from
     * @param major the name of the major for the degree
     */
    public void readRequiredCourses(String filename, String major) {
        boolean eof;
        HashMap<String, ArrayList<String>> line;
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fin);
            eof = false;
            
            // read the entire binary file
            while(!eof) {
                line = (HashMap<String, ArrayList<String>>) in.readObject();
                // check if anything is there
                if(line == null) {
                    eof = true;
                } else { // cont reading
                    // find matching key, then set required courses
                    for(String readMajor : line.keySet()) {
                        if(readMajor.toLowerCase().equals(major.toLowerCase())) {
                            setRequiredCourses(line.get(readMajor));
                        }
                    }
                    eof = true; // there should only be one CourseCatalog object in the file, so stop
                }
            }
            // close streams
            in.close();
            fin.close();
            
        } catch (IOException e) {
            System.out.println("Error: IOException -> " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: ClassNotFoundException -> " + e);
        }
    }
    
    /**
     * Search method - finds a Course in the list based on unique course code
     * @param courseCode the desired course code being searched for
     * @return the Course with the matching course code, otherwise null if not found
     */
    public Course findRequiredCourse(String courseCode)
    {
        for(Course c : requiredCourses) {
            if(c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null; // course code not found
    }
    
    /**
     * Abstract Accessor method - Retrieves the Major of the Concrete Degree
     * @return the Major of the Concrete Degree
     */
    abstract public String getMajor();
    
    /**
     * Abstract boolean method - Checks if the user meets requirements to graduate from their Degree and Major at UoG
     * @param thePlan the PlanOfStudy that the user is following
     * @return true if the user meets requirements to graduate
     */
    abstract public boolean meetsRequirements(PlanOfStudy thePlan);         // need creditsRequired from Honours or General
    /**
     * Abstract accessor method - Retrieves the number of credits remaining until the user can graduate from their Degree and Major at UoG
     * @param thePlan the PlanOfStudy that the user is following
     * @return the number of credits remaining until the user can graduate from their Degree and Major at UoG
     */
    abstract public double numberOfCreditsRemaining(PlanOfStudy thePlan);   // need creditsRequired from Honours or General
    
    /**
     * Abstract Accessor method - Retrieves the required number of credits to graduate
     * @return the number of credits to graduate
     */
    abstract public double getCreditsRequired();
    
    // =========================================================================
    
    /**
     * Retrieves all info about a Degree
     * @return a string representation of the Degree's information
     */
    @Override
    public String toString()
    {
        int counter = 0;
        String toReturn = "Degree: " + degreeTitle + "\nRequired Courses:\n";
        for(Course c : requiredCourses) {
            if(counter != 0) {
                toReturn += "\n";
            }
            toReturn += c.toString();
            counter++;
        }
        return toReturn;
    }
    
    /**
     * Comparator method -- Compares two Degrees to each other
     * @param p the other Object being compared
     * @return true if they have the same attributes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof Degree)) {
            return false;
        } else {
            Degree other = (Degree)p;
            return degreeTitle.equals(other.getDegreeTitle()) && requiredCourses.equals(other.getRequiredCourses());
        }
    }
    
} // end (abstract) Degree class
