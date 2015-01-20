package DAO;

import play.*;

import entity.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author Yining
 */
/**
 * This manager is responsible to accounts and provides methods for accounts
 * operations.
 */
public class AccountURLManager {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static ArrayList<Account> accounts;

    public AccountURLManager() {
    }

    /**
     * This method populates all accounts from given ArrayList of accounts to the
     * account table inside the database.
     *
     * @param accounts a given ArrayList of account objects
     * @return true if all accounts are successfully populated, else return
     * false
     */
    public static boolean populateAccountURLDB(String url, String accountId, String generateTime) {
        String sql = "insert into account_url values(?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, url);
            ps.setString(2, accountId);
            ps.setString(3, generateTime);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }

	/**
     * This method retrieves the account id with given url
     * from account_url database
     * @param url the given url of a "reset-password" account
     * @return a String object of accountid who has the given url
     */
    public static String retrieveAccountIdByURL(String currentURL) {
        String sql = "select * from account_URL where url = ?";
        String accountId = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, currentURL);
            rs = ps.executeQuery();
            while (rs.next()) {
                //String url = rs.getString(1);
                accountId = rs.getString(2);
            	break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return accountId;
    }
	
	/**
     * This method retrieves the url_generated time with given url
     * from account_url database
     * @param url the given url of a "reset-password" account
     * @return a Date object of accountid who has the given url
     */
    public static String retrieveTimedByURL(String currentURL) {
        String sql = "select * from account_URL where url = ?";
        String time = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, currentURL);
            rs = ps.executeQuery();
            while (rs.next()) {
                //String url = rs.getString(1);
                //String accountId = rs.getString(2);
		time = rs.getString(3);
            	break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return time;
    }
	
	/**
     * This method deletes the url and account id
     * from account_url database
     * @param url the given url
     */
    public static void removeURL(String currentURL) {
        String sql = "delete from account_url where url = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, currentURL);
            ps.executeUpdate();
        } catch (Exception e) {
			System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    }
}
