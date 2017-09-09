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
	* @param category corresponding Vocareum category () 
	*/

	public TestCase(String name, String message, String category, int value, boolean result) {
		this.name = name;
		this.message = message;
		this.category = category;
		this.value = value;
		this.result = result;
	}

	public TestCase(String name, String message, String category, int value) {
		this.name = name;
		this.message = message;
		this.category = category;
		this.value = value;
		this.result = false;
	}

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
				counts.add(tc.getValue());
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

	public static boolean compile(File submission) throws Exception {
		if (submission == null) return false;
		Process pro = Runtime.getRuntime().exec("javac " + submission.getPath());
		String error = streamError(pro);
		return error.length() == 0;
	}

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