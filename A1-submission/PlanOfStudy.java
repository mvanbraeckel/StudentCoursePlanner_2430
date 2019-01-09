//package planner;

// imports
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- PlanOfStudy
 * --> represents a plan of study that helps a Student user plan their future path at the University of Guelph
 *      (has a Student user, selected Degree program, list of courses being planned for)
 * 
 * --> Uses FileReader and FileWriter classes for reading and writing.
 */
public class PlanOfStudy implements Serializable
{
    // ATTRIBUTES
    private Student user;   
    private Degree degreeProgram;
    private ArrayList<Course> scheduledCourses;
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates a PlanOfStudy with default values
     */
    public PlanOfStudy()
    {
        user = new Student();
        degreeProgram = new BCG(); // default into a general, bc it's easier
        scheduledCourses = new ArrayList<>();
    }
    /**
     * Full Constructor - instantiates a PlanOfStudy with full values
     * @param user the Student the PlanOfStudy is for
     * @param deg the degree program they are enrolled in
     * @param courses an ArrayList of courses they scheduled for
     */
    public PlanOfStudy(Student user, Degree deg, ArrayList<Course> courses) {
        this();
        this.user = user.clone();        
        degreeProgram = deg;
        for(Course c : courses) {
            scheduledCourses.add(new Course(c));
        }
    }
    
    // BEHAVIOURS
    /**
     * Accessor method - Retrieves the Student user for the PlanOfStudy
     * @return the student user for the PlanOfStudy
     */
    public Student getUser()
    {
        return user;
    }
    /**
     * Mutator method - Sets the Student user for the PlanOfStudy
     * @param s the new Student user for the PlanOfStudy
     */
    public void setUser(Student s)
    {
        user.setFirstName(s.getFirstName());
        user.setLastName(s.getLastName());
        user.setStudentNumber(s.getStudentNumber());
    }
    
    /**
     * Accessor method - Retrieves the Degree for the PlanOfStudy
     * @return the Degree for the PlanOfStudy
     */
    public Degree getDegreeProgram()
    {
        return degreeProgram;
    }
    /**
     * Mutator method - Sets the Degree
     * @param deg the new Degree
     */
    public void setDegreeProgram(Degree deg)
    {
        degreeProgram = deg;
    }
    
    /**
     * Accessor method - Retrieves the list of courses in the user's PlanOfStudy
     * @return the list of courses in the user's PlanOfStudy
     */
    public ArrayList<Course> getScheduledCourses()
    {
        return scheduledCourses;
    }
    /**
     * Mutator method - Sets the list of courses in the user's PlanOfStudy
     * @param courses the new list of courses for the PlanOfStudy
     */
    public void setScheduledCourses(ArrayList<Course> courses)
    {
        scheduledCourses.clear();
        for(Course c : courses) {
            scheduledCourses.add(new Course(c));
        }
    }
    
    /**
     * Accessor method - Retrieves the number of credits earned so far
     * @return the number of credits earned so far
     */
    public double getCreditsEarned()
    {
        double creditsEarned = 0;
        for(Course c : scheduledCourses) {
            if(c.getCourseStatus().equals("Complete") && Double.parseDouble(c.getCourseGrade()) >= 50.0) {
                creditsEarned += c.getCourseCredit();
            }
        }
        return creditsEarned;
    }
    
