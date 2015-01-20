package controllers;

import play.*;
import play.mvc.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import DAO.*;
import entity.*;
import views.html.*;


public class ProjectController extends Controller {
    
	//Retrieve a list of PM whose number of ongoing projects in the given period < max project number
	//This method is called by retrieveAvailableAndUnavailablePM method in Project Controller
	public static JSONArray retrieveAvailablePM(String input) {
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	JSONArray returnJSONArray = new JSONArray(); 
    	String startDate = (String)inputJson.get("startDate");
    	String endDate = (String)inputJson.get("endDate");
    	if(startDate==null||startDate.equals("")){
    		ArrayList<Staff> availablePMList = StaffManager.retrieveAllPM();
    		for(int i=0;i<availablePMList.size();i++){
	    		Staff PM = availablePMList.get(i);
	    		JSONObject returnJson = new JSONObject();
	    		returnJson.put("maxProjectNum",PM.getMaxProjectNum());
	        	returnJson.put("status",PM.getStatus());
	        	returnJson.put("email",PM.getEmail());
	        	returnJson.put("staffID",PM.getStaffID());
	        	returnJson.put("role",PM.getRole());
	        	returnJson.put("name",PM.getName());
	        	
	        	returnJson.put("ongoingProjectNum",ProjectManager.retrieveOngoingProjectNumber(PM.getStaffID(), startDate, endDate));
				returnJSONArray.add(returnJson);
	    	}
    	}else{
    	
	    	ArrayList<Staff> availablePMList = StaffManager.retrieveAvailablePM(startDate, endDate);
	    	System.out.println(availablePMList.size());
	    	for(int i=0;i<availablePMList.size();i++){
	    		Staff PM = availablePMList.get(i);
	    		JSONObject returnJson = new JSONObject();
	    		returnJson.put("maxProjectNum",PM.getMaxProjectNum());
	        	returnJson.put("status",PM.getStatus());
	        	returnJson.put("email",PM.getEmail());
	        	returnJson.put("staffID",PM.getStaffID());
	        	returnJson.put("role",PM.getRole());
	        	returnJson.put("name",PM.getName());
	        	
	        	returnJson.put("ongoingProjectNum",ProjectManager.retrieveOngoingProjectNumber(PM.getStaffID(), startDate, endDate));
				returnJSONArray.add(returnJson);
	    	}
	    	
	    	ArrayList<Staff> totallyFreePMList = StaffManager.retrieveTotallyFreePM(startDate, endDate);
	    	
	    	for(int i=0;i<totallyFreePMList.size();i++){
	    		Staff PM = totallyFreePMList.get(i);
	    		JSONObject returnJson = new JSONObject();
	    		returnJson.put("maxProjectNum",PM.getMaxProjectNum());
	        	returnJson.put("status",PM.getStatus());
	        	returnJson.put("email",PM.getEmail());
	        	returnJson.put("staffID",PM.getStaffID());
	        	returnJson.put("role",PM.getRole());
	        	returnJson.put("name",PM.getName());
	        	
	        	returnJson.put("ongoingProjectNum",ProjectManager.retrieveOngoingProjectNumber(PM.getStaffID(), startDate, endDate));
				returnJSONArray.add(returnJson);
	    	}
    	}
    	
    	return returnJSONArray;
    }
      
	//Retrieve a list of PM whose number of ongoing projects in the given period >= max project number
	//This method is called by retrieveAvailableAndUnavailablePM method in Project Controller
    public static JSONArray retrieveUnavailablePM(String input) {
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	JSONArray returnJSONArray = new JSONArray(); 
    	String startDate = (String)inputJson.get("startDate");
    	String endDate = (String)inputJson.get("endDate");
    	
    	if(startDate==null||startDate.equals("")){
    		//return empty JSON when no date is given
    	}else{
    	
	    	ArrayList<Staff> unavailablePMList = StaffManager.retrieveUnavailablePM(startDate, endDate);
	    	
	    	for(int i=0;i<unavailablePMList.size();i++){
	    		Staff PM = unavailablePMList.get(i);
	    		JSONObject returnJson = new JSONObject();
	    		returnJson.put("maxProjectNum",PM.getMaxProjectNum());
	        	returnJson.put("status",PM.getStatus());
	        	returnJson.put("email",PM.getEmail());
	        	returnJson.put("staffID",PM.getStaffID());
	        	returnJson.put("role",PM.getRole());
	        	returnJson.put("name",PM.getName());
	        	
	        	returnJson.put("ongoingProjectNum",ProjectManager.retrieveOngoingProjectNumber(PM.getStaffID(), startDate, endDate));
				returnJSONArray.add(returnJson);
	    	}
    	}
    	return (returnJSONArray);
    }
    
    //Retrieve a list of PM whose number of ongoing projects in the given period < max project number; and another list of PM whose number of ongoing projects in the given period >= max project number
  	//This method is called when user tries to create a project and assign PM to it 
    public static Result retrieveAvailableAndUnavailablePM(String input) {
    	JSONObject returnJson = new JSONObject();
    	returnJson.put("availablePMList",retrieveAvailablePM(input));
    	returnJson.put("unavailablePMList",retrieveUnavailablePM(input));
    	//Object inputObj = JSONValue.parse(ProjectController.RetrieveAvailablePM(input));
    	return ok(returnJson.toJSONString());
    }
    
    //Return a list of all projects
    //called in project summary page 
    public static Result retrieveAllProjects(){
    	JSONArray returnJSONArray = new JSONArray(); 
    	ArrayList<Project> projectList = ProjectManager.retrieveAll();
    	for(int i=0;i<projectList.size();i++){
    		Project project = projectList.get(i);
    		JSONObject returnJson = new JSONObject();
    		returnJson.put("projectId",project.getProjectId());
        	returnJson.put("projectDescription",project.getProjectDescription());
        	returnJson.put("projectManager", project.getProjectManagerId());
        	int projectManagerID = project.getProjectManagerId(); 
        	Staff projectManager = StaffManager.retrieveStaffByID(projectManagerID);
        	String projectManagerName = projectManager.getName();
        	returnJson.put("projectManagerName",projectManagerName);
        	returnJson.put("projectStartDate",project.getProjectStartDate().toString());
        	returnJson.put("projectEndDate",project.getProjectEndDate().toString());
        	returnJson.put("warrantyStartDate",project.getWarrantyStartDate().toString());
        	returnJson.put("warrantyEndDate",project.getWarrantyEndDate().toString());
        	returnJson.put("client", project.getClient());
        	returnJson.put("status", project.getStatus());
        	//String listID = ProjectManager.retrieveListId(project.getProjectId(),project.getStatus());
        	//returnJson.put("listID", listID);
        	returnJson.put("lastModifiedBy", project.getLastModifiedBy());
        	returnJson.put("lastModifiedDate", project.getLastModifiedDate().toString());
        	
			returnJSONArray.add(returnJson);
    	}
    	return ok(returnJSONArray.toJSONString());
    }
    
