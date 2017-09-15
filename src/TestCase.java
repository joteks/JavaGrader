/**
* @author Robyn Berkel
* @verison 1.1
*/


import java.lang.reflect.*;
import java.lang.*;
import java.util.regex.Pattern;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.nio.file.*;

public class TestCase {

	private String name;
	private String message;
	private String category;
	private int value;
	private boolean result;

	/**
	* @param name name of test (typically Test 1, Test 2, etc)
	* @param message message of test, overall description that conveys to student what this test is grading
	* @param category corresponding Vocareum category (Ex. Completion, Correctness, Style, etc)
	* @param value point value of test
	* @param result true if test should be counted as passed, false if test should be counted as failed 
	*/

	public TestCase(String name, String message, String category, int value, boolean result) {
		this.name = name;
		this.message = message;
		this.category = category;
		this.value = value;
		this.result = result;
	}

	/**
	* @param name name of test (typically Test 1, Test 2, etc)
	* @param message message of test, overall description that conveys to student what this test is grading
	* @param category corresponding Vocareum category (Ex. Completion, Correctness, Style, etc)
	* @param value point value of test
	*/

	public TestCase(String name, String message, String category, int value) {
		this.name = name;
		this.message = message;
		this.category = category;
		this.value = value;
		this.result = false;
	}

	/**
	* Getter method for name field
	* @return name of test
	*/

	public String getName() {
		return name;
	}

	/**
	* Getter method for message field
	* @return message of test, overall description that conveys to student what this test is grading
	*/

	public String getMessage() {
		return message;
	}

	/**
	* Getter method for category field
	* @return category corresponding Vocareum category (Ex. Completion, Correctness, Style, etc)
	*/

	public String getCategory() {
		return category;
	}

	/**
	* Getter method for value field
	* @return value point value of test
	*/

	public int getValue() {
		return value;
	}

	/**
	* Getter method for result field
	* @return result true if test should be counted as passed, false if test should be counted as failed 
	*/

	public boolean getResult() {
		return result;
	}

	/**
	* Setter method for name field
	* @param name of test
	*/

	public void setName(String name) {
		this.name=name;
	}

	/**
	* Setter method for message field
	* @param message of test, overall description that conveys to student what this test is grading
	*/

	public void setMessage(String message) {
		this.message=message;
	}

	/**
	* Setter method for category field
	* @param category corresponding Vocareum category (Ex. Completion, Correctness, Style, etc)
	*/

	public void setCategory(String category) {
		this.category=category;
	}

	/**
	* Setter method for value field
	* @param value point value of test
	*/

	public void setValue(int value) {
		this.value=value;
	}

	/**
	* Setter method for result field
	* @param result true if test should be counted as passed, false if test should be counted as failed 
	*/

	public void setResult(boolean result) {
		this.result=result;
	}

	/**
	* Format the result of all tests into a csv-style string that fits Vocareum expection, outputted to a file called gradefile.txt
	* @param tests Array of tests to push to file
	* @return true if successfull, false otherwise
	* @throws Exception any exception
	*/

	public static boolean pushAll(TestCase[] tests) throws Exception{
		//Group tests by category
		ArrayList<String> categories = new ArrayList<String>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		for (TestCase tc : tests) {
			if (tc.getCategory()==null) return false;
			String cat = tc.getCategory();
			if (categories.contains(cat) && tc.getResult()) {
				int index = categories.indexOf(cat);
				counts.set(index, counts.get(index) + tc.getValue());
			} else if (!categories.contains(cat) && tc.getResult()){
				categories.add(cat);
				counts.add(tc.getValue());
			} else if (!categories.contains(cat)){
				categories.add(cat);
				counts.add(0);
			}
			String r = "Failed";
			if (tc.getResult()) r = "Passed";
            System.out.printf("%-10s\t%-100s\t%s\n", tc.getName(), tc.getMessage(), r);
		}
        File out = new File("gradefile.txt");
        PrintWriter pw = new PrintWriter(out);
		for (int i = 0; i < categories.size(); i++) {
            pw.println(categories.get(i) + "," + counts.get(i));
		}
        pw.close();
		return false;
	}

	/**
	* Compile the given file
	* @param submission file to compile
	* @return true if successful compilation with no errors, false otherwise
	* @throws Exception any exception
	*/

	public static boolean compile(File submission) throws Exception {
		if (submission == null) return false;
		Process pro = Runtime.getRuntime().exec("javac " + submission.getPath());
		String error = streamError(pro);
		return error.length() == 0;
	}

	/**
	* Runs the main method of the file given with the input and checks if it matches the regular expression given.
	* <p>
	* Only works on files with a main method. Ideally, this should only be run on files whose methods have been replaced but general content was written by intructor.
	* Does still work for student submission, but calling this method on a raw student submission doesn't fit the intended grading workflow
	* @param submission work to be run
	* @param input input given to main method, can be null if no input is needed
	* @param regex regular expression to match the output with
	* @return true if file runs successfully and the output matches the regular expression, false otherwise
	* @throws Exception any exception
	*/

	public static boolean runMain(File submission, File input, String regex) throws Exception {
		if (compile(submission)) {
			String[] path = submission.getPath().substring(0, submission.getPath().length()-5).split("/");
			Random r = new Random();
			File output = new File("temp" + r.nextInt(10000) + ".txt");

			Process p = Runtime.getRuntime().exec("java -cp " + path[0] + " " + path[1]);
            BufferedReader in = new BufferedReader(
                                new InputStreamReader(p.getInputStream()));
            String out = "";
            String line = null;
            while ((line = in.readLine()) != null) {
                out += line + "\n";
            }
			return out.matches(regex);
		}
		return false;
	}


	private static String commentsHelper(String file) {
		char[] dots = file.toCharArray();
		boolean openstring = false;
		boolean openchar = false;
		boolean comment = false;
		boolean multicomment = false;
		int[] remove = new int[dots.length];
		int count = 0;
		char prev = '9';
		for (char d : dots) {
			if (comment || multicomment) {
				remove[count] = 1;
			}
			if (d == '\'') {
				if (comment || multicomment) {
					;
				} else if (openstring) {
					;
				} else if (!openchar) {
					openchar = true;
				} else if (prev != '\\') {
					openchar = false;
				} else {
					;
				}
			} else if (d == '\"') {
				if (comment || multicomment) {
					;
				} else if (openchar) {
					;
				} else if (!openstring) {
					openstring = true;
				} else if (prev != '\\') {
					openstring = false;
				} else {
					;
				}
			} else if (d == '/') {
				if (multicomment) {
					if (prev == '*') {
						multicomment = false;
					}
				} else if (!comment) {
					if (prev == '/' && !openstring && !openchar) {
						comment = true;
						remove[count-1] = 1;
					}
				}
			} else if (d == '\n') {
				if (comment) {
					comment = false;
				}
			} else if (d == '*') {
				if (prev == '/') {
					if (!multicomment && !openstring && !openchar) {
						multicomment = true;
						remove[count-1] = 1;
					}
				}
			}
			prev = d;
			if (comment || multicomment) {
				remove[count] = 1;
			}
			count++;
		}
		count = 0;
		String newFile = "";
		for (int i : remove) {
			if (i == 0) {
				newFile += dots[count];
			}
			count++;
		}
		return newFile;
	}

	private static String removeComments(File submission) throws Exception {
		Scanner read = new Scanner(submission);
		String result = "";
		while (read.hasNextLine()) { result += read.nextLine() + "\n"; }
		return commentsHelper(result);
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