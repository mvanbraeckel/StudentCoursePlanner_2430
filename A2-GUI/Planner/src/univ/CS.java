package univ;

// imports
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- CS (extends HonoursDegree)
 * --> Represents a concrete degree in CS (BComp Honours) at the University of Guelph 
 *      (is a subclass of HonoursDegree, has specific requirements to graduate with this degree)
 */
public class CS extends HonoursDegree implements Serializable
{
    // ATTRIBUTES
    private static final String MAJOR = "CS";
    
    // CONSTRUCTORS
    /**
     * Default Constructor - instantiates an CS with default values
     */
    public CS()
    {
        super();
        super.setDegreeTitle("Bachelor of Computing");
    }
    /**
     * Full Constructor - instantiates an CS with full attributes
     * @param title the title of the CS
     * @param reqCourses the list of required Courses for the CS
     * @param catalog the CourseCatalog containing a list of all courses at the University of Guelph
     */
    public CS(String title, ArrayList<Course> reqCourses, CourseCatalog catalog)
    {
        super(title, reqCourses, catalog);
        super.setDegreeTitle("Bachelor of Computing");
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
     * Accessor method - Retrieves the number of credits remaining until the user can graduate from their Degree and Major at UoG
     * @param allTheCoursesPlannedAndTaken the PlanOfStudy that the user is following
     * @return the number of credits remaining until the user can graduate from their Degree and Major at UoG
     */
    @Override
    public double numberOfCreditsRemaining(ArrayList<Course> allTheCoursesPlannedAndTaken)
    {
        // assume all Courses in the list are part of the calculation (they earned the credits for them)
        // accounts for duplicate courses
        // check total credits (w/ restriction to <= 6.0 credits 1000 lv)
        HashMap<String,Double> uniqueCourses = new HashMap<>();
        String codeSplit[];
        double numCreditsEarned = 0.0;
        double introCourseCredits = 0.0;
        
        // -- compensate for earning more than 6.0 credits 1000 lv
        for(Course c : allTheCoursesPlannedAndTaken) {
            // add it to the list of passed courses, if it's not already there
            uniqueCourses.putIfAbsent(c.getCourseCode(), c.getCourseCredit());
            
            codeSplit = c.getCourseCode().split("\\*");
            // check if it's a 1000 level course or lower
            if(Integer.parseInt(codeSplit[1]) < 2000) {
                introCourseCredits += c.getCourseCredit();
            }
        }
        // count total number of credits earned
        for(String code : uniqueCourses.keySet()) {
            numCreditsEarned += uniqueCourses.get(code); //accumulate
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
     * Boolean method - Checks if the user meets requirements to graduate and receive a CS degree at UoG
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
        
        double creditsCIS = 0.0;
        double creditsCIS3000 = 0.0;
        double creditsCIS4000 = 0.0;
        String codeSplit[];
        // check min 11.25 CIS credits
        // check min 6.00 CIS credits 3000+ lv and min 2.00 CIS credits 4000+ lv
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // check if it's a CIS course
            if(codeSplit[0].equals("CIS")) {
                creditsCIS += c.getCourseCredit();
                // check if it's a 3000 level CIS course
                if(codeSplit[1].charAt(0) >= '3') {
                    creditsCIS3000 += c.getCourseCredit();
                    // check if it's a 4000 level CIS course
                    if(codeSplit[1].charAt(0) >= '4') {
                        creditsCIS4000 += c.getCourseCredit();
                    }
                }
            }
        }
        if(creditsCIS < 11.25) {
            toReturn = false;
            output += "\nYou still need " + (11.25 - creditsCIS) + " credits from CIS courses";
        }
        if(creditsCIS3000 < 6.0) {
            toReturn = false;
            output += "\nYou still need " + (6.0 - creditsCIS3000) + " credits from CIS courses in the 3000+ level";
        }
        if(creditsCIS4000 < 2.0) {
            toReturn = false;
            output += "\nYou still need " + (2.0 - creditsCIS4000) + " credits from CIS courses in the 3000+ level";
        }
        
        HashMap<String,Double> subjectCount = new HashMap<>();
        HashMap<String,Double> subjectCount3000 = new HashMap<>();
        // check min 4.00 credits area of application & that there's min 1.00 credits AoA 3000+ lv (not CIS)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // if it's a passing grade, add it to the current count for that subject, or default 0
            if(!codeSplit[0].equals("CIS")) {
                subjectCount.put(codeSplit[0], subjectCount.getOrDefault(codeSplit[0], 0.0) + c.getCourseCredit());
                // check if it's a 3000+ level course, do the same for that count
                if(codeSplit[1].charAt(0) >= '3') {
                    subjectCount3000.put(codeSplit[0], subjectCount3000.getOrDefault(codeSplit[0], 0.0) + c.getCourseCredit());
                }
            }
        }
        inList = false;
        boolean inList3000 = false;
        double aoaMax = 0;
        double aoa3000Max = 0;
        for(String subject : subjectCount.keySet()) {
            // set highscores for AoA and AoA 3000 lv credit
            if(subjectCount.get(subject) > aoaMax) {
                aoaMax = subjectCount.get(subject);
            }
            if(subjectCount3000.get(subject) != null && subjectCount3000.get(subject) > aoa3000Max) {
                aoa3000Max = subjectCount3000.get(subject);
            }
            // check if enough credits were earned in the AoA, then if enough were earned at the 3000 level for it
            if(subjectCount.get(subject) >= 4.0) {
                inList = true;
                if(subjectCount3000.get(subject) != null && subjectCount3000.get(subject) >= 1.0) {
                    inList3000 = true;
                }
            }
        }
        if(!inList) {
            toReturn = false;
            output += "\nYou still need " + (4.0 - aoaMax) + " more credits in an area of application";
        }
        if(!inList3000) {
            toReturn = false;
            output += "\nYou still need " + (1.0 - aoaMax) + " credits in an area of application at the 3000+ level";
        }
        
        /*double sum = 0.0;
        int courseCount = 0;
        double sumCIS = 0.0;
        int courseCountCIS = 0;
        // check 70% avg for CIS courses and 60% avg for all courses
        for(Attempt fromPlan : thePlan.getTranscript()) {
            codeSplit = fromPlan.getCourseAttempted().getCourseCode().split("\\*");
            // if it's completed, add its grade
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                // accumulate sum
                sum += Double.parseDouble(fromPlan.getAttemptGrade());
                courseCount++;
            
                // accumulate sum for CIS courses
                if(codeSplit[0].equals("CIS")) {
                    sumCIS += Double.parseDouble(fromPlan.getAttemptGrade());
                    courseCountCIS++;
                }
            }
        }
        double avg = sum / (double) courseCount;
        double avgCIS = sumCIS / (double) courseCountCIS;
        if(avg < 60.0) {
            toReturn = false;
            System.out.println("Sorry, your cumulative average is " + avg + "%, but it needs to be at least 60%");
        }
        if(avgCIS < 70.0) {
            toReturn = false;
            System.out.println("Sorry, your cumulative average for CIS courses is " + avgCIS + "%, but it needs to be at least 70%");
        }*/
        
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
        
        double creditsCIS = 0.0;
        double creditsCIS3000 = 0.0;
        double creditsCIS4000 = 0.0;
        String codeSplit[];
        // check min 11.25 CIS credits
        // check min 6.00 CIS credits 3000+ lv and min 2.00 CIS credits 4000+ lv
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // check if it's a CIS course
            if(codeSplit[0].equals("CIS")) {
                creditsCIS += c.getCourseCredit();
                // check if it's a 3000 level CIS course
                if(codeSplit[1].charAt(0) >= '3') {
                    creditsCIS3000 += c.getCourseCredit();
                    // check if it's a 4000 level CIS course
                    if(codeSplit[1].charAt(0) >= '4') {
                        creditsCIS4000 += c.getCourseCredit();
                    }
                }
            }
        }
        if(creditsCIS < 11.25) {
            toReturn = false;
            output += "\nYou still need " + (11.25 - creditsCIS) + " credits from CIS courses";
        }
        if(creditsCIS3000 < 6.0) {
            toReturn = false;
            output += "\nYou still need " + (6.0 - creditsCIS3000) + " credits from CIS courses in the 3000+ level";
        }
        if(creditsCIS4000 < 2.0) {
            toReturn = false;
            output += "\nYou still need " + (2.0 - creditsCIS4000) + " credits from CIS courses in the 3000+ level";
        }
        
