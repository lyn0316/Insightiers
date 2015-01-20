package DAO;

import play.*;
import entity.*;

import java.util.*;
import java.util.Date;
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
public class ProjectManager {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static ArrayList<Project> projectList;
    private static Project project;   

    public ProjectManager() {
    }
    
    //This method populates project to the project table when a project is created
    //called by createProject in ProjectController
    public static Project populateProjectDB(String projectInitials, String projectDescription, int pmId, String projectStartDate,
    		String projectEndDate, String client, String modifiedBy) {
        String sql = "insert into project(projectId, projectDescription, projectManagerId, projectStartDate, projectEndDate, warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, subprojectNumber) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            //generate project ID
            String projectId = new String();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate = sdf.parse(projectStartDate);
            java.util.Date edate = sdf.parse(projectEndDate);
            java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
            java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
            projectId = ProjectManager.generateProjectId(projectInitials, sqlstartDate);
            
            ps.setString(1, projectId);
            ps.setString(2, projectDescription);
            ps.setInt(3, pmId);
            ps.setDate(4, sqlstartDate);
            ps.setDate(5, sqlendDate);
            
            //generate project warranty start date and end date
            Calendar cal = Calendar.getInstance();
            cal.setTime(sqlendDate);
            cal.add(Calendar.DATE, 1);
            java.sql.Date warrantyStartDate = new java.sql.Date(cal.getTime().getTime());
            cal.setTime(warrantyStartDate);
            cal.add(Calendar.MONTH, 3);
            java.sql.Date warrantyEndDate = new java.sql.Date(cal.getTime().getTime());
            
            ps.setDate(6, warrantyStartDate);
            ps.setDate(7, warrantyEndDate);
            ps.setString(8, client);
            
            //put To do as project current status
            java.util.Date currentDate = new java.util.Date();
            java.sql.Date sqlCurrentDate = new java.sql.Date(currentDate.getTime());
            String status = new String();
            //int compareStart = sqlCurrentDate.compareTo(sqlstartDate);
            //int compareEnd = sqlCurrentDate.compareTo(sqlendDate);
            //int compareWarranty = sqlCurrentDate.compareTo(warrantyEndDate);
            //if(compareEnd > 0 && compareWarranty < 0){
            //	status = "On warranty";
            //}else if(compareEnd > 0){
            //	status = "Done";
            //}else if(compareStart < 0){
            	status = "To do";
            //}else{
            //	status = "Doing";
            //}
            
            ps.setString(9, status);
            ps.setString(10, modifiedBy);
            ps.setDate(11, sqlCurrentDate);
            ps.executeUpdate();
            
            //generate the new project object
            pro = new Project(projectId, projectDescription, pmId, sqlstartDate, sqlendDate, warrantyStartDate,
            		warrantyEndDate, client, status, modifiedBy, sqlCurrentDate);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
        return pro;
    }
    
    public static boolean populateProjectDBFromTrello(String projectId, String projectDescription, String projectStartDate,
    		String projectEndDate, String client, String status, String lastModifiedDate) {
        String sql = "insert into project(projectId, projectDescription, projectManagerId, projectStartDate,"
        		+ " projectEndDate, warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, "
        		+ "lastModifiedDate, subprojectNumber) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate = sdf.parse(projectStartDate);
            java.util.Date edate = sdf.parse(projectEndDate);
            java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
            java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
            
            ps.setString(1, projectId);
            ps.setString(2, projectDescription);
            ps.setString(3, null);
            ps.setDate(4, sqlstartDate);
            ps.setDate(5, sqlendDate);
            
            //generate project warranty start date and end date
            Calendar cal = Calendar.getInstance();
            cal.setTime(sqlendDate);
            cal.add(Calendar.DATE, 1);
            java.sql.Date warrantyStartDate = new java.sql.Date(cal.getTime().getTime());
            cal.setTime(warrantyStartDate);
            cal.add(Calendar.MONTH, 3);
            java.sql.Date warrantyEndDate = new java.sql.Date(cal.getTime().getTime());
            
            ps.setDate(6, warrantyStartDate);
            ps.setDate(7, warrantyEndDate);
            ps.setString(8, client);
            
            ps.setString(9, status);
            ps.setString(10, "Trello");
            ps.setDate(11, sqlstartDate);
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
        return true;
    }
    
