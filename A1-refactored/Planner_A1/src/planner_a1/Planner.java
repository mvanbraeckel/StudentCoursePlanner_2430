package planner_a1;

import univ.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Mitchell Van Braeckel (mvanbrae@uoguelph.ca) 1002297
 * @version 20/10/2018
 * CIS2430 Assignment 1 -- Planner
 * --> has a main method and can:

  from a CSV file, load a list of classes which I have taken or plan to take, along with the status of the course and my grade if available
  select my degree (required choices: BCH (BComp Honours) , BCG (BComp General), optional: one other degree)
  select my major from my selected degree
  add courses to my plan of study and mark them as in-progress (IP), complete (C), or planned (P)
  mark courses in my plan of study as required (R), elective (E), or minor (area of application) (MA)
  remove a course from my plan of study
  change my grade in a course that is in my plan of study
  save my plan of study to a file
  load my plan of study from a previously saved file
  view a list of *required* courses for my degree and major that are *not* represented in my plan of study
  view a list of the prerequisite courses for any required course for my degree and major
  view the number of credits I have completed in my plan of study
  view the number of credits I have remaining to complete my plan of study
  determine if I have met the completion requirements of my chosen degree

 --> Student Transcript file: represents the courses that a student has taken the first time the planner program is run.
    --> line: CourseCode,Status,Grade,Semester
    NOTE: The grade element of the line might be empty
    --> eg: CIS*1500,Complete,84,F17
            CIS*2500,Complete,80,W18
            CIS*1910,Complete,81,W18
            CIS*2430,InProgress,,F18

 --> Saving State file: The application must save the current state of the PlanOfStudy and the Course and Degree information.
                    You may choose to use multiple save files or a single file to accomplish saving.
                    The design of the save file(s) is part of the assignment and is not dictated.
                    You should be able to justify the design you choose.
 */
