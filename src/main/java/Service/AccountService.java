package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService () {
        accountDAO = new AccountDAO();
    }

    /**
     * Checks account info validity, gets DAO to insert if ok.
     * 
     * @param account - account info to add/insert
     * @return - Account object representing new row created for info
     */
    public Account addAccount (Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        // check if info valid, if so insert with DAO and return new account
        if (!username.equals("") &&
        password.length() >= 4 &&
        accountDAO.getAccountByUsername(username) == null) {
            return accountDAO.insertAccount(account);
        }

        // return null if info not valid
        return null;
    }

    /**
     * Gets account by username.
     * 
     * @param username - username of account to get
     * @return - Account object representing row in 'account' table with matching username
     */
    public Account getAccountByUsername (String username) {
        return accountDAO.getAccountByUsername(username);
    }

    /**
     * Gets account by user id.
     * 
     * @param userID - user id of account to get
     * @return - Account object representing row in 'account' table with matching user id
     */
    public Account getAccountByUserID (int userID) {
        return accountDAO.getAccountByUserID(userID);
    }

    /**
     * Gets account by username and password. If there is no account with the given username,
     * or the password does not match with it, returns null.
     * 
     * @param username - username of account to get
     * @param password - password to check match
     * @return - Account object for row in 'account' table with given username and password
     */
    public Account getAccountByUsernameAndPassword (String username, String password) {
        // get account by username
        Account result = getAccountByUsername(username);

        // if correct password, return result
        if (result != null && result.getPassword().equals(password)) {
            return result;
        }

        // if incorrect password, return null
        return null;
    }

}
