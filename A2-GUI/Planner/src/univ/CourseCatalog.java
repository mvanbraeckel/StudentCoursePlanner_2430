package univ;

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
import javax.swing.JOptionPane;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- CourseCatalog
 * NOTE: Courses in the catalog will always have a unique course code
 * --> Represents all the Courses at the University of Guelph in catalog list
 */
public class CourseCatalog implements Serializable
{
    // ATTRIBUTES
    private ArrayList<Course> courses;
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates and initializes the attribute
     */
    public CourseCatalog()
    {
        courses = new ArrayList<>();
        initializeCatalog("courseList.csv");
    }
    /**
     * Primary Constructor - instantiates with a list of Courses
     * @param courses the ArrayList of Courses to be stored in the catalog
     */
    public CourseCatalog(ArrayList<Course> courses)
    {
        courses = new ArrayList();
        for(Course c : courses) {
            this.courses.add(c.shallowClone());
        }
    }
    
    
    // BEHAVIOURS
    /**
     * Accessor method - Retrieves the number of Courses in the CourseCatalog
     * @return the number of Courses in the CourseCatalog
     */
    public int getCatalogSize()
    {
        return courses.size();
    }
    /**
     * Accessor method - Retrieves all the Courses in the CourseCatalog
     * @return the full list of Courses in the CourseCatalog
     */
    public ArrayList<Course> getFullCourseList() {
        return courses;
    }
    
