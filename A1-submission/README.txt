/*
 * @version 25/10/2018
 * Object Oriented Programming - A1
 *	-> generally explains thought process and extra stuff behind my code (aside from assignment description)
 */

Assumption: This was coded using NetBeans IDE. Since packages were not wanted, I removed the package statements and I assume it should run with javac commands.
		If for some reason it does not work, by using netbeans and uncommenting the package statements, and having it in the proper associated file organization, it should run
NOTE: the above assumption is extremely relevant because noMachine would not connect and allow me to test it in BlueJ or on the SOCS machine

NOTE: meetsRequirements generates odd numbers for number of credits required/earned for that degree, but the regular view option from Planner menu works normally.

Asusmption: since saveState() doesn't have parameters, I always save to "savedPlan.bin"
Assumption: saving a CourseCatalog always writes to "catalog.bin"
Assumption: saving a requiredCourses list always writes to "requiredCourses.bin"

Assumption: the user should have a default program so that things don't break completely if other Planner user stories are executed before data is loaded and applies
Assumption: the user does not try to break the program with bad input like nulls and empty cases and badly formatted input (most error checking is covered, but not all)

Assumption: where it makes sense, I have combined user stories to be sequential
Assumption: the concrete degree classes use inheritance to minimize code duplication/retyping

Assumption: credits are only earned if the course is completed and a mark of 50+ is achieved

Assumption: General degrees need 15.0 credits to finish
Assumption: Honours degrees need 20.0 credits to finish

See comments for more description on each class

***
TO RUN: (on command line)
javac *.java

then you can run either of the following programs (Bootstrap needs to be run first if no files are created yet):
java Bootstrap
java Planner
***

/**
 * BOOTSTRAP
 * --> has a main method and can:
  load a list of available courses from a CSV file, replacing any existing representation of those courses
 --> Course List file: Note that prerequisite list will be a colon-delimited list of prerequisite courses
    --> line: CourseCode,CreditWeight,CourseName,PrerequisiteList
    --> eg: CIS*2430,0.5,Object Oriented Programming,CIS*2500
            CIS*2750,0.75,Software Systems Development and Integration,CIS*2520:CIS*2430
  load a list of the required courses for a degree from CSV file, replacing any existing representation of that degree
 --> Required Courses file: file should only be loaded once the Course List has been loaded.
    --> line: represents the required courses for a single degree (DegreeName,RequiredCourse1,RequiredCourse2,etc)
    --> eg: BCG, CIS*1500,CIS*1910,CIS*2430,CIS*2500,CIS*2520,CIS*2750,CIS*2910,CIS*3530
  save the current representation of course and degree information to a file that is used by the planning application
 */

/**
 * PLANNER
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