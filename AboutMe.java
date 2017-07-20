/**
 * Topic 1, Project 1 : AboutMe.java
 * @author AUDREYOTT
 * 
 */
import java.util.Scanner;
public class AboutMe {
  
    
  
  public String myName(){
	return "robyn";
  }
  public String mySchool() {
    return "purdue";
  }
  public int myAge() {
    return 20;
  }
   public static void main(String[] args){
     System.out.println("Hello!");
     
     AboutMe t = new AboutMe();
     Scanner read = new Scanner(System.in);
     System.out.println(read.nextLine());
     
     System.out.println("My name is " + t.myName() + ", and I attend " + t.mySchool() +  ". I am " + t.myAge() + " years old." );
     System.out.println("Thank you!");
   }
}
     
     

   
     
                                               
                        

                       
                        
  


     
  
                        