    // Planner user story #1 -> must read StudentTranscript.csv into a catalog
    public void importData(String filename)
    {   // declare variables
        boolean eof = false;
        boolean validInput = true;
        String line = "";
        
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            eof = false;
            
            scheduledCourses.clear(); //reset
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
                    if(!isDouble(lineSplit[2]) && !lineSplit[2].equals("")) {
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
                    
                    // check semester (must be F,W, or S semester character, with 2 digits following) or nothing
                    if((lineSplit[3].charAt(0) != 'F' && lineSplit[3].charAt(0) != 'W' && lineSplit[3].charAt(0) != 'S')
                            || lineSplit[3].length() != 3 || !isInteger(lineSplit[3].substring(1, 3))) {
                        System.out.println("Error: importData(" + filename + "): semester '" + lineSplit[3] + "' is invalid");
                        validInput = false;
                    }
                    
                    // if the input line was good, find it in the catalog
                    if(validInput) {
                        Course found = degreeProgram.getCatalog().findCourse(lineSplit[0]);
                        if(found == null) {
                            System.out.println("Error: importData(" + filename + "): course code '" + lineSplit[0] +
                                    "' is not a course at the University of Guelph");
                        } else { //match was found
                             // create a copy, then set attributes before adding to the plan's list of scheduled courses
                             Course c = new Course(found);
                             c.setCourseStatus(lineSplit[1]);
                             c.setCourseGrade(lineSplit[2]);
                             c.setSemesterTaken(lineSplit[3]);
                             scheduledCourses.add(c);
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
     */
    public void saveState()
    {
        saveState("savedPlan.bin");
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
            
            PlanOfStudy thePlan = new PlanOfStudy(user, degreeProgram, scheduledCourses);
            // write the plan's attributes to a binary file
            out.writeObject(thePlan);
            
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
        PlanOfStudy line;
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fin);
            eof = false;
            
            // read the entire binary file
            while(!eof) {
                line = (PlanOfStudy) in.readObject();
                // check if anything is there
                if(line == null) {
                    eof = true;
                } else { // cont reading
                    // set this plan's attributes to the read plan's attributes
                    user = line.getUser();
                    degreeProgram = line.getDegreeProgram();
                    scheduledCourses = line.getScheduledCourses();
                    eof = true; // there should only be one list of Courses in the file, so stop
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
     * Action method - Adds a Course the scheduledCourses list using the CourseCatalog and a specific semester
     * @param courseCode the code of the Course being added
     * @param semester the semester the added Course will be taken
     */
    public void addCourse(String courseCode, String semester)
    {
        Course found = degreeProgram.getCatalog().findCourse(courseCode);
        // only add if a matching course is found
        if(found != null) {
            Course c = new Course(found);
            c.setSemesterTaken(semester);
            scheduledCourses.add(c);
        } else {
            System.out.println("Error: did not find course code '" + courseCode + "' in the Course Catalog.");
        }
    }
    /**
     * Action method - Removes a Course from the scheduledCourses list based on course code and semester
     * @param courseCode the code of the Course being removed
     * @param semester the semester the removed Course is being taken
     */
    public void removeCourse(String courseCode, String semester)
    {
        // only remove if it's in the list
        scheduledCourses.remove(getCourse(courseCode, semester));
    }
    
    /**
     * Search method - finds a Course in the course list based on unique course code
     * @param courseCode the desired course code being searched for
     * @return the Course with the matching course code, otherwise null if not found
     */
    public Course findCourse(String courseCode)
    {
        for(Course c : scheduledCourses) {
            if(c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null; // course code not found
    }
    /**
     * Accessor method - Retrieves a Course in the scheduledCourses list based on course code and semester
     * @param courseCode the desired course code being searched for
     * @param semester the semester Course is being taken
     * @return the Course with the matching course code, otherwise null if not found
     */
    public Course getCourse(String courseCode, String semester)
    {
        for(Course c : scheduledCourses) {
            if(c.getCourseCode().equals(courseCode) && c.getSemesterTaken().equals(semester)) {
                return c;
            }
        }
        return null; // match not found
    }
    
    /**
     * Mutator method - Sets the status of a Course in the scheduledCourses
     * @param courseCode the desired course code being searched for
     * @param semester the semester Course is being taken
     * @param courseStatus the new status of the Course
     */
    public void setCourseStatus(String courseCode, String semester, String courseStatus)
    {
        // loops through the course list attribute to find a matching course code and semester, then updates that course's status
        Course found = getCourse(courseCode, semester);
        if(found != null) {
            found.setCourseStatus(courseStatus);
        } else {
            System.out.println("Error: did not find course code '" + courseCode + "' with semester '" + semester + "' in the Course Catalog.");
        }
    }
    /**
     * Mutator method - Sets the grade of a Course in the scheduledCourses
     * @param courseCode the desired course code being searched for
     * @param semester the semester Course is being taken
     * @param grade the new grade of the Course
     */
    public void setCourseGrade(String courseCode, String semester, String grade)
    {
        // loops through the course list attribute to find a matching course code and semester, then updates that course's grade
        Course found = getCourse(courseCode, semester);
        if(found != null) {
            found.setCourseGrade(grade);
        } else {
            System.out.println("Error: did not find course code '" + courseCode + "' with semester '" + semester + "' in the Course Catalog.");
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
    
    // =========================================================================
    
    /**
     * Retrieves all info about a PlanOfStudy
     * @return a string representation of the PlanOfStudy
     */
    @Override
    public String toString()
    {
        int counter = 0;
        String toReturn = user.toString() + "\n" + degreeProgram.toString() + "\nCredits Earned: " + getCreditsEarned() + "\nCourse List:\n";
        for(Course c : scheduledCourses) {
            if(counter != 0) {
                toReturn += "\n";
            }
            toReturn += c.toString();
            counter++;
        }
        return toReturn;
    }
    
    /**
     * Comparator method -- Compares two plans of study to each other
     * @param p the other Object being compared
     * @return true if they have the same Student user, Degree program, and course list
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof PlanOfStudy)) {
            return false;
        } else {
            PlanOfStudy other = (PlanOfStudy)p;
            return user.equals(other.getUser()) && degreeProgram.equals(other.getDegreeProgram()) &&
                    scheduledCourses.equals(other.getScheduledCourses());
        }
    }
    
} // end PlanOfStudy class
