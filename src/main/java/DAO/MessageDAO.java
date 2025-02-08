package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /**
     * Inserts message into 'message' table.
     * 
     * @param message - Message object with info to insert
     * @return - Message object representing new row
     */
    public Message insertMessage (Message message) {
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to add new message:
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) " +
            "VALUES (?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //set prepared statement values
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            //execute prepared statement
            ps.executeUpdate();

            // return new message added to database
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = pkeyResultSet.getInt("message_id");
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),
                message.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if message not added
        return null;
    }

    /**
     * Gets all messages in 'message' table.
     * 
     * @return - ArrayList of Message objects representing all rows in 'message' table.
     */
    public List<Message> getAllMessages () {
        Message message;
        List<Message> allMessages = new ArrayList<>();
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to get all messages:
            String sql = "SELECT * FROM message;";
            
            //execute statement
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // get all messages from 'message' table and add to list
            while (resultSet.next()) {
                message = new Message(resultSet.getInt("message_id"),
                resultSet.getInt("posted_by"), resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch"));
                allMessages.add(message);
            }

            // return all messages
            return allMessages;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if fail to get messages
        return null;
    }

    /**
     * Gets message with given message id.
     * 
     * @param message_id - id of message to get
     * @return - Message object representing row in 'message' table with given message id
     */
    public Message getMessageByID (int message_id) {
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to get message by ID:
            String sql = "SELECT * FROM message WHERE message_id=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            //set prepared statement values
            ps.setInt(1, message_id);

            //execute prepared statement
            ResultSet resultSet = ps.executeQuery();

            // return message retrieved from database
            if (resultSet.next()) {
                return new Message(message_id, resultSet.getInt("posted_by"),
                resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if message not retrieved
        return null;
    }

    /**
     * Deletes row in 'message' table with given message id.
     * 
     * @param message_id - id of message to delete
     */
    public void deleteMessageByID (int message_id) {
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to delete message by ID:
            String sql = "DELETE FROM message WHERE message_id=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            //set prepared statement values
            ps.setInt(1, message_id);

            //execute prepared statement
            ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates text of message with same id as given message.
     * Replaces text of that message with given message's text.
     * 
     * @param messageWithUpdatedText - Message object with new text and id of message to update
     * @return - Message object representing updated row
     */
    public Message updateMessageText (Message messageWithUpdatedText) {
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to update message text:
            String sql = "UPDATE message SET message_text=? WHERE message_id=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            //set prepared statement values
            ps.setString(1, messageWithUpdatedText.getMessage_text());
            ps.setInt(2, messageWithUpdatedText.getMessage_id());

            //execute prepared statement
            int numRowsAffected = ps.executeUpdate();

            // return updated message
            if (numRowsAffected == 1) {
                return getMessageByID(messageWithUpdatedText.getMessage_id());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if message not updated correctly
        return null;
    }

    /**
     * Gets all messages by given account id.
     * 
     * @param account_id - account id to get messages for
     * @return - ArrayList of Message objects representing all rows in 'message' table
     * matching given account id
     */
    public List<Message> getAllMessagesByAccountID (int account_id) {
        Message message;
        List<Message> allMessages = new ArrayList<>();
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to get all messages:
            String sql = "SELECT * FROM message WHERE posted_by=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            // set prepared statement values
            ps.setInt(1, account_id);
            
            //execute prepared statement
            ResultSet resultSet = ps.executeQuery();

            // get all messages by given account from db and add to list
            while (resultSet.next()) {
                message = new Message(resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch"));
                allMessages.add(message);
            }

            // return all messages
            return allMessages;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if fail to get messages
        return null;
    }

}
