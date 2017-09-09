

/**
 * Topic 1, Project 1 : AboutMe.java
 * @author AUDREYOTT
 * 
 */
import java.util.Scanner;
public class AboutMe {
  
  public String myName() {
	 return "berkel";
  }
  public String mySchool() {
    return "hello";
  }
  public int myAge() {
    return 0;
  }
   public static void main (String[] args) {
     System.out.println("Hello!");
     
     AboutMe t = new AboutMe();
     
     
     System.out.println("My name is " + t.myName() + ", and I attend " + t.mySchool() +  ". I am " + t.myAge() + " years old." );
     System.out.println("Thank you!");
   }
}