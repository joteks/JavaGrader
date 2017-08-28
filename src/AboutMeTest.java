

/**
 * Topic 1, Project 1 : AboutMe.java
 * @author AUDREYOTT
 * 
 */
import java.util.Scanner;
public class AboutMeTest {
  
  public String myName() {
	 return "berkel";
  }
  public String mySchool() {
    return "";
  }
  public int myAge() {
    return 0;
  }
   public static void main (String[] args) {
     System.out.println("Hello!");
     
     AboutMeTest t = new AboutMeTest();
     
     
     System.out.println("My name is " + ", and I attend " + t.mySchool() +  ". I am " + t.myAge() + " years old." );
     System.out.println("Thank you!");
   }
}