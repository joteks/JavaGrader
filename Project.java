import java.io.*;

public class Project {
	public static void main(String[] args) {
		try {
			File f = new File("AboutMe.java");
			System.out.println("Compiles: " + TestCase.compile(f));
			System.out.println("Run Main: " + TestCase.runMain(f, new File("input.txt"), "^.*helooo.*$"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}