    //Return the specified project based on the projectID given
    //called when users view details of a project and edit project
    public static Result retrieveProjectByID(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String projectID = (String)inputJson.get("projectID");
    		Project project = ProjectManager.retrieveProjectById(projectID);
    		
    		returnJson.put("projectId",projectID);
        	returnJson.put("projectDescription",project.getProjectDescription());
        	returnJson.put("projectStartDate",project.getProjectStartDate().toString());
        	returnJson.put("projectManager", project.getProjectManagerId());
        	returnJson.put("projectEndDate", project.getProjectEndDate().toString());
        	returnJson.put("warrantyStartDate",project.getWarrantyStartDate().toString());
        	returnJson.put("warrantyEndDate",project.getWarrantyEndDate().toString());
        	returnJson.put("client",project.getClient());
        	//status
        	returnJson.put("status",project.getStatus());
        	//String listID = ProjectManager.retrieveListId(project.getStatus());
        	//returnJson.put("listID", listID);
        	returnJson.put("boardId",project.getBoardId());
        	returnJson.put("lastModifiedBy", project.getLastModifiedBy());
        	returnJson.put("lastModifiedDate", project.getLastModifiedDate().toString());
        	
        	Staff projectManager = StaffManager.retrieveStaffByID(project.getProjectManagerId());
        	returnJson.put("projectManagerName",projectManager.getName());
        	
        	ArrayList<Staff> staffList = StaffManager.retrieveStaffByProject(projectID);
        	JSONArray staffJSONArray = new JSONArray();
    		for(int i=0;i<staffList.size();i++){
	    		Staff staff = staffList.get(i);
	    		JSONObject staffJson = new JSONObject();
	    		staffJson.put("status",staff.getStatus());
	    		staffJson.put("email",staff.getEmail());
	    		staffJson.put("staffID",staff.getStaffID());
	    		staffJson.put("role",staff.getRole());
	    		staffJson.put("name",staff.getName());
	        	
	    		staffJSONArray.add(staffJson);
	    	}
    		returnJson.put("staffList",staffJSONArray);
        	
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }

