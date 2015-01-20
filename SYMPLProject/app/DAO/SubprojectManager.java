package DAO;

import play.*;
import entity.*;

import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.*;

/**
 *
 * @author Yixi
 */
/**
 * This manager is responsible to staffs and provIDes methods for staffs
 * operations.
 */
public class SubprojectManager {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static ArrayList<Project> projectList;
    private static Project project;   

    public SubprojectManager() {
    }
    
    /**
     * This method populates subproject to the subproject table
     * inside the database.
     *
     * @return this Subproject if the subproject is successfully populated, else return null
     */
    public static Subproject populateSubprojectDB(String projectID, String subprojectName, String subprojectDescription, String startDate, String endDate, String subprojectType) {
        String sql = "insert into subproject(projectId, subprojectId,subprojectName, "
        		+ "subprojectDescription, startDate, endDate, subprojectType, status) "
        		+ "values(?, ?, ?, ?, ?, ?, ?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        Subproject subproject = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate = sdf.parse(startDate);
            java.util.Date edate = sdf.parse(endDate);
            java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
            java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
            
            //generate subproject ID
            
            int currentSubProjectNumber = ProjectManager.retrieveSubprojectNumber(projectID);
            int subprojectID = currentSubProjectNumber+1;
            ProjectManager.updateSubprojectNumber(projectID, subprojectID);

            ps.setString(1, projectID);
            ps.setInt(2, subprojectID);
            System.out.println(subprojectID);
            ps.setString(3, subprojectName);
            ps.setString(4, subprojectDescription);
            ps.setDate(5, sqlstartDate);
            ps.setDate(6, sqlendDate);
            ps.setString(7, subprojectType);
            
            //check subproject current status
            java.util.Date currentDate = new java.util.Date();
            java.sql.Date sqlCurrentDate = new java.sql.Date(currentDate.getTime());
            String status = new String();
            int compareStart = sqlCurrentDate.compareTo(sqlstartDate);
            int compareEnd = sqlCurrentDate.compareTo(sqlendDate);
            
            if(compareEnd > 0){
            	status = "Done";
            }else if(compareStart < 0){
            	status = "To do";
            }else{
            	status = "Doing";
            }
            
            ps.setString(8, status);
            ps.executeUpdate();
            
            
            //generate the new subproject object
            subproject = new Subproject(projectID, subprojectID, subprojectName,subprojectDescription, sdate, edate,subprojectType, status);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
        return subproject;
    }
    