    //This method populates assigned staffs of specific project to the project staff table
    //called by createProject, updateProject in ProjectController
    public static boolean populateProjectStaffs(String projectId, int staffId){
    	String sql = "insert into project_staff(staffId, projectId) values(?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staffId);
            ps.setString(2, projectId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionManager.close(conn, ps, null);
        }
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
     * This method generates the project id based on the given client and project details
     *
     * @return a String value of generated project Id
     */
    public static String generateProjectId(String projectInitials, java.sql.Date date){
    	String projectId = new String();
    	projectInitials.toUpperCase();
    	DateFormat df = new SimpleDateFormat("yyMM");
        String time = df.format(date);
        String month = time.substring(2, 4);
        int num = ProjectManager.numOfProjectsStartFromMonth(month) + 1;
        String number = String.format("%03d", num);
        projectId = "PR"+time+number+projectInitials;
    	return projectId;
    }
    
    //Return a list of all projects
    //called by RetrieveAllProjects in ProjectController
    public static ArrayList<Project> retrieveAll(){
    	ArrayList<Project> projectList = new ArrayList();
        String sql = "select * from project order by projectID";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Project project;
            while (rs.next()) {
                String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date startDate = rs.getDate(4);
                java.sql.Date endDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String boardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, boardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return projectList;
    }
    
    //Return the specified project based on the projectID given
    //called by retrieveProjectByID in ProjectController and updateProject in ProjectManager
    public static Project retrieveProjectById(String projectId){
    	Project project = null;
    	String sql = "select * from project where projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
            	String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date startDate = rs.getDate(4);
                java.sql.Date endDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String boardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, boardId, subProjectNumber);
                break;
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return project;
    }
    
    public static Project retrieveSubprojectById(String projectId, String subprojectIdStr){
    	Project project = null;
    	String sql = "select * from subproject where projectId = ? AND subprojectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            int subprojectId = Integer.parseInt(subprojectIdStr);
            ps.setString(1, projectId);
            ps.setInt(2, subprojectId);
            rs = ps.executeQuery();
            while (rs.next()) {
            	
            	String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date startDate = rs.getDate(4);
                java.sql.Date endDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String cardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, cardId, subProjectNumber);
                break;
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return project;
    }
    

    //return listId from trelloId based on projectId and status
    //called by createSubproject, updateSubproject, updateFromTrello, RetrieveAllSubprojects, RetrieveSubprojectByID in ProjectController
    public static String retrieveListId(String projectId, String status){
    	String sql = "select listID from trelloid where status = ? AND projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String trelloID = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, projectId);
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
    
    //retrieve status based on listID
    public static String retrieveStatus(String listID){
    	String sql = "select status from trelloid where listID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, listID);
            rs = ps.executeQuery();
            while (rs.next()) {
            	status = rs.getString(1);
            	break;
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return status;
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
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String cardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, cardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return projectList;
    }
    
    //retrieve a list of projects under the given staffID
    //called by retrieveProjectByStaff in ProjectController
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
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String boardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, boardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (Exception e) {
        	System.out.println(e);
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
    	return projectList;
    }
    
	//retrieve a list of ongoing projects within a given period of time
	//called by retrieveOngoingProjects in ProjectController 
    public static ArrayList<Project> retrieveOngoingProjects (String startDate, String endDate) {
        String sql = "SELECT * FROM project WHERE (projectStartDate <= ? and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? and projectEndDate <= ?)";
        projectList = new ArrayList<Project>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setString(4, endDate);
            ps.setString(5, startDate);
            ps.setString(6, endDate);
            
            rs = ps.executeQuery();

            while (rs.next()) {
            	String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date sqlstartDate = rs.getDate(4);
                java.sql.Date sqlendDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String boardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, sqlstartDate, sqlendDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, boardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return projectList;
    }
    
    //retrieves the number of ongoing projects under the given staffID and within the given period
    //this method is called by retrieveAvailablePM and retrieveUnavailablePM in ProjectController
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
     * This method updates the project details to the project table
     * inside the database.
     *
     * @return true if the project is successfully updated
     */
    public static Project updateProejctActualStartDate(String startDate, String projectId){
    	String sql = "update project set actualStartDate = ? where projectId = ?";
    	Connection conn = null;
    	PreparedStatement ps = null;
    	Project pro = null;
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate = sdf.parse(startDate);
            java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, sqlstartDate);
            ps.setString(2, projectId);
            ps.executeUpdate();
            
            pro = ProjectManager.retrieveProjectById(projectId);
    	}catch(Exception e){
    		System.out.println(e);
    	}finally{
    		ConnectionManager.close(conn, ps, null);
    	}
    	return pro;
    }
    
    public static Project updateProejctActualEndDate(String endDate, String projectId){
    	String sql = "update project set actualEndDate = ? where projectId = ?";
    	Connection conn = null;
    	PreparedStatement ps = null;
    	Project pro = null;
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate = sdf.parse(endDate);
            java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, sqlstartDate);
            ps.setString(2, projectId);
            ps.executeUpdate();
            
            pro = ProjectManager.retrieveProjectById(projectId);
    	}catch(Exception e){
    		System.out.println(e);
    	}finally{
    		ConnectionManager.close(conn, ps, null);
    	}
    	return pro;
    }
    
