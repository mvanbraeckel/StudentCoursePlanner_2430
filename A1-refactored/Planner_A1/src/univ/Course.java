package univ;

// imports
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 23/11/2018
 * CIS2430 Assignment 2 -- Course (refactored)
 * --> Represents a Course at the University of Guelph (has a title and code, credit weight, list of prerequisites, and semesterOffered (F,W,B (both)))
 * --> Should be immutable to client classes as students should not be able to change the information about a course.
 */
public class Course implements Serializable
{
    // ATTRIBUTES
    private String code, title, semesterOffered;
    private double credit;
    private ArrayList<Course> prereq;
    
    // CONSTRUCTORS
    /**
     * Default Constructors - instantiates a Course using default attribute values
     */
    public Course()
    {
        code = "Unknown";
        title = "Unknown";
        credit = 0.0;
        prereq = new ArrayList<>();
        semesterOffered = "Unknown";
    }
    /**
     * Primary Constructor - instantiates a Course with just a course code
     * @param courseCode the code of the Course
     */
    public Course(String courseCode)
    {
        this();
        code = courseCode;
    }
    /**
     * File Constructor - instantiates a Course using 4 values read from the data file
     * @param courseCode the code of the Course
     * @param creditWeight the amount of credits earned for completing the Course
     * @param courseName the title of the Course
     * @param prerequisiteList the list of Courses that are prerequisite requirements for the Course
     */
    public Course(String courseCode, double creditWeight, String courseName, ArrayList<Course> prerequisiteList)
    {
        this(courseCode);
        credit = creditWeight;
        title = courseName;
        for(Course c : prerequisiteList) {
            prereq.add(new Course(c));
        }
    }
    /**
     * Full Constructor - instantiates a Course with full attributes
     * @param courseCode the code of the Course
     * @param creditWeight the amount of credits earned for completing the Course
     * @param courseName the title of the Course
     * @param prerequisiteList the list of Courses that are prerequisite requirements for the Course
     * @param semOffered the semester the Course is offered
     */
    public Course(String courseCode, double creditWeight, String courseName, ArrayList<Course> prerequisiteList, String semOffered)
    {
        this(courseCode);
        credit = creditWeight;
        title = courseName;
        for(Course c : prerequisiteList) {
            prereq.add(new Course(c));
        }
        semesterOffered = semOffered;
    }
    /**
     * Deep Copy Constructor - instantiates a new Course that is a complete, exact copy of another
     * @param original the Course to be copied
     */
    public Course(Course original)
    {
        this(original.getCourseCode(), original.getCourseCredit(), original.getCourseTitle(),
                original.copyPrerequisites(), original.getSemesterOffered());
    }
    
    // BEHAVIOURS
    /**
     * Accessor method - Retrieves the code of the Course
     * @return the code of the Course
     */
    public String getCourseCode()
    {
        return code;
    }
    /**
     * Mutator method - Sets the code of the Course
     * @param courseCode the new code of the Course
     */
    protected void setCourseCode(String courseCode)
    {
        code = courseCode;
    }
    
    /**
     * Accessor method - Retrieves the title of the Course
     * @return the title of the Course
     */
    public String getCourseTitle()
    {
        return title;
    }
    /**
     * Mutator method - Sets the title of the Course
     * @param courseTitle the new title of the Course
     */
    protected void setCourseTitle(String courseTitle)
    {
        title = courseTitle;
    }
    
    /**
     * Accessor method - Retrieves the credit of the Course
     * @return the credit of the Course
     */
    public double getCourseCredit()
    {
        return credit;
    }
    /**
     * Mutator method - Sets the credit of the Course
     * @param credit the new credit of the Course
     */
    protected void setCourseCredit(double credit)
    {
        this.credit = credit;
    }
    
    /**
     * Accessor method - Retrieves the list of Courses that are prerequisite requirements for this Course
     * @return the ArrayList of Courses that are prerequisites
     */
    public ArrayList<Course> getPrerequisites()
    {
        return prereq;
    }
    /**
     * Mutator method - Sets the list of prerequisite required Courses for the Course
     * @param preReqList the new ArrayList of prerequisites
     */
    protected void setPrerequisites(ArrayList<Course> preReqList) 
    {
        prereq = preReqList;
    }
    /**
     * Copy method - Creates a deep copy of the prerequisite required Course list
     * @return the complete copied ArrayList of prerequisite Courses
     */
    public ArrayList<Course> copyPrerequisites()
    {
        ArrayList<Course> prereqCopy = new ArrayList<>();
        for(Course c : prereq) {
            prereqCopy.add(new Course(c));
        }
        return prereqCopy;
    }
    
    /**
     * Accessor method - Retrieves the semester the Course is offered
     * @return the semester the Course is offered
     */
    public String getSemesterOffered()
    {
        return semesterOffered;
    }
    /**
     * Mutator method - Sets the the semester the Course is offered
     * @param semOffered the semester the Course is offered
     */
    protected void setSemesterOffered(String semOffered)
    {
        semesterOffered = semOffered;
    }
    
    // =========================================================================
    
    /**
     * Retrieves all info about a Course
     * @return a string representation of the Course
     */
    @Override
    public String toString()
    {
        String toReturn = "Course Code: " + code +
                "\tCourse Title: " + title +
                "\tCourse Credit: " + credit +
                "\tSemester Offered: " + semesterOffered +
                "\tPrerequisite Courses: ";
        int counter = 0;
        for(Course c : prereq) {
            if(counter != 0) {
                toReturn += ",";
            }
            toReturn += c.getCourseCode();
            counter++;
        }
        return toReturn;
    }
    
    /**
     * Comparator method -- Compares two Courses to each other
     * @param p the other Object being compared
     * @return true if they have the same attributes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof Course)) {
            return false;
        } else {
            Course other = (Course)p;
            return code.equals(other.getCourseCode()) && credit == other.getCourseCredit() && title.equals(other.getCourseTitle()) &&
                    prereq.equals(other.copyPrerequisites()) && semesterOffered.equals(other.getSemesterOffered());
        }
    }
    
    /**
     * Clone method -- Creates an exact copy of a Course
     * @return a full clone of the Course
     */
    /*@Override
    public Course clone()
    {
        return new Course(code, credit, title, copyPrerequisites(), status, semestertaken, grade);
    }*/
} // end Course class
