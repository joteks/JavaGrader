/**
* @author Robyn Berkel
* @verison 1.0
*/



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

public class Parser {

	/**
	* All comments found in file parsed by parse method
	*/

	public List<Comment> comments;

	/**
	* All constructors found in file parsed by parse method
	*/

	public List<ConstructorDeclaration> constructors;

	/**
	* All methods found in file parsed by parse method
	*/

	public List<MethodDeclaration> methods;

	/**
	* All fields found in file parsed by parse method
	*/

	public List<FieldDeclaration> fields;

	/**
	* All problems (compile-time errors) found in file parsed by parse method
	*/

	public List<Problem> problems;

	/*
	* Initializes list fields
	*/

	public Parser() {
		comments = new ArrayList<Comment>();
		constructors = new ArrayList<ConstructorDeclaration>();
		methods = new ArrayList<MethodDeclaration>();
		fields = new ArrayList<FieldDeclaration>();
		problems = new ArrayList<Problem>();
	}


	/**
	* Parses the file given and adds file data to the class fields to be used with other Parser methods.
	* <p>
	* Not static to avoid needing to parse file data multiple times
	* @param file the file to parse, data from file will be added to class fields
	* @return true if at least partial parse was successful
	* @throws Exception any exception
	*/

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
				else if (n instanceof MethodDeclaration) {
					methods.add((MethodDeclaration)n);
				}
				else if (n instanceof FieldDeclaration) fields.add((FieldDeclaration)n);
			}
			return true;
	}

	/**
	* Returns a String reporting compile-time errors found. 
	* <p>
	* These "problems" include problems that make code ungradable and problems that can be worked around. 
	* @return Returns a String starting with "Compile-time errors were found at these locations: " then lists problem locations by (line, column)
	*/

	public String reportProblems() {
		String result = "Compile-time errors were found at these locations:";
		for (Problem p : problems) {
			if (p.getLocation().isPresent()) {
				result += "\n" + p.getLocation().get().toRange().begin;
			}
		}
		return result;
	}

	/**
	* Checks if given method exists in the parsed file. Only checks return type, name, and parameter list
	* @param signature method signature to look for. Should be in format returnType MethodName (paramType, paramType, paramType, ... )
	* @return true if signature is found, false if signature not found
	*/

	public boolean checkMethodSignature(String signature) {
		for (MethodDeclaration m : methods) {
			if (m.toString().equals(signature)) {
				return true;
			}
		}
		return false;
	}

	/**
	* Calls replaceMethod for each signature listed in sigs
	* @param sigs array of method signatures each in the format returnType MethodName (paramType, paramType, paramType, ... )
	* @param work path of file to replace with
	* @return array of new files with replacements, possibly contains null values
	* @throws Exception any exception
	*/

	public File[] replace(String[] sigs, String work) throws Exception {
		File[] files = new File[sigs.length];
		for (int i = 0; i < sigs.length; i++) {
			files[i] = replaceMethod(sigs[i], work);
		}
		return files;
	}

	/**
	* Looks for method signature in "methods" class field. If found, then looks for method signature in "file" (String parameter).
	* If found, it takes the method body of method in "methods" class field and replaces method body in "file" with it.
	* <p>
	* Ending result is a new file with the contents of "file", except now the matching method body is the body from the original parsed file (student submission)
	* @param signature method signature each in the format returnType MethodName (paramType, paramType, paramType, ... )
	* @param file path of file to replace with
	* @return array of new files with replacements, possibly contains null values
	*/

	public File replaceMethod(String signature, String file) {
		try {
			File f = new File(file);
			JavaParser parser = new JavaParser();
			ParseResult<CompilationUnit> result = parser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(f));

			//try to retrieve nodes
			if (!result.getResult().isPresent()) return null;
			CompilationUnit cu = result.getResult().get();

			//get methods
			int start = file.lastIndexOf("/") + 1;
			if (start == -1) start = 0;
			ClassOrInterfaceDeclaration childNodes = cu.getClassByName(file.substring(start, file.length()-5)).get();
			for (Node n : childNodes.getChildNodes()) {
				if (n instanceof MethodDeclaration) {
					MethodDeclaration toSet = (MethodDeclaration)n;
					if (toSet.getDeclarationAsString(false, false, false).equals(signature)) {
						for (MethodDeclaration m : methods) {
							if (m.getDeclarationAsString(false, false, false).equals(signature)) {

								//make modification
								toSet.setBody(m.getBody().get());
								Random r = new Random();
								String temp = "temp" + r.nextInt(10000);
								File directory = new File(temp);
								directory.mkdir();
								File newfile = new File(temp + "/AboutMe.java");
								PrintWriter pw = new PrintWriter(newfile);
								pw.write(cu.toString());
								pw.close();
								return newfile;
							}
						}
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	* Returns list of small expressions extracted from all statements found in the method corresponding to "method" parameter.
	* <p>
	* Expression types are defined in JavaParser project: com.github.javaparser.ast.expr.*
	* @param method method signature to find statements from, matches format returnType MethodName (paramType, paramType, paramType, ... )
	* @param exprclass specific Expression class to look for. Ex. MethodCallExpr, BinaryExpr, etc. These Expression types are defined in JavaParser
	* @return list of expressions that are instances of the class defined by exprclass found in the indicated method
	* @throws Exception any exception
	*/

	public List<Expression> findExpressionsOfType(String method, Class exprclass) throws Exception {
		for (MethodDeclaration m : methods) {
			if (m.getDeclarationAsString(false, false, false).equals(method)) {
				//find the statement
				List<Expression> expressions = new ArrayList<Expression>();
				NodeList<Statement> statements = m.getBody().get().getStatements();
				for (Statement s : statements) {
					if (s instanceof ExpressionStmt) {
						Expression e = ((ExpressionStmt)s).getExpression();
						List<Expression> list = getAllExpressions(e);
						for (Expression x : list) {
							if (exprclass.isInstance(x)) {
								expressions.add(x);
							}
						}
					}
				}
				return expressions;
			}
		}
		return null;
	}

	/**
	* Helper method (made public for possible future cases) for findExpressionsOfType. Recursive method to find all pieces of an Expression.
	* @param ex root expression from which to derive all smaller Expressions
	* @return list of all found Expressions, including all nodes not just leaf nodes
	* @throws Exception any exception
	*/

	public List<Expression> getAllExpressions(Expression ex) throws Exception {
		List<Expression> e = new ArrayList<Expression>();
		e.add(ex);
		if (ex instanceof ArrayAccessExpr) {
			ArrayAccessExpr ae = (ArrayAccessExpr)ex;
			e.addAll(getAllExpressions(ae.getName()));
			e.addAll(getAllExpressions(ae.getIndex()));
		}
		else if (ex instanceof ArrayInitializerExpr) {
			ArrayInitializerExpr ae = (ArrayInitializerExpr)ex;
			NodeList<Expression> exs = ae.getValues();
			for (Expression s : exs) {
				e.addAll(getAllExpressions(s));
			}
		}
		else if (ex instanceof AssignExpr) {
			AssignExpr ae = (AssignExpr)ex;
			e.addAll(getAllExpressions(ae.getTarget()));
			e.addAll(getAllExpressions(ae.getValue()));
		}
		else if (ex instanceof BinaryExpr) {
			BinaryExpr be = (BinaryExpr)ex;
			e.addAll(getAllExpressions(be.getLeft()));
			e.addAll(getAllExpressions(be.getRight()));
		}
		else if (ex instanceof CastExpr) {
			CastExpr be = (CastExpr)ex;
			e.addAll(getAllExpressions(be.getExpression()));
		}
		else if (ex instanceof ConditionalExpr) {
			ConditionalExpr be = (ConditionalExpr)ex;
			e.addAll(getAllExpressions(be.getCondition()));
			e.addAll(getAllExpressions(be.getElseExpr()));
			e.addAll(getAllExpressions(be.getThenExpr()));
		}
		else if (ex instanceof EnclosedExpr) {
			EnclosedExpr be = (EnclosedExpr)ex;
			if (be.getInner().isPresent()) {
				e.addAll(getAllExpressions(be.getInner().get()));	
			}
		}
		else if (ex instanceof FieldAccessExpr) {
			FieldAccessExpr be = (FieldAccessExpr)ex;
			e.addAll(getAllExpressions(be.getScope()));	
		}
		else if (ex instanceof InstanceOfExpr) {
			InstanceOfExpr be = (InstanceOfExpr)ex;
			e.addAll(getAllExpressions(be.getExpression()));	
		}
		else if (ex instanceof InstanceOfExpr) {
			InstanceOfExpr be = (InstanceOfExpr)ex;
			e.addAll(getAllExpressions(be.getExpression()));	
		}
		else if (ex instanceof LambdaExpr) {
			LambdaExpr ae = (LambdaExpr)ex;
			if (ae.getExpressionBody().isPresent()) {
				e.addAll(getAllExpressions(ae.getExpressionBody().get()));
			}
		}
		else if (ex instanceof MethodCallExpr) {
			MethodCallExpr ae = (MethodCallExpr)ex;
			NodeList<Expression> exs = ae.getArguments();
			for (Expression s : exs) {
				e.addAll(getAllExpressions(s));
			}
			if (ae.getScope().isPresent()) {
				e.addAll(getAllExpressions(ae.getScope().get()));
			}
		}
		else if (ex instanceof MethodReferenceExpr) {
			MethodReferenceExpr be = (MethodReferenceExpr)ex;
			e.addAll(getAllExpressions(be.getScope()));	
		}
		else if (ex instanceof ObjectCreationExpr) {
			ObjectCreationExpr ae = (ObjectCreationExpr)ex;
			NodeList<Expression> exs = ae.getArguments();
			for (Expression s : exs) {
				e.addAll(getAllExpressions(s));
			}
			if (ae.getScope().isPresent()) {
				e.addAll(getAllExpressions(ae.getScope().get()));
			}
		}
		else if (ex instanceof SuperExpr) {
			SuperExpr ae = (SuperExpr)ex;
			if (ae.getClassExpr().isPresent()) {
				e.addAll(getAllExpressions(ae.getClassExpr().get()));
			}
		}
		else if (ex instanceof ThisExpr) {
			ThisExpr ae = (ThisExpr)ex;
			if (ae.getClassExpr().isPresent()) {
				e.addAll(getAllExpressions(ae.getClassExpr().get()));
			}
		}
		else if (ex instanceof UnaryExpr) {
			UnaryExpr be = (UnaryExpr)ex;
			e.addAll(getAllExpressions(be.getExpression()));	
		}
		else if (ex instanceof VariableDeclarationExpr) {
			VariableDeclarationExpr ae = (VariableDeclarationExpr)ex;
			NodeList<AnnotationExpr> exs = ae.getAnnotations();
			for (AnnotationExpr s : exs) {
				e.addAll(getAllExpressions(s));
			}
		}
		return e;
	}
}
