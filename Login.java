package login;


import java.util.Scanner;
import java.util.ArrayList;


public class Login {

     String firstName;
     String lastName;
     String username;
     String cellphoneNumber;
     String password;
     boolean registered;
     
    // PART 3 ARRAYS
    public static ArrayList<Message> sentMessages = new ArrayList<>();
    public static ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static ArrayList<Message> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();     
      
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

     String firstName;
     String lastName;
     String username;
     String cellphoneNumber;
     String password = null;

        System.out.println("Please enter your name");
        firstName = input.nextLine();
        System.out.println("Please enter Your Last Name");
        lastName = input.nextLine();

        Login login = new Login(firstName, lastName);

        System.out.println("Please enter a username");
        username = input.nextLine();

        System.out.println("Please enter your password");
        password = input.nextLine();

        System.out.println("Please enter your cellphone number");
        cellphoneNumber = input.nextLine();

        String registrationResult = login.registerUser(username, password, cellphoneNumber, firstName, lastName);
        System.out.println(registrationResult);

        if (registrationResult.equals("Registration successful.")) {
            System.out.println("Please enter your username to login");
            String loginUsername = input.nextLine();

            System.out.println("Please enter your password to login");
            String loginPassword = input.nextLine();

            //Stored the login status in a variable
            String loginStatus = login.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginStatus);

            if (loginStatus.startsWith("Welcome")) {
                System.out.println("\nWelcome to QuickChat.");
                
                System.out.println("How many messages do you wish to enter?");
                int numMessages = 0;
                try {
                    numMessages = Integer.parseInt(input.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Exiting QuickChat.");
                    input.close();
                    return;
                }

                ArrayList<Message> sentMessages = new ArrayList<>();
                boolean running = true;

                while (running) {
                    
                    System.out.println("\n============================");
                    System.out.println(" MAIN MENU ");
                    System.out.println("\n============================");
                    System.out.println("\nPlease choose an option:");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.println("4) Stored Messages");
                    
                    String menuChoice = input.nextLine();

                    switch (menuChoice) {
                        case "1":
                            for (int i = 1; i <= numMessages; i++) {
                                System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
                                
                                String recipient;
                                while (true) {
                                    System.out.println("Enter recipient cell number (must contain international code, e.g., +27...):");
                                    recipient = input.nextLine();
                                    Message tempCheck = new Message(i, recipient, "test");
                                    String cellCheck = tempCheck.checkRecipientCell();
                                    if (cellCheck.equals("Cell phone number successfully captured.")) {
                                        break;
                                    } else {
                                        System.out.println(cellCheck);
                                    }
                                }

                                String msgText;
                                while (true) {
                                    System.out.println("Enter your message (max 250 characters):");
                                    msgText = input.nextLine();
                                    if (msgText.length() <= 250) {
                                        System.out.println("Message ready to send.");
                                        break;
                                    } else {
                                        int excess = msgText.length() - 250;
                                        System.out.println("Message exceeds 250 characters by " + excess + "; please reduce the size.");
                                    }
                                }

                                Message newMessage = new Message(i, recipient, msgText);

                                System.out.println("Choose an action:");
                                System.out.println("1) Send Message");
                                System.out.println("2) Disregard Message");
                                System.out.println("3) Store Message to send later");
                                
                                String actionChoice = input.nextLine();
                                int actionInt = 0;
                                try {
                                    actionInt = Integer.parseInt(actionChoice);
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid action. Disregarding.");
                                }

                                String actionResult = newMessage.sentMessage(actionInt);
                                System.out.println(actionResult);

                                if (actionInt == 1) {
                                    sentMessages.add(newMessage);
                                    System.out.println("\n--- Message Details ---");
                                    System.out.println(newMessage.printMessages());
                                } else if (actionInt == 2) {
                                    System.out.println("Press 0 to delete the message");
                                    input.nextLine(); 
                                } else if (actionInt == 3) {
                                    storedMessages.add(newMessage);
                                }
                                
                                // Stored the ID and Hash
                                messageIDs.add(newMessage.getMessageID());
                                messageHashes.add(newMessage.getMessageHash());
                            }
                            break;
                            
                        case "2":
                            System.out.println("Coming Soon.");
                            break;
                            
                        case "3":
                            running = false;
                            System.out.println("Exiting QuickChat. Goodbye!");
                            break;
                            
                        case "4":
                            handleStoredMessagesMenu(input);
                            break;
                            
                        default:
                            System.out.println("Invalid option. Please choose 1, 2, or 3.");
                    }
                }
                
                System.out.println("\nTotal messages successfully sent: " + sentMessages.size());
            }
        }

