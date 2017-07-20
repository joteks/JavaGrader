import java.lang.reflect.*;
import java.lang.*;
import java.util.regex.Pattern;
import java.io.*;
import java.util.*;

public class TestCase {
	private String name;
	private String message;
	private String category;
	private int value;
	private boolean result;

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public String getCategory() {
		return category;
	}

	public int getValue() {
		return value;
	}

	public boolean getResult() {
		return result;
	}

	public void setName(String name) {
		this.name=name;
	}

	public void setMessage(String message) {
		this.message=message;
	}

	public void setCategory(String category) {
		this.category=category;
	}

	public void setValue(int value) {
		this.value=value;
	}

	public void setResult(boolean result) {
		this.result=result;
	}

	public static boolean compile(File submission) throws Exception {
		Process pro = Runtime.getRuntime().exec("javac "+submission.getPath());
		String error = streamError(pro);
		System.out.println(error);
		return error.length() == 0;
	}

	public static boolean runMain(File submission, File input, String regex) throws Exception {
		if (compile(submission)) {
			String path = submission.getPath();
			path = path.substring(0, path.length()-5);
			String cmd = "java " + path;
			ProcessBuilder pb = new ProcessBuilder("java", "AboutMe");
			pb.redirectInput(input);
			Process pro = pb.start();
			pro.waitFor();
			//String output = streamOutput(pro);
			String output = "heloooooo\nooo";
			return output.matches(regex);
		}
		return false;
	}

	private static String streamError(Process pro) {
		String error = "";
		Scanner scan = new Scanner(pro.getErrorStream());
		while (scan.hasNext()) { error += scan.next(); }
		scan.close();
		return error;
	}

	private static String streamOutput(Process pro) {
		String output = "";
		Scanner scan = new Scanner(pro.getInputStream());
		while (scan.hasNext()) { output += scan.nextLine() + "\n"; }
		scan.close();
		return output;
	}

}