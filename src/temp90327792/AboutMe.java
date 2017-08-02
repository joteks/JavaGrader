

import java.util.Scanner;
public class AboutMe {
  
    
  
  public String myName(){
	return "checkcheckchecking";
  }ing mySchool() {
    return "pur\'due";
  }
  public int myAge() {
    return 20;
  }
   public static void main(String[] args){
     System.out.println("Hel\"//lo!");
     
     AboutMe t = new AboutMe();
     Scanner read = new Scanner(System.in);
     System.out.println("helooo");
     System.out.println("My name is " + t.myName() + ", and I attend " + t.mySchool() +  ". I am " + t.myAge() + " years old." );
     System.out.println('/');
   }
}
