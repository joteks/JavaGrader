import java.io.*;
import java.util.Scanner;

public class Project {
	public static void main(String[] args) {
		try {
			File student = new File("AboutMe.java");
			File solution = new File("asnlib/AboutMe.java");
			File n = TestCase.replaceMethod(student, solution, "public String myName\\(\\)");
			File s = TestCase.replaceMethod(student, solution, "public String mySchool\\(\\)");
			File a = TestCase.replaceMethod(student, solution, "public String myAge\\(\\)");
			File m = TestCase.replaceMethod(student, solution, "public static void main\\(String[] args\\)");
			TestCase[] tests = {
				new TestCase("Test 1", "AboutMe.java compiles", "Correctness", 1, TestCase.compile(student)),
				new TestCase("Test 2", "Class header written correctly", "Completion", 1, TestCase.matchPattern(student, "(.|\n)*AboutMe(.|\n)*")),
				new TestCase("Test 3", "Main method signature written correctly", "Completion", 1, TestCase.matchPattern(student, "(.|\n)*AboutMe(.|\n)*")),
				new TestCase("Test 4", "myName method signature written correctly", "Completion", 1, TestCase.matchPattern(student, "^(.|\n)*AboutMe(.|\n)*$")),
				new TestCase("Test 5", "mySchool method signature written correctly", "Completion", 1, TestCase.matchPattern(student, "^(.|\n)*AboutMe(.|\n)*$")),
				new TestCase("Test 6", "myAge method signature written correctly", "Completion", 1, TestCase.matchPattern(student, "^(.|\n)*AboutMe(.|\n)*$")),
				new TestCase("Test 7", "Main method uses myName, mySchool, and myAge method calls", "Completion", 1, TestCase.runMain(m, "hi", "Robyn")),
				new TestCase("Test 8", "Main method greets user", "Correctness", 1, TestCase.runMain(m, "hi", "hello")),
				new TestCase("Test 9", "myName functionality", "Correctness", 1, TestCase.runMain(n, "hi", "Robyn")),
				new TestCase("Test 10", "mySchool functionality", "Correctness", 1, TestCase.runMain(s, "hi", "Purdue")),
				new TestCase("Test 11", "myAgo functionality", "Correctness", 1, TestCase.runMain(a, "hi", "20"))
			};
			TestCase.pushAll(tests);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}