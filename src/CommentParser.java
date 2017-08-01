import java.util.Scanner;
import java.io.File;

public class CommentParser {
	public static String removeComments(String file) {
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

	public static void main(String[] args) {
		try {
			Scanner read = new Scanner(new File(args[0]));
			String file = "";
			while (read.hasNextLine()) { file += read.nextLine() + "\n"; }
			System.out.println(removeComments(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
