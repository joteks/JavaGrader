import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.body.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

public class Parser {

	List<Comment> comments;
	List<ConstructorDeclaration> constructors;
	List<MethodDeclaration> methods;
	List<FieldDeclaration> fields;
	List<Problem> problems;

	public Parser() {
		comments = new ArrayList<Comment>();
		constructors = new ArrayList<ConstructorDeclaration>();
		methods = new ArrayList<MethodDeclaration>();
		fields = new ArrayList<FieldDeclaration>();
		problems = new ArrayList<Problem>();
	}


	public boolean parse(String file) throws Exception {
			File f = new File(file);
			JavaParser parser = new JavaParser();
			ParseResult<CompilationUnit> result = parser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(f));

			//to get beginning of problem: p.getLocation().get().toRange().begin
			problems = result.getProblems();

			//try to retrieve nodes
			if (!result.getResult().isPresent()) return false;
			CompilationUnit cu = result.getResult().get();

			//get comments, methods, fields, and constructors
			comments = cu.getComments();
			Optional<ClassOrInterfaceDeclaration> child = cu.getClassByName(file.substring(0, file.length()-5));
			if (!child.isPresent()) {
				return false;
			}
			ClassOrInterfaceDeclaration childNodes = child.get();
			for (Node n : childNodes.getChildNodes()) {
				if (n instanceof ConstructorDeclaration) constructors.add((ConstructorDeclaration)n);
				else if (n instanceof MethodDeclaration) methods.add((MethodDeclaration)n);
				else if (n instanceof FieldDeclaration) fields.add((FieldDeclaration)n);
			}
			return true;
	}

	public String reportProblems() {
		String result = "Compile-time errors were found at these locations:";
		for (Problem p : problems) {
			if (p.getLocation().isPresent()) {
				result += "\n" + p.getLocation().get().toRange().begin;
			}
		}
		return result;
	}

	public boolean checkMethodSignature(String signature) {
		for (MethodDeclaration m : methods) {
			if (m.toString().equals(signature)) {
				return true;
			}
		}
		return false;
	}

	public boolean replaceMethod(String signature, String file) throws Exception {
		File f = new File(file);
		JavaParser parser = new JavaParser();
		ParseResult<CompilationUnit> result = parser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(f));

		//try to retrieve nodes
		if (!result.getResult().isPresent()) return false;
		CompilationUnit cu = result.getResult().get();

		//get methods
		ClassOrInterfaceDeclaration childNodes = cu.getClassByName(file.substring(0, file.length()-5)).get();
		for (Node n : childNodes.getChildNodes()) {
			if (n instanceof MethodDeclaration) {
				MethodDeclaration toSet = (MethodDeclaration)n;
				if (toSet.getDeclarationAsString(false, false, false).equals(signature)) {
					for (MethodDeclaration m : methods) {
						if (m.getDeclarationAsString(false, false, false).equals(signature)) {

							//make modification
							toSet.setBody(m.getBody().get());
							File newfile = new File("AboutMeModified.java");
							PrintWriter pw = new PrintWriter(newfile);
							pw.write(cu.toString());
							pw.close();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			Parser p = new Parser();
			System.out.println(p.parse("AboutMe.java"));
			System.out.println(p.reportProblems());
			System.out.println(p.methods.get(0).getDeclarationAsString(false, false, false));
			System.out.println(p.replaceMethod("String myName()", "AboutMeTest.java"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
