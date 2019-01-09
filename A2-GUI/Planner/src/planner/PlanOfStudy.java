package planner;

// imports
import univ.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

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
    private Degree degreeProgram;
    private ArrayList<Attempt> transcript;
    private ArrayList<Attempt> plannedList;
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates a PlanOfStudy with default values
     */
    public PlanOfStudy()
    {
        degreeProgram = new BCG(); // default into a general, bc it's easier
        transcript = new ArrayList<>();
        plannedList = new ArrayList<>();
    }
    /**
     * Full Constructor - instantiates a PlanOfStudy with full values
     * @param deg the degree program they are enrolled in
     * @param transcript the Student's transcript containing a list of Attempts of Courses (completed or in progress)
     * @param plannedCourses the Student's list of Courses they plan to take
     */
    public PlanOfStudy(Degree deg, ArrayList<Attempt> transcript, ArrayList<Attempt> plannedCourses) {
        this();
        degreeProgram = deg;
        for(Attempt c : transcript) {
            this.transcript.add(new Attempt(c));
        }
        for(Attempt p : plannedCourses) {
            this.transcript.add(new Attempt(p));
        }
    }
    
    // BEHAVIOURS
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
     * Accessor method - Retrieves the Student's transcript
     * @return the Student's transcript
     */
    public ArrayList<Attempt> getTranscript()
    {
        return transcript;
    }
    /**
     * Mutator method - Sets the Student's transcript
     * @param transcript the Student's new transcript
     */
    public void setTranscript(ArrayList<Attempt> transcript)
    {
        this.transcript.clear();
        for(Attempt c : transcript) {
            this.transcript.add(new Attempt(c));
        }
    }
    
    /**
     * Accessor method - Retrieves the Student's list of planned course attempts
     * @return the Student's list of planned course attempts
     */
    public ArrayList<Attempt> getPlannedList()
    {
        return plannedList;
    }
    /**
     * Mutator method - Sets the Student's list of planned course attempts
     * @param list the Student's new list of planned course attempts
     */
    public void setPlannedList(ArrayList<Attempt> list)
    {
        plannedList.clear();
        for(Attempt c : plannedList) {
            plannedList.add(new Attempt(c));
        }
    }
    
    /**
     * Accessor method - Retrieves the number of credits earned so far
     * @return the number of credits earned so far
     */
    public double getCreditsEarned()
    {
        double creditsEarned = 0;
        String sGrade = "";
        HashMap<String,Double> passedCourses = new HashMap<>();
        
        for(Attempt c : transcript) {
            // must be complete and have a passing grade (P or number [50,100])
            if(c.getAttemptStatus().equals("Complete")) {
                sGrade = c.getAttemptGrade();
                if(sGrade.equals("P") || (isDouble(sGrade) && Double.parseDouble(sGrade) >= 50.0 && Double.parseDouble(sGrade) <= 100.0)) {
                    // add it to the list of passed courses, if it's not already there
                    passedCourses.putIfAbsent(c.getCourseAttempted().getCourseCode(), c.getCourseAttempted().getCourseCredit());
                }
            }
        }
        // count total number of credits earned
        for(String code : passedCourses.keySet()) {
            creditsEarned += passedCourses.get(code);
        }
        return creditsEarned;
    }
    
    /**
     * Accessor method - Retrieves an Attempt in the transcript list based on course code and semester
     * @param courseCode the desired course code being searched for
     * @param semester the semester it is being taken
     * @return the Attempt with the matching course code, otherwise null if not found
     */
    public Attempt getAttemptFromTranscript(String courseCode, String semester)
    {
        for(Attempt a : transcript) {
            if(a.getCourseAttempted().getCourseCode().equals(courseCode) && a.getSemesterTaken().equals(semester)) {
                return a;
            }
        }
        return null; // match not found
    }
    /**
     * Accessor method - Retrieves an Attempt in the planned list based on course code and semester
     * @param courseCode the desired course code being searched for
     * @param semester the semester it is being taken
     * @return the Attempt with the matching course code, otherwise null if not found
     */
    public Attempt getAttemptFromPlannedList(String courseCode, String semester)
    {
        for(Attempt a : plannedList) {
            if(a.getCourseAttempted().getCourseCode().equals(courseCode) && a.getSemesterTaken().equals(semester)) {
                return a;
            }
        }
        return null; // match not found
    }
    
    /**
     * Search method - finds an Attempt in the course list based on unique course code
     * @param courseCode the desired course code being searched for
     * @return the Attempt with the matching course code, otherwise null if not found
     */
    public Attempt findAttemptInTranscript(String courseCode)
    {
        for(Attempt a : transcript) {
            if(a.getCourseAttempted().getCourseCode().equals(courseCode)) {
                return a;
            }
        }
        return null; // course code not found
    }
    
    /**
     * Action method - Adds an Attempt to the transcript list
     * @param courseCode the course code of the Attempt being added
     * @param status the status of the Attempt being added
     * @param grade the grade of the Attempt being added
     * @param type the type of the Attempt being added
     * @param semester the semester it will be taken
     * @return true if it works
     */
    public boolean addToTranscript(String courseCode, String status, String grade, String type, String semester)
    {
        Course found = degreeProgram.getCatalog().findCourse(courseCode);
        // only add if a matching course is found
        if(found != null) {
            Attempt a = new Attempt(found);
            a.setAttemptStatus(status);
            a.setAttemptGrade(grade);
            a.setAttemptType(type);
            a.setSemesterTaken(semester);
            
            ArrayList<String> prereqsNotMatchPass = new ArrayList<>();
            String sGrade = "";
            // only add if all prereqs are completed with a passing grade
            boolean passedAllPrereqs = true;
            boolean matchPass = false;
            for(Course prereq : a.getCourseAttempted().getPrerequisites()) {
                matchPass = false;
                if(!prereq.getCourseCode().equals("NONE")) {
                    for(Attempt fromTranscript : transcript) {
                        if(fromTranscript.getCourseAttempted().getCourseCode().equals(prereq.getCourseCode())) {
                            // check if it's a passing grade
                            sGrade = fromTranscript.getAttemptGrade();
                            if(sGrade.equals("P") || (isInteger(sGrade) && Integer.parseInt(sGrade) >= 50.0 && Integer.parseInt(sGrade) <= 100.0)) {
                                matchPass = true;
                                break; // found match -> so skip
                            }
                        }
                    }
                    if(!matchPass) {
                        prereqsNotMatchPass.add(prereq.getCourseCode());
                        passedAllPrereqs = false;
                    }
                }
            }
            
            // can only add if this course is not already in this semester, and all prereqs have been passed
            if(getAttemptFromTranscript(courseCode, semester) == null && getAttemptFromPlannedList(courseCode, semester) == null) {
                if(passedAllPrereqs) {
                    transcript.add(a);
                    return true;
                } else {
                    String output = "Error: cannot add course code '" + courseCode + "' to the transcript because " +
                            "you have not passed the following prerequisite courses: ";
                    int counter = 0;
                    for(String code : prereqsNotMatchPass) {
                        if(counter != 0) {
                            output += ", " + code;
                        } else {
                            output += code;
                        }
                        counter++;
                    }
                    JOptionPane.showMessageDialog(null, output,
                            "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error: cannot add '" + courseCode + 
                        "' in semester '" + semester + "' because it is already in that semester",
                        "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error: did not find course code '" + courseCode + "' in the Course Catalog",
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    /**
     * Action method - Adds an Attempt to the planned list
     * @param courseCode the course code of the Attempt being added
     * @param status the status of the Attempt being added
     * @param grade the grade of the Attempt being added
     * @param type the type of the Attempt being added
     * @param semester the semester it will be taken
     * @return true if it works
     */
    public boolean addToPlannedList(String courseCode, String status, String grade, String type, String semester)
    {
        Course found = degreeProgram.getCatalog().findCourse(courseCode);
        // only add if a matching course is found
        if(found != null) {
            Attempt a = new Attempt(found);
            a.setAttemptStatus(status);
            a.setAttemptGrade(grade);
            a.setAttemptType(type);
            a.setSemesterTaken(semester);
            
            // can only add if this course is not already in this semester, and all prereqs have been passed
            if(getAttemptFromTranscript(courseCode, semester) == null && getAttemptFromPlannedList(courseCode, semester) == null) {
                plannedList.add(a);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error: cannot add '" + courseCode + 
                        "' in semester '" + semester + "' because it is already in that semester",
                        "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error: did not find course code '" + courseCode + "' in the Course Catalog",
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Action method - Removes an Attempt from the transcript list based on course code and semester
     * @param courseCode the course code of the Attempt being removed
     * @param semester the semester the removed Attempt was being taken
     * @return true if it worked
     */
    public boolean removeFromTranscript(String courseCode, String semester)
    {
        // only remove if it's in the list
        Attempt toRemove = getAttemptFromTranscript(courseCode, semester);
        if(toRemove != null) {
            transcript.remove(toRemove);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error: did not find course code '" + courseCode + 
                    "' in semester '" + semester + "' within the transcript",
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    /**
     * Action method - Removes an Attempt from the planned list based on course code and semester
     * @param courseCode the course code of the Attempt being removed
     * @param semester the semester the removed Attempt was being taken
     * @return true if it worked
     */
    public boolean removeFromPlannedList(String courseCode, String semester)
    {
        // only remove if it's in the list
        Attempt toRemove = getAttemptFromPlannedList(courseCode, semester);
        if(toRemove != null) {
            plannedList.remove(toRemove);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error: did not find course code '" + courseCode + 
                    "' in semester '" + semester + "' within the list of planned course attempts",
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Mutator method - Sets the status of an Attempt in the transcript
     * @param courseCode the desired course code being searched for
     * @param semester the semester it is being taken
     * @param newStatus the new status of the Attempt
     * @return true if it worked
     */
    public boolean setAttemptStatus(String courseCode, String semester, String newStatus)
    {
        // loops through the transcript to find a matching course code and semester, then updates it's status
        Attempt found = getAttemptFromTranscript(courseCode, semester);
        if(found != null) {
            found.setAttemptStatus(newStatus);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Errors encountered - setAttemptStatus: did not find course code '" + courseCode + "' with semester '" + semester + "' in the transcript.",
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    /**
     * Mutator method - Sets the grade of an Attempt in the transcript
     * @param courseCode the desired course code being searched for
     * @param semester the semester it is being taken
     * @param grade the new grade of the Attempt
     * @return true if it worked
     */
    public boolean setAttemptGrade(String courseCode, String semester, String grade)
    {
        // loops through the transcript to find a matching course code and semester, then updates that course's grade
        Attempt found = getAttemptFromTranscript(courseCode, semester);
        if(found != null) {
            found.setAttemptGrade(grade);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Errors encountered - setAttemptGrade: did not find course code '" + courseCode + "' with semester '" + semester + "' in the transcript.",
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Sorts the Student's transcript by semester taken
     */
    public void sortTranscript() {
        sortAttemptsAscending(transcript);
    }
    /**
     * Sorts the Student's plannedList by semester they plan to take it
     */
    public void sortPlannedList() {
        sortAttemptsAscending(plannedList);
    }
    /**
     * Alphabetically sorts a given list of attempted courses by the semester taken (ignoring case) in ascending order
     * @param attemptList the list of Attempts to be sort
     */
    public void sortAttemptsAscending(ArrayList<Attempt> attemptList) {
        boolean flag = true;    //set flag to true to begin first pass
        int j = 0;              //how much the end of the array has been sorted
        Attempt temp;               //temporary holding variable
        // runs until a swap didn't occur during a loop through of entire array
        while(flag){
            flag = false;       //set flag to false awaiting a possible swap
            j++;                //accumulate since each iteration the first and last values are sorted
            // run through the unsorted part of array
            for(int i = 0; i < attemptList.size() - j; i++){
                // check if the current value is greater than the next value
                if(attemptList.get(i).getSemesterTaken().compareToIgnoreCase(attemptList.get(i+1).getSemesterTaken()) > 0) {
                    // swaps lower value on right with a higher value on left
                    temp = attemptList.get(i);
                    attemptList.set(i, attemptList.get(i+1));
                    attemptList.set(i+1, temp);
                    flag = true;            //shows a swap occurred  
                } // end if
            }
        }
    }
    
    /**
     * Displays a combined list of Attempts for transcript and plannedList sorted by semesterTaken
     * @return the output to display the Attempt list sorted by semesterTaken
     */
    public String viewPlanBySemester()
    {
        ArrayList<Attempt> combinedList = new ArrayList<>();
        // add all attempts from transcript and plannedList into one combined list
        for(Attempt a : transcript) {
            combinedList.add(a);
        }
        for(Attempt a : plannedList) {
            combinedList.add(a);
        }
        // sort it by semester taken
        sortAttemptsAscending(combinedList);
        
        String headerDivide = "====================\t==================================================\t=============\t================\t==============================" +
                "\t==============\t=============\t============\t===========\n";
        // create the output
        String output = "Here is your plan of study, organized by semester:\n\n" + headerDivide + 
                String.format("%-" + 18 + "." + 18 + "s", "Course Code") +
                "\tCourse Title\t\t\t\t\t\tCourse Credit\tSemester Offered\t" +
                String.format("%-" + 30 + "." + 30 + "s", "Prerequisite Courses") + 
                "\tSemester Taken\tCourse Status\tCourse Grade\tCourse Type\n" + headerDivide;
        for(Attempt a : combinedList) {
            output += a.toString() + "\n";
        }
        output += headerDivide;
        
        if(combinedList.isEmpty()) {
            output += "\nYour plan of study is empty";
        } else if(transcript.isEmpty()) {
            output += "\nYour transcript is empty";
        } else if(plannedList.isEmpty()) {
            output += "\nYour do not have any planned course attempts";
        }
        
        return output;
    }
    
    // =========================================================================
    
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
        String toReturn = degreeProgram.toString() + "\nCredits Earned:\t\t" + getCreditsEarned() + "\n\n" + viewPlanBySemester();
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
            return degreeProgram.equals(other.getDegreeProgram()) &&
                    transcript.equals(other.getTranscript());
        }
    }
    
} // end PlanOfStudy class