public class Planner
{
    /**
     * @param args -the command line arguments
     */
    public static void main(String[] args)
    {
        // declare variables
        Scanner sc = new Scanner(System.in);
        String menuInput = "";
        
        Student theUser = new Student();
        
        // welcome prompt
        System.out.println("Welcome to Planner!");
        
        // intial menu
        while(!menuInput.equals("1") && !menuInput.equals("2") && !menuInput.equals("3") && !menuInput.toLowerCase().equals("quit")) {
            System.out.println("\nPlease select the number of the option you would like to complete:\n" +
                    "\t1. Load a saved Plan of Study\n" +
                    "\t2. Create a new Plan of Study\n" +
                    "\t3. Quit\nSelect option: ");
            menuInput = sc.nextLine().trim();
            
            // check input
            if(menuInput.equals("1")) {         // =================== 1 ===================
                loadPlanOfStudy(theUser);
                
            } else if(menuInput.equals("2")) {  // =================== 2 ===================
                createNewPlanOfStudy(theUser);
                
            } else if(menuInput.equals("3") || menuInput.toLowerCase().equals("quit")) {
                System.out.println("Good bye!");
                System.exit(0);
            } else {
                // display error msg
                System.out.println("Invalid option: please select one of the options by number [1,15]");
            }
        } // end while loop
        
        
        menuInput = "";
        // continue until user selects to exit
        while(!menuInput.equals("16") && !menuInput.toLowerCase().equals("quit")) {
            System.out.println("\nPlease select the number of the option you would like to complete:\n" +
                    "\t1. Load a Student Transcript from a CSV file that contains a list of courses with a status " +
                        "('Complete', 'InProgress', or 'Planned') and a grade if it is complete\n" +
                    "\t2. Select your degree and major\n" +
                    "\t3. Add a course to your Plan of Study, marking it as 'Complete', 'InProgress', or 'Planned'\n" +
                    "\t4. Mark a course in your Plan of Study as 'Complete', 'InProgress', or 'Planned'\n" +
                    "\t5. Mark a course in your Plan of Study as 'Required', 'Elective', or 'AreaOfApplication'\n" +
                    "\t6. Remove a course from your Plan of Study\n" +
                    "\t7. Update a grade of a course in your Plan of Study\n" +
                    "\t8. Save your Plan of Study\n" +
                    "\t9. Load a saved Plan Of Study\n" +
                    "\t10. View a list of required courses for your degree that aren't in your Plan of Study\n" +
                    "\t11. View a list of prerequisites for a required course\n" +
                    "\t12. View number of credits completed\n" +
                    "\t13. View number of credits remaining to complete\n" +
                    "\t14. Determine if you meet graduation requirements\n" +
                    "\t15. Print your Plan of Study\n" +
                    "\t16. Quit\nSelect option: ");
            menuInput = sc.nextLine().trim();
            
            // check input
            if(menuInput.equals("1")) {         // =================== 1 ===================
                loadTranscript(theUser);
                
            } else if(menuInput.equals("2")) {  // =================== 2 ===================
                selectDegreeAndMajor(theUser.getPlan());
                
            } else if(menuInput.equals("3")) {  // =================== 3 ===================
                addCourse(theUser.getPlan());
                
            } else if(menuInput.equals("4")) {  // =================== 4 ===================
                if(theUser.getPlan().getTranscript().isEmpty()) {
                    System.out.println("Sorry, there are no courses in your Plan of Study yet.");
                } else {
                    markCourseStatus(theUser.getPlan());
                }
                
            } else if(menuInput.equals("5")) {  // =================== 5 ===================
                if(theUser.getPlan().getTranscript().isEmpty()) {
                    System.out.println("Sorry, there are no courses in your Plan of Study yet.");
                } else {
                    markCourseType(theUser.getPlan());
                }
                
            } else if(menuInput.equals("6")) {  // =================== 6 ===================
                if(theUser.getPlan().getTranscript().isEmpty()) {
                    System.out.println("Sorry, there are no courses in your Plan of Study yet.");
                } else {
                    removeCourseFromPlanCourseList(theUser.getPlan());
                }
                
            } else if(menuInput.equals("7")) {  // =================== 7 ===================
                if(theUser.getPlan().getTranscript().isEmpty()) {
                    System.out.println("Sorry, there are no courses in your Plan of Study yet.");
                } else {
                    updateGrade(theUser.getPlan());
                }
                
            } else if(menuInput.equals("8")) {  // =================== 8 ===================
                theUser.saveState("savedPlan.bin");
                
            } else if(menuInput.equals("9")) {  // =================== 9 ===================
                loadPlanOfStudy(theUser);
                
            } else if(menuInput.equals("10")) { // ================== 10 ===================
                if(theUser.getPlan().getTranscript().isEmpty()) {
                    printBasicRequiredCourseList(theUser.getPlan());
                } else {
                    viewMissingRequiredCoursesInPlan(theUser.getPlan());
                }
                
            } else if(menuInput.equals("11")) { // ================== 11 ===================
                viewPrerequisitesOfRequiredCourse(theUser.getPlan());
                
            } else if(menuInput.equals("12")) { // ================== 12 ===================
                System.out.println("You have currently completed " + theUser.getPlan().getCreditsEarned() + " credits.");
                
            } else if(menuInput.equals("13")) { // ================== 13 ===================
                System.out.println("You need " + Math.max(theUser.getPlan().getDegreeProgram().getCreditsRequired() - theUser.getPlan().getCreditsEarned(), 0) +
                        " more credits to complete your plan of study.");
                
            } else if(menuInput.equals("14")) { // ================== 14 ===================
                boolean ok = theUser.getPlan().getDegreeProgram().meetsRequirements(theUser.getPlan());
                if(!ok) {
                    System.out.println("\n===================================================================\n" +
                            "Unfortunately, you do not meet requirements to graduate.");
                } else {
                    System.out.println("\n===================================================================\n" +
                            "Congratulations, you meet requirements to graduate!");
                }
                
            } else if(menuInput.equals("15")) { // ================== 15 ===================
                System.out.println("Your plan of study:\n===================================================================\n" +
                        theUser.getPlan().toString() + "\n===================================================================");
                
            } else if(menuInput.equals("16") || menuInput.toLowerCase().equals("quit")) {
                System.out.println("Good bye!");
                System.exit(0);
                
            } else {
                // display error msg
                System.out.println("Invalid option: please select one of the options by number [1,15]");
            }
            
        } // end while loop
    } // end main
    
