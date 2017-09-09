import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.body.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.metamodel.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

public class Project {
	public static void main(String[] args) {

		TestCase[] tests = {
				new TestCase("Test 1", "AboutMe.java compiles", "Correctness", 1),
				new TestCase("Test 2", "Class header written correctly", "Completion", 1),
				new TestCase("Test 3", "Main method signature written correctly", "Completion", 1),
				new TestCase("Test 4", "myName method signature written correctly", "Completion", 1),
				new TestCase("Test 5", "mySchool method signature written correctly", "Completion", 1),
				new TestCase("Test 6", "myAge method signature written correctly", "Completion", 1),
				new TestCase("Test 7", "Main method uses myAge method call", "Completion", 1),
				new TestCase("Test 8", "Main method uses mySchool method call", "Completion", 1),
				new TestCase("Test 9", "Main method uses myName method call", "Completion", 1),
				new TestCase("Test 10", "Main method greets user", "Correctness", 1),
				new TestCase("Test 11", "myName functionality", "Correctness", 1),
				new TestCase("Test 12", "mySchool functionality", "Correctness", 1),
				new TestCase("Test 13", "myAge functionality", "Correctness", 1)
			};

		try {
			String work = "AboutMe.java";
			String solution = "asnlib/AboutMe.java";

			//Report problems
			Parser parser = new Parser();
			boolean parsed = parser.parse(work);
			if (parser.problems.size() != 0) {
				System.out.println(parser.reportProblems());
			}
			if (!parsed) {
				System.out.println("You need to fix the errors listed above before any grading can be done.");
				return;
			}

			tests[0].setResult(TestCase.compile(new File(work)));
			tests[1].setResult(true);

			//replace methods and create temporary files
			String[] sigs = {"void main(String[])", "String myName()", "String mySchool()", "int myAge()"};
			File[] files = parser.replace(sigs, solution);
			
			//check files and give points for methods found
			for (int i = 0; i < files.length; i++) {
				if (files[i] == null) {
					System.out.println("Method not found: " + sigs[i]);
				} else {
					tests[i + 2].setResult(true);
				}
			}

			//Test myName, myAge, mySchool
			tests[10].setResult(TestCase.runMain(files[1], null, "(?s).*My name: \\D+.*"));
			tests[11].setResult(TestCase.runMain(files[2], null, "(?s).*My school: \\D+.*"));
			tests[12].setResult(TestCase.runMain(files[3], null, "(?s).*My age: \\d+.*"));

			List<Expression> exs = parser.findExpressionsOfType("void main(String[])", MethodCallExpr.class);
			for (Expression ex : exs) {
				if (ex instanceof MethodCallExpr) {
					MethodCallExpr mx = (MethodCallExpr)ex;
					String name = mx.getName().asString();
					if (name.equals("myAge")) tests[6].setResult(true);
					if (name.equals("mySchool")) tests[7].setResult(true);
					if (name.equals("myName")) tests[8].setResult(true);
					if (name.equals("println") || mx.getName().equals("printf") || mx.getName().equals("print")) 
						tests[9].setResult(true);
				}
			}

			TestCase.pushAll(tests);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}