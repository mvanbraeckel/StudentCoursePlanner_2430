package planner_a1;

// imports
import univ.*;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 23/11/2018
 * CIS2430 Assignment 2 -- Attempt
 * --> Contains all the information about a Student's attempt at a Course, such as semester and final grade, and a reference to the actual course.
 *      Note that Students can have more than one attempt for the same course.
 *      (grade earned by a Student, semester taken by a Student, status in regards to a Student taking it (IP, C, P), and type (R, E, MA))
 *      ( and a reference to the actual course)
 *      NOTE: grade is either a number, P (pass), F (fail), INC (incomplete), or MNR (mark not released)
 */
public class Attempt
{
    // ATTRIBUTES
    private Course course;
    private String status, semesterTaken, grade, type;
     
    // CONSTRUCTORS
    /**
     * Default Constructors - instantiates an Attempt using default attribute values
     */
    public Attempt()
    {
        status = "N/A";
        semesterTaken = "N/A";
        grade = "N/A";
        type = "N/A";
        course = new Course();
    }
    /**
     * Primary Constructor - instantiates an Attempt with just a Course
     * @param c the Course of the Attempt
     */
    public Attempt(Course c)
    {
        this();
        course = new Course(c);
    }
    /**
     * Full Constructor - instantiates an Attempt with full attributes
     * @param c the Course of the Attempt
     * @param status the status of the Attempt
     * @param semTaken the semester of the Attempt
     * @param grade the grade of the Attempt
     * @param type the type of the Attempt
     */
    public Attempt(Course c, String status, String semTaken, String grade, String type)
    {
        this(c);
        this.status = status;
        semesterTaken = semTaken;
        this.grade = grade;
        this.type = type;
    }
    /**
     * Deep Copy Constructor - instantiates a new Attempt that is a complete, exact copy of another
     * @param original the Attempt to be copied
     */
    public Attempt(Attempt original)
    {
        this(new Course(original.getCourseAttempted()), original.getAttemptStatus(), original.getSemesterTaken(),
                original.getAttemptGrade(), original.getAttemptType());
    }
   
    // BEHAVIOURS
    /**
     * Accessor method - Retrieves the status of the Attempt
     * @return the status of the Attempt
     */
    public String getAttemptStatus()
    {
        return status;
    }
    /**
     * Mutator method - Sets the status of the Attempt
     * @param status the new status of the Attempt
     */
    public void setAttemptStatus(String status)
    {
        this.status = status;
    }
    
    /**
     * Accessor method - Retrieves the grade earned in the Attempt
     * @return the grade earned in the Attempt
     */
    public String getAttemptGrade()
    {
        return grade;
    }
    /**
     * Mutator method - Sets the grade earned in the Attempt
     * @param grade the new grade earned in the Attempt
     */
    public void setAttemptGrade(String grade)
    {
        this.grade = grade;
    }
    
    /**
     * Accessor method - Retrieves the semester the Attempt was taken
     * @return the semester the Attempt was taken
     */
    public String getSemesterTaken()
    {
        return semesterTaken;
    }
    /**
     * Mutator method - Sets the semester the Attempt was taken
     * @param semester the new semester the Attempt was taken
     */
    public void setSemesterTaken(String semester)
    {
        semesterTaken = semester;
    }
    
    /**
    * Accessor method - Retrieves the type of the Attempt
    * @return the type of the Attempt
    */
    public String getAttemptType()
    {
        return type;
    }
    /**
     * Mutator method - Sets the type of the Attempt
     * @param type the new type of the Attempt
     */
    public void setAttemptType(String type)
    {
        this.type = type;
    }
    
    /**
     * Accessor method - Retrieves the Course of the Attempt
     * @return the Course of the Attempt
     */
    public Course getCourseAttempted() {
        return course;
    }
    /**
     * Mutator method - Sets the Course of the Attempt
     * @param theCourse the new Course of the Attempt
     */
    public void setCourseAttempted(Course theCourse) {
        course = theCourse;
    }
    
    // =========================================================================
    
    /**
     * Retrieves all info about an Attempt
     * @return a string representation of the Attempt
     */
    @Override
    public String toString()
    {
        String toReturn = course.toString() + 
                "\nCourse Status: " + status +
                "\tSemester Taken: " + semesterTaken +
                "\tCourse Grade: " + grade +
                "\tCourse Type: " + type;
        return toReturn;
    }
    
    /**
     * Comparator method -- Compares two Attempts to each other
     * @param p the other Object being compared
     * @return true if they have the same attributes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof Attempt)) {
            return false;
        } else {
            Attempt other = (Attempt)p;
            return course.equals(other.getCourseAttempted()) && status.equals(other.getAttemptStatus()) &&
                    semesterTaken.equals(other.getSemesterTaken()) && grade.equals(other.getAttemptGrade()) && type.equals(other.getAttemptType());
        }
    }
}