        HashMap<String,Double> subjectCount = new HashMap<>();
        HashMap<String,Double> subjectCount3000 = new HashMap<>();
        // check min 4.00 credits area of application & that there's min 1.00 credits AoA 3000+ lv (not CIS)
        for(Course c : allTheCoursesPlannedAndTaken) {
            codeSplit = c.getCourseCode().split("\\*");
            // if it's a passing grade, add it to the current count for that subject, or default 0
            if(!codeSplit[0].equals("CIS")) {
                subjectCount.put(codeSplit[0], subjectCount.getOrDefault(codeSplit[0], 0.0) + c.getCourseCredit());
                // check if it's a 3000+ level course, do the same for that count
                if(codeSplit[1].charAt(0) >= '3') {
                    subjectCount3000.put(codeSplit[0], subjectCount3000.getOrDefault(codeSplit[0], 0.0) + c.getCourseCredit());
                }
            }
        }
        inList = false;
        boolean inList3000 = false;
        double aoaMax = 0;
        double aoa3000Max = 0;
        for(String subject : subjectCount.keySet()) {
            // set highscores for AoA and AoA 3000 lv credit
            if(subjectCount.get(subject) > aoaMax) {
                aoaMax = subjectCount.get(subject);
            }
            if(subjectCount3000.get(subject) != null && subjectCount3000.get(subject) > aoa3000Max) {
                aoa3000Max = subjectCount3000.get(subject);
            }
            // check if enough credits were earned in the AoA, then if enough were earned at the 3000 level for it
            if(subjectCount.get(subject) >= 4.0) {
                inList = true;
                if(subjectCount3000.get(subject) != null && subjectCount3000.get(subject) >= 1.0) {
                    inList3000 = true;
                }
            }
        }
        if(!inList) {
            toReturn = false;
            output += "\nYou still need " + (4.0 - aoaMax) + " more credits in an area of application";
        }
        if(!inList3000) {
            toReturn = false;
            output += "\nYou still need " + (1.0 - aoaMax) + " credits in an area of application at the 3000+ level";
        }
        
