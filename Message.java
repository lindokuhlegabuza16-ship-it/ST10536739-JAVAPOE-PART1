package login;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Message {
   
    String messageID;
    int messageNumber;
    String messageText;
    String messageHash;
    String recipient;
    String flag;
    
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.flag = "Pending";

    }
    
    public Message(String messageID, int messageNumber, String recipient, String messageText, String messageHash) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.flag = "Stored";
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.length() <= 12 && recipient.startsWith("+")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) return "";
        
        String firstTwoID = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        
        if (firstWord.isEmpty()) firstWord = words[0];
        if (lastWord.isEmpty()) lastWord = words[words.length - 1];

        return (firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                this.flag = "Sent";
                return "Message successfully sent.";
            case 2:
                this.flag = "Disregard";
                return "Press 0 to delete the message.";
            case 3:
                this.flag = "Stored";
                storeMessage(); 
                return "Message successfully stored.";
            default:
                return "Invalid selection.";
        }
    }

    public String printMessages() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }
    
    // NEW
    public String getReportFormat() {
        return "Message Hash: " + messageHash + "\n" + 
                "Recipient: " + recipient + "\n" + 
                "Message: " + messageText + "\n" + 
                "---------------------------------------"; 
    }

    public void storeMessage() {
        String safeMessage = messageText.replace("\"", "\\\"");
        String json = "{\n" +
                "  \"messageID\": \"" + messageID + "\",\n" +
                "  \"messageNumber\": " + messageNumber + ",\n" +
                "  \"recipient\": \"" + recipient + "\",\n" +
                "  \"message\": \"" + safeMessage + "\",\n" +
                "  \"messageHash\": \"" + messageHash + "\"\n" +
                "}\n";

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json);
            file.write("----------------------------------------\n");
        } catch (IOException e) {
            System.out.println("Error storing message to JSON file.");
        }
    }
    
    // NEW: Method to read JSON file and return an ArrayList of Message objects
    public static ArrayList<Message> readStoredMessagesFromJSON() {
        ArrayList<Message> storedMessages = new ArrayList<>();
        try {    
            String content = new String(Files.readAllBytes(Paths.get("Messages.jason")));
            String[] jsonObjects = content.split("----------------------------------------");
            
            for (String obj : jsonObjects) {
                if (obj.trim().isEmpty()) continue;
                
                String id = extractJsonValue(obj, "messageID");
                String numStr = extractJsonValue(obj, "messageNumber");
                String rec = extractJsonValue(obj, "recipient");
                String msg = extractJsonValue(obj, "message");
                String hash = extractJsonValue(obj, "messageHash");
                
                int num = numStr.isEmpty() ? 0 : Integer.parseInt(numStr);
                
                storedMessages.add(new Message(id, num, rec, msg, hash));
            }            
        } catch (Exception e) {
            // File doesn't exist yet or error
        }
        return storedMessages;
    }
    
    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\": \"";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            // check without space after colon (for integers)
            searchKey = "\"" + key + "\": ";
            start = json.indexOf(searchKey);
            if (start == -1) return "";
            start += searchKey.length();
            int end = json.indexOf("\n", start);
            return json.substring(start, end).replace(",", "").trim();
        }
        start += searchKey.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }  
    
    
    // Getters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText;}
    public String getFlag() { return flag; }
}

