
package userregistrationsystem;

import login.Login;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import login.Message;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 *
 * @author Student
 */
public class LoginTest {
    
    Login login;
    
   
    @BeforeClass
    public static void setUpClass() {
    
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        login =new Login("Kyle", "Smith");
        // Clear arrays before testing
        login.sentMessages.clear();
        login.disregardedMessages.clear();
        login.storedMessages.clear();
        login.messageIDs.clear();
        login.messageHashes.clear();
    }
    
    @After
    public void tearDown() {
        login = null;
    }
    
    
   @Test
   public void testCheckUserName(){
           assertTrue("Valid username should return true",login.checkUserName("kyl_1"));
           assertFalse("Invalid username should return false",login.checkUserName("Kyle!!!!!!!"));
   }
   
   @Test
   public void testCheckPasswordComplexity(){
       assertTrue("Valid password should return true",login.checkPasswordComplexity("Ch&&sec@ke99!"));
       assertFalse("Invalid password should return false",login.checkPasswordComplexity("password"));
   }

   @Test
   public void testCheckCellPhoneNumber(){
       assertTrue("Valid phone should return true",login.checkCellPhoneNumber("+27838968976"));
       assertFalse("Invalid phone should return false",login.checkCellPhoneNumber("08966553"));
   }
   
   @Test
   public void testRegisterUser(){
       String success = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
       
       assertEquals("Registration successful.", success);
       
       String invalidUser = login.registerUser("Kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
       
       assertEquals("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.", invalidUser);
       
       String invalidPass = login.registerUser("Kyl_1", "password", "+27838968976", "Kyle", "Smith");
       
       assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter,"
               + " a number, and a special character.", invalidPass);
       
       String invalidCell = login.registerUser("kyl_1", "Ch&&sec@ke99!","08966553", "Kyle", "Smith");
        
       assertEquals("Cell phone number incorrectly formatted or does not contain international code.", invalidCell); 

   }
   
   @Test public void testLoginUser(){
      
       login.registerUser("Kyl_1","Ch&&sec@ke99!","+27838968976","Kyle","Smith");
       
       assertTrue("Correct credentials should return true",login.loginUser("Kyl_1","Ch&&sec@ke99!"));
       
       assertFalse("Incorrect credentials should return false",login.loginUser("wrong","wrongpass"));
   }
   
   @Test public void testReturnLoginStatus(){
       
       login.registerUser("Kyl_1","Ch&&sec@ke99!","+27838968976","Kyle","Smith");
       
       String success = login.returnLoginStatus("Kyl_1","Ch&&sec@ke99!");
       
       assertEquals("Welcome Kyle Smith it is great to see you again.", success);
       
       String fail = login.returnLoginStatus("wrong","wrongpass");
       
       assertEquals("Username or password incorrect, please try again.", fail);
   }
   
      @Test
   public void testSentMessagesArrayPopulated() {
       // Manually create messages and set their flags to mock user actions
       Message m1 = new Message(1, "+27834557896", "Did you get the cake?");
       m1.sentMessage(1); // Flag as Sent
       
       Message m2 = new Message(4, "0838884567", "It is dinner time !");
       m2.sentMessage(1);
       
       // Add to the array as the main program would
       if (m1.getFlag().equals("Sent")) login.sentMessages.add(m1);
       if (m2.getFlag().equals("Sent")) login.sentMessages.add(m2);
       
       // Test if the array contains the expected data
       assertEquals(2, Login.sentMessages.size());
       assertEquals("Did you get the cake?", Login.sentMessages.get(0).getMessageText());
       assertEquals("It is dinner time !", Login.sentMessages.get(1).getMessageText());
   }
}
