import java.io.*;

public class Project {
	public static void main(String[] args) {
		try {
			File f = new File("AboutMe.java");
			System.out.println("Compiles: " + TestCase.compile(f));
			System.out.println("Run Main: " + 
				TestCase.runMain(f, new File("input.txt"), "^(.|\n)*helooo(.|\n)*$"));
			System.out.println("Run Main: " + 
				TestCase.runMain(f, "heloooo", "^(.|\n)*helooo(.|\n)*$"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}