        input.close();
    }
    
    // Part 3: Stored Messages Menu Logic
    private static void handleStoredMessagesMenu(Scanner input) {
        // Load fromJSON just in case there are messages saved from a previous run
        ArrayList<Message> allStored = Message.readStoredMessagesFromJSON();
        //merge the file messages with current session stored messages (avoiding duplicates if possible
        
        for(Message m : storedMessages) {
            boolean exists = false;
            for(Message f : allStored) {
                if(f.getMessageID().equals(m.getMessageID())) { exists = true; break; }
                
            }
            if(!exists) allStored.add(m);
        }
        
        if (allStored.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        
        boolean subMenu = true;
        while (subMenu) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search messages for a particular recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display full report");
            System.out.println("x) Back to Main Menu");
            
            String choice = input.nextLine().toLowerCase();
             
            switch (choice) {
                case "a" :
                    for (Message m : allStored) {
                    System.out.println("Sender: Developer | Recipient: " + m.getRecipient());
                    }
                    break;
                case "b" :
                    Message longest = allStored.get(0);
                    for (Message m : allStored) {
                        if (m.getMessageText().length() > longest.getMessageText().length()) {
                            longest = m;
                        }
                    }
                    
                    System.out.println("Longest Message: " + longest.getMessageText());
                    break;
                    
                case "c" :
                    System.out.println("Enter Message ID to search:");
                    String searchId = input.nextLine();
                    boolean foundId = false;
                    for (Message m : allStored) {
                        if (m.getMessageID().equals(searchId)) {
                            System.out.println("Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessageText());
                            foundId = true;
                            break;
                        }
                    }
                    
                    if (!foundId) System.out.println("Message ID not found.");
                    break;
                    
                case "d" :
                    System.out.println("Enter recipient number to search:");
                    String searchRec = input.nextLine();
                    boolean foundRec = false;
                    for (Message m : allStored) {
                        if (m.getRecipient().equals(searchRec)) {
                            System.out.println(m.getMessageText());
                            foundRec = true;
                        }
                    }
                    
                    if (!foundRec) System.out.println("No messages found for this recipient.");
                    break;
                    
                case "e" :
                    System.out.println("Enter Message Hash to delete:");
                    String searchHash = input.nextLine();
                    boolean deleted = false;
                    for (int i = 0; i < allStored.size(); i++) {
                        if (allStored.get(i).getMessageHash().equals(searchHash)) {
                            System.out.println("Message: \"" + allStored.get(i).getMessageText() + "\" sucessfully deleted.");
                            allStored.remove(i);
                            deleted = true;
                            break;
                        }
                    }
                    
                    if (!deleted) System.out.println("Message hash not found.");
                    break;
                    
                case "f" :
                    System.out.println("\n--- Full Stored Messages Report ---");
                    for (Message m: allStored) {
                        System.out.println(m.getReportFormat());
                    }
                    break;
                    
                case "x" :
                    subMenu = false;
                    break;
                default :
                    System.out.println("Invalid choice.");
            }
        }
    }

    public Login(String firstName, String lastName) {
        this.firstName = firstName == null ? "" : firstName;
        this.lastName = lastName == null ? "" : lastName;
        this.registered = false;
    }

    public boolean checkUserName(String username) {
        return username != null && username.length() <= 5 && username.contains("_");
    }

    //==========================================
    //         CheckPasswordComplexity          
    //==========================================
    
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecialCharacter = password.matches(".*[^A-Za-z0-9].*");
        return hasCapital && hasNumber && hasSpecialCharacter;
    }

    public boolean checkCellPhoneNumber(String cellphoneNumber) {
        return cellphoneNumber != null && cellphoneNumber.matches("^\\+27[0-9]{9}$");
    }

    public String registerUser(String username, String password, String cellphoneNumber, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellphoneNumber)) {
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

    public boolean loginUser(String username, String password) {
        return registered && this.username != null && this.password != null
                && this.username.equals(username) && this.password.equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
    
}
