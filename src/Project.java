import java.io.*;
import java.util.Scanner;

public class Project {
	public static void main(String[] args) {
		try {
			File f = new File("../AboutMe.java");
			File s = new File("AboutMe.java");
			File n = TestCase.replaceMethod(f, s, "public String myName\\(\\)");
			TestCase[] tests = {
				new TestCase("Test 1", "AboutMe.java does not compile", "Completion", 1, TestCase.compile(f)),
				new TestCase("Test 2", "Main method prints hello", "Correctness", 1, TestCase.runMain(f, new File("input.txt"), "^(.|\n)*helooo(.|\n)*$")),
				new TestCase("Test 3", "AboutMe declared properly", "Completion", 1, TestCase.matchPattern(f, "^(.|\n)*AboutMe(.|\n)*$")),
				new TestCase("Test 4", "Method myName is correct", "Correctness", 1, TestCase.runMain(n, new File("input.txt"), "^(.|\n)*helooo(.|\n)*$"))
			};
			TestCase.pushAll(tests);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}