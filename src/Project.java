import java.io.*;
import java.util.Scanner;

public class Project {
	public static void main(String[] args) {
		try {
			File f = new File("../AboutMe.java");
			File s = new File("AboutMe.java");
			System.out.println("Compiles: " + TestCase.compile(f));
			System.out.println("Run Main: " + 
				TestCase.runMain(f, new File("input.txt"), "^(.|\n)*helooo(.|\n)*$"));
			System.out.println("Run Main: " + 
				TestCase.runMain(f, "heloooo", "^(.|\n)*helooo(.|\n)*$"));
			System.out.println("Run matchPattern: " + TestCase.matchPattern(f, "^(.|\n)*AboutMe(.|\n)*$"));
			System.out.println("Run replaceMethod: " + TestCase.replaceMethod(f, s, "public String myName\\(\\)").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}