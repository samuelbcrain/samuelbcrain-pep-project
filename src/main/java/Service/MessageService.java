package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService () {
        messageDAO = new MessageDAO();
    }

    /**
     * Checks message info validity, if ok gets DAO to insert in 'message' table.
     * 
     * @param message - Message object with info to insert
     * @return - Message object representing new row
     */
    public Message addMessage (Message message) {
        String message_text = message.getMessage_text();
        int userID = message.getPosted_by();

        AccountService accountService = new AccountService();

        // check if message info ok
        if (!message_text.equals("") &&
        message_text.length() <= 255 &&
        accountService.getAccountByUserID(userID) != null) {
            // if ok get DAO to insert, return new Message
            return messageDAO.insertMessage(message);
        }

        // if message info NOT ok, return null
        return null;
        
    }

    /**
     * Gets all messages in 'message' table.
     * 
     * @return - ArrayList of Messages reprenting all rows in 'message' table.
     */
    public List<Message> getAllMessages () {
        return messageDAO.getAllMessages();
    }

    /**
     * Gets message by id.
     * 
     * @param message_id - id of message to get
     * @return - Message object representing row in 'message' table with given message_id
     */
    public Message getMessageByID (int message_id) {
        return messageDAO.getMessageByID(message_id);
    }

    /**
     * Deletes message by id.
     * 
     * @param message_id - id of message to delete
     * @return - Message object representing row deleted from 'message' table
     */
    public Message deleteMessageByID (int message_id) {
        // get message requested to be deleted
        Message message = getMessageByID(message_id);

        // if message exists, tell DAO to delete
        if (message != null) {
            messageDAO.deleteMessageByID(message_id);
        }

        // return copy from before deletion (if did not exist will return null)
        return message;
    }

    /**
     * Checks if given message has valid message text, and if the id matches a message
     * already in the 'message' table. If so, uses DAO to update existing message with
     * text from given message.
     * 
     * @param messageWithUpdatedInfo - message with info for updating 'message' table
     * @return Message object representing updated 'message' table row
     */
    public Message updateMessageText (Message messageWithUpdatedInfo) {
        String newText = messageWithUpdatedInfo.getMessage_text();

        // check if given message's text is valid
        if (!newText.equals("") && newText.length() <= 255) {

            // if valid, check if row with given message's id exists in 'message' table
            Message oldMessage = getMessageByID(messageWithUpdatedInfo.getMessage_id());

            if (oldMessage != null) {
                // if id in table, update row and return as Message
                return messageDAO.updateMessageText(messageWithUpdatedInfo);
            }
        }

        // if text invalid or id does NOT exist, return null
        return null;
    }

    /**
     * Gets all messages by account ID.
     * 
     * @param account_id - account id to get messages for
     * @return - ArrayList of Message objects representing all rows in 'message' table
     * matching given account id
     */
    public List<Message> getAllMessagesByAccountID (int account_id) {
        return messageDAO.getAllMessagesByAccountID(account_id);
    }

}
