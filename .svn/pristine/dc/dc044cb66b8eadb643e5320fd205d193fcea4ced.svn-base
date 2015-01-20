package DAO;

import play.*;
import entity.*;

import java.util.*;
import java.sql.*;

/**
 *
 * @author 
 */
/**
 * This manager is responsible to accounts and provides methods for accounts
 * operations.
 */
public class AccountManager {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static ArrayList<Account> accounts;

    public AccountManager() {
    }

    /**
     * This method populates all accounts from given ArrayList of accounts to the
     * account table inside the database.
     *
     * @param accounts a given ArrayList of account objects
     * @return true if all accounts are successfully populated, else return
     * false
     */
    public static boolean populateAccountDB(String accountId, String password, String name, String email, String accountType) {
        String sql = "insert into account values(?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, accountId);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, email);
            ps.setString(5, accountType);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
    
    public static boolean deleteAccount(String accountId){
    	String sql = "delete from account where accountId = ?";
    	Connection conn = null;
    	PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, accountId);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
        	return false;
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }	
    }
    
    public static boolean updateAccount(String accountId, String name, String password, String email, String accountType){
    	String sql = "update account set name = ?, emailaddress = ?, password = ?, accountType = ? where accountId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, accountType);
            ps.setString(5, accountId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
	
	/**
     * This method update the password to the particular user from
     * account table inside the database.
     *
     * @param accounts a boolean value of true or false
     * @return true if update successfully, else return false
     */
    public static boolean resetPassword(String accountId, String password) {
        String sql = "update account set password = ? where accountId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, password);
            ps.setString(2, accountId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }

    /**
     * This method retrieves all accounts from the account table inside the
     * database.
     *
     * @return an ArrayList of account
     */
    public static ArrayList<Account> retrieveAll() {
        accounts = new ArrayList<Account>();
        String sql = "select * from account order by accountId";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Account account;
            while (rs.next()) {
                String accountId = rs.getString(1);
                String password = rs.getString(2);
                String name = rs.getString(3);
                String emailAddress = rs.getString(4);
                String accountType = rs.getString(5);
                account = new Account(accountId, password, name, emailAddress, accountType);
                accounts.add(account);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return accounts;
    }

    //This method retrieves a specific account with given userid.
    //called by createProject in ProjectManager
    public static Account retrieveAccountByID(String accountId) {
        String sql = "select * from account where accountId = ?";
        Account account = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, accountId);
            rs = ps.executeQuery();

            while (rs.next()) {
                String accountIdCurrent = rs.getString(1);
                String password = rs.getString(2);
                String name = rs.getString(3);
                String emailAddress = rs.getString(4);
                String accountType = rs.getString(5);
                account = new Account(accountIdCurrent, password, name, emailAddress, accountType);
            	break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return account;
    }
	
	/**
     * This method retrieves a specific account with given email address.
     *
     * @param email the given email address of an account
     * @return an account object who has the given email address
     */
    public static Account retrieveAccountByEmail(String email) {
        String sql = "select * from account where emailAddress = ?";
        Account account = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            while (rs.next()) {
                String accountId = rs.getString(1);
                String password = rs.getString(2);
                String name = rs.getString(3);
                String emailAddress = rs.getString(4);
                String accountType = rs.getString(5);
                account = new Account(accountId, password, name, emailAddress, accountType);
                break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return account;
    }

    /**
     * This method retrieves all accounts with the given account type from the account table inside the
     * database.
     *
     * @return an ArrayList of account
     */
    public static ArrayList<Account> retrieveAccountsByType(String type) {
        accounts = new ArrayList<Account>();
        String sql = "select * from account where accountType = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, type);
			rs = ps.executeQuery();
            Account account;
            while (rs.next()) {
                String accountId = rs.getString(1);
                String password = rs.getString(2);
                String name = rs.getString(3);
                String emailAddress = rs.getString(4);
                String accountType = rs.getString(5);
                account = new Account(accountId, password, name, emailAddress, accountType);
                accounts.add(account);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return accounts;
    }
    
    public static String generatePassword(){
    	char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ1234567890".toCharArray();
    	StringBuilder sb = new StringBuilder();
    	Random random = new Random();
    	for (int i = 0; i < 8; i++) {
    	    char c = chars[random.nextInt(chars.length)];
    	    sb.append(c);
    	}
    	String password = sb.toString();
    	return password;
    }
    
    public static String retrieveEmailByID(String accountId) {
        String sql = "select emailaddress from account where accountId = ?";
        Account account = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, accountId);
            rs = ps.executeQuery();

            while (rs.next()) {
                email = rs.getString(1);
            	break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return email;
    }
}
