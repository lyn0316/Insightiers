package DAO;

import play.*;
import entity.*;

import java.util.*;
import java.sql.*;
import java.text.*;
/**
 *
 * @author Yining
 */
/**
 * This manager is responsible to staffs and provIDes methods for staffs
 * operations.
 */
public class StaffManager {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static ArrayList<Staff> staffList;

    public StaffManager() {
    }

    /**
     * This method populates staff to the staff table
     * insIDe the database.
     *
     * @param staff a given staff objects
     * @return true if the staff is successfully populated, else return
     * false
     */
    public static boolean populateStaffDB(int maxProjectNum, String name, String role, String email, 
    		String memberId, String token) {
        String sql = "insert into staff(maxProjectNum, name, role, email, status, memberid, token) values(?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, maxProjectNum);
            ps.setString(2, name);
            ps.setString(3, role);
            ps.setString(4, email);
            ps.setString(5, "active");
            ps.setString(6, memberId);
            ps.setString(7, token);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
	
    //This method retrieves all staffs from the staff table 
    //called by retrieveStaffByProject in ProjectController
    public static ArrayList<Staff> retrieveAll() {
        staffList = new ArrayList<Staff>();
        String sql = "select * from staff order by staffID";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Staff staff;
            while (rs.next()) {
                int staffID = rs.getInt(1);
                int maxProjectNum = rs.getInt(2);
                String name = rs.getString(3);
                String role = rs.getString(4);
                String email = rs.getString(5);
                String status = rs.getString(6);
                String memberId = rs.getString(7);
                String token = rs.getString(8);
                staff = new Staff(staffID, maxProjectNum, name, role, email, status, memberId, token);
                staffList.add(staff);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return staffList;
    }
    
    //This method deletes all staffs of a given project from the project_staff table inside the
    //called by updateProject in ProjectController
    public static boolean deleteProjectStaffs(String projectId){
    	String sql = "delete from project_staff where projectId = ?";
    	Connection conn = null;
    	PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
        	return false;
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    }
    
    
    /**
     * This method retrieves a specific staff with given staffID.
     *
     * @param staffID the given staffID of a staff
     * @return a staff object who has the given staffID
     */
    public static Staff retrieveStaffByID(int staffID) {
        String sql = "select * from staff where staffID = ?";
        Staff staff = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffID);
            rs = ps.executeQuery();
            while (rs.next()) {
                int staffIDCurrent = rs.getInt(1);
                int maxProjectNum = rs.getInt(2);
                String name = rs.getString(3);
                String role = rs.getString(4);
                String email = rs.getString(5);
                String status = rs.getString(6);
                String memberId = rs.getString(7);
                String token = rs.getString(8);
                staff = new Staff(staffIDCurrent, maxProjectNum, name, role, email, status, memberId, token);
            	break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return staff;
    }
    
    public static int retrieveStaffIDByEmail(String email) {
        String sql = "select staffID from staff where email = ?";
        Staff staff = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int staffID = 0;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                staffID = rs.getInt(1);
                break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return staffID;
    }
    
    /**
     * This method retrieves a specific staff with given memberId.
     *
     * @param staffID the given staffID of a staff
     * @return a staff object who has the given staffID
     */
    public static Staff retrieveStaffByMemberId(String memberID) {
        String sql = "select * from staff where memberId = ?";
        Staff staff = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, memberID);
            rs = ps.executeQuery();
            while (rs.next()) {
                int staffIDCurrent = rs.getInt(1);
                int maxProjectNum = rs.getInt(2);
                String name = rs.getString(3);
                String role = rs.getString(4);
                String email = rs.getString(5);
                String status = rs.getString(6);
                String memberId = rs.getString(7);
                String token = rs.getString(8);
                staff = new Staff(staffIDCurrent, maxProjectNum, name, role, email, status, memberId, token);
            	break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return staff;
    }
    
    public static ArrayList<Staff> retrieveByStatus(String status) {
        staffList = new ArrayList<Staff>();
        String sql = "select * from staff where status = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            Staff staff;
            while (rs.next()) {
                int staffID = rs.getInt(1);
                int maxProjectNum = rs.getInt(2);
                String name = rs.getString(3);
                String role = rs.getString(4);
                String email = rs.getString(5);
                String currentstatus = rs.getString(6);
                String memberId = rs.getString(7);
                String token = rs.getString(8);
                staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
                staffList.add(staff);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return staffList;
    }
   
    //Retrieve a list of PM whose number of ongoing projects in the given period is smaller than max project number
    //this method is called by retrieveAvailablePM in ProjectController 
    public static ArrayList<Staff> retrieveAvailablePM(String startDate, String endDate) {
        String sql = "SELECT * FROM staff WHERE role = 'Project Manager' AND staffId in (SELECT staffId FROM "
        		+ "project_staff WHERE projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? "
        		+ "and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or "
        		+ "(projectStartDate >= ? and projectEndDate <= ?)) GROUP BY staffId)HAVING maxProjectNum > "
        		+ "(SELECT pro_no FROM (SELECT COUNT(*) AS pro_no , staffId AS sd FROM project_staff "
        		+ "WHERE projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? and "
        		+ "projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or "
        		+ "(projectStartDate >= ? and projectEndDate <= ?)) GROUP BY staffId)as temp WHERE staffId = sd)";
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date sdate = sdf.parse(startDate);
        java.util.Date edate = sdf.parse(endDate);
       
        java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
        java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
        
        conn = ConnectionManager.getConnection();
        ps = conn.prepareStatement(sql);
        ps.setDate(1, sqlstartDate);
        ps.setDate(2, sqlstartDate);
        ps.setDate(3, sqlendDate);
        ps.setDate(4, sqlendDate);
        ps.setDate(5, sqlstartDate);
        ps.setDate(6, sqlendDate);
            
        ps.setDate(7, sqlstartDate);
        ps.setDate(8, sqlstartDate);
        ps.setDate(9, sqlendDate);
        ps.setDate(10, sqlendDate);
        ps.setDate(11, sqlstartDate);
        ps.setDate(12, sqlendDate);
        rs = ps.executeQuery();
        Staff staff;
            
        while (rs.next()) {
          	int staffID = rs.getInt(1);
            int maxProjectNum = rs.getInt(2);
            String name = rs.getString(3);
            String role = rs.getString(4);
            String email = rs.getString(5);
            String currentstatus = rs.getString(6);
            String memberId = rs.getString(7);
            String token = rs.getString(8);
            staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
            staffList.add(staff);
        }
            
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    // retrieve all PM who achieve his max project number in the given time period 
    // called by retrieveUnavailablePM method in ProjectController
    public static ArrayList<Staff> retrieveUnavailablePM(String startDate, String endDate) {
        String sql = "SELECT * FROM staff WHERE role = 'Project Manager' AND staffId in (SELECT staffId FROM project_staff  WHERE projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? and projectEndDate <= ?)) GROUP BY staffId)HAVING maxProjectNum <= (SELECT pro_no FROM (SELECT COUNT(*) AS pro_no , staffId AS sd FROM project_staff  WHERE projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? and projectEndDate <= ?)) GROUP BY staffId)as temp WHERE staffId = sd)";
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date sdate = sdf.parse(startDate);
        java.util.Date edate = sdf.parse(endDate);
       
        java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
        java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
        
        conn = ConnectionManager.getConnection();
        ps = conn.prepareStatement(sql);
        ps.setDate(1, sqlstartDate);
        ps.setDate(2, sqlstartDate);
        ps.setDate(3, sqlendDate);
        ps.setDate(4, sqlendDate);
        ps.setDate(5, sqlstartDate);
        ps.setDate(6, sqlendDate);
            
        ps.setDate(7, sqlstartDate);
        ps.setDate(8, sqlstartDate);
        ps.setDate(9, sqlendDate);
        ps.setDate(10, sqlendDate);
        ps.setDate(11, sqlstartDate);
        ps.setDate(12, sqlendDate);
        rs = ps.executeQuery();
        Staff staff;
            
        while (rs.next()) {
          	int staffID = rs.getInt(1);
            int maxProjectNum = rs.getInt(2);
            String name = rs.getString(3);
            String role = rs.getString(4);
            String email = rs.getString(5);
            String currentstatus = rs.getString(6);
            String memberId = rs.getString(7);
            String token = rs.getString(8);
            staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
            staffList.add(staff);
        }
            
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    //retrieve all PM who have ZERO ongoing projects in the given time period 
    //this is called by retrieveAvailablePM method in ProjectController
    public static ArrayList<Staff> retrieveTotallyFreePM(String startDate, String endDate) {
        String sql = "SELECT * FROM staff WHERE role = 'Project Manager' AND staffId not in "
        		+ "(SELECT project_staff.staffId FROM project_staff  WHERE projectId in "
        		+ "(SELECT projectId FROM project WHERE (projectStartDate <= ? and projectEndDate >= ?) "
        		+ "or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? "
        		+ "and projectEndDate <= ?)) GROUP BY staffId)";
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date sdate = sdf.parse(startDate);
        java.util.Date edate = sdf.parse(endDate);
       
        java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
        java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
        
        conn = ConnectionManager.getConnection();
        ps = conn.prepareStatement(sql);
        ps.setDate(1, sqlstartDate);
        ps.setDate(2, sqlstartDate);
        ps.setDate(3, sqlendDate);
        ps.setDate(4, sqlendDate);
        ps.setDate(5, sqlstartDate);
        ps.setDate(6, sqlendDate);
            
        rs = ps.executeQuery();
        Staff staff;
            
        while (rs.next()) {
          	int staffID = rs.getInt(1);
            int maxProjectNum = rs.getInt(2);
            String name = rs.getString(3);
            String role = rs.getString(4);
            String email = rs.getString(5);
            String currentstatus = rs.getString(6);
            String memberId = rs.getString(7);
            String token = rs.getString(8);
            staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
            staffList.add(staff);
        }
            
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    // retrieve all staff whose role is PM.
    // called by retrieveAvailablePM in ProjectController
    public static ArrayList<Staff> retrieveAllPM() {
        String sql = "SELECT * FROM staff WHERE role = 'Project Manager'";
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Staff staff;
            while (rs.next()) {
              	int staffID = rs.getInt(1);
                int maxProjectNum = rs.getInt(2);
                String name = rs.getString(3);
                String role = rs.getString(4);
                String email = rs.getString(5);
                String currentstatus = rs.getString(6);
                String memberId = rs.getString(7);
                String token = rs.getString(8);
                staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
                staffList.add(staff);
               
            }
        }catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }

        return staffList;
    }
    
    public static boolean deleteStaff(int staffID) {
        String sql = "delete from staff where staffID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
    
    public static boolean updateStaffInfo(int staffID, int maxProjectNum, String name, String role, String email, String status) {
        String sql = "update staff set maxProjectNum = ?, name = ?, role = ?, email = ?, status = ? where staffID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, maxProjectNum);
            ps.setString(2, name);
            ps.setString(3, role);
            ps.setString(4, email);
            ps.setString(5, status);
            ps.setInt(6, staffID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
    
    //retrieve a list of staff with the given skills
    //called by retrieveStaffBySkills
    public static ArrayList<Staff> retrieveStaffBySkills(String skill) {
        String sql = "SELECT * FROM staff WHERE staffId in (SELECT staffId FROM staff_skill "
        		+ "WHERE skillName =?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, skill);
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    //retrieve a list of staff from this project
    //called by RetrieveStaffByProject in ProjectManager
    public static ArrayList<Staff> retrieveStaffByProject(String projectID) {
        String sql = "SELECT * FROM staff WHERE staffId in (SELECT staffId FROM project_staff "
        		+ "WHERE projectId =?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, projectID);
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    public static int retrieveNumberOfStaffWithSkill(String skillName) {
        String sql = "select COUNT(*) from staff_skill where skillName = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        int number = 0;
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, skillName);
        	rs = ps.executeQuery();
            
        	while (rs.next()) {
        		number = rs.getInt(1);
        		break;
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return number;
    }
    
    //retrieve all staff whose skills match the skill given and have ongoing projects in the given time period yet still available - for RetrieveAvailableStaffBySkills method in Project Controller
    public static ArrayList<Staff> retrieveAvailableStaffBySkills(String skill, String startDate, String endDate) {
        String sql = "SELECT * FROM staff WHERE staffId in (SELECT staffId FROM project_staff "
        		+ "WHERE projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? "
        		+ "and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or "
        		+ "(projectStartDate >= ? and projectEndDate <= ?)) GROUP BY staffId)HAVING maxProjectNum > "
        		+ "(SELECT pro_no FROM (SELECT COUNT(*) AS pro_no , staffId AS sd FROM project_staff "
        		+ "WHERE projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? and "
        		+ "projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? "
        		+ "and projectEndDate <= ?)) GROUP BY staffId)as temp WHERE staffId = sd) AND staffId in "
        		+ "(SELECT staffId FROM staff_skill WHERE skillName =?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	java.util.Date sdate = sdf.parse(startDate);
        	java.util.Date edate = sdf.parse(endDate);
       
        	java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
        	java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
        
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setDate(1, sqlstartDate);
        	ps.setDate(2, sqlstartDate);
        	ps.setDate(3, sqlendDate);
        	ps.setDate(4, sqlendDate);
        	ps.setDate(5, sqlstartDate);
        	ps.setDate(6, sqlendDate);
            
        	ps.setDate(7, sqlstartDate);
        	ps.setDate(8, sqlstartDate);
        	ps.setDate(9, sqlendDate);
        	ps.setDate(10, sqlendDate);
        	ps.setDate(11, sqlstartDate);
        	ps.setDate(12, sqlendDate);
        
        	ps.setString(13, skill);
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    //retrieve all staff whose skills match the skill given and is totally free in the given time period - for RetrieveAvailableStaffBySkills method in Project Controller
    public static ArrayList<Staff> retrieveTotallyFreeStaffBySkills(String skill, String startDate, String endDate) {
        String sql = "SELECT * FROM staff WHERE staffId not in (SELECT project_staff.staffId FROM project_staff "
        		+ "WHERE projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? and "
        		+ "projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? "
        		+ "and projectEndDate <= ?)) GROUP BY staffId) AND staffId in (SELECT staffId FROM staff_skill "
        		+ "WHERE skillName =?)";
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	java.util.Date sdate = sdf.parse(startDate);
        	java.util.Date edate = sdf.parse(endDate);
       
        	java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
        	java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setDate(1, sqlstartDate);
        	ps.setDate(2, sqlstartDate);
        	ps.setDate(3, sqlendDate);
        	ps.setDate(4, sqlendDate);
        	ps.setDate(5, sqlstartDate);
        	ps.setDate(6, sqlendDate);
        	
        	ps.setString(7, skill);
            
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    // retrieve all Staff who achieve his max project number in the given time period - for RetrieveUnavailablePM method in ProjectController
    public static ArrayList<Staff> retrieveUnavailableStaffBySkills(String skill, String startDate, String endDate) {
        String sql = "SELECT * FROM staff WHERE staffId in (SELECT staffId FROM project_staff WHERE projectId "
        		+ "in (SELECT projectId FROM project WHERE (projectStartDate <= ? and projectEndDate >= ?) or "
        		+ "(projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? and projectEndDate "
        		+ "<= ?)) GROUP BY staffId)HAVING maxProjectNum <= (SELECT pro_no FROM (SELECT COUNT(*) AS pro_no"
        		+ " , staffId AS sd FROM project_staff  WHERE projectId in (SELECT projectId FROM project WHERE "
        		+ "(projectStartDate <= ? and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate "
        		+ "<= ?) or (projectStartDate >= ? and projectEndDate <= ?)) GROUP BY staffId)as temp WHERE staffId"
        		+ " = sd) AND staffId in (SELECT staffId FROM staff_skill WHERE skillName =?)";
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	java.util.Date sdate = sdf.parse(startDate);
        	java.util.Date edate = sdf.parse(endDate);
        	
        	java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
        	java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setDate(1, sqlstartDate);
        	ps.setDate(2, sqlstartDate);
        	ps.setDate(3, sqlendDate);
        	ps.setDate(4, sqlendDate);
        	ps.setDate(5, sqlstartDate);
        	ps.setDate(6, sqlendDate);
            
        	ps.setDate(7, sqlstartDate);
        	ps.setDate(8, sqlstartDate);
        	ps.setDate(9, sqlendDate);
        	ps.setDate(10, sqlendDate);
        	ps.setDate(11, sqlstartDate);
        	ps.setDate(12, sqlendDate);
        	
        	ps.setString(13, skill);
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}
            
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    //return an arraylist of all skill name in the skill table
    public static ArrayList<String> retrieveAllSkills() {
        String sql = "SELECT * FROM skill";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<String> skills = new ArrayList<String>();
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	rs = ps.executeQuery();
            
        	while (rs.next()) {
        		String skillName = rs.getString(1);
        		skills.add(skillName);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return skills;
    }
    
    public static int retrieveNumberOfProjectsByPM(int projectManagerId) {
        String sql = "SELECT COUNT(*) FROM project WHERE projectManagerId = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        int number = 0;
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setInt(1, projectManagerId);
        	rs = ps.executeQuery();
            
        	while (rs.next()) {
        		number = rs.getInt(1);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return number;
    }

    public static ArrayList<Staff> filterStaffListByStaffInfo(String input) {
        String sql = "SELECT * FROM staff WHERE maxProjectNum LIKE ? OR name LIKE ? OR role LIKE ? OR email LIKE ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	String searchCriteria = input+"%";
        	ps.setString(1, searchCriteria);
        	ps.setString(2, searchCriteria);
        	ps.setString(3, searchCriteria);
        	ps.setString(4, searchCriteria);
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
    public static ArrayList<Staff> filterStaffListBySkill(String input) {
        String sql = "SELECT * FROM staff WHERE staffID in (select staffId from staff_skill where skillName LIKE ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	String searchCriteria = input+"%";
        	ps.setString(1, searchCriteria);
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
    
  //retrieve a list of staff from this project
    public static ArrayList<Staff> retrieveStaffBySubproject(String projectID, int subprojectID) {
        String sql = "SELECT * FROM staff WHERE staffId in (SELECT staffId FROM subproject_staff "
        		+ "WHERE projectId =? AND subprojectId = ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        staffList = new ArrayList<Staff>();
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, projectID);
        	ps.setInt(2, subprojectID);
        	rs = ps.executeQuery();
        	Staff staff;
            
        	while (rs.next()) {
        		int staffID = rs.getInt(1);
        		int maxProjectNum = rs.getInt(2);
        		String name = rs.getString(3);
        		String role = rs.getString(4);
        		String email = rs.getString(5);
        		String currentstatus = rs.getString(6);
        		String memberId = rs.getString(7);
        		String token = rs.getString(8);
        		staff = new Staff(staffID, maxProjectNum, name, role, email, currentstatus, memberId, token);
        		staffList.add(staff);
        	}   
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return staffList;
    }
}