        /*double sum = 0.0;
        int courseCount = 0;
        double sumCIS = 0.0;
        int courseCountCIS = 0;
        // check 70% avg for CIS courses and 60% avg for all courses
        for(Attempt fromPlan : thePlan.getTranscript()) {
            codeSplit = fromPlan.getCourseAttempted().getCourseCode().split("\\*");
            // if it's completed, add its grade
            if(fromPlan.getAttemptStatus().equals("Complete")) {
                // accumulate sum
                sum += Double.parseDouble(fromPlan.getAttemptGrade());
                courseCount++;
            
                // accumulate sum for CIS courses
                if(codeSplit[0].equals("CIS")) {
                    sumCIS += Double.parseDouble(fromPlan.getAttemptGrade());
                    courseCountCIS++;
                }
            }
        }
        double avg = sum / (double) courseCount;
        double avgCIS = sumCIS / (double) courseCountCIS;
        if(avg < 60.0) {
            toReturn = false;
            System.out.println("Sorry, your cumulative average is " + avg + "%, but it needs to be at least 60%");
        }
        if(avgCIS < 70.0) {
            toReturn = false;
            System.out.println("Sorry, your cumulative average for CIS courses is " + avgCIS + "%, but it needs to be at least 70%");
        }*/
        
        return output;
    }
    
    // =========================================================================
    
    /**
     * Retrieves all info about an CS
     * @return a string representation of the CS's information
     */
    @Override
    public String toString()
    {
        return "Major:\t\t\t" + MAJOR + "\n" + super.toString();
    }
    
    /**
     * Comparator method -- Compares two CSs to each other
     * @param p the other Object being compared
     * @return true if they have the same attributes
     */
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof CS)) {
            return false;
        } else {
            return super.equals((HonoursDegree)p); //?? not sure about casting
        }
    }
    
} // end (concrete) CS subclass