    /**
     * Displays the prerequisite courses of a selected required course for the degree and major
     * @param pos the Plan of Study being used
     */
    public static void viewPrerequisitesOfRequiredCourse(PlanOfStudy pos) {
        Scanner sc = new Scanner(System.in);
        
        String input = "";
        boolean validInput = false;
        int courseChoice = 0;
        
        // prompt user to choose a course
        while(!validInput) {
            printBasicRequiredCourseList(pos);
            input = trimCourseCode(sc.nextLine().trim());
            
            // check number was correct
            if(!isInteger(input)) {
                System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
            } else {
                // check range of int
                courseChoice = Integer.parseInt(input); // holds index+1
                if(courseChoice < 1 || courseChoice > pos.getDegreeProgram().getRequiredCourses().size()) {
                    System.out.println("Invalid input: please select one of the options by number [1," + 
                            pos.getDegreeProgram().getRequiredCourses().size() + "]");
                } else {
                    validInput = true;
                    // print basic form of prereq courses
                    System.out.println("All its prerequisite courses are printed below.");
                    int i = 0;
                    for(Course prereqCourse : pos.getDegreeProgram().getRequiredCourses().get(courseChoice-1).getPrerequisites()) {
                        System.out.println("\t" + i + ". " + prereqCourse.getCourseCode() + " - " + prereqCourse.getCourseTitle());
                        i++;
                    }
                }
            }
        } // end while loop
    }
    
    /**
     * Allows the user to access a course in the Plan of Study and change its grade
     * @param pos the Plan of Study being used
     */
    public static void updateGrade(PlanOfStudy pos) {
        Scanner sc = new Scanner(System.in);
        
        String input = "";
        boolean validInput = false;
        int courseChoice = 0;
        
        // prompt user to choose a course
        while(!validInput) {
            printBasicPlanCourseList(pos);
            input = trimCourseCode(sc.nextLine().trim());
            
            // check number was correct
            if(!isInteger(input)) {
                System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
            } else {
                // check range of int
                courseChoice = Integer.parseInt(input);
                if(courseChoice < 1 || courseChoice > pos.getTranscript().size()) {
                    System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
                } else {
                    validInput = true;
                    // now courseChoice holds index+1
                }
            }
        } // end while loop
        
        boolean isComplete = true;
        // check that the courses status is complete (only completed courses have grades)
        if(!pos.getTranscript().get(courseChoice-1).getAttemptStatus().equals("Complete")) {
            System.out.println("Sorry, you have not completed that course yet, so you cannot change its grade");
            isComplete = false;
        }
        
        if(isComplete) {
            // prompt user for grade
            validInput = false;
            while(!validInput) {
                System.out.println("Enter its grade: ");
                input = sc.nextLine().trim();
                // check input
                if(!isValidGradeUserInput(input)) {
                    System.out.println("Invalid input: '" + input + "' must be a decimal value [0,100]");
                } else {
                    validInput = true;
                    pos.getTranscript().get(courseChoice-1).setAttemptGrade(input);
                }
            }
        }
    }
    
    /**
     * Allows the user to access a course in the Plan of Study and remove it
     * @param pos the Plan of Study being used
     */
    public static void removeCourseFromPlanCourseList(PlanOfStudy pos) {
        Scanner sc = new Scanner(System.in);
        
        String input = "";
        boolean validInput = false;
        int courseChoice = 0;
        
        // prompt user to choose a course
        while(!validInput) {
            printBasicPlanCourseList(pos);
            input = trimCourseCode(sc.nextLine().trim());
            
            // check number was correct
            if(!isInteger(input)) {
                System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
            } else {
                // check range of int
                courseChoice = Integer.parseInt(input); // holds index+1
                if(courseChoice < 1 || courseChoice > pos.getTranscript().size()) {
                    System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
                } else {
                    validInput = true;
                    pos.getTranscript().remove(courseChoice-1);
                }
            }
        } // end while loop
    }
    
