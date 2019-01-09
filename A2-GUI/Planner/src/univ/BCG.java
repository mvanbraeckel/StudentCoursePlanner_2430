package univ;

// imports
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- BCG (extends GeneralDegree)
 * --> Represents a concrete BCG (BComp General) degree at the University of Guelph 
 *      (is a subclass of GeneralDegree, has specific requirements to graduate with this degree)
 */
public class BCG extends GeneralDegree implements Serializable
{
    // ATTRIBUTES
    private static final String MAJOR = "BCG";
    private static final String SCIENCES[] = {"BIOL", "MATH", "ECON", "BIOM", "ECOL", "FOOD", "PHYS", "CHEM", "ZOO", "STAT"};
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates an BCG with default values
     */
    public BCG()
    {
        super();
        super.setDegreeTitle("Bachelor of Computing");
    }
    /**
     * Full Constructor - instantiates an BCG with full attributes
     * @param title the title of the BCG
     * @param reqCourses the list of required Courses for the BC`G
     * @param catalog the CourseCatalog containing a list of all courses at the University of Guelph
     */
    public BCG(String title, ArrayList<Course> reqCourses, CourseCatalog catalog)
    {
        super(title, reqCourses, catalog);
        super.setDegreeTitle("Bachelor of Computing");
    }
    
    // BEHAVIOURS
    /**
     * Accessor method - Retrieves the Major of the Concrete Degree
     * @return the Major of the Concrete Degree
     */
    @Override
    public String getMajor() {
        return MAJOR;
    }
    
    /**
     * Loads the list of required courses from a binary file then sets it here according to the matching major
     * @param filename the name of the binary file to be read from
     */
    public void readRequiredCourses(String filename) {
        super.readRequiredCourses(filename, MAJOR);
    }
    
    /**
     * Search method - finds a science Course in the list based on subject
     * @param subject the desired science subject being searched for
     * @return true if it is found, false if not found
     */
    public boolean isScienceCourse(String subject)
    {
        for(String s : SCIENCES) {
            if(s.equals(subject)) {
                return true;
            }
        }
        return false; // course code not found
    }
    
    /**
     * Accessor method - Retrieves the number of credits remaining until the user can graduate from their Degree and Major at UoG
     * @param allTheCoursesPlannedAndTaken the PlanOfStudy that the user is following
     * @return the number of credits remaining until the user can graduate from their Degree and Major at UoG
     */
    @Override
    public double numberOfCreditsRemaining(ArrayList<Course> allTheCoursesPlannedAndTaken)
    {
        // assume all Courses in the list are part of the calculation (they earned the credits for them)
        // accounts for duplicate courses
        // check total credits (w/ restriction to <= 6.0 credits 1000 lv and <= 11.0 credits in same subject)
        HashMap<String,Double> uniqueCourses = new HashMap<>();
        HashMap<String,Double> subjectCount = new HashMap<>();
        String codeSplit[];
        double numCreditsEarned = 0.0;
        double introCourseCredits = 0.0;
        
        // -- compensate for earning more than 11.0 credits in one subject or more than 6.0 credits 1000 lv
        for(Course c : allTheCoursesPlannedAndTaken) {
            // add it to the list of passed courses, if it's not already there
            uniqueCourses.putIfAbsent(c.getCourseCode(), c.getCourseCredit());
            
            codeSplit = c.getCourseCode().split("\\*");
            // add it to the current count for that subject, or default 0
            subjectCount.put(codeSplit[0], subjectCount.getOrDefault(codeSplit[0], 0.0) + c.getCourseCredit());
            // check if it's a 1000 level course or lower
            if(Integer.parseInt(codeSplit[1]) < 2000) {
                introCourseCredits += c.getCourseCredit();
            }
        }
        // count total number of credits earned
        for(String code : uniqueCourses.keySet()) {
            numCreditsEarned += uniqueCourses.get(code); //accumulate
        }
        for(String subject : subjectCount.keySet()) {
            if(subjectCount.get(subject) > 11.0) {
                numCreditsEarned -= (subjectCount.get(subject) - 11.0); //compensate
            }
        }
        if(introCourseCredits > 6.0) {
            numCreditsEarned -= (introCourseCredits - 6.0);   //compensate
        }
        
        if(numCreditsEarned < 0) {
            numCreditsEarned = 0;
        }
        // calculate
        double numCreditsRemaining = getCreditsRequired() - numCreditsEarned;
        if(numCreditsRemaining < 0) {
            numCreditsRemaining = 0;
        }
        return numCreditsRemaining;
    }
    
