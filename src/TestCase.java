import java.lang.reflect.*;
import java.lang.*;
import java.util.regex.Pattern;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;

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
			ProcessBuilder pb = new ProcessBuilder("java", path);
			pb.redirectInput(input);
			Process pro = pb.start();
			pro.waitFor();
			String output = "heloooooo\nooo";
			return output.matches(regex);
		}
		return false;
	}

	public static boolean runMain(File submission, String input, String regex) throws Exception {
		PrintWriter out = new PrintWriter("runmaintemp.txt");
		out.println(input);
		File f = new File("runmaintemp.txt");
		boolean result = runMain(submission, f, regex);
		f.delete();
		return result;

	}

	public static File replaceMethod(File submission, File solution, String method) throws Exception {
		//find index of regex match
		String submit = removeComments(submission);
		Pattern pattern = Pattern.compile(method);
		Matcher matcher = pattern.matcher(submit);
		if (matcher.find() == false) {
			return null;
		}
		int end = matcher.end();

		//get body of method from submission
		Stack<Character> stack = new Stack<Character>();
		stack.push('{');
		char c;
		boolean openchar = false;
		boolean openstring = false;
		int i = end;
		String body = "{";
		while (!stack.empty() && i < submit.length()-1) {
			i++;
			c = submit.charAt(i);
			body += c;
			if ((c == '{' || c == '}') && !openchar && !openstring) {
				if (c=='{') {
					stack.push('{');
				} else {
					try {
						stack.pop();
					} catch (EmptyStackException e) {
						return null;
					}
				}
			}
			if (c == '\'' && !openstring && !openchar) {
				openchar = true;
			} else if (c == '\'' && openchar) {
				openchar = false;
			} else if (c == '\"' && !openstring && !openchar) {
				openstring = true;
			} else if (c == '\"' && openstring) {
				openstring = false;
			}
		}

		//replace body of method from solution
		String sol = removeComments(solution);
		matcher = pattern.matcher(sol);
		if (matcher.find() == false) {
			return null;
		}
		end = matcher.end();

		String result = sol.substring(0, end);
		result += body;

		stack = new Stack<Character>();
		stack.push('{');

		openchar = false;
		openstring = false;
		i = end;
		body = "{";
		while (!stack.empty() && i < submit.length()-1) {
			i++;
			c = submit.charAt(i);
			body += c;
			if ((c == '{' || c == '}') && !openchar && !openstring) {
				if (c=='{') {
					stack.push('{');
				} else {
					try {
						stack.pop();
					} catch (EmptyStackException e) {
						return null;
					}
				}
			}
			if (c == '\'' && !openstring && !openchar) {
				openchar = true;
			} else if (c == '\'' && openchar) {
				openchar = false;
			} else if (c == '\"' && !openstring && !openchar) {
				openstring = true;
			} else if (c == '\"' && openstring) {
				openstring = false;
			}
		}

		result += sol.substring(i + 1);
		//create directory named temp
		Random r = new Random();
		int number = r.nextInt(99999999);
		File directory = new File("temp" + number);
		directory.mkdir();
		File temp = new File("temp" + number + "/" + solution.getName());
		PrintWriter fw = new PrintWriter(temp);
		System.out.println(result);
		fw.write(result);
		fw.close();
		return temp;
	}

	public static boolean matchPattern(File submission, String regex) throws Exception {
		String file = removeComments(submission);
		return file.matches(regex);
	}

	private static String removeComments(File submission) throws Exception {
		Scanner read = new Scanner(submission);
		String result = "";
		while (read.hasNextLine()) { result += read.nextLine() + "\n"; }
		return CommentParser.removeComments(result);
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