    /**
     * Allows the user to access a course in the Plan of Study and mark its type
     * @param pos the Plan of Study being used
     */
    public static void markCourseType(PlanOfStudy pos) {
        Scanner sc = new Scanner(System.in);
        
        String input = "";
        boolean validInput = false;
        int courseChoice = 0;
        
        // prompt user to choose a course
        while(!validInput) {
            printBasicPlanCourseList(pos);
            input = trimCourseCode(sc.nextLine().trim());
            
            // check number was correct
            if(!isInteger(input)) {
                System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
            } else {
                // check range of int
                courseChoice = Integer.parseInt(input);
                if(courseChoice < 1 || courseChoice > pos.getTranscript().size()) {
                    System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
                } else {
                    validInput = true;
                    // now courseChoice holds index+1
                }
            }
        } // end while loop
        
        // prompt user to mark its type
        validInput = false;
        while(!validInput) {
            System.out.println("Enter its type ('Required', 'Elective', or 'AreaOfApplication'): ");
            input = sc.nextLine().trim();
            // check input
            if(input.equals("Required") || input.equals("Elective") || input.equals("AreaOfApplication")) {
                validInput = true;
                pos.getTranscript().get(courseChoice-1).setAttemptType(input);  
            } else {
                // display error msg
                System.out.println("Invalid option: please 'Required', 'Elective', or 'AreaOfApplication' exactly");
            }
        } // end while loop
    }
    
    /**
     * Allows the user to access a course in the Plan of Study and mark its status
     * @param pos the Plan of Study being used
     */
    public static void markCourseStatus(PlanOfStudy pos) {
        Scanner sc = new Scanner(System.in);
        
        String input = "";
        boolean validInput = false;
        int courseChoice = 0;
        
        // prompt user to choose a course
        while(!validInput) {
            printBasicPlanCourseList(pos);
            input = trimCourseCode(sc.nextLine().trim());
            
            // check number was correct
            if(!isInteger(input)) {
                System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
            } else {
                // check range of int
                courseChoice = Integer.parseInt(input);
                if(courseChoice < 1 || courseChoice > pos.getTranscript().size()) {
                    System.out.println("Invalid input: please select one of the options by number [1," + pos.getTranscript().size() +"]");
                } else {
                    validInput = true;
                    // now courseChoice holds index+1
                }
            }
        } // end while loop
        
        // prompt user to mark its status
        validInput = false;
        while(!validInput) {
            System.out.println("Enter its status ('Complete', 'InProgress', or 'Planned'): ");
            input = sc.nextLine().trim();
            // check input
            if(input.equals("Complete") || input.equals("InProgress") || input.equals("Planned")) {
                validInput = true;
                pos.getTranscript().get(courseChoice-1).setAttemptStatus(input);  
            } else {
                // display error msg
                System.out.println("Invalid option: please 'Complete', 'InProgress', or 'Planned' exactly");
            }
        } // end while loop
    }
    
    /**
     * Displays all required courses for the degree and major that aren't in the Plan of Study course list
     * @param pos the Plan of Study being used
     */
    public static void viewMissingRequiredCoursesInPlan(PlanOfStudy pos)
    {
        ArrayList<String> missingCourseList = new ArrayList<>();
        boolean match = false;
        
        for(Course c : pos.getDegreeProgram().getRequiredCourses()) {
            match = false;
            for(Attempt fromPlan : pos.getTranscript()) {
                if(fromPlan.getCourseAttempted().getCourseCode().equals(c.getCourseCode())) {
                    match = true;
                    break; // found a match
                }
            }
            if(!match) {
                missingCourseList.add(c.getCourseCode() + " - " + c.getCourseTitle());
            }
        }
        System.out.println("You are missing the following required courses from your plan:");
        int i = 1;
        for(String s : missingCourseList) {
            System.out.println("\t" + i + ". " + s);
        }
    }
    