    //update the boardId in the project table
    //called by updateProjectBoardId in ProjectController
    public static Project updateProejctBoardId(String boardId, String projectId){
    	String sql = "update project set boardId = ? where projectId = ?";
    	Connection conn = null;
    	PreparedStatement ps = null;
    	Project pro = null;
    	try{
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, boardId);
            ps.setString(2, projectId);
            ps.executeUpdate();
            
            pro = ProjectManager.retrieveProjectById(projectId);
    	}catch(Exception e){
    		System.out.println(e);
    	}finally{
    		ConnectionManager.close(conn, ps, null);
    	}
    	return pro;
    }
    
    //update the listID of a given status of a given project in the trelloid table
    //called by updateProjectListId in ProjectController
    public static Project updateProejctListId( String projectId, String status , String listId){
    	String sql = "insert into trelloid (projectId,status,listId, iddescription) values(?, ?, ?, '') ";
    	Connection conn = null;
    	PreparedStatement ps = null;
    	Project pro = null;
    	try{
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            ps.setString(2, status);
            ps.setString(3, listId);
            ps.executeUpdate();
            
            pro = ProjectManager.retrieveProjectById(projectId);
    	}catch(Exception e){
    		System.out.println(e);
    	}finally{
    		ConnectionManager.close(conn, ps, null);
    	}
    	return pro;
    }
    
    //retrieve boardId in the project table based on the projectId given
    //called by createSubproject in ProjectController
    public static String retrieveProejctBoardId(String projectId){
    	String sql = "SELECT boardId from project where projectId = ?";
    	Connection conn = null;
    	PreparedStatement ps = null;
    	String boardId = null;
    	try{
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                boardId = rs.getString(1);
                break;
            }

    	}catch(Exception e){
    		System.out.println(e);
    	}finally{
    		ConnectionManager.close(conn, ps, null);
    	}
    	return boardId;
    }
    
    //retrieve projectId in the project table based on the boardId given
    //called by updateFromTrello in ProjectController
    public static String retrieveProejctIdByBoardId(String boardId){
    	String sql = "SELECT projectId from project where boardId = ?";
    	Connection conn = null;
    	PreparedStatement ps = null;
    	String projectId = null;
    	try{
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, boardId);
            rs = ps.executeQuery();
            while (rs.next()) {
            	projectId = rs.getString(1);
            }

    	}catch(Exception e){
    		System.out.println(e);
    	}finally{
    		ConnectionManager.close(conn, ps, null);
    	}
    	return projectId;
    }
    
    //update project (not for status change)
    //called by updateProject in ProjectController
    public static Project updateProject(String projectId, String projectDescription, String startDate, String endDate, int projectManagerId, String modifiedBy) {
        String sql = "update project set projectDescription = ?, projectManagerId = ?, projectStartDate = ?, "
        		+ "projectEndDate = ?, warrantyStartDate = ?, warrantyEndDate = ?, lastModifiedBy = ?, "
        		+ "lastModifiedDate = ? where projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sdate = sdf.parse(startDate);
            java.util.Date edate = sdf.parse(endDate);
            
            java.sql.Date sqlstartDate = new java.sql.Date(sdate.getTime());
            java.sql.Date sqlendDate = new java.sql.Date(edate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, projectDescription);
            ps.setInt(2, projectManagerId);
            ps.setDate(3, sqlstartDate);
            ps.setDate(4, sqlendDate);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(sqlendDate);
            cal.add(Calendar.DATE, 1);
            java.sql.Date warrantyStartDate = new java.sql.Date(cal.getTime().getTime());
            cal.setTime(warrantyStartDate);
            cal.add(Calendar.MONTH, 3);
            java.sql.Date warrantyEndDate = new java.sql.Date(cal.getTime().getTime());
            
            ps.setDate(5, warrantyStartDate);
            ps.setDate(6, warrantyEndDate);
            
            java.util.Date currentDate = new java.util.Date();
            java.sql.Date sqlCurrentDate = new java.sql.Date(currentDate.getTime());
            /* business logic change
            String status = new String();
            int compareStart = sqlCurrentDate.compareTo(sqlstartDate);
            int compareEnd = sqlCurrentDate.compareTo(sqlendDate);
            int compareWarranty = sqlCurrentDate.compareTo(warrantyEndDate);
            
            if(compareEnd > 0 && compareWarranty < 0){
            	status = "On warranty";
            }else if(compareEnd > 0){
            	status = "Done";
            }else if(compareStart < 0){
            	status = "To do";
            }else{
            	status = "Doing";
            }
            
            ps.setString(7, status);
            */
            ps.setString(7, modifiedBy);
            ps.setDate(8, sqlCurrentDate);
            ps.setString(9, projectId);
            ps.executeUpdate();
            
            pro = ProjectManager.retrieveProjectById(projectId);
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return pro;
    }
    
    public static Project updateProjectDescFromTrello(String projectId, String projectDescription, String lastModifiedDate) {
        String sql = "update project set projectDescription = ?, "
        		+ "lastModifiedBy = ?, "
        		+ "lastModifiedDate = ? where projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date lMDate = sdf.parse(lastModifiedDate);
            
            java.sql.Date sqllMDate = new java.sql.Date(lMDate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, projectDescription);
            ps.setString(2, "Trello");
            ps.setDate(3, sqllMDate);
            ps.setString(4, projectId);
            ps.executeUpdate();
            
            pro = ProjectManager.retrieveProjectById(projectId);
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return pro;
    }
    
    public static Project updateProjectStatusFromTrello(String projectId, String status, String lastModifiedDate) {
        String sql = "update project set status = ?, "
        		+ "lastModifiedBy = ?, "
        		+ "lastModifiedDate = ? where projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date lMDate = sdf.parse(lastModifiedDate);
            
            java.sql.Date sqllMDate = new java.sql.Date(lMDate.getTime());
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, status);
            ps.setString(2, "Trello");
            ps.setDate(3, sqllMDate);
            ps.setString(4, projectId);
            ps.executeUpdate();
            
            pro = ProjectManager.retrieveProjectById(projectId);
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return pro;
    }
    
    public static int retrieveSubprojectNumber (String projectID) {
        String sql = "SELECT subprojectNumber FROM project WHERE projectId=? ";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int subprojectNumber = 0;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectID);
            
            rs = ps.executeQuery();

            while (rs.next()) {
            	subprojectNumber= rs.getInt(1);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return subprojectNumber;
    }
    
    public static int updateSubprojectNumber(String projectId, int subprojectNumber) {
        String sql = "update project set subprojectNumber = ? where projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        try {
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
            ps.setInt(1, subprojectNumber);
            ps.setString(2, projectId);
            ps.executeUpdate();
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return subprojectNumber;
    }
    
    /* Change of business logic
    public static String retrieveProjectActualStartDate (String projectId) {
        String sql = "SELECT MIN(actualStartDate) FROM subproject WHERE projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        String actualStartDateStr = null;
        try {
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
        	System.out.print(ps);
            rs = ps.executeQuery();

            while (rs.next()) {
            	java.sql.Date actualStartDate = rs.getDate(1);
            	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            	actualStartDateStr = df.format(actualStartDate);
            }
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return actualStartDateStr;
    }
    
    //retrieve the actual end date of a project from the project 
    //called by retrieveProjectActualEndDate in ProjectController
    public static String retrieveProjectActualEndDate (String projectId) {
        String sql = "SELECT actualProjectEndDate FROM project WHERE projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        String actualEndDateStr = null;
        try {
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
        	System.out.print(ps);
            rs = ps.executeQuery();

            while (rs.next()) {
            	java.sql.Date actualEndDate = rs.getDate(1);
            	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            	actualEndDateStr = df.format(actualEndDate);
            }
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return actualEndDateStr;
    }
    
    //return true if all project tasks have ended
    //called by retrieveProjectActualEndDate in ProjectController
    public static boolean checkProjectEnd (String projectId) {
        String sql = "SELECT COUNT(*) FROM subproject WHERE actualEndDate IS NULL AND projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        int count = 0;
        try {
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
        	System.out.print(ps);
            rs = ps.executeQuery();

            while (rs.next()) {
            count = rs.getInt(1);
            }
         
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        if(count == 0){
        	return true;
        }else{
        	return false;
        }
    }
    
  //return true if there is at least one project tasks have started
    public static boolean checkProjectStart (String projectId) {
        String sql = "SELECT COUNT(*) FROM subproject WHERE actualStartDate IS NOT NULL AND projectId = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        Project pro = null;
        int count = 0;
        try {
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
            ps.setString(1, projectId);
        	System.out.print(ps);
            rs = ps.executeQuery();

            while (rs.next()) {
            count = rs.getInt(1);
            }
         
        } catch (SQLException e) {
        	System.out.println(e);
        } catch (Exception exc){
        	System.out.println(exc);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        if(count > 0){
        	return true;
        }else{
        	return false;
        }
    }
    */
    
	//retrieve a list of projects which due in the given period of time
	//called by retrieveProjectsDue in ProjectController
    public static ArrayList<Project> retrieveProjectsDue (String startDate, String endDate) {
        String sql = "SELECT * FROM project WHERE warrantyStartDate >= ? and warrantyStartDate <= ?";
        projectList = new ArrayList<Project>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            rs = ps.executeQuery();

            while (rs.next()) {
            	String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date sqlstartDate = rs.getDate(4);
                java.sql.Date sqlendDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String boardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, sqlstartDate, sqlendDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, boardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return projectList;
    }
    
    //retrieve a list of project starting within the period given
    //called by retrieveAverageProjectNumberPerMonth in ProjectController
    public static ArrayList<Project> retrieveProjectsStart (String startDate, String endDate) {
        String sql = "SELECT * FROM project WHERE projectStartDate >= ? and projectStartDate <= ?";
        projectList = new ArrayList<Project>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            rs = ps.executeQuery();

            while (rs.next()) {
            	String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date sqlstartDate = rs.getDate(4);
                java.sql.Date sqlendDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String boardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, sqlstartDate, sqlendDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, boardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return projectList;
    }
    
    //retrieve a list of projects on warranty within the given period of time
    //called by retrieveProjectsOnWarranty in ProjectController
    public static ArrayList<Project> retrieveProjectsOnWarranty (String startDate, String endDate) {
        String sql = "SELECT * FROM project WHERE (warrantyStartDate <= ? and warrantyEndDate >= ?) or (warrantyEndDate >= ? and warrantyStartDate <= ?) or (warrantyStartDate >= ? and warrantyEndDate <= ?)";
        projectList = new ArrayList<Project>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setString(4, endDate);
            ps.setString(5, startDate);
            ps.setString(6, endDate);
            
            rs = ps.executeQuery();

            while (rs.next()) {
            	String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date sqlstartDate = rs.getDate(4);
                java.sql.Date sqlendDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String boardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, sqlstartDate, sqlendDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, boardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return projectList;
    }
    
    //return a list of projects based on the search input of project information
    //called by filterProjectListByProjectInfo in ProjectController
    public static ArrayList<Project> filterProjectListByProjectInfo(String input) {
        String sql = "SELECT * FROM project WHERE projectId LIKE ? OR projectDescription LIKE ? "
        		+ "OR projectStartDate LIKE ? OR projectEndDate LIKE ? OR warrantyEndDate LIKE ? "
        		+ "OR warrantyStartDate LIKE ? OR client LIKE ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        projectList = new ArrayList<Project>();
        try{
        	
        	conn = ConnectionManager.getConnection();
        	ps = conn.prepareStatement(sql);
        	String searchCriteria = input+"%";
        	ps.setString(1, searchCriteria);
        	ps.setString(2, searchCriteria);
        	ps.setString(3, searchCriteria);
        	ps.setString(4, searchCriteria);
        	ps.setString(5, searchCriteria);
        	ps.setString(6, searchCriteria);
        	ps.setString(7, searchCriteria);
        	rs = ps.executeQuery();
            
        	while (rs.next()) {
        		String projectID = rs.getString(1);
                String description = rs.getString(2);
                int projectManagerId = rs.getInt(3);
                java.sql.Date startDate = rs.getDate(4);
                java.sql.Date endDate = rs.getDate(5);
                java.sql.Date actualStartDate = rs.getDate(6);
                java.sql.Date actualEndDate = rs.getDate(7);
                java.sql.Date warrantyStartDate = rs.getDate(8);
                java.sql.Date warrantyEndDate = rs.getDate(9);
                String client = rs.getString(10);
                String status = rs.getString(11);
                String lastModifiedBy = rs.getString(12);
                java.sql.Date lastModifiedDate = rs.getDate(13);
                String cardId = rs.getString(14);
                int subProjectNumber = rs.getInt(15);
                project = new Project(projectID, description, projectManagerId, startDate, endDate, actualStartDate, actualEndDate,
                		warrantyStartDate, warrantyEndDate, client, status, lastModifiedBy, lastModifiedDate, cardId, subProjectNumber);
                projectList.add(project);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }finally {
            ConnectionManager.close(conn, ps, null);
        }
        return projectList;
    }
    
	//retrieve the number of ongoing projects for the given period of time
	//called by retrieveNumOfOngoingProjectsPerDay in ProjectController
    public static int retrieveNumOfOngoingProjectsPerDay(String startDate, String endDate) {
        String sql = "SELECT count(*) FROM project WHERE (projectStartDate <= ? and projectEndDate >= ?) or (projectEndDate >= ? and projectStartDate <= ?) or (projectStartDate >= ? and projectEndDate <= ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int number = 0;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setString(4, endDate);
            ps.setString(5, startDate);
            ps.setString(6, endDate);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                number = rs.getInt(1);
                break;
            }
        } catch (Exception e) {
        } finally {
            ConnectionManager.close(conn, ps, rs);
        }
        return number;
    }
}