    /**
     * Action method - Adds a Course to the catalog
     * @param toAdd the Course to be added to the catalog
     */
    protected void addCourse(Course toAdd)
    {
        // only add if it's not already in the catalog
        if(findCourse(toAdd.getCourseCode()) == null) {
            courses.add(toAdd);
        } else {
            JOptionPane.showMessageDialog(null, "Add Course to Catalog Error: NullPointerException encountered - Course being added was null", 
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Action method - Removes a Course from the catalog
     * @param toRemove the Course to be removed from the catalog
     */
    public void removeCourse(Course toRemove)
    {
        // only remove if it's in the catalog
        //Course r = findCourse(toRemove.getCourseCode());
        courses.remove(findCourse(toRemove.getCourseCode()));
    }
    
    /**
     * File Read method - reads and loads a CSV file that contains a list of courses at UoG
     * @param filename the name of the CSV file to be read
     */
    public final void initializeCatalog(String filename)
    {
        boolean encounteredErrors = false;
        String output = "";
        // declare variables
        boolean eof = false;
        boolean validInput = true;
        String line = "";
        Course c;
        
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            eof = false;
            
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
                    
                    //break code into subject and number, clear extra whitespace
                    String codeSplit[] = lineSplit[0].split("\\*");
                    for(String s : codeSplit) {
                        s = s.trim();
                    }
                    // load potentially altered course code back
                    if(codeSplit.length > 1) {
                        for(int i = 0; i < codeSplit.length; i++) {
                            if(i == 0) {
                                lineSplit[0] = codeSplit[i];
                            } else {
                                lineSplit[0] += "*" + codeSplit[i];
                            }
                        }
                    }
                    
                    // check course code (length for subject and number)
                    if(codeSplit.length != 2 || codeSplit[0].length() < 3 || codeSplit[0].length() > 4 || 
                            codeSplit[1].length() != 4 || !isInteger(codeSplit[1])) {
                        output += "\nError: initializeCatalog(" + filename + "): course code '" + lineSplit[0] + "' is invalid";
                        encounteredErrors = true;
                        validInput = false;
                        continue; //do not add to catalog because invalid course code format
                    }
                    
                    // check credit
                    if(!isDouble(lineSplit[1])) {
                        output += "\nError: initializeCatalog(" + filename + "): course credit '" + lineSplit[1] + "' is invalid";
                        encounteredErrors = true;
                        validInput = false;
                        continue; //do not add to catalog because invalid credit
                    }
                    
                    // check course title is not empty
                    if(lineSplit[2].equals("")) {
                        output += "\nError: initializeCatalog(" + filename + "): course title must exist";
                        encounteredErrors = true;
                        validInput = false;
                        continue; //do not add to catalog because invalid credit
                    }
                    
                    // check semester offered
                    if(!lineSplit[3].equals("F") && !lineSplit[3].equals("W") && !lineSplit[3].equals("B")) {
                        output += "\nError: initializeCatalog(" + filename + "): semester offered '" + lineSplit[3] + "' is invalid";
                        encounteredErrors = true;
                        validInput = false;
                        continue; //do not add to catalog because invalid credit
                    }
                    
                    boolean noPrerequisites = false;
                    String validPrereq = ""; // stores a string to be split of all good input prereq courses
                    // check list of prerequisite courses
                    if(lineSplit.length == 5) {
                        String prereqSplit[] = lineSplit[4].split(":"); //break into multiple course codes, clear extra whitespace

                        String prereqCodeSplit[];
                        int counter = 0;
                        // check each course code for prereq courses is valid
                        for(String code : prereqSplit) {
                            prereqCodeSplit = code.trim().split("\\*"); //break code into subject and number, clear extra whitespace
                            for(String s : prereqCodeSplit) {
                                s = s.trim();
                            }
                            // load potentially altered course code back
                            if(prereqCodeSplit.length > 1) {
                                for(int i = 0; i < prereqCodeSplit.length; i++) {
                                    if(i == 0) {
                                        code = prereqCodeSplit[i];
                                    } else {
                                        code += "*" + prereqCodeSplit[i];
                                    }
                                }
                            }

                            // now, check course code (length for subject and number)
                            if(prereqCodeSplit.length != 2 || prereqCodeSplit[0].length() < 3 || prereqCodeSplit[0].length() > 4 || 
                                    prereqCodeSplit[1].length() != 4 || !isInteger(prereqCodeSplit[1])) {
                                output += "\nError: initializeCatalog(" + filename + "): prerequisite course code '" + code + "' is invalid";
                                encounteredErrors = true;
                                continue; //do not add as a prerequisite course because invalid course code format
                            }

                            // if reaches here, then the the course prerequisite is valid, so add it to the storing list
                            if(counter == 0) {
                                validPrereq += code;
                            } else {
                                validPrereq += ":" + code;
                            }                        
                            counter++; // accumulate since it was a good prereq course input
                        }
                    } else {
                        noPrerequisites = true;
                    }
                    String validPrereqSplit[] = validPrereq.split(":"); // break the good input prereq courses into separate course codes
                    // if the input line was good, add it to the catalog
                    if(validInput) {
                        c = new Course(lineSplit[0]);
                        c.setCourseCredit(Double.parseDouble(lineSplit[1]));
                        c.setCourseTitle(lineSplit[2]);
                        c.setSemesterOffered(lineSplit[3]);
                        
                        ArrayList<Course> prerequisites = new ArrayList<>();
                        if(noPrerequisites) {
                            prerequisites.add(new Course("NONE"));
                        } else {
                            for(String prereqCourseCode : validPrereqSplit) {
                                Course prereqCourse;
                                // check if it has already been read
                                Course found = findCourse(prereqCourseCode);
                                if(found == null) {
                                    prereqCourse = new Course(prereqCourseCode); //only has a course code
                                } else { //match was found
                                    prereqCourse = found; //references the already added prerequisite Course
                                }
                                prerequisites.add(prereqCourse);
                            }
                        }
                        
                        c.setPrerequisites(prerequisites);
                        courses.add(c);
                    }
                    
                } // end else
            } // end while loop
        } catch (IOException e) {
            output = "Error - initializeCatalog(" + filename + "): IOException -> " + e + "\n" + output;
            JOptionPane.showMessageDialog(null, "Errors encountered - initializeCatalog(" + filename + "):\n\n" + output,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
        }
        
        // go through and make sure all prerequisite courses hold full courses (not just codes)
        boolean allTitlesFilled = false;
        int counter = 0;
        ArrayList<Course> prereqList;
        
        while(!allTitlesFilled) {
            counter = 0;
            for(Course course : courses) {
                prereqList = course.getPrerequisites();
                if(!prereqList.isEmpty()) {
                    for(Course prereqCourse : prereqList) {
                        if(!prereqCourse.getCourseCode().equals("NONE") && prereqCourse.getCourseTitle().equals("Unknown")) {
                            counter++;
                            // check if it has already been read
                            Course found = findCourse(prereqCourse.getCourseCode());
                            if(found != null) {
                                int index = prereqList.indexOf(prereqCourse);
                                prereqList.set(index, found); //references the already added prerequisite Course
                            }
                        }
                    }
                }
            }
            // means that none have a title of unknown
            if(counter == 0) {
                allTitlesFilled = true;
            }
        }
        
        // display errors that occured
        if(encounteredErrors) {
            JOptionPane.showMessageDialog(null, "Errors encountered - initializeCatalog(" + filename + "):\n" + output,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * File Write method - Writes a list of Courses at the University of Guelph to the "catalog.bin" file
     */
    public void saveCatalog()
    {
        saveCatalog("catalog.bin");
    }
    /**
     * File Write method - Writes a list of Courses at the University of Guelph to a binary file
     * @param filename the name of the binary file being written to
     */
    public void saveCatalog(String filename)
    {
        try {
            FileOutputStream fout = new FileOutputStream("catalog.bin");
            ObjectOutputStream out = new ObjectOutputStream(fout);
            
            // write the catalog as a list of Courses to a binary file
            out.writeObject(courses);
            
            // close streams
            out.close();
            fout.close();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error - saveCatalog: IOException -> " + e,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * File Read method - Reads and loads a list of Courses at the University of Guelph from a binary file
     * @param filename the name of the binary file to be read from
     */
    protected void readSavedCatalog(String filename) {
        boolean eof;
        ArrayList<Course> line;
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fin);
            eof = false;
            
            // read the entire binary file
            while(!eof) {
                line = (ArrayList<Course>) in.readObject();
                // check if anything is there
                if(line == null) {
                    eof = true;
                } else { // cont reading
                    courses = line;
                    eof = true; // there should only be one CourseCatalog object in the file, so stop
                }
            }
            // close streams
            in.close();
            fin.close();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error - readSavedCatalog: IOException -> " + e,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error - readSavedCatalog: ClassNotFoundException -> " + e,
                    "CIS*2430: Planner Assignment 2", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Search method - finds a Course in the catalog based on unique course code
     * @param courseCode the desired course code being searched for
     * @return the Course with the matching course code, otherwise null if not found
     */
    public Course findCourse(String courseCode)
    {
        for(Course c : courses) {
            if(c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null; // course code not found
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
     * Retrieves all info about a CourseCatalog
     * @return a string representation of the CourseCatalog (all the string representations of the Courses it contains)
     */
    @Override
    public String toString()
    {
        String toReturn = "";
        int counter = 0;
        for(Course c : courses) {
            if(counter != 0) {
                toReturn += "\n";
            }
            toReturn += c.toString();
            counter++;
        }
        return toReturn;
    }
    
    /**
     * Comparator method -- Compares two CourseCatalogs to each other
     * @param p the other Object being compared
     * @return true if they have the same the same number of courses with the same course codes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof CourseCatalog)) {
            return false;
        } else {
            CourseCatalog other = (CourseCatalog)p;
            if(getCatalogSize() != other.getCatalogSize()) {
                return false; // not the same size
            } else {
                // a Course is not contained in the other CourseCatalog
                for(Course c : courses) {
                    if(other.findCourse(c.getCourseCode()) == null) {
                        return false; // not found in other CourseCatalog
                    }
                }
            }
            return true;
        }
    }
} // end CourseCatalog class