    /**
     * Adds a course to the Plan of Study using user input to create that course
     * @param pos the Plan of Study being used
     */
    public static void addCourse(PlanOfStudy pos)
    {
        Scanner sc = new Scanner(System.in);
        
        String input = "";
        boolean validInput = false;
        Attempt toAdd = new Attempt();
        
        // prompt user for Course information
        while(!validInput) {
            System.out.println("Enter the course code: ");
            input = trimCourseCode(sc.nextLine().trim());
            
            if(!isValidCourseCode(input)) {
                System.out.println("Invalid input: '" + input + "' is not a proper course code");
            } else if(pos.getDegreeProgram().getCatalog().findCourse(input) == null) {
                System.out.println("Invalid input: '" + input + "' is not a course at the University of Guelph");
            } else {
                validInput = true;
                // create a new Course using that
                toAdd = new Attempt(pos.getDegreeProgram().getCatalog().findCourse(input));
            }
        }
        
        // prompt user to mark its status
        validInput = false;
        while(!validInput) {
            System.out.println("Enter its status ('Complete', 'InProgress', or 'Planned'): ");
            input = sc.nextLine().trim();
            // check input
            if(input.equals("Complete") || input.equals("InProgress") || input.equals("Planned")) {
                validInput = true;
                toAdd.setAttemptStatus(input);  
            } else {
                // display error msg
                System.out.println("Invalid option: please 'Complete', 'InProgress', or 'Planned' exactly");
            }
        } // end while loop
        
        // prompt user for grade if that was complete
        if(toAdd.getAttemptStatus().equals("Complete")) {
            validInput = false;
            while(!validInput) {
                System.out.println("Enter its grade: ");
                input = sc.nextLine().trim();
                // check input
                if(!isValidGradeUserInput(input)) {
                    System.out.println("Invalid input: '" + input + "' must be a decimal value [0,100]");
                } else {
                    validInput = true;
                    toAdd.setAttemptGrade(input);
                }
            }
        }
        
        // actually add it to the list
        pos.getTranscript().add(toAdd);
    }
    
    /**
     * Loads (Reads) a Student Transcript CSV file and loads it into the plan of study (replacing the old one)
     * @param theUser the Plan of Study being used
     */
    public static void loadTranscript(Student theUser)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter the name of the transcipt CSV file you wish to load into your Plan Of Study: ");
        String filename = sc.nextLine().trim();

