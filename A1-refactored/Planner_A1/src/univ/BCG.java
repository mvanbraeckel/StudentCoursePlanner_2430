package univ;

// imports
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import planner_a1.*;

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
    }
    
    // BEHAVIOURS
    /**
     * Accessor method - Retrieves the Major of the Concrete Degree
     * @return the Major of the Concrete Degree
     */
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
     * @param thePlan the PlanOfStudy that the user is following
     * @return the number of credits remaining until the user can graduate from their Degree and Major at UoG
     */
    @Override
    public double numberOfCreditsRemaining(PlanOfStudy thePlan)
    {
        // check total credits (w/ restriction to <= 6.0 credits 1000 lv and <= 11.0 credits in same subject)
        double numCreditsEarned = thePlan.getCreditsEarned();
        HashMap<String,Double> subjectCount = new HashMap<>();
        String codeSplit[];
        double introCourseCredits = 0.0;
        
        // -- compensate for earning more than 11.0 credits in one subject or more than 6.0 credits 1000 lv
        for(Attempt fromPlan : thePlan.getTranscript()) {
            codeSplit = fromPlan.getCourseAttempted().getCourseCode().split("\\*");
            // if it's a passing grade, add it to the current count for that subject, or default 0
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                subjectCount.put(codeSplit[0], subjectCount.getOrDefault(codeSplit[0], 0.0) + fromPlan.getCourseAttempted().getCourseCredit());
                // check if it's a 1000 level course or lower
                if(Integer.parseInt(codeSplit[1]) < 2000) {
                    introCourseCredits += fromPlan.getCourseAttempted().getCourseCredit();
                }
            }
        }
        for(String subject : subjectCount.keySet()) {
            if(subjectCount.get(subject) > 11.0) {
                numCreditsEarned = numCreditsEarned - (subjectCount.get(subject) - 11.0); //compensate
            }
        }
        if(introCourseCredits > 6.0) {
            numCreditsEarned = numCreditsEarned - (introCourseCredits - 6.0);   //compensate
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
     * @param thePlan the PlanOfStudy that the user is following
     * @return true if the user meets requirements to graduate
     */
    @Override
    public boolean meetsRequirements(PlanOfStudy thePlan)
    {
        boolean toReturn = true;
        // check number of credits remaining
        double creditsRemaining = numberOfCreditsRemaining(thePlan);
        if(creditsRemaining > 0) {
            toReturn = false;
            System.out.println("You still need to earn " + creditsRemaining + " more credits");
        }
        
        boolean pass = false;
        // check required courses
        for(Course c : getRequiredCourses()) {
            pass = false;
            for(Attempt fromPlan : thePlan.getTranscript()) {
                if(fromPlan.getCourseAttempted().getCourseCode().equals(c.getCourseCode())) {
                    if(fromPlan.getAttemptStatus().equals("Complete")) {
                        pass = true;
                        break; // found a match, course was completed w/ passing grade -> so skip
                    }
                }
            }
            if(!pass) {
                toReturn = false;
                System.out.println("You still need to pass " + c.getCourseCode() + ": " + c.getCourseTitle());
            }
        }
        
        double creditsCISSTAT2000 = 0.0;
        String codeSplit[];
        String fromPlanCode = "";
        // check for +0.5 credits CIS/STAT 2000+ lv (not required course)
        for(Attempt fromPlan : thePlan.getTranscript()) {
            // if it's a passing grade, add it to the current count for that subject
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                fromPlanCode = fromPlan.getCourseAttempted().getCourseCode();
                if(findRequiredCourse(fromPlanCode) == null && (fromPlanCode.substring(0,3).equals("CIS") || 
                        fromPlanCode.substring(0,4).equals("STAT")) &&  fromPlanCode.charAt(fromPlanCode.length() - 4) >= '2') {
                    // if it's not one of the required courses, then count it
                    codeSplit = fromPlanCode.split("\\*");
                    if(codeSplit[0].equals("CIS") && (!codeSplit[1].equals("2430") && !codeSplit[1].equals("2500") &&
                            !codeSplit[1].equals("2520") && !codeSplit[1].equals("2750") && !codeSplit[1].equals("2910"))) {
                        creditsCISSTAT2000 += fromPlan.getCourseAttempted().getCourseCredit();
                    }
                }
            }
        }
        if(creditsCISSTAT2000 < 0.5) {
            toReturn = false;
            System.out.println("You still need " + (0.5 - creditsCISSTAT2000) + " credits from a CIS or STAT course in the 2000+ level");
        }
        
        double creditsCIS3000 = 0.0;
        // check for +1.0 credits CIS 3000+ (not required)
        for(Attempt fromPlan : thePlan.getTranscript()) {
            codeSplit = fromPlan.getCourseAttempted().getCourseCode().split("\\*");
            // if it's a passing grade, add it to the current count for that subject
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                // check if it's a 3000 level CIS course, and not a required course
                if(codeSplit[0].equals("CIS") && codeSplit[1].charAt(0) >= '3' && !codeSplit[1].equals("3530")) {
                    creditsCIS3000 += fromPlan.getCourseAttempted().getCourseCredit();
                }
            }
        }
        if(creditsCIS3000 < 1.0) {
            toReturn = false;
            System.out.println("You still need " + (1.0 - creditsCIS3000) + " credits from CIS courses in the 3000+ level");
        }
        
        double credits3000 = 0.0;
        // check for min 4.00 credits 3000+ lv
        for(Attempt fromPlan : thePlan.getTranscript()) {
            codeSplit = fromPlan.getCourseAttempted().getCourseCode().split("\\*");
            // if it's a passing grade, add it to the current count for that subject
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                // check if it's a 3000 level course
                if(codeSplit[1].charAt(0) >= '3') {
                    credits3000 += fromPlan.getCourseAttempted().getCourseCredit();
                }
            }
        }
        if(credits3000 < 4.0) {
            toReturn = false;
            System.out.println("You still need " + (4.0 - credits3000) + " credits from courses in the 3000+ level");
        }
        
        double creditsSCIENCE = 0.0;
        // check for min 2.00 science credits (use array)
        for(Attempt fromPlan : thePlan.getTranscript()) {
            // if it's a passing grade, add it to the current count for that subject
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                codeSplit = fromPlan.getCourseAttempted().getCourseCode().split("\\*");
                if(isScienceCourse(codeSplit[0])) {
                    creditsSCIENCE += fromPlan.getCourseAttempted().getCourseCredit();
                }
            }
        }
        if(creditsSCIENCE < 2.0) {
            toReturn = false;
            System.out.println("You still need " + (2.0 - creditsSCIENCE) + " science credits");
        }
        
        double creditsART = 0.0;
        // check for min 2.00 college of arts / college of social and applied human sciences (not required, not CIS (elective), not science)
        for(Attempt fromPlan : thePlan.getTranscript()) {
            // if it's a passing grade, add it to the current count for that subject
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                codeSplit = fromPlan.getCourseAttempted().getCourseCode().split("\\*");
                if(findRequiredCourse(fromPlan.getCourseAttempted().getCourseCode()) == null &&
                        !isScienceCourse(codeSplit[0]) && !codeSplit[0].equals("CIS")) {
                    creditsART += fromPlan.getCourseAttempted().getCourseCredit();
                }
            }
        }
        if(creditsART < 2.0) {
            toReturn = false;
            System.out.println("You still need " + (2.0 - creditsART) + " science credits");
        }
        
        return toReturn;
    } 
    
    // =========================================================================
    
    /**
     * Retrieves all info about an BCG
     * @return a string representation of the BCG's information
     */
    @Override
    public String toString()
    {
        return "Major: " + MAJOR + "\n" + super.toString();
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
