//package planner;

// imports
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- Course
 * --> Represents a Course at the University of Guelph (has a title and code, credit weight, list of prerequisites,
 *      grade earned by a Student, semester taken by a Student, status in regards to a Student taking it (IP, C, P && R, E, MA)
 */
public class Course implements Serializable
{
    // ATTRIBUTES
    private String code, title, status, semesterTaken, grade, type;
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
        status = "N/A";
        semesterTaken = "N/A";
        credit = 0.0;
        grade = "0.0";
        prereq = new ArrayList<>();
        type = "N/A";
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
     * @param code the code of the Course
     * @param credit the amount of credits earned for completing the Course
     * @param title the title of the Course
     * @param prereq the list of Courses that are prerequisite requirements for the Course
     * @param status the current status of the Course
     * @param semesterTaken the most recent semester the course was taken
     * @param grade the grade achieved in the Course
     * @param type the type of Course it is (required, elective, or minor/area of application)
     */
    public Course(String code, double credit, String title, ArrayList<Course> prereq, String status, String semesterTaken, String grade, String type)
    {
        this(code, credit, title, prereq);
        this.status = status;
        this.semesterTaken = semesterTaken;
        this.grade = grade;
        this.type = type;
    }
    /**
     * Deep Copy Constructor - instantiates a new Course that is a complete, exact copy of another
     * @param original the Course to be copied
     */
    public Course(Course original)
    {
        this(original.getCourseCode(), original.getCourseCredit(), original.getCourseTitle(), original.copyPrerequisites(),
                original.getCourseStatus(), original.getSemesterTaken(), original.getCourseGrade(), original.getCourseType());
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
    public void setCourseCode(String courseCode)
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
    public void setCourseTitle(String courseTitle)
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
    public void setCourseCredit(double credit)
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
    public void setPrerequisites (ArrayList<Course> preReqList) 
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
     * Accessor method - Retrieves the status of the Course
     * @return the status of the Course
     */
    public String getCourseStatus()
    {
        return status;
    }
    /**
     * Mutator method - Sets the status of the Course
     * @param courseStatus the new status of the Course
     */
    public void setCourseStatus(String courseStatus)
    {
        status = courseStatus;
    }
    
    /**
     * Accessor method - Retrieves the grade earned in the Course
     * @return the grade earned in the Course
     */
    public String getCourseGrade()
    {
        return grade;
    }
    /**
     * Mutator method - Sets the grade earned in the Course
     * @param grade the new grade earned in the Course
     */
    public void setCourseGrade(String grade)
    {
        this.grade = grade;
    }
    
    /**
     * Accessor method - Retrieves the semester the Course was taken
     * @return the semester the Course was taken
     */
    public String getSemesterTaken()
    {
        return semesterTaken;
    }
    /**
     * Mutator method - Sets the semester the Course was taken
     * @param semester the new semester the Course was taken
     */
    public void setSemesterTaken(String semester)
    {
        semesterTaken = semester;
    }
    
    /**
    * Accessor method - Retrieves the type of the Course
    * @return the type of the Course
    */
    public String getCourseType()
    {
        return type;
    }
    /**
     * Mutator method - Sets the type of the Course
     * @param courseType the new type of the Course
     */
    public void setCourseType(String courseType)
    {
        type = courseType;
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
                "\nCourse Credit: " + credit +
                "\nCourse Title: " + title +
                "\nPrerequisite Courses: ";
        int counter = 0;
        for(Course c : prereq) {
            if(counter != 0) {
                toReturn += ",";
            }
            toReturn += c.getCourseCode();
            counter++;
        }
        toReturn += "\nCourse Status: " + status +
                "\nSemester Taken: " + semesterTaken +
                "\nCourse Grade: " + grade +
                "\nCourse Type: " + type;
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
                    prereq.equals(other.copyPrerequisites()) && status.equals(other.getCourseStatus()) &&
                    semesterTaken.equals(other.getSemesterTaken()) && grade.equals(other.getCourseGrade()) && type.equals(other.getCourseType());
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
