To run the tests with AboutMe project:

cd src
make.sh
java Project

The above commands will run tests on the AboutMe.java file in src (not the AboutMe.java file in src/asnlib). To test other student solutions, replace src/AboutMe.java with student solution.


To implement AboutMe project tests on Vocareum:

cd src
make.sh

Create new assignment on Vocareum called AboutMe. Set grading criteria to Completion -> 10 and Correctness -> 5.

Go to configure assignment and upload src/TestCase.class and src/Parser.class to $LIB (only have to do once for all assignments)

Upload src/Project.class and src/asnlib/AboutMe.java to $ASNLIB (don't put a asnlib directory in $ASNLIB)

Copy/paste contents of src/SubmissionScript.sh to $Scripts/Submission AND $Scripts/Grading


To create new project on Vocareum:

1. Replace src/Project.java with your own implementation
	- Use documentation in src/javadoc/index.html to do this
2. Add solution file(s) to src/asnlib folder
3. Add student submission file(s) to src folder
4. Add javaparser-core-3.2.7.jar to classpath (Library/Java/Extensions if using Mac)
5. Run make.sh
6. Run Project.class to test : java Project
7. Upload src/Project.class file to $ASNLIB folder on corresponding Vocareum assignment
8. Upload contents of src/asnlib to $ASNLIB folder on corresponding Vocareum assignment 
	- do not create asnlib folder within $ASNLIB
9. Copy/paste contents of src/SubmissionScript.sh to $Scripts/Submission AND $Scripts/Grading
