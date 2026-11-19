package login;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DisplayMessages {

    private final String jsonFile;

    // Changed from fixed-size arrays to Lists so we can add elements dynamically
    private List<String> sentMessages = new ArrayList<>();
    private List<String> disregardedMessages = new ArrayList<>();
    private List<String> storedMessages = new ArrayList<>();
    private List<String> messageHashes = new ArrayList<>();
    private List<String> messageIDs = new ArrayList<>();
    
    // Parallel arrays for searching
    private List<String> recipients = new ArrayList<>();
    private List<String> senders = new ArrayList<>();
    private List<String> statuses = new ArrayList<>();

    // Internal records retain the complete message details
    private final List<MessageRecord> messages = new ArrayList<>();

    // Default constructor
    public DisplayMessages() {
        this("messages.json");
    }

    // Parameterized constructor
    public DisplayMessages(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    // --- INNER CLASS DEFINITION ---
    // This must be defined outside the constructor
    public static class MessageRecord {
        String id;
        String hash;
        String recipient;
        String text;
        String status;
        String sender;

        public MessageRecord(String id, String hash, String recipient, String text, String status, String sender) {
            this.id = id;
            this.hash = hash;
            this.recipient = recipient;
            this.text = text;
            this.status = status;
            this.sender = sender;
        }
    }

    /*
     * Call this method whenever a message is created.
     * Status should be "Sent", "Stored", or "Disregard".
     */
    public void addMessage(String id, String hash, String recipient, String text, String status) {
        // Add to the master list
        messages.add(new MessageRecord(id, hash, recipient, text, status, "Developer"));

        // Update all the parallel arrays
        populateArrays();

        // If the message is stored, save it to the JSON file
        if ("Stored".equalsIgnoreCase(status)) {
            saveDisplayMessagesToJSON();
        }
    }

    // Populate all required arrays from actual message records.
    public final void populateArrays() {
        // Clear existing lists to prevent duplicates when repopulating
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipients.clear();
        senders.clear();
        statuses.clear();

        for (MessageRecord message : messages) {
            // Populate the general arrays
            messageHashes.add(message.hash);
            messageIDs.add(message.id);
            recipients.add(message.recipient);
            senders.add(message.sender);
            statuses.add(message.status);

            // Categorize by status
            if ("Sent".equalsIgnoreCase(message.status)) {
                sentMessages.add(message.text);
            } else if ("Disregard".equalsIgnoreCase(message.status) || "Disregarded".equalsIgnoreCase(message.status)) {
                disregardedMessages.add(message.text);
            } else if ("Stored".equalsIgnoreCase(message.status)) {
                storedMessages.add(message.text);
            }
        }
    }

    // Helper method to save stored messages to JSON
    private void saveDisplayMessagesToJSON() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(jsonFile, true))) {
            // Get the most recently added stored message
            MessageRecord latest = messages.get(messages.size() - 1);
            
            // Format as JSON and write
            String json = "{\n" +
                    "  \"messageID\": \"" + latest.id + "\",\n" +
                    "  \"messageHash\": \"" + latest.hash + "\",\n" +
                    "  \"recipient\": \"" + latest.recipient + "\",\n" +
                    "  \"message\": \"" + latest.text + "\",\n" +
                    "  \"status\": \"" + latest.status + "\"\n" +
                    "}\n";
            
            writer.println(json);
            writer.println("----------------------------------------");
            
        } catch (IOException e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
        }
    }

    // Optional: Add a getter method to access the lists if needed by your Login class
    public List<String> getSentMessages() { return sentMessages; }
    public List<String> getStoredMessages() { return storedMessages; }
    public List<String> getDisregardedMessages() { return disregardedMessages; }
    public List<MessageRecord> getAllMessages() { return messages; }
}