    /**
     * Boolean method - Checks if the user meets requirements to graduate and receive a BCG at UoG
     * @param allTheCoursesPlannedAndTaken the PlanOfStudy that the user is following
     * @return true if the user meets requirements to graduate
     */
    @Override
    public boolean meetsRequirements(ArrayList<Course> allTheCoursesPlannedAndTaken)
    {
        // assume all Courses in the list are part of the calculation (they earned the credits for them)
        boolean toReturn = true;
        String output = "";
        // check number of credits remaining
        double creditsRemaining = numberOfCreditsRemaining(allTheCoursesPlannedAndTaken);
        if(creditsRemaining > 0) {
            toReturn = false;
            output += "\nYou still need to earn " + creditsRemaining + " more credits\n";
        }
        
        boolean inList = false;
        // check required courses
        for(Course req : getRequiredCourses()) {
            inList = false;
            for(Course c : allTheCoursesPlannedAndTaken) {
                if(c.getCourseCode().equals(req.getCourseCode())) {
                    inList = true;
                    break; // found a match, course was completed w/ passing grade -> so skip
                }
            }
            if(!inList) {
                toReturn = false;
                output += "\nYou still need to pass " + req.getCourseCode() + ": " + req.getCourseTitle();
            }
        }
        output += "\n";
        
        double creditsCISSTAT2000 = 0.0;
        String code = "";
        // check for +0.5 credits CIS/STAT 2000+ lv (not required course)
        for(Course c : allTheCoursesPlannedAndTaken) {
            code = c.getCourseCode();
            if(findRequiredCourse(code) == null && (code.substring(0,3).equals("CIS") || 
                    code.substring(0,4).equals("STAT")) && code.charAt(code.length() - 4) >= '2') {
                // if it's not one of the required courses, and it's 2000 lv CIS or STAT, then count it
                creditsCISSTAT2000 += c.getCourseCredit();
            }
        }
        if(creditsCISSTAT2000 < 0.5) {
            toReturn = false;
            output += "\nYou still need " + (0.5 - creditsCISSTAT2000) + " credits from a CIS or STAT course in the 2000+ level that is not a required course";
        }
        
        String codeSplit[];
        double creditsCIS3000 = 0.0;
        // check for +1.0 credits CIS 3000+ (not required)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // check if it's a 3000 level CIS course, and not a required course
            if(codeSplit[0].equals("CIS") && codeSplit[1].charAt(0) >= '3' && findRequiredCourse(code) == null) {
                creditsCIS3000 += c.getCourseCredit();
            }
        }
        if(creditsCIS3000 < 1.0) {
            toReturn = false;
            output += "\nYou still need " + (1.0 - creditsCIS3000) +  " credits from CIS courses in the 3000+ level that is not a required course";
        }
        
