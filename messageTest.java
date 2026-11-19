package userregistrationsystem;

import login.Message;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class messageTest {
    
    Message message;
    ArrayList<Message> testStoredMessages;
    
    public messageTest() {
        
    }
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    @Before
    public void setUp() {
        message = new Message(1, "+27677428312", "Hi Mike, can you join us for dinner tonight?");
        
        testStoredMessages = new ArrayList<>();
        
        // Populate array with the exact test data from the assignment
        testStoredMessages.add(new Message("1111111111", 1, "+27834557896", "Did you get the cake?", "11:1:DIDCAKE"));
        testStoredMessages.add(new Message("2222222222", 2, "+27838884567", "Where are you? You are late ! I have asked you to be on time.", "22:2:WHEREON"));
        testStoredMessages.add(new Message("3333333333", 3, "+27834484567", "Yohoooo, I am at your gate.", "33:3:YOHOGATE"));
        testStoredMessages.add(new Message("4444444444", 4, "0838884567", "It is dinner time !", "44:4:IT!"));
        testStoredMessages.add(new Message("5555555555", 5, "+27838884567", "Ok, I am leaving without you.", "55:5:OKYOU"));        
    }
    
    @After
    public void tearDown() {
        message = null;
        testStoredMessages = null;        
    }
    
    @Test
    public void testCheckMessageID() {
        assertTrue("Message ID should be 10 characters or less", message.checkMessageID());
    }
    
    @Test
    public void testCheckRecipientCell() {
        String validNumber = message.checkRecipientCell();
        assertEquals("Cell phone number successfully captured.", validNumber);
        
        Message invalidMsg = new Message(2, "0677428312", "Test");
        String invalidNumber = invalidMsg.checkRecipientCell();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", invalidNumber);
    }
    
    @Test
    public void testCreateMessageHash() {
        String hash = message.getMessageHash();
        String expectedEnd = ":1:HITONIGHT";
        assertTrue("Message hash should end with " + expectedEnd, hash.endsWith(expectedEnd));
        assertEquals(hash.toUpperCase(), hash);
    }
    
    @Test
    public void testSentMessage() {
        assertEquals("Message successfully sent.", message.sentMessage(1));
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }
    
    
    @Test
    public void testDisplayLongestMessage() {
        Message longest = testStoredMessages.get(0);
        for (Message m : testStoredMessages) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
            longest = m;
        }
      }
        
        assertEquals("Where are you? You are late ! I have asked you to be on time.", longest.getMessageText());
    }

    @Test
    public void testSearchForMessageID() {
        String searchId = "4444444444"; // Test Data Message 4
        String foundMessage = "";
        for (Message m : testStoredMessages) {
            if (m.getMessageID().equals(searchId)) {
                foundMessage = m.getMessageText();
                break;
            }
        }
        
        assertEquals("It is dinner time !", foundMessage);
    }
    
    @Test
    public void testSearchMessagesForRecipient() {
        String recipient = "+27838884567"; // Test Data for Message 2 & 5
        ArrayList<String> foundMessages = new ArrayList<>();
        for (Message m : testStoredMessages) {
            if (m.getRecipient().equals(recipient)) {
                foundMessages.add(m.getMessageText());
            }
        }
        
        assertEquals(2, foundMessages.size());
        assertEquals("Where are you? You are late ! I have asked you to be on time.", foundMessages.get(0));
        assertEquals("Ok, I am leaving without you.", foundMessages.get(1));
    }
    
    @Test
    public void testDeletedMessageUsingHash() {
        String hashToDelete = "22:2:WHEREON"; //Test Message 2 hash
        String deletedMessageText = "";
        
        for (int i = 0; i < testStoredMessages.size(); i++) {
            if (testStoredMessages.get(i).getMessageHash().equals(hashToDelete)) {
                deletedMessageText = testStoredMessages.get(i).getMessageText();
                testStoredMessages.remove(i);
                break;
            }
        }
        
        assertEquals("Where are you? You are late ! I have asked you to be on time.", deletedMessageText);
        assertEquals(4, testStoredMessages.size()); // Array size should decrease
    }
    
    @Test
    public void testDisplayReport() {
        // Testing if the format matches the requirements (Hash, Recipient, Message)
        String expectedReportStart = "Message Hash: 11:1:DIDCAKE\nRecipient: +27834557896\nMessage: Did you get the cake?";
        String actualReport = testStoredMessages.get(0).getReportFormat();
        
        assertTrue(actualReport.startsWith(expectedReportStart));
    }    
    
    
}
