package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.*;

public class AccountDAO {
    
    /**
     * Inserts info from an Account object as a row into the 'account' table.
     * 
     * @param account - Account object with info to insert
     * @return - Account object representing new row (null if not added)
     */
    public Account insertAccount (Account account) {
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to add new user account:
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //set prepared statement values
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            //execute prepared statement
            ps.executeUpdate();

            // return new account added to database
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if account not added
        return null;
    }

    /**
     * Gets Account object representing row with given username.
     * 
     * @param username - username of account to get
     * @return - Account object of matching row (null if no match)
     */
    public Account getAccountByUsername (String username) {
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to search for user with given username
            String sql = "SELECT * FROM account WHERE username=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            //set prepared statement values
            ps.setString(1, username);

            //execute prepared statement
            ResultSet resultSet = ps.executeQuery();

            // return account with given username
            if(resultSet.next()){
                int account_id = resultSet.getInt("account_id");// was a getLong casted to int
                String password = resultSet.getString("password");
                return new Account(account_id, username, password);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if no such account in database
        return null;
    }

    /**
     * Gets Account object representing row with given user id.
     * 
     * @param userID - user id of account to get
     * @return - Account object of matching row (null if no match)
     */
    public Account getAccountByUserID (int userID) {
        Connection con = ConnectionUtil.getConnection();
        try {
            // sql to search for user with given userID
            String sql = "SELECT * FROM account WHERE account_id=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            //set prepared statement values
            ps.setInt(1, userID);

            //execute prepared statement
            ResultSet resultSet = ps.executeQuery();

            // return account with given userID
            if(resultSet.next()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                return new Account(userID, username, password);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if no such account in database
        return null;
    }

}
