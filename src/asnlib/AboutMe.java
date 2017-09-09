

/**
 * Topic 1, Project 1 : AboutMe.java
 * @author AUDREYOTT
 * 
 */
import java.util.Scanner;
public class AboutMe {
  
  public String myName() {
	 return "test1";
  }
  public String mySchool() {
    return "test2";
  }
  public int myAge() {
    return 451;
  }
   public static void main (String[] args) { 
     AboutMe am = new AboutMe();    
     System.out.println("My name: " + am.myName());
     System.out.println("My school: " + am.mySchool());
     System.out.println("My age: " + am.myAge());
   }
}