        double credits3000 = 0.0;
        // check for min 4.00 credits 3000+ lv
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // check if it's a 3000 level course
            if(codeSplit[1].charAt(0) >= '3') {
                credits3000 += c.getCourseCredit();
            }
        }
        if(credits3000 < 4.0) {
            toReturn = false;
            output += "\nYou still need " + (4.0 - credits3000) + " credits from courses in the 3000+ level";
        }
        
        double creditsSCIENCE = 0.0;
        // check for min 2.00 science credits (use array)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            if(isScienceCourse(codeSplit[0])) {
                creditsSCIENCE += c.getCourseCredit();
            }
        }
        if(creditsSCIENCE < 2.0) {
            toReturn = false;
            output += "\nYou still need " + (2.0 - creditsSCIENCE) + " science credits";
        }
        
        double creditsART = 0.0;
        // check for min 2.00 college of arts / college of social and applied human sciences (not required, not CIS (elective), not science)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            if(findRequiredCourse(c.getCourseCode()) == null && !isScienceCourse(codeSplit[0]) && !codeSplit[0].equals("CIS")) {
                creditsART += c.getCourseCredit();
            }
        }
        if(creditsART < 2.0) {
            toReturn = false;
            output += "\nYou still need " + (2.0 - creditsART) + " science credits";
        }
        
        return toReturn;
    }
    
    @Override
    public String getMeetsRequirementsOutput(ArrayList<Course> allTheCoursesPlannedAndTaken)
    {
        // assume all Courses in the list are part of the calculation (they earned the credits for them)
        boolean toReturn = true;
        String output = "";
        // check number of credits remaining
        double creditsRemaining = numberOfCreditsRemaining(allTheCoursesPlannedAndTaken);
        if(creditsRemaining > 0) {
            toReturn = false;
            output += "\nYou still need to earn " + creditsRemaining + " more credits\n";
        }
        
        boolean inList = false;
        // check required courses
        for(Course req : getRequiredCourses()) {
            inList = false;
            for(Course c : allTheCoursesPlannedAndTaken) {
                if(c.getCourseCode().equals(req.getCourseCode())) {
                    inList = true;
                    break; // found a match, course was completed w/ passing grade -> so skip
                }
            }
            if(!inList) {
                toReturn = false;
                output += "\nYou still need to pass " + req.getCourseCode() + ": " + req.getCourseTitle();
            }
        }
        output += "\n";
        
        double creditsCISSTAT2000 = 0.0;
        String code = "";
        // check for +0.5 credits CIS/STAT 2000+ lv (not required course)
        for(Course c : allTheCoursesPlannedAndTaken) {
            code = c.getCourseCode();
            if(findRequiredCourse(code) == null && (code.substring(0,3).equals("CIS") || 
                    code.substring(0,4).equals("STAT")) && code.charAt(code.length() - 4) >= '2') {
                // if it's not one of the required courses, and it's 2000 lv CIS or STAT, then count it
                creditsCISSTAT2000 += c.getCourseCredit();
            }
        }
        if(creditsCISSTAT2000 < 0.5) {
            toReturn = false;
            output += "\nYou still need " + (0.5 - creditsCISSTAT2000) + " credits from a CIS or STAT course in the 2000+ level that is not a required course";
        }
        
        String codeSplit[];
        double creditsCIS3000 = 0.0;
        // check for +1.0 credits CIS 3000+ (not required)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // check if it's a 3000 level CIS course, and not a required course
            if(codeSplit[0].equals("CIS") && codeSplit[1].charAt(0) >= '3' && findRequiredCourse(code) == null) {
                creditsCIS3000 += c.getCourseCredit();
            }
        }
        if(creditsCIS3000 < 1.0) {
            toReturn = false;
            output += "\nYou still need " + (1.0 - creditsCIS3000) +  " credits from CIS courses in the 3000+ level that is not a required course";
        }
        
        double credits3000 = 0.0;
        // check for min 4.00 credits 3000+ lv
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // check if it's a 3000 level course
            if(codeSplit[1].charAt(0) >= '3') {
                credits3000 += c.getCourseCredit();
            }
        }
        if(credits3000 < 4.0) {
            toReturn = false;
            output += "\nYou still need " + (4.0 - credits3000) + " credits from courses in the 3000+ level";
        }
        
        double creditsSCIENCE = 0.0;
        // check for min 2.00 science credits (use array)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            if(isScienceCourse(codeSplit[0])) {
                creditsSCIENCE += c.getCourseCredit();
            }
        }
        if(creditsSCIENCE < 2.0) {
            toReturn = false;
            output += "\nYou still need " + (2.0 - creditsSCIENCE) + " science credits";
        }
        
        double creditsART = 0.0;
        // check for min 2.00 college of arts / college of social and applied human sciences (not required, not CIS (elective), not science)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            if(findRequiredCourse(c.getCourseCode()) == null && !isScienceCourse(codeSplit[0]) && !codeSplit[0].equals("CIS")) {
                creditsART += c.getCourseCredit();
            }
        }
        if(creditsART < 2.0) {
            toReturn = false;
            output += "\nYou still need " + (2.0 - creditsART) + " science credits";
        }
        return output;
    } 
    
    // =========================================================================
    
    /**
     * Retrieves all info about an BCG
     * @return a string representation of the BCG's information
     */
    @Override
    public String toString()
    {
        return "Major:\t\t\t" + MAJOR + "\n" + super.toString();
    }
    
    /**
     * Comparator method -- Compares two BCGs to each other
     * @param p the other Object being compared
     * @return true if they have the same attributes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof BCG)) {
            return false;
        } else {
            return super.equals((GeneralDegree)p); //?? not sure about casting
        }
    }
    
} // end (concrete) BCG subclass
