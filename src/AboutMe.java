public class AboutMe {
   
  public String myName() { //method 1 name
    return "Tyson Freeze";
  }
  
  public String mySchool() { //method 2 school
    return "Ocean View High School";
  }
  
  public int myAge() { //method 3 age
    return 16;
  }
  
  public static void main(String[] args){ //execution code
  
    //AboutMe am = new AboutMe(); //declare new am object of class AboutMe
    String name = am.myName(); //string type var myName = return of getName method
    String school = am.mySchool(); //string type var myName = return of getSchool method
    int age = am.myAge(); //int type var myAge = return of getAge function
    System.out.println("My name is "+name+", and I attend "+school+". I am "+age+" years old.");
  }
}