    //return a list of staff under this project and a list of staff outside the project but with the same skillset
    //called when user tries to create a task and assgin staff to it 
    public static Result retrieveStaffByProject(String input) {
    	JSONObject returnJson = new JSONObject();
    	JSONArray returnJSONArray1 = new JSONArray(); 
    	JSONArray returnJSONArray2 = new JSONArray();
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String projectID = (String) inputJson.get("projectID");
    		JSONArray skillsListJson = (JSONArray)inputJson.get("skills");
    		
    		//retrieve the list of staff from this project
    		ArrayList<Staff> staffList1 = StaffManager.retrieveStaffByProject(projectID);
    		
    		for(int i=0;i<staffList1.size();i++){
	    		Staff staff = staffList1.get(i);
	    		JSONObject returnJson1 = new JSONObject();
	        	returnJson1.put("status",staff.getStatus());
	        	returnJson1.put("email",staff.getEmail());
	        	returnJson1.put("staffID",staff.getStaffID());
	        	returnJson1.put("role",staff.getRole());
	        	returnJson1.put("name",staff.getName());
	        	
				returnJSONArray1.add(returnJson1);
	    	}
    		returnJson.put("staffFromThisProject",returnJSONArray1);
    		
    		//retrieve the list of staff outside this project but with the same skill set
    		ArrayList<Staff> staffList2 = new ArrayList<Staff>();
    		
    		if(skillsListJson==null||skillsListJson.size()==0){
    			ArrayList<Staff> staffList = StaffManager.retrieveAll();
    			for(int a = 0; a< staffList.size();a++){
    				boolean dupilicate = false;
    				for (int b = 0; b< staffList1.size();b++){
    					if(staffList1.get(b).getStaffID()==staffList.get(a).getStaffID()){
    						dupilicate = true;
    					}
    				}
    				if(dupilicate==false){
    					staffList2.add(staffList.get(a));
    				}
    			}
    			
    		}else{
	    		for(int i=0 ; i<skillsListJson.size(); i++){
	    			String skill = (String)skillsListJson.get(i);
	    			ArrayList<Staff> staffList = StaffManager.retrieveStaffBySkills(skill);
	    			for(int j=0;j<staffList.size();j++){
	    				boolean dupilicate = false;
	    				for (int k=0;k<staffList2.size();k++){
	    					if(staffList2.get(k).getStaffID()==staffList.get(j).getStaffID()){
	    						dupilicate = true;
	    					}
	    				}
	    				
	    				for (int k=0;k<staffList1.size();k++){
	    					if(staffList1.get(k).getStaffID()==staffList.get(j).getStaffID()){
	    						dupilicate = true;
	    					}
	    				}
	    				
	    				if(dupilicate==false){
	    					staffList2.add(staffList.get(j));
	    				}
	    			}
	    		}
    		}
    		
    		for(int i=0;i<staffList2.size();i++){
	    		Staff staff = staffList2.get(i);
	    		JSONObject returnJson2 = new JSONObject();
	    		returnJson2.put("status",staff.getStatus());
	    		returnJson2.put("email",staff.getEmail());
	    		returnJson2.put("staffID",staff.getStaffID());
	    		returnJson2.put("role",staff.getRole());
	    		returnJson2.put("name",staff.getName());
	        	
	    		returnJSONArray2.add(returnJson2);
	    	}
    		returnJson.put("staffOutsideThisProject",returnJSONArray2);
    	}catch(Exception e){
    		
    	}
    	return ok(returnJson.toString());
    }

    //retrieve a list of projects under the given staffID
    //called by the staff calendar
    public static Result retrieveProjectByStaff(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		JSONArray returnJSONArray = new JSONArray(); 
		String staffIDStr = (String)inputJson.get("staffID");
		int staffID = Integer.parseInt(staffIDStr);
		ArrayList<Project> projectList = ProjectManager.retrieveProjectByStaff(staffID);
		if(projectList!=null){
			for(int i=0;i<projectList.size();i++){
				JSONObject returnJson = new JSONObject();
				Project project = projectList.get(i);
	    		returnJson.put("projectID",project.getProjectId());
	    		Date startDate = project.getProjectStartDate();
	        	returnJson.put("startDate",startDate.toString());
	        	Date endDate = project.getProjectEndDate();
	        	returnJson.put("endDate",endDate.toString());
	        	Date warrantyStartDate = project.getWarrantyStartDate();
	        	returnJson.put("warrantyStartDate",warrantyStartDate.toString());
	        	Date warrantyEndDate = project.getWarrantyEndDate();
	        	returnJson.put("warrantyEndDate",warrantyEndDate.toString());
	        	int projectManagerID = project.getProjectManagerId(); 
	        	Staff projectManager = StaffManager.retrieveStaffByID(projectManagerID);
	        	String projectManagerName = projectManager.getName();
	        	returnJson.put("projectManagerName",projectManagerName);
				returnJSONArray.add(returnJson);
	    	}
		}
		return ok(returnJSONArray.toJSONString());
	}

    //retrieve the actual start date of a project
  	//called for delay purpose in project page
	public static Result retrieveProjectActualStartDate(String input) {
		JSONObject returnJson = new JSONObject(); 
		try{
			Object inputObj = JSONValue.parse(input);
			JSONObject inputJson = (JSONObject)inputObj;
			String projectID = (String)inputJson.get("projectID");
			Project project = ProjectManager.retrieveProjectById(projectID);		
			if(project.getStatus().equals("To do")){
	    		returnJson.put("actualStartDate","this project has not started");
	    	}else{
	    		returnJson.put("actualStartDate",project.getActualStartDate());
	    	}
		}catch(Exception e){
			returnJson.put("status",false);
		}
		return ok(returnJson.toJSONString());
	}

	//retrieve the actual end date of a project
	//called for delay purpose in project page
	public static Result retrieveProjectActualEndDate(String input) {
		JSONObject returnJson = new JSONObject(); 
		try{
			Object inputObj = JSONValue.parse(input);
			JSONObject inputJson = (JSONObject)inputObj;
			String projectID = (String)inputJson.get("projectID");
			Project project = ProjectManager.retrieveProjectById(projectID);
			if (project.getStatus().equals("Closed")){
				returnJson.put("actualEndDate",project.getActualEndDate());
			}else{
				returnJson.put("actualEndDate","this project has not ended");
			}
			
		}catch(Exception e){
			returnJson.put("status",false);
		}
		return ok(returnJson.toJSONString());
	}

	//retrieve all ongoing projects within a given period of time
	//called in the simulation page when users choose to view all details of ongoing projects
	public static Result retrieveOngoingProjects(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String startDate = (String)inputJson.get("startDate");
		String endDate = (String)inputJson.get("endDate");
		JSONArray returnJSONArray = new JSONArray(); 
		int number = 0;
		try{
			ArrayList<Project> projectList = ProjectManager.retrieveOngoingProjects(startDate,endDate);
			if(projectList!=null){
	    		for(int i=0;i<projectList.size();i++){
	    			JSONObject returnJson = new JSONObject();
	    			Project project = projectList.get(i);
		    		returnJson.put("projectID",project.getProjectId());
		    		Date projectStartDate = project.getProjectStartDate();
		        	returnJson.put("startDate",projectStartDate.toString());
		        	Date projectEndDate = project.getProjectEndDate();
		        	returnJson.put("endDate",projectEndDate.toString());
		        	Date warrantyStartDate = project.getWarrantyStartDate();
		        	returnJson.put("warrantyStartDate",warrantyStartDate.toString());
		        	Date warrantyEndDate = project.getWarrantyEndDate();
		        	returnJson.put("warrantyEndDate",warrantyEndDate.toString());
		        	int projectManagerID = project.getProjectManagerId(); 
		        	Staff projectManager = StaffManager.retrieveStaffByID(projectManagerID);
		        	String projectManagerName = projectManager.getName();
		        	returnJson.put("projectManagerName",projectManagerName);
					returnJSONArray.add(returnJson);
	        	}
			}
		}catch(Exception e){
		}
		return ok(returnJSONArray.toJSONString());
	}

	//retrieve the number of ongoing projects for each day during the given period of time
	//called in the simulation page, line chart
	public static Result retrieveNumOfOngoingProjectsPerDay(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		JSONArray returnJSONArray = new JSONArray(); 
		
		String startDateStr = (String)inputJson.get("startDate");
		String endDateStr = (String)inputJson.get("endDate");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     
	    Calendar c = Calendar.getInstance();
	    
		try{
			java.util.Date sdate = sdf.parse(startDateStr);
			java.util.Date edate = sdf.parse(endDateStr);
			while(sdate.compareTo(edate)<=0){
				int number = ProjectManager.retrieveNumOfOngoingProjectsPerDay(sdf.format(sdate), sdf.format(sdate));
				JSONObject returnJson = new JSONObject();
				returnJson.put("Date",sdf.format(sdate));
				returnJson.put("numberOfProjects",number);
				returnJSONArray.add(returnJson);
				
				c.setTime(sdate);
		        c.add(Calendar.DATE, 1);
		        sdate = c.getTime();
			}
			
		}catch(Exception e){
		}
		return ok(returnJSONArray.toJSONString());
	}

	//retrieve number of project started for each month in the given year
	//called in the simulation page, calendar view
	public static Result retrieveAverageProjectNumberPerMonth(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		JSONArray returnJSONArray = new JSONArray(); 
		String year = (String)inputJson.get("year");
		int number = 0;
		//Jan
		JSONObject returnJson1 = new JSONObject();
		returnJson1.put("month","Jan");
		String startDate1 = year + "-" + "01" + "-01";
		String endDate1 = year + "-" + "01" + "-31";
		ArrayList<Project> projectList1 = ProjectManager.retrieveProjectsStart(startDate1,endDate1);
		number = projectList1.size();
		returnJson1.put("number",number);
		returnJSONArray.add(returnJson1);
		
		//Feb
		JSONObject returnJson2 = new JSONObject();
		returnJson2.put("month","Feb");
		String startDate2 = year + "-" + "02" + "-01";
		String endDate2 = year + "-" + "02" + "-28";
		ArrayList<Project> projectList2 = ProjectManager.retrieveProjectsStart(startDate2,endDate2);
		number = projectList2.size();
		returnJson2.put("number",number);
		returnJSONArray.add(returnJson2);
		
		//Mar
		JSONObject returnJson3 = new JSONObject();
		returnJson3.put("month","Mar");
		String startDate3 = year + "-" + "03" + "-01";
		String endDate3 = year + "-" + "03" + "-31";
		ArrayList<Project> projectList3 = ProjectManager.retrieveProjectsStart(startDate3,endDate3);
		number = projectList3.size();
		returnJson3.put("number",number);
		returnJSONArray.add(returnJson3);
		
		//Apr
		JSONObject returnJson4 = new JSONObject();
		returnJson4.put("month","Apr");
		String startDate4 = year + "-" + "04" + "-01";
		String endDate4 = year + "-" + "04" + "-30";
		ArrayList<Project> projectList4 = ProjectManager.retrieveProjectsStart(startDate4,endDate4);
		number = projectList4.size();
		returnJson4.put("number",number);
		returnJSONArray.add(returnJson4);
		
		//May
		JSONObject returnJson5 = new JSONObject();
		returnJson5.put("month","May");
		String startDate5 = year + "-" + "05" + "-01";
		String endDate5 = year + "-" + "05" + "-31";
		ArrayList<Project> projectList5 = ProjectManager.retrieveProjectsStart(startDate5,endDate5);
		number = projectList5.size();
		returnJson5.put("number",number);
		returnJSONArray.add(returnJson5);
		
		//Jun
		JSONObject returnJson6 = new JSONObject();
		returnJson6.put("month","Jun");
		String startDate6 = year + "-" + "06" + "-01";
		String endDate6 = year + "-" + "06" + "-30";
		ArrayList<Project> projectList6 = ProjectManager.retrieveProjectsStart(startDate6,endDate6);
		number = projectList6.size();
		returnJson6.put("number",number);
		returnJSONArray.add(returnJson6);
		
		//July
		JSONObject returnJson7 = new JSONObject();
		returnJson7.put("month","Jul");
		String startDate7 = year + "-" + "07" + "-01";
		String endDate7 = year + "-" + "07" + "-31";
		ArrayList<Project> projectList7 = ProjectManager.retrieveProjectsStart(startDate7,endDate7);
		number = projectList7.size();
		returnJson7.put("number",number);
		returnJSONArray.add(returnJson7);
		
		//Aug
		JSONObject returnJson8 = new JSONObject();
		returnJson8.put("month","Aug");
		String startDate8 = year + "-" + "08" + "-01";
		String endDate8 = year + "-" + "08" + "-31";
		ArrayList<Project> projectList8 = ProjectManager.retrieveProjectsStart(startDate8,endDate8);
		number = projectList8.size();
		returnJson8.put("number",number);
		returnJSONArray.add(returnJson8);
		
		//Sep
		JSONObject returnJson9 = new JSONObject();
		returnJson9.put("month","Sep");
		String startDate9 = year + "-" + "09" + "-01";
		String endDate9 = year + "-" + "09" + "-30";
		ArrayList<Project> projectList9 = ProjectManager.retrieveProjectsStart(startDate9,endDate9);
		number = projectList9.size();
		returnJson9.put("number",number);
		returnJSONArray.add(returnJson9);
		
		//Oct
		JSONObject returnJson10 = new JSONObject();
		returnJson10.put("month","Oct");
		String startDate10 = year + "-" + "10" + "-01";
		String endDate10 = year + "-" + "10" + "-31";
		ArrayList<Project> projectList10 = ProjectManager.retrieveProjectsStart(startDate10,endDate10);
		number = projectList10.size();
		returnJson10.put("number",number);
		returnJSONArray.add(returnJson10);
		
		//Nov
		JSONObject returnJson11 = new JSONObject();
		returnJson11.put("month","Nov");
		String startDate11 = year + "-" + "11" + "-01";
		String endDate11 = year + "-" + "11" + "-30";
		ArrayList<Project> projectList11 = ProjectManager.retrieveProjectsStart(startDate11,endDate11);
		number = projectList11.size();
		returnJson11.put("number",number);
		returnJSONArray.add(returnJson11);
		
		//Dec
		JSONObject returnJson12 = new JSONObject();
		returnJson12.put("month","Dec");
		String startDate12 = year + "-" + "12" + "-01";
		String endDate12 = year + "-" + "12" + "-31";
		ArrayList<Project> projectList12 = ProjectManager.retrieveProjectsStart(startDate12,endDate12);
		number = projectList12.size();
		returnJson12.put("number",number);
		returnJSONArray.add(returnJson12);
		
		return ok(returnJSONArray.toJSONString());
	}

	//retrieve a list of projects which due in the given period of time
	//called in simulation result
	public static Result retrieveProjectsDue(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String startDate = (String)inputJson.get("startDate");
		String endDate = (String)inputJson.get("endDate");
		JSONArray returnJSONArray = new JSONArray(); 
		int number = 0;
		try{
			ArrayList<Project> projectList = ProjectManager.retrieveProjectsDue(startDate,endDate);
			if(projectList!=null){
	    		for(int i=0;i<projectList.size();i++){
	    			JSONObject returnJson = new JSONObject();
	    			Project project = projectList.get(i);
		    		returnJson.put("projectID",project.getProjectId());
		    		Date projectStartDate = project.getProjectStartDate();
		        	returnJson.put("startDate",projectStartDate.toString());
		        	Date projectEndDate = project.getProjectEndDate();
		        	returnJson.put("endDate",projectEndDate.toString());
		        	Date warrantyStartDate = project.getWarrantyStartDate();
		        	returnJson.put("warrantyStartDate",warrantyStartDate.toString());
		        	Date warrantyEndDate = project.getWarrantyEndDate();
		        	returnJson.put("warrantyEndDate",warrantyEndDate.toString());
		        	int projectManagerID = project.getProjectManagerId(); 
		        	Staff projectManager = StaffManager.retrieveStaffByID(projectManagerID);
		        	String projectManagerName = projectManager.getName();
		        	returnJson.put("projectManagerName",projectManagerName);
					returnJSONArray.add(returnJson);
	        	}
			}
		}catch(Exception e){
		}
		return ok(returnJSONArray.toJSONString());
	}

	//retrieve a list of projects on warranty within the given period of time
	//called in simulation result
	public static Result retrieveProjectsOnWarranty(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String startDate = (String)inputJson.get("startDate");
		String endDate = (String)inputJson.get("endDate");
		JSONArray returnJSONArray = new JSONArray(); 
		int number = 0;
		try{
			ArrayList<Project> projectList = ProjectManager.retrieveProjectsOnWarranty(startDate,endDate);
			number = projectList.size();
			if(projectList!=null){
	    		for(int i=0;i<projectList.size();i++){
	    			JSONObject returnJson = new JSONObject();
	    			Project project = projectList.get(i);
		    		returnJson.put("projectID",project.getProjectId());
		    		Date projectStartDate = project.getProjectStartDate();
		        	returnJson.put("startDate",projectStartDate.toString());
		        	Date projectEndDate = project.getProjectEndDate();
		        	returnJson.put("endDate",projectEndDate.toString());
		        	Date warrantyStartDate = project.getWarrantyStartDate();
		        	returnJson.put("warrantyStartDate",warrantyStartDate.toString());
		        	Date warrantyEndDate = project.getWarrantyEndDate();
		        	returnJson.put("warrantyEndDate",warrantyEndDate.toString());
		        	int projectManagerID = project.getProjectManagerId(); 
		        	Staff projectManager = StaffManager.retrieveStaffByID(projectManagerID);
		        	String projectManagerName = projectManager.getName();
		        	returnJson.put("projectManagerName",projectManagerName);
					returnJSONArray.add(returnJson);
	        	}
			}
		}catch(Exception e){
		}
		return ok(returnJSONArray.toJSONString());
	}

	//create project
	//called when user creates a project
	public static Result createProject(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		JSONObject returnJson = new JSONObject();
		String projectDescription = (String)inputJson.get("projectDescription");
		String projectInitials = (String)inputJson.get("projectInitials");
		String projectManagerIDStr = (String)inputJson.get("projectManager");
		int projectManagerID = Integer.parseInt(projectManagerIDStr);
		String accountID = session("accountID");
		//String accountID = "testing";
		Account account = AccountManager.retrieveAccountByID(accountID);
		String accountName = account.getName();
		//String accountName = "testing";
		String projectStartDate = (String)inputJson.get("projectStartDate");
		String projectEndDate = (String)inputJson.get("projectEndDate");
		String client = (String)inputJson.get("client");
		
		Project project = ProjectManager.populateProjectDB(projectInitials, projectDescription, projectManagerID, projectStartDate, projectEndDate, client,accountName);
		String projectID = project.getProjectId();
		JSONArray staffListJson = (JSONArray)inputJson.get("staffList");
		if (staffListJson!=null){
			for(int i=0 ; i<staffListJson.size(); i++){
				String staffIDStr = (String)staffListJson.get(i);
				int staffID = Integer.parseInt(staffIDStr);
				ProjectManager.populateProjectStaffs(projectID, staffID);
			}
		}
		
		ProjectManager.populateProjectStaffs(projectID, projectManagerID);
		
		Date endDate = project.getProjectEndDate();
		String month = "";
		if(endDate.getMonth()==1){
			month = "January";
		}else if(endDate.getMonth()==2){
			month = "February";
		}else if(endDate.getMonth()==3){
			month = "March";
		}else if(endDate.getMonth()==4){
			month = "April";
		}else if(endDate.getMonth()==5){
			month = "May";
		}else if(endDate.getMonth()==6){
			month = "June";
		}else if(endDate.getMonth()==7){
			month = "July";
		}else if(endDate.getMonth()==8){
			month = "August";
		}else if(endDate.getMonth()==9){
			month = "September";
		}else if(endDate.getMonth()==10){
			month = "October";
		}else if(endDate.getMonth()==11){
			month = "November";
		}else{
			month = "December";
		}
		int year = endDate.getYear()+1900; 
		String dueDate = month + " " + endDate.getDate() + ", " + year + " 12:00:00";
		
		returnJson.put("boardName", projectID);
		returnJson.put("Desc", project.getProjectDescription());
		returnJson.put("IDMembers", staffListJson);
		returnJson.put("status", project.getStatus());
		//returnJson.put("idList", ProjectManager.retrieveListId(project.getStatus()));
		returnJson.put("dueDate", dueDate);
		
		return ok(returnJson.toJSONString());
	}

	//update the boardId in the project table
    //called after a project is created in Trello as a board
    public static Result updateProjectBoardId(String input){
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	JSONObject returnJson = new JSONObject();
    	String boardId = (String)inputJson.get("id");
    	String projectID = (String)inputJson.get("boardName");
    	   	
		Project project = ProjectManager.updateProejctBoardId(boardId, projectID);
		returnJson.put("status",true);
		return ok(returnJson.toJSONString());
    }
    
    //update the respective listID of a project in trelloid table
    //called after a project is created in Trello as a board
    public static Result updateProjectListId(String input){
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	JSONObject returnJson = new JSONObject();
    	String projectID = (String)inputJson.get("boardName");
    	String toDo = (String)inputJson.get("To do");
    	String doing = (String)inputJson.get("Doing");
    	String done = (String)inputJson.get("Done"); 
    	   	
		ProjectManager.updateProejctListId(projectID,"To do",toDo);
		ProjectManager.updateProejctListId(projectID,"Doing",doing);
		ProjectManager.updateProejctListId(projectID,"Done",done);
		returnJson.put("status",true);
		return ok(returnJson.toJSONString());
    }
    
    //update project
    //called when user updates a project
    public static Result updateProject(String input) {
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	JSONObject returnJson = new JSONObject();
    	String projectDescription = (String)inputJson.get("projectDescription");
    	String projectID = (String)inputJson.get("projectID");
    	String projectManagerIDStr = (String)inputJson.get("projectManager");
    	int projectManagerID = Integer.parseInt(projectManagerIDStr);
    	String accountID = session("accountID");
    	//String accountID = "testing";
    	Account account = AccountManager.retrieveAccountByID(accountID);
    	String accountName = account.getName();
    	//String accountName = "testing";
    	String projectStartDate = (String)inputJson.get("projectStartDate");
    	String projectEndDate = (String)inputJson.get("projectEndDate");
    	String client = (String)inputJson.get("client");
    	
		Project project = ProjectManager.updateProject(projectID, projectDescription, projectStartDate, projectEndDate, projectManagerID, accountName);
		JSONArray staffListJson = (JSONArray)inputJson.get("staffList");
		StaffManager.deleteProjectStaffs(projectID);
		if (staffListJson!=null){
    		for(int i=0 ; i<staffListJson.size(); i++){
    			String staffIDStr = (String)staffListJson.get(i);
    			int staffID = Integer.parseInt(staffIDStr);
    			ProjectManager.populateProjectStaffs(projectID, staffID);
    		}
    	}
    	ProjectManager.populateProjectStaffs(projectID, projectManagerID);
		
    	Date endDate = project.getProjectEndDate();
    	String month = "";
    	if(endDate.getMonth()==1){
    		month = "January";
    	}else if(endDate.getMonth()==2){
    		month = "February";
    	}else if(endDate.getMonth()==3){
    		month = "March";
    	}else if(endDate.getMonth()==4){
    		month = "April";
    	}else if(endDate.getMonth()==5){
    		month = "May";
    	}else if(endDate.getMonth()==6){
    		month = "June";
    	}else if(endDate.getMonth()==7){
    		month = "July";
    	}else if(endDate.getMonth()==8){
    		month = "August";
    	}else if(endDate.getMonth()==9){
    		month = "September";
    	}else if(endDate.getMonth()==10){
    		month = "October";
    	}else if(endDate.getMonth()==11){
    		month = "November";
    	}else{
    		month = "December";
    	}
    	int year = endDate.getYear()+1900; 
    	String dueDate = month + " " + endDate.getDate() + ", " + year + " 12:00:00";
    	
    	returnJson.put("boardID", project.getBoardId());
    	returnJson.put("boardName", projectID);
    	returnJson.put("Desc", project.getProjectDescription());
    	returnJson.put("IDMembers", staffListJson);
    	returnJson.put("status", project.getStatus());
    	//returnJson.put("idList", ProjectManager.retrieveListId(project.getStatus()));
    	returnJson.put("dueDate", dueDate);
		
		return ok(returnJson.toJSONString());
    }
    
    //return a list of projects based on the search input of project information
    //called by search bar in project page
	public static Result filterProjectListByProjectInfo(String input) {
		JSONArray returnJSONArray = new JSONArray();
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String projectInfo = (String)inputJson.get("projectInfo");
		
		ArrayList<Project> projectList = ProjectManager.filterProjectListByProjectInfo(projectInfo);
		if(projectList!=null){
			for(int i=0;i<projectList.size();i++){
				JSONObject returnJson = new JSONObject();
				Project project = projectList.get(i);
	    		returnJson.put("projectID",project.getProjectId());
	    		Date projectStartDate = project.getProjectStartDate();
	        	returnJson.put("startDate",projectStartDate.toString());
	        	Date projectEndDate = project.getProjectEndDate();
	        	returnJson.put("endDate",projectEndDate.toString());
	        	Date warrantyStartDate = project.getWarrantyStartDate();
	        	returnJson.put("warrantyStartDate",warrantyStartDate.toString());
	        	Date warrantyEndDate = project.getWarrantyEndDate();
	        	returnJson.put("warrantyEndDate",warrantyEndDate.toString());
	        	int projectManagerID = project.getProjectManagerId(); 
	        	Staff projectManager = StaffManager.retrieveStaffByID(projectManagerID);
	        	String projectManagerName = projectManager.getName();
	        	returnJson.put("projectManagerName",projectManagerName);
	        	returnJson.put("client",project.getClient());
				returnJSONArray.add(returnJson);
	    	}
		}
		return ok(returnJSONArray.toJSONString());
	}

	//create subproject
	//called when user creates a subproject
	public static Result createSubproject(String input) {
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	JSONObject returnJson = new JSONObject();
    	String subprojectDescription = (String)inputJson.get("subprojectDescription");
    	String subprojectName = (String)inputJson.get("subprojectName");
    	String projectID = (String)inputJson.get("projectID");
    	String subprojectType = (String)inputJson.get("subprojectType");
    	String predecessorStr = (String)inputJson.get("predecessorID");
    	int predecessor = Integer.parseInt(predecessorStr);
    	String startDate = (String)inputJson.get("subprojectStartDate");
    	String endDate = (String)inputJson.get("subprojectEndDate");
    	
    	Subproject subproject = SubprojectManager.populateSubprojectDB(projectID, subprojectName,subprojectDescription, startDate, endDate, subprojectType);
		int subprojectID = subproject.getSubprojectId();
		JSONArray staffListJson = (JSONArray)inputJson.get("staffList");
    	if (staffListJson!=null){
    		for(int i=0 ; i<staffListJson.size(); i++){
    			String staffIDStr = (String)staffListJson.get(i);
    			int staffID = Integer.parseInt(staffIDStr);
    			SubprojectManager.populateSubprojectStaffs(projectID, subprojectID, staffID);
    		}
    	}

    	if(predecessor!=0){
    		SubprojectManager.populateSubprojectSequence(projectID, predecessor, subprojectID);
    	}
    	
    	Date subprojectEndDate = subproject.getEndDate();
    	String month = "";
    	if(subprojectEndDate.getMonth()==1){
    		month = "January";
    	}else if(subprojectEndDate.getMonth()==2){
    		month = "February";
    	}else if(subprojectEndDate.getMonth()==3){
    		month = "March";
    	}else if(subprojectEndDate.getMonth()==4){
    		month = "April";
    	}else if(subprojectEndDate.getMonth()==5){
    		month = "May";
    	}else if(subprojectEndDate.getMonth()==6){
    		month = "June";
    	}else if(subprojectEndDate.getMonth()==7){
    		month = "July";
    	}else if(subprojectEndDate.getMonth()==8){
    		month = "August";
    	}else if(subprojectEndDate.getMonth()==9){
    		month = "September";
    	}else if(subprojectEndDate.getMonth()==10){
    		month = "October";
    	}else if(subprojectEndDate.getMonth()==11){
    		month = "November";
    	}else{
    		month = "December";
    	}
    	int year = subprojectEndDate.getYear()+1900; 
    	String dueDate = month + " " + subprojectEndDate.getDate() + ", " + year + " 12:00:00";
    	String number = String.format("%03d", subprojectID);
    	String cardName = number + "-" + subprojectName;
    	
    	returnJson.put("cardName", cardName);
    	returnJson.put("projectID", projectID);
    	returnJson.put("boardID", ProjectManager.retrieveProejctBoardId(projectID));
    	returnJson.put("subprojectID", subproject.getSubprojectId());
    	returnJson.put("subprojectName", subproject.getSubprojectName());
    	returnJson.put("Desc", subproject.getSubprojectDescription());
    	returnJson.put("IDMembers", staffListJson);
    	returnJson.put("status", subproject.getStatus());
    	returnJson.put("idList", ProjectManager.retrieveListId(projectID,subproject.getStatus()));
    	returnJson.put("dueDate", dueDate);
		
		return ok(returnJson.toJSONString());
    }
    
	//update subproject
	//called when user updates a subproject
    public static Result updateSubproject(String input) {
		Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		JSONObject returnJson = new JSONObject();
		String subprojectDescription = (String)inputJson.get("subprojectDescription");
		String subprojectName = (String)inputJson.get("subprojectName");
		String projectID = (String)inputJson.get("projectID");
		String subprojectIDStr = (String)inputJson.get("subprojectID");
		int subprojectID = Integer.parseInt(subprojectIDStr);
		String subprojectType = (String)inputJson.get("subprojectType");
		String predecessorStr = (String)inputJson.get("predecessorID");
		int predecessor = Integer.parseInt(predecessorStr);
		String startDate = (String)inputJson.get("subprojectStartDate");
		String endDate = (String)inputJson.get("subprojectEndDate");
		
		SubprojectManager.updateSubproject(projectID, subprojectID, subprojectName, subprojectDescription, startDate, endDate, subprojectType);
		
		JSONArray staffListJson = (JSONArray)inputJson.get("staffList");
		
		SubprojectManager.deleteAllSubprojectStaff(projectID,subprojectID);
		if (staffListJson!=null){
			for(int i=0 ; i<staffListJson.size(); i++){
				String staffIDStr = (String)staffListJson.get(i);
				int staffID = Integer.parseInt(staffIDStr);
				SubprojectManager.populateSubprojectStaffs(projectID, subprojectID, staffID);
			}
		}
		
		SubprojectManager.deleteAllSubprojectSequence(projectID,subprojectID);
		if(predecessor!=0){
			SubprojectManager.populateSubprojectSequence(projectID, predecessor, subprojectID);
		}
		
		Subproject subproject = SubprojectManager.retrieveSubprojectById(projectID, subprojectID);
		Date subprojectEndDate = subproject.getEndDate();
		String month = "";
		if(subprojectEndDate.getMonth()==1){
			month = "January";
		}else if(subprojectEndDate.getMonth()==2){
			month = "February";
		}else if(subprojectEndDate.getMonth()==3){
			month = "March";
		}else if(subprojectEndDate.getMonth()==4){
			month = "April";
		}else if(subprojectEndDate.getMonth()==5){
			month = "May";
		}else if(subprojectEndDate.getMonth()==6){
			month = "June";
		}else if(subprojectEndDate.getMonth()==7){
			month = "July";
		}else if(subprojectEndDate.getMonth()==8){
			month = "August";
		}else if(subprojectEndDate.getMonth()==9){
			month = "September";
		}else if(subprojectEndDate.getMonth()==10){
			month = "October";
		}else if(subprojectEndDate.getMonth()==11){
			month = "November";
		}else{
			month = "December";
		}
		int year = subprojectEndDate.getYear()+1900; 
		String dueDate = month + " " + subprojectEndDate.getDate() + ", " + year + " 12:00:00";
		String number = String.format("%03d", subprojectID);
		String cardName = number + "-" + subproject.getSubprojectName();
		
		returnJson.put("cardName", cardName);
		returnJson.put("projectID", projectID);
		returnJson.put("boardID", ProjectManager.retrieveProejctBoardId(projectID));
		returnJson.put("subprojectID", subproject.getSubprojectId());
		returnJson.put("subprojectName", subproject.getSubprojectName());
		returnJson.put("Desc", subproject.getSubprojectDescription());
		returnJson.put("IDMembers", staffListJson);
		returnJson.put("status", subproject.getStatus());
		returnJson.put("idList", ProjectManager.retrieveListId(projectID,subproject.getStatus()));
		returnJson.put("dueDate", dueDate);
		returnJson.put("subprojectCardID", subproject.getSubprojectCardId());
		
		return ok(returnJson.toJSONString());
	}

    //update subproject cardID
    //called after a subproject card is created in trello
	public static Result updateSubprojectCardId(String input){
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	JSONObject returnJson = new JSONObject();
    	String subprojectCardId = (String)inputJson.get("subprojectCardID");
    	String trelloCardId = (String)inputJson.get("subprojectCardName");
    	String projectId = (String)inputJson.get("boardName");
    	int subprojectId = Integer.parseInt(trelloCardId.substring(0,3));
    	   	
		SubprojectManager.updateSubprojectCardId(projectId, subprojectId, subprojectCardId);
		returnJson.put("status",true);
		return ok(returnJson.toJSONString());
    }
    
    //this method takes a JSON array of card objects from trello and update database accordingly
	//called once user clicks the update button
    public static Result updateFromTrello(String input){
    	Object inputObj = JSONValue.parse(input);
    	JSONArray inputJson = (JSONArray)inputObj;
    	JSONObject returnJson = new JSONObject();
    	JSONArray statusUpdateLog = new JSONArray();
    	JSONArray descUpdateLog = new JSONArray(); 
    	
    	//retrieve the staffId of the login user
		//String accountId = session("accountID");
		//String email = AccountManager.retrieveEmailByID(accountId);
		//int staffId = StaffManager.retrieveStaffIDByEmail(email);
    	//int staffId = 9;
    	
    	//loop through the array list of card objects
    	for (int i=0;i<inputJson.size();i++){
    		JSONObject cardObj = (JSONObject)inputJson.get(i);
    		String boardId  = (String)cardObj.get("idBoard");
    		String projectId  = ProjectManager.retrieveProejctIdByBoardId(boardId);

			//this is a subproject
    		String cardName = (String)cardObj.get("name");
			String subprojectIdStr = cardName.substring(0,3);
			int subprojectId = Integer.parseInt(subprojectIdStr);

			Project project = ProjectManager.retrieveProjectById(projectId);
			
			//check if this subproject is under the login user, if false, end the loop
			//if((project!=null)&&(project.getProjectManagerId()==staffId)){
				String cardId  = (String)cardObj.get("id");
				String description  = (String)cardObj.get("desc");
	    		String idList  = (String)cardObj.get("idList");
	    		String newStatus  = ProjectManager.retrieveStatus(idList);
	    		//String client = projectId.substring(9);
	    		//String dueDate = (String)cardObj.get("due");
	    		//String projectEndDate = dueDate.substring(0,10);
	    		String dateLastActivity = (String)cardObj.get("dateLastActivity");
	    		String lastModifiedDate = dateLastActivity.substring(0,10);
	    		//JSONArray stafflist =  (JSONArray)cardObj.get("idMembers");
	    		Subproject subproject = SubprojectManager.retrieveSubprojectById(projectId, subprojectId);
	    		
	    		if(subproject!=null){
	    			//check if the description is updated, if true, update the change in db and log the changes 
    	    		if(!subproject.getSubprojectDescription().equalsIgnoreCase(description)){
    	    			JSONObject descUpdate = new JSONObject();
    	    			descUpdate.put("originalDesc",subproject.getSubprojectDescription());
    	    			descUpdate.put("newDesc",description);
    	    			descUpdate.put("projectId", projectId);
    	    			descUpdate.put("subprojectId", subprojectId);
    	    			descUpdate.put("subprojectName", subproject.getSubprojectName());
    	    			
    	    			SubprojectManager.updateSubprojectDescFromTrello(projectId, subprojectId, description);
    	    			descUpdateLog.add(descUpdate);
    	    		}
	    		}
	    		
	    		//check if the status is updated, if true, log the changes 
	    		if(!newStatus.equals(subproject.getStatus())){
	    			JSONObject statusUpdate = new JSONObject();
	    			statusUpdate.put("originalStatus", subproject.getStatus());
	    			statusUpdate.put("originalIdList", ProjectManager.retrieveListId(projectId,subproject.getStatus()));
	    			statusUpdate.put("newStatus", newStatus);
	    			statusUpdate.put("newIdList", idList);
	    			statusUpdate.put("projectId", projectId);
	    			statusUpdate.put("subprojectId", subprojectId);
	    			statusUpdate.put("boardId", boardId);
	    			statusUpdate.put("cardId", cardId);
	    			statusUpdate.put("subprojectName", subproject.getSubprojectName());
	    			statusUpdateLog.add(statusUpdate);
	    		}
	    		
	    		returnJson.put("statusUpdateLog", statusUpdateLog);
	    		returnJson.put("descUpdateLog", descUpdateLog);
			//}
    		
    	}
		return ok(returnJson.toJSONString());
    }
    
    //this method is called after the status update of a project or subproject is approved by a PM. 
    //It takes in the projectId & subprojectId & updated status and updates the DB accordingly
    public static Result updateStatusFromTrello(String input){
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	String projectId = (String)inputJson.get("projectId");
    	String newStatus = (String)inputJson.get("newStatus");
    	
		String subprojectIdStr = (String)inputJson.get("subprojectId");
		int subprojectId = Integer.parseInt(subprojectIdStr);
		SubprojectManager.updateSubprojectStatusFromTrello(projectId, subprojectId, newStatus);

    	return ok("success");
    }
    
    public static Result retrieveAllSubprojects(String input){
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	String projectID = (String)inputJson.get("projectID");
    	
    	JSONArray returnJSONArray = new JSONArray(); 
    	ArrayList<Subproject> subprojectList = SubprojectManager.retrieveAll(projectID);
    	for(int i=0;i<subprojectList.size();i++){
    		Subproject subproject = subprojectList.get(i);
    		JSONObject returnJson = new JSONObject();
    		returnJson.put("projectId",subproject.getProjectId());
    		returnJson.put("subprojectId",subproject.getSubprojectId());
    		returnJson.put("subprojectName",subproject.getSubprojectName());
        	returnJson.put("subprojectDescription",subproject.getSubprojectDescription());
        	returnJson.put("subprojectStartDate",subproject.getStartDate().toString());
        	returnJson.put("subprojectEndDate",subproject.getEndDate().toString());
        	if(subproject.getActualStartDate()==null){
        		returnJson.put("actualStartDate","this subproject has not started");
        	}else{
        		returnJson.put("actualStartDate",subproject.getActualStartDate().toString());
        	}
        	if(subproject.getActualEndDate()==null){
        		returnJson.put("actualEndDate","this subproject has not ended");
        	}else{
        		returnJson.put("actualEndDate",subproject.getActualEndDate().toString());
        	}
        	returnJson.put("subporjectType", subproject.getSubprojectType());
        	returnJson.put("status", subproject.getStatus());
        	String listID = ProjectManager.retrieveListId(projectID, subproject.getStatus());
        	returnJson.put("listID", listID);
        	returnJson.put("subprojectCardID", subproject.getSubprojectCardId());
			returnJSONArray.add(returnJson);
    	}
    	return ok(returnJSONArray.toJSONString());
    }
    
    public static Result retrieveSubprojectByID(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String projectID = (String)inputJson.get("projectID");
    		String subprojectIDStr = (String)inputJson.get("subprojectID");
    		int subprojectID = Integer.parseInt(subprojectIDStr);
    		Subproject subproject = SubprojectManager.retrieveSubprojectById(projectID, subprojectID);
    		
    		returnJson.put("projectId",subproject.getProjectId());
    		returnJson.put("subprojectId",subproject.getSubprojectId());
    		returnJson.put("subprojectName",subproject.getSubprojectName());
        	returnJson.put("subprojectDescription",subproject.getSubprojectDescription());
        	returnJson.put("subprojectStartDate",subproject.getStartDate().toString());
        	returnJson.put("subprojectEndDate",subproject.getEndDate().toString());
        	if(subproject.getActualStartDate()==null){
        		returnJson.put("actualStartDate","this subproject has not started");
        	}else{
        		returnJson.put("actualStartDate",subproject.getActualStartDate().toString());
        	}
        	if(subproject.getActualEndDate()==null){
        		returnJson.put("actualEndDate","this subproject has not ended");
        	}else{
        		returnJson.put("actualEndDate",subproject.getActualEndDate().toString());
        	}
        	returnJson.put("subporjectType", subproject.getSubprojectType());
        	returnJson.put("status", subproject.getStatus());
        	String listID = ProjectManager.retrieveListId(projectID, subproject.getStatus());
        	returnJson.put("listID", listID);
        	returnJson.put("subprojectCardID", subproject.getSubprojectCardId());
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    //return a list of staff under this subproject
    public static Result retrieveStaffBySubproject(String input) {
    	JSONArray returnJSONArray = new JSONArray(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String projectID = (String) inputJson.get("projectID");
    		String subprojectIDStr = (String) inputJson.get("subprojectID");
    		int subprojectID = Integer.parseInt(subprojectIDStr);

    		//retrieve the list of staff from this subproject
    		ArrayList<Staff> staffList = StaffManager.retrieveStaffBySubproject(projectID,subprojectID);
    		
    		for(int i=0;i<staffList.size();i++){
	    		Staff staff = staffList.get(i);
	    		JSONObject returnJson = new JSONObject();
	        	returnJson.put("status",staff.getStatus());
	        	returnJson.put("email",staff.getEmail());
	        	returnJson.put("staffID",staff.getStaffID());
	        	returnJson.put("role",staff.getRole());
	        	returnJson.put("name",staff.getName());
	        	
				returnJSONArray.add(returnJson);
	    	}
    	}catch(Exception e){
    		
    	}
    	return ok(returnJSONArray.toString());
    }
}