    public static Subproject updateSubprojectDescFromTrello(String projectId, int subprojectId, String subprojectDescription) {
        String sql = "update subproject set subprojectDescription = ?"
        		+ "where projectId = ? AND subprojectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Subproject pro = null;
        try {
        	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //java.util.Date lMDate = sdf.parse(lastModifiedDate);
            
            //java.sql.Date sqllMDate = new java.sql.Date(lMDate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, subprojectDescription);
            //ps.setString(2, "Trello");
            //ps.setDate(3, sqllMDate);
            ps.setString(2, projectId);
            ps.setInt(3, subprojectId);
            ps.executeUpdate();
            
            pro = SubprojectManager.retrieveSubprojectById(projectId,subprojectId);
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return pro;
    }
    
    public static Subproject updateSubprojectStatusFromTrello(String projectId, int subprojectId, String status) {
        String sql = "update subproject set status = ? "
        		+ "where projectId = ? AND subprojectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Subproject pro = null;
        try {
        	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //java.util.Date lMDate = sdf.parse(lastModifiedDate);
            
            //java.sql.Date sqllMDate = new java.sql.Date(lMDate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, status);
            //ps.setString(2, "Trello");
            //ps.setDate(3, sqllMDate);
            ps.setString(2, projectId);
            ps.setInt(3, subprojectId);
            ps.executeUpdate();
            
            pro = SubprojectManager.retrieveSubprojectById(projectId,subprojectId);
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return pro;
    }
    
    public static void updateSubprojectCardId(String projectID, int subprojectID, String subprojectCardId){
    	 String sql = "update subproject set subprojectCardId = ?"
         		+ "where projectId = ? AND subprojectId = ?";
         Connection conn = null;
         PreparedStatement ps = null;
         try {
             conn = ConnectionManager.getConnection();
             ps = conn.prepareStatement(sql);
             
             ps.setString(1, subprojectCardId);
             ps.setString(2, projectID);
             ps.setInt(3, subprojectID);
             ps.executeUpdate();
             
         } catch (SQLException e) {
         	System.out.println(e);
         } catch (Exception exc){
         	System.out.println(exc);
         }finally {
             ConnectionManager.close(conn, ps, null);
         }
    }
    
    /**
     * This method retrieves all subprojects from the subproject table in the database according to projectID
     *
     * @return an array list of all subprojects
     */
    public static ArrayList<Subproject> retrieveAll(String projectID){
    	ArrayList<Subproject> subprojectList = new ArrayList();
        String sql = "select * from subproject where projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectID);
            rs = ps.executeQuery();
            Subproject subproject;
            while (rs.next()) {
            	int subprojectID = rs.getInt(1);
            	String projectId = rs.getString(2);
            	String subprojectName = rs.getString(3);
                String subprojectDescription = rs.getString(4);
                java.sql.Date startDate = rs.getDate(5);
                java.sql.Date endDate = rs.getDate(6);
                java.sql.Date actualStartDate = rs.getDate(7);
                java.sql.Date actualEndDate = rs.getDate(8);
                String subporjectType = rs.getString(9);
                String status = rs.getString(10);
                String subprojectCardId = rs.getString(11);
                subproject = new Subproject(projectID, subprojectID, subprojectName, subprojectDescription, startDate, endDate, subporjectType, status, 
                		actualStartDate, actualEndDate, subprojectCardId);
                subprojectList.add(subproject);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return subprojectList;
    }
    
    public static void deleteAllSubprojectStaff(String projectID, int subprojectID){
        String sql = "DELETE FROM subproject_staff WHERE projectId = ? AND subprojectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectID);
            ps.setInt(2, subprojectID);
            ps.executeUpdate();
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    }
    
    public static void deleteAllSubprojectSequence(String projectID, int subprojectID){
        String sql = "DELETE FROM subprojectsequence WHERE projectId = ? AND successor = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectID);
            ps.setInt(2, subprojectID);
            ps.executeUpdate();
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    }
    
    /**
     * This method populates assigned staffs of specific subproject to the subproject staff table
     * inside the database.
     *
     * @return true if the staffId and projectId are successfully populated
     */
    public static boolean populateSubprojectStaffs(String projectId, int subprojectId, int staffId){
    	String sql = "insert into subproject_staff(subprojectId ,projectId,staffId ) values(?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subprojectId);            
            ps.setString(2, projectId);
            ps.setInt(3, staffId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
    
    public static boolean populateSubprojectSequence(String projectId, int predecessorId, int subprojectId){
    	String sql = "insert into subprojectsequence(predecessor, successor, projectId ) values(?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, predecessorId);  
            ps.setInt(2, subprojectId);   
            ps.setString(3, projectId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
    
    /**
     * This method retrieves the subproject with given projectId from the project table
     * inside the database.
     *
     * @param a given projectId and subprojectId
     * @return the subproject with given projectId and subprojectId
     */
    public static Subproject retrieveSubprojectById(String projectId, int subprojectId){
    	Subproject subproject = null;
    	String sql = "select * from subproject where projectId = ? AND subprojectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            ps.setInt(2, subprojectId);
            rs = ps.executeQuery();
            while (rs.next()) {
            	int subprojectID = rs.getInt(1);
            	String projectID = rs.getString(2);
            	String subprojectName = rs.getString(3);
                String subprojectDescription = rs.getString(4);
                java.sql.Date startDate = rs.getDate(5);
                java.sql.Date endDate = rs.getDate(6);
                java.sql.Date actualStartDate = rs.getDate(7);
                java.sql.Date actualEndDate = rs.getDate(8);
                String subporjectType = rs.getString(9);
                String status = rs.getString(10);
                String subprojectCardId = rs.getString(11);
                subproject = new Subproject(projectID, subprojectID, subprojectName, subprojectDescription, startDate, endDate, subporjectType, status, 
                		actualStartDate, actualEndDate, subprojectCardId);
            	break;
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return subproject;
    }
    
    /**
     * This method counts the number of projects started in the given month in project table
     * inside the database.
     *
     * @param a given month
     * @return the number of the projects that starts in the given month
     */
    public static int numOfProjectsStartFromMonth(String mm){
        String sql = "select count(*) from project where MONTH(projectStartDate) = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int number = 0;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            int month = Integer.parseInt(mm);
            ps.setInt(1, month);
            rs = ps.executeQuery();
            while (rs.next()) {
                number = rs.getInt(1);
                break;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return number;
    }
    
    /**
     * This method retrieves the project with given projectId from the project table
     * inside the database.
     *
     * @param a given projectId
     * @return the project with given projectId
     */
    public static String retrieveListId(String status){
    	String sql = "select listID from trelloid where status = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String trelloID = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) {
                trelloID = rs.getString(1);
            	break;
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return trelloID;
    }
    
    
    /**
     * This method retrieves an arraylist of staff that within the same project
     * inside the database.
     *
     * @return an array list of staffs who are allocated to the given project
     */
    public static ArrayList<Staff> retrieveStaffByProject(String projectId){
    	ArrayList<Staff> staffList = new ArrayList();
    	String sql = "SELECT * FROM staff WHERE staffId in (SELECT staffId FROM project_staff WHERE projectId = ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
            	int staffID = rs.getInt(1);
                int maxProjectNum = rs.getInt(2);
                String name = rs.getString(3);
                String role = rs.getString(4);
                String email = rs.getString(5);
                String status = rs.getString(6);
                String memberId = rs.getString(7);
                String token = rs.getString(8);
                Staff staff = new Staff(staffID, maxProjectNum, name, role, email, status, memberId, token);
                staffList.add(staff);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return staffList;
    }
    
    /**
     * This method retrieves an array list of projects that is doing by a given PM
     * inside the database.
     *
     * @return an array list of projects
     */
    public static ArrayList<Project> retrieveProjectByPM(int staffId){
    	ArrayList<Project> projectList = new ArrayList();
    	String sql = "SELECT * FROM project WHERE projectManagerId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffId);
            rs = ps.executeQuery();
            Project project;
            while (rs.next()) {
                String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date startDate = rs.getDate(4);
                java.sql.Date endDate = rs.getDate(5);
                java.sql.Date warrantyStartDate = rs.getDate(6);
                java.sql.Date warrantyEndDate = rs.getDate(7);
                String client = rs.getString(8);
                String status = rs.getString(9);
                String lastModifiedBy = rs.getString(10);
                java.sql.Date lastModifiedDate = rs.getDate(11);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, 
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate);
                projectList.add(project);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return projectList;
    }
    
    /**
     * This method retrieves an array list of projects that is doing by a given staff (developer)
     * inside the database.
     *
     * @return an array list of projects
     */
    public static ArrayList<Project> retrieveProjectByStaff(int staffId){
    	ArrayList<Project> projectList = new ArrayList();
    	String sql = "SELECT * FROM project WHERE projectId in (SELECT projectId FROM project_staff  WHERE staffId = ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffId);
            rs = ps.executeQuery();
            Project project;
            while (rs.next()) {
                String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date startDate = rs.getDate(4);
                java.sql.Date endDate = rs.getDate(5);
                java.sql.Date warrantyStartDate = rs.getDate(6);
                java.sql.Date warrantyEndDate = rs.getDate(7);
                String client = rs.getString(8);
                String status = rs.getString(9);
                String lastModifiedBy = rs.getString(10);
                java.sql.Date lastModifiedDate = rs.getDate(11);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, 
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate);
                projectList.add(project);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return projectList;
    }
    
    /**
     * This method retrieves a list of ongoing projects from the given staff within the given time period.
     *
     * @param startDate and endDate of the time period
     * @return a list of projects whose period overlaps with the given time period 
     */
    public static ArrayList<Subproject> retrieveOngoingSubprojectsByStaff (int staffID ,String startDate, String endDate) {
        String sql = "select * from ((select s.subprojectId, s.projectId, subprojectName, subprojectDescription, startDate,"
        		+ " endDate, actualStartDate, actualEndDate, subprojectType, status from "
        		+ "subproject_staff ss, subproject s where ss.subprojectId = s.subprojectId and ss.projectId = s.projectId "
        		+ "and staffId = ?) as temp) where (startDate <= ? and endDate >= ?) or (endDate >= ? and startDate <= ?) "
        		+ "or (startDate >= ? and endDate <= ?)";
        ArrayList<Subproject> subprojectList = new ArrayList<Subproject>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffID);
            ps.setString(2, startDate);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            ps.setString(5, endDate);
            ps.setString(6, startDate);
            ps.setString(7, endDate);
            rs = ps.executeQuery();
            Subproject subproject;
            while (rs.next()) {
            	int subprojectID = rs.getInt(1);
            	String projectId = rs.getString(2);
            	String subprojectName = rs.getString(3);
                String subprojectDescription = rs.getString(4);
                java.sql.Date subprojectStartDate = rs.getDate(5);
                java.sql.Date subprojectEndDate = rs.getDate(6);
                java.sql.Date actualStartDate = rs.getDate(7);
                java.sql.Date actualEndDate = rs.getDate(8);
                String subporjectType = rs.getString(9);
                String status = rs.getString(10);
                String subprojectCardId = rs.getString(11);
                subproject = new Subproject(projectId, subprojectID, subprojectName, subprojectDescription, subprojectStartDate, subprojectEndDate, subporjectType, status, 
                		actualStartDate, actualEndDate, subprojectCardId);
                subprojectList.add(subproject);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return subprojectList;
    }
    
    /**
     * This method retrieves the number of ongoing projects
     * inside the database.
     *
     * @return the number of projects that were ongoing
     */
    public static int retrieveOngoingProjectNumber (int staffID, String startDate, String endDate) {
        String sql = "SELECT COUNT(*) FROM project_staff WHERE staffId=? AND projectId in (SELECT projectId FROM project WHERE (projectStartDate <= ? and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? and projectEndDate <= ?)) GROUP BY staffId";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int ongoingProjectNum = 0;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffID);
            ps.setString(2, startDate);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            ps.setString(5, endDate);
            ps.setString(6, startDate);
            ps.setString(7, endDate);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                ongoingProjectNum = rs.getInt(1);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return ongoingProjectNum;
    }
    
    /**
     * This method updates the subproject details to the subproject table
     * inside the database.
     *
     */
    public static void updateSubproject(String projectId, int subprojectId, String subprojectName, String subprojectDescription, String startDate, String endDate, String subprojectType) {
        String sql = "update subproject set subprojectName = ?, subprojectDescription = ?, startDate = ?, "
        		+ "endDate = ?, subprojectType = ?"
        		+ "where projectId = ? AND subprojectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate = sdf.parse(startDate);
            java.util.Date edate = sdf.parse(endDate);
            
            java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
            java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, subprojectName);
            ps.setString(2, subprojectDescription);
            ps.setDate(3, sqlstartDate);
            ps.setDate(4, sqlendDate);
            ps.setString(5, subprojectType);         
            ps.setString(6, projectId);
            ps.setInt(7, subprojectId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
    }
    
    public static ArrayList<Subproject> retrieveSubprojectByStaff (int staffID){
    	ArrayList<Subproject> subprojectList = new ArrayList();
        String sql = "select * from subproject_staff ss, subproject s where ss.subprojectId = s.subprojectId and ss.projectId = s.projectId and staffId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffID);
            rs = ps.executeQuery();
            Subproject subproject;
            while (rs.next()) {
            	int subprojectID = rs.getInt(4);
            	String projectId = rs.getString(5);
            	String subprojectName = rs.getString(6);
                String subprojectDescription = rs.getString(7);
                java.sql.Date startDate = rs.getDate(8);
                java.sql.Date endDate = rs.getDate(9);
                java.sql.Date actualStartDate = rs.getDate(10);
                java.sql.Date actualEndDate = rs.getDate(11);
                String subporjectType = rs.getString(12);
                String status = rs.getString(13);
                String subprojectCardId = rs.getString(14);
                subproject = new Subproject(projectId, subprojectID, subprojectName, subprojectDescription, startDate, endDate, subporjectType, status, 
                		actualStartDate, actualEndDate, subprojectCardId);
                subprojectList.add(subproject);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return subprojectList;
    }
}
