import java.util.Scanner;
public class AboutMe {

 
private String myName;
private String mySchool;
private int myAge;
 
 AboutMe(String myName, String mySchool, int myAge){
  this.myName = myName;
  this.mySchool = mySchool;
  this.myAge = myAge;
 }
 
 
 public static void main(String[] args) {
   
  Scanner input = new Scanner(System.in);
  
  System.out.println("Welcome User: Please enter the information that is asked for");
      
  String name = input.nextLine();
  
  String school = input.nextLine();
  
  int age = Integer.parseInt(input.nextLine());
  
  AboutMe about = new AboutMe(name,school,age);
      
  System.out.println("My name is " + about.myName() + ", and I attend " + about.mySchool() + ". I am " + about.myAge()   + " years old.");
  
  input.close();

 }
 
 String myName(){
     return myName;
   }
   
   
   String mySchool(){
     return mySchool;
   }
   
   int myAge(){
     return myAge;
     
   }

}