        theUser.importData(filename);
    }
    
    // ======================= for initial menu (maybe more) & helpers ==========================
    
    /**
     * Loads (Reads) a previously saved Plan of Study from a binary file to replace the current one
     * @param theUser the Plan of Study being replaced
     */
    public static void loadPlanOfStudy(Student theUser)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter the name of the binary file you wish to load a saved Plan of Study from: ");
        String filename = sc.nextLine().trim();

        theUser.readSavedState(filename);
    }
    
    /**
     * Creates a new Plan of Study (allows user to input Student user info, and select degree and major)
     * @param theUser the Plan of Study being used
     */
    public static void createNewPlanOfStudy(Student theUser)
    {
        Scanner sc = new Scanner(System.in);
        
        String input = "";
        // prompt user for student information
        System.out.println("Enter your first name: ");
        input = sc.nextLine().trim();
        theUser.setFirstName(input);
        
        System.out.println("Enter your last name: ");
        input = sc.nextLine().trim();
        theUser.setLastName(input);
        
        boolean validID = false;
        while(!validID) {
            System.out.println("Enter your student ID number: ");
            input = sc.nextLine().trim();
            // check input
            if(isInteger(input)) {
                validID = true;
                theUser.setStudentNumber(Integer.parseInt(input));
            } else {
                System.out.println("Error: must enter a positive integer for your student ID");
            }
        }
        
        selectDegreeAndMajor(theUser.getPlan());
    }
    
    /**
     * Allows the user to select a new degree and major for the Plan of Study
     * @param pos the Plan of Study being used
     */
    public static void selectDegreeAndMajor(PlanOfStudy pos)
    {
        Scanner sc = new Scanner(System.in);
        
        String input = "";        
        boolean validInput = false;
        
        // prompt user to choose Degree
        while(!validInput) {
            System.out.println("Select your degree and major:\n" +
                    "\t1. BCG:BCG  - Bachelor of Computing (General), Generic\n" + 
                    "\t2. BCH:CS   - Bachelor of Computing (Honours), Computer Science\n" +
                    "\t3. BCH:SEng - Bachelor of Computing (Honours), Software Engineering\n");
            input = sc.nextLine().trim();
            // check input
            if(input.equals("1")) {         // ================== 1 ===================
                validInput = true;
                pos.setDegreeProgram(new BCG());
                
            } else if(input.equals("2")) {  // ================== 2 ===================
                validInput = true;
                pos.setDegreeProgram(new CS());
                
            } else if(input.equals("3")) {  // ================== 3 ===================
                validInput = true;
                pos.setDegreeProgram(new SEng());
                
            } else {
                // display error msg
                System.out.println("Invalid option: please select one of the options by number [1,3]");
            }
        } // end while loop
        
        // make sure to initialize these again after they're changed
        pos.getDegreeProgram().getCatalog().readSavedCatalog("catalog.bin");
        pos.getDegreeProgram().readRequiredCourses("requiredCourses.bin", pos.getDegreeProgram().getMajor());
    }
    
    // ====================================================================================================================
    // --------------------------------------------------------------------------------------------------------------------
    // ====================================================================================================================
    
    /**
     * Boolean method - Checks if a string is an integer
     * @param sNum the string form of the number being checked
     * @return true if it successfully parses to an integer
     */
    public static boolean isInteger(String sNum)
    {
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
    public static boolean isDouble(String sNum) {
        try {
            Double.parseDouble(sNum);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Checks if the user input was a valid course code
     * @param code the course code user input being checked
     * @return true if the user input was a valid course code
     */
    public static boolean isValidCourseCode(String code)
    {
        // break code into subject and number, clear extra whitespace
        // then, load potentially altered course code back
        code = trimCourseCode(code);
        String codeSplit[] = code.split("\\*");
        
        // check proper format
        if(codeSplit.length != 2 || codeSplit[0].length() < 3 || codeSplit[0].length() > 4 || 
                codeSplit[1].length() != 4 || !isInteger(codeSplit[1])) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Checks if the user input was a valid grade
     * @param sNum the string form of the grade user input being checked
     * @return true if the user input was a valid grade
     */
    public static boolean isValidGradeUserInput(String sNum) {
        if(isDouble(sNum)) {
            // it's a number, but check it's a valid grade range
            if(Double.parseDouble(sNum) >= 0.0 && Double.parseDouble(sNum) <= 100.0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes all whitespace from a course code
     * @param code the course code to be altered
     * @return a trimmed course code without whitespace
     */
    public static String trimCourseCode(String code)
    {
        String codeSplit[] = code.split("\\*"); //break code into subject and number, clear extra whitespace
        for(String s : codeSplit) {
            s = s.trim();
        }
        // load potentially altered course code back
        if(codeSplit.length > 1) {
            for(int i = 0; i < codeSplit.length; i++) {
                if(i == 0) {
                    code = codeSplit[i];
                } else {
                    code += "*" + codeSplit[i];
                }
            }
        }
        return code;
    }
    
    /**
     * Prints the course list of the Plan Of Study (only code and title)
     * @param pos the PlanOfStudy being used
     */
    public static void printBasicPlanCourseList(PlanOfStudy pos) {
        System.out.println("All the courses in your Plan of Study are printed below.\n" +
                "Please select the one you would like to access based on the number it appears beside");
        int i = 1;
        for(Attempt c : pos.getTranscript()) {
            System.out.println("\t" + i + ". " + c.getCourseAttempted().getCourseCode() + " - " + c.getCourseAttempted().getCourseTitle());
            i++;
        }
        System.out.println("Select option: ");
    }
    
    /**
     * Prints the required courses list for the Plan Of Study (only code and title)
     * @param pos the PlanOfStudy being used
     */
    public static void printBasicRequiredCourseList(PlanOfStudy pos) {
        System.out.println("All the required courses for your degree and major are printed below.\n" +
                "Please select the one you would like to access based on the number it appears beside");
        int i = 1;
        for(Course c : pos.getDegreeProgram().getRequiredCourses()) {
            System.out.println("\t" + i + ". " + c.getCourseCode() + " - " + c.getCourseTitle());
            i++;
        }
        System.out.println("Select option: ");
    }
} // end Planner class
