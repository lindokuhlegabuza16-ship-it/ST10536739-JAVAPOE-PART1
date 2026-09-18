package login;

import java.util.Scanner;

public class Login {

     String firstName;
         String lastName;
         String username;
         String cellphoneNumber;
         String password;
         boolean registered;
     
     public static void main(String[] args){
         
         Scanner input=new Scanner(System.in);
         
         String firstName;
         String lastName;
         String username;
         String cellphoneNumber;
         String password = null;
         boolean registered;
         
         System.out.println("Please enter your name");
         firstName=input.nextLine();
         System.out.println("Please enter Your Last Name");
         lastName=input.nextLine();
         
         Login login = new Login(firstName, lastName);
         
         System.out.println("Please enter a username");
         username=input.nextLine();
         
         System.out.println("Please enter your password");
         password=input.nextLine();
         
         System.out.println("Please enter your cellphone number");
         cellphoneNumber=input.nextLine();
         
         String registrationResult = login.registerUser(username, password, cellphoneNumber, firstName, lastName);
         
         System.out.println(registrationResult);
         
         if(registrationResult.equals("Registration successful.")){
             
             System.out.println("Please enter your username to login");
             String loginUsername = input.nextLine();
             
             System.out.println("Please enter your password to login");
             String loginPassword = input.nextLine();
             
               System.out.println(
                login.returnLoginStatus(loginUsername, loginPassword)
        );
         }
         
         input.close();
     }
        
      public Login(String firstname, String lastName) {
          
        this.firstName = firstName == null? "" : firstname;
        this.lastName = lastName == null? "" : lastName;
        this.registered = false;
    }

    
    public boolean checkUserName(String username) {
        return username != null
                && username.length()<=5
                && username.contains("_");
    }
    
    //=====================================
    // CHECK PASSWORD COMPLEXITY
    //=====================================
    public boolean checkPasswordComplexity(String password){
        if(password ==null || password.length()<8){
            return false;
        }
        
        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecialCharacter = password.matches(".*[^A-Za-z0-9].*");
        
        return hasCapital && hasNumber && hasSpecialCharacter;
    }
    
    public boolean checkCellPhoneNumber(String cellphoneNumber){
        return cellphoneNumber !=null
                && cellphoneNumber.matches("^\\+27[0-9]{9}$");
    }
    
    public String registerUser(String username, String password, String cellphoneNumber, String firstName, String lastName){
        
        if(!checkUserName(username)){
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
            }
        if(!checkPasswordComplexity(password)){
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
            }
        if(!checkCellPhoneNumber(cellphoneNumber)){
           return "Cell phone number incorrectly formatted or does not contain international code.";
           }
        
        this.username = username;
        this.password = password;
        this.cellphoneNumber = cellphoneNumber;
        this.firstName = firstName == null ? "" : firstName;
        this.lastName = lastName == null ? "" : lastName;
        this.registered = true;
        
        return "Registration successful.";
    }
    
    public boolean loginUser(String username, String password){
        return registered
                && this.username != null
                && this.password != null
                && this.username.equals(username)
                && this.password.equals(password);
    }
    
    public String returnLoginStatus(String username, String password){
        
        if(loginUser(username, password)){
            return "Welcome "+ firstName+" "+ lastName+ " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
    
}



