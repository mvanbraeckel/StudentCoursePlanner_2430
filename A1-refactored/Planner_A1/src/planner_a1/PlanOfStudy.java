package planner_a1;

// imports
import univ.*;
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
        for(Attempt c : transcript) {
            if(c.getAttemptStatus().equals("Complete")) {
                creditsEarned += c.getCourseAttempted().getCourseCredit();
            }
        }
        return creditsEarned;
    }
    
    // Planner user story #1 -> must read StudentTranscript.csv into a catalog
    /*public void importData(String filename)
    {   // declare variables
        boolean eof = false;
        boolean validInput = true;
        String line = "";
        
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            eof = false;
            
            transcript.clear(); //reset
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
                             transcript.add(c);
                        }                       
                    }
                    
                } // end else
            } // end while loop
        } catch (IOException e) {
            System.out.println("Error: IOException: " + e);;
        }
    }*/
    
    /*
     * File Write method - writes a single PlanOfStudy object to a binary file to save it
     */
    /*public void saveState()
    {
        saveState("savedPlan.bin");
    }*/
    /*
     * File Write method - writes a single PlanOfStudy object to a binary file to save it
     * @param filename the name of the binary file being written to
     */
    /*public void saveState(String filename)
    {
        try {
            FileOutputStream fout = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            
            PlanOfStudy thePlan = new PlanOfStudy(degreeProgram, transcript);
            // write the plan's attributes to a binary file
            out.writeObject(thePlan);
            
            // close streams
            out.close();
            fout.close();
            
        } catch (IOException e) {
            System.out.println("Error: IOException -> " + e);
        }
    }*/
    
    /*
     * File Read method - reads a single saved PlanOfStudy object from a binary file
     * @param filename the name of the binary file being read from
     */
    /*public void readSavedState(String filename)
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
                    transcript = line.getTranscript();
                    eof = true; // there should only be one list of Courses in the file, so stop
                }
            }
            // close streams
            in.close();
            fin.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error: FileNotFoundException -> " + ex);
        } catch (IOException ex) {
            System.out.println("Error: IOException -> " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: ClassNotFoundException -> " + ex);
        } 
    }
    
    /**
     * Accessor method - Retrieves an Attempt in the transcript list based on course code and semester
     * @param courseCode the desired course code being searched for
     * @param semester the semester it is being taken
     * @return the Attempt with the matching course code, otherwise null if not found
     */
    public Attempt getAttempt(String courseCode, String semester)
    {
        for(Attempt c : transcript) {
            if(c.getCourseAttempted().getCourseCode().equals(courseCode) && c.getSemesterTaken().equals(semester)) {
                return c;
            }
        }
        return null; // match not found
    }
    /**
     * Search method - finds an Attempt in the course list based on unique course code
     * @param courseCode the desired course code being searched for
     * @return the Attempt with the matching course code, otherwise null if not found
     */
    public Attempt findAttempt(String courseCode)
    {
        for(Attempt c : transcript) {
            if(c.getCourseAttempted().getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null; // course code not found
    }
    /**
     * Action method - Adds an Attempt to the transcript list using the CourseCatalog and a specific semester
     * @param courseCode the course code of the for the Attempt being added
     * @param semester the semester it will be taken
     */
    public void addCourse(String courseCode, String semester)
    {
        Course found = degreeProgram.getCatalog().findCourse(courseCode);
        // only add if a matching course is found
        if(found != null) {
            Attempt c = new Attempt(found);
            c.setSemesterTaken(semester);
            transcript.add(c);
        } else {
            System.out.println("Error: did not find course code '" + courseCode + "' in the Course Catalog.");
        }
    }
    /**
     * Action method - Removes an Attempt from the transcript list based on course code and semester
     * @param courseCode the course code of the Attempt being removed
     * @param semester the semester the removed Attempt was being taken
     */
    public void removeCourse(String courseCode, String semester)
    {
        // only remove if it's in the list
        transcript.remove(getAttempt(courseCode, semester));
    }
    
    /**
     * Mutator method - Sets the status of an Attempt in the transcript
     * @param courseCode the desired course code being searched for
     * @param semester the semester it is being taken
     * @param newStatus the new status of the Attempt
     */
    public void setAttemptStatus(String courseCode, String semester, String newStatus)
    {
        // loops through the transcript to find a matching course code and semester, then updates it's status
        Attempt found = getAttempt(courseCode, semester);
        if(found != null) {
            found.setAttemptStatus(newStatus);
        } else {
            System.out.println("Error: did not find course code '" + courseCode + "' with semester '" + semester + "' in the transcript.");
        }
    }
    /**
     * Mutator method - Sets the grade of an Attempt in the transcript
     * @param courseCode the desired course code being searched for
     * @param semester the semester it is being taken
     * @param grade the new grade of the Attempt
     */
    public void setAttemptGrade(String courseCode, String semester, String grade)
    {
        // loops through the transcript to find a matching course code and semester, then updates that course's grade
        Attempt found = getAttempt(courseCode, semester);
        if(found != null) {
            found.setAttemptGrade(grade);
        } else {
            System.out.println("Error: did not find course code '" + courseCode + "' with semester '" + semester + "' in the transcript.");
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
        String toReturn = degreeProgram.toString() + "\nCredits Earned: " + getCreditsEarned() + "\nCourse List:\n";
        for(Attempt c : transcript) {
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
            return degreeProgram.equals(other.getDegreeProgram()) &&
                    transcript.equals(other.getTranscript());
        }
    }
    
} // end PlanOfStudy class
