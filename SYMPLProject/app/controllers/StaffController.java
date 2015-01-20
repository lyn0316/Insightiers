package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import DAO.*;
import entity.*;
import views.html.*;

import com.eaio.uuid.UUID;

public class StaffController extends Controller {
		
    public static Result login() {
        return ok(login.render());
    }
    
    public static Result retrieveAll() {
    	JSONArray returnJSONArray = new JSONArray(); 
		ArrayList<Staff> staffList = StaffManager.retrieveAll();
		for(int i=0;i<staffList.size();i++){
			Staff s = staffList.get(i);
			JSONObject returnJson = new JSONObject();
			returnJson.put("maxProjectNum",s.getMaxProjectNum());
        	returnJson.put("status",s.getStatus());
        	returnJson.put("email",s.getEmail());
        	returnJson.put("staffID",s.getStaffID());
        	returnJson.put("role",s.getRole());
        	returnJson.put("name",s.getName());
			returnJSONArray.add(returnJson);
		}
    	return ok(returnJSONArray.toJSONString());
    }
    
     
    public static Result retrieveByID(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String staffIDStr = (String)inputJson.get("staffID");
    		int staffID = Integer.parseInt(staffIDStr);
    		Staff staff = StaffManager.retrieveStaffByID(staffID);
    		if ( staff != null) {
            	String email = staff.getEmail();
            	String role = staff.getRole();
            	String name = staff.getName();
            	String status = staff.getStatus();
            	int maxProjectNum = staff.getMaxProjectNum();
            	returnJson.put("maxProjectNum",maxProjectNum);
            	returnJson.put("status",status);
            	returnJson.put("email",email);
            	returnJson.put("staffID",staffID);
            	returnJson.put("role",role);
            	returnJson.put("name",name);
            }else{
            }
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    public static Result updateStaff(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String staffID = (String)inputJson.get("staffID");
    		int sID = Integer.parseInt(staffID);
    		String maxProjectNum = (String)inputJson.get("maxProjectNum");
    		int mPN = Integer.parseInt(maxProjectNum);
    		String name = (String)inputJson.get("name");
    		String role = (String)inputJson.get("role");
    		String email = (String)inputJson.get("email");
    		String status = (String)inputJson.get("status");
    		boolean update = StaffManager.updateStaffInfo(sID, mPN, name, role, email, status);
    		if ( update = true) {
    			returnJson.put("status",true);
            }else{
            	returnJson.put("status", false);
            }
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    public static Result createStaff(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String maxProjectNum = (String)inputJson.get("maxProjectNum");
    		int mPN = Integer.parseInt(maxProjectNum);
    		String name = (String)inputJson.get("name");
    		String role = (String)inputJson.get("role");
    		String email = (String)inputJson.get("email");
    		String applicationKey = (String)inputJson.get("applicationKey");
    		String token = (String)inputJson.get("token");
    		boolean create = StaffManager.populateStaffDB(mPN, name, role, email, applicationKey, token);
    		if ( create = true) {
    			returnJson.put("status",true);
            }else{
            	returnJson.put("status", false);
            }
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    public static Result deleteStaff(String input){
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String staffID = (String)inputJson.get("staffID");
    		int sID = Integer.parseInt(staffID);
    		boolean deactivateStaff = StaffManager.deleteStaff(sID);
    		if ( deactivateStaff = true) {
    			returnJson.put("status",true);
            }else{
            	returnJson.put("status", false);
            }
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    //Return a list of staff based on the list of skills given
    //called when tries to create a project and assign staff to it 
    public static Result retrieveStaffBySkills(String input) {
    	JSONArray returnJSONArray = new JSONArray(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		JSONArray skillsListJson = (JSONArray)inputJson.get("skills");
    		ArrayList<Staff> returnList = new ArrayList<Staff>();
    		
    		if(skillsListJson==null||skillsListJson.size()==0){
    			returnList = StaffManager.retrieveAll();
    		}else{
	    		for(int i=0 ; i<skillsListJson.size(); i++){
	    			String skill = (String)skillsListJson.get(i);
	    			ArrayList<Staff> staffList = StaffManager.retrieveStaffBySkills(skill);
	    			for(int j=0;j<staffList.size();j++){
	    				boolean dupilicate = false;
	    				for (int k=0;k<returnList.size();k++){
	    					if(returnList.get(k).getStaffID()==staffList.get(j).getStaffID()){
	    						dupilicate = true;
	    					}
	    				}
	    				if(dupilicate==false){
	    					returnList.add(staffList.get(j));
	    				}
	    			}
	    		}
    		}
    		
    		for(int i=0;i<returnList.size();i++){
	    		Staff staff = returnList.get(i);
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
    	return ok(returnJSONArray.toJSONString());
    }
    
    
    //retrieve all subprojects under a given staff
    public static Result retrieveAllSubproject(String input){
    	Object inputObj = JSONValue.parse(input);
    	JSONObject inputJson = (JSONObject)inputObj;
    	String staffIDStr = (String)inputJson.get("staffID");
    	int staffID = Integer.parseInt(staffIDStr);
    	
    	JSONArray returnJSONArray = new JSONArray(); 
    	ArrayList<Subproject> subprojectList = SubprojectManager.retrieveSubprojectByStaff(staffID);
    	for(int i=0;i<subprojectList.size();i++){
    		Subproject subproject = subprojectList.get(i);
    		JSONObject returnJson = new JSONObject();
    		returnJson.put("projectId",subproject.getProjectId());
    		returnJson.put("subprojectId",subproject.getSubprojectId());
    		returnJson.put("subprojectName",subproject.getSubprojectName());
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
        	
			returnJSONArray.add(returnJson);
    	}
    	return ok(returnJSONArray.toJSONString());
    }
    
    public static Result retrieveNumberOfStaffWithSkill() {
    	JSONArray returnJSONArray = new JSONArray(); 
    	try{
    		ArrayList<String> skills = StaffManager.retrieveAllSkills();
    		for(int i=0;i<skills.size();i++){
    			String skillName = skills.get(i);
        		JSONObject returnJson = new JSONObject();
        		returnJson.put("skillName",skillName);
        		int number = StaffManager.retrieveNumberOfStaffWithSkill(skillName);
        		returnJson.put("number",number);
    			returnJSONArray.add(returnJson);
        	}
    		
    	}catch(Exception e){
    	}
    	return ok(returnJSONArray.toJSONString());
    }
    
    public static Result retrieveNumberOfProjectsByPM() {
    	JSONArray returnJSONArray = new JSONArray(); 
    	try{
    		ArrayList<Staff> PMList = StaffManager.retrieveAllPM();
    		for(int i=0;i<PMList.size();i++){
    			Staff PM = PMList.get(i);
    			int projectManagerId = PM.getStaffID();
        		JSONObject returnJson = new JSONObject();
        		returnJson.put("PMName",PM.getName());
        		int number = StaffManager.retrieveNumberOfProjectsByPM(projectManagerId);
        		returnJson.put("number",number);
    			returnJSONArray.add(returnJson);
        	}
    		
    	}catch(Exception e){
    	}
    	return ok(returnJSONArray.toJSONString());
    }
    
    //retrieve all tasks each staff are working on today
    public static Result retrieveAllOngoingTasksByStaff(String input) {
    	JSONArray returnJSONArray = new JSONArray(); 
    	Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String startDate = (String)inputJson.get("startDate");
		String endDate = (String)inputJson.get("endDate");
    	try{
    		ArrayList<Staff> StaffList = StaffManager.retrieveAll();
    		for(int i=0;i<StaffList.size();i++){
    			Staff staff = StaffList.get(i);
    			JSONObject returnJson = new JSONObject();
    			
    			int staffID = staff.getStaffID();
    			returnJson.put("staffID",staff.getStaffID());
    			returnJson.put("staffName",staff.getName());
    			returnJson.put("role",staff.getRole());
    			
    			ArrayList<Subproject> subprojectList = SubprojectManager.retrieveOngoingSubprojectsByStaff(staffID,startDate,endDate);
    			JSONArray subprojectJSONArray = new JSONArray();
    			for(int j=0;j<subprojectList.size();j++){
    	    		Subproject subproject = subprojectList.get(j);
    	    		JSONObject subprojectJson = new JSONObject();
    	    		subprojectJson.put("projectId",subproject.getProjectId());
    	    		subprojectJson.put("subprojectId",subproject.getSubprojectId());
    	    		subprojectJson.put("subprojectName",subproject.getSubprojectName());
    	    		subprojectJson.put("subprojectDescription",subproject.getSubprojectDescription());
    	    		subprojectJson.put("subprojectStartDate",subproject.getStartDate().toString());
    	    		subprojectJson.put("subprojectEndDate",subproject.getEndDate().toString());
    	        	if(subproject.getActualStartDate()==null){
    	        		subprojectJson.put("actualStartDate","this subproject has not started");
    	        	}else{
    	        		subprojectJson.put("actualStartDate",subproject.getActualStartDate().toString());
    	        	}
    	        	if(subproject.getActualEndDate()==null){
    	        		subprojectJson.put("actualEndDate","this subproject has not ended");
    	        	}else{
    	        		subprojectJson.put("actualEndDate",subproject.getActualEndDate().toString());
    	        	}
    	        	subprojectJSONArray.add(subprojectJson);
    			}
    			returnJson.put("ongoingTasks",subprojectJSONArray);
    			
    			returnJSONArray.add(returnJson);
        	}
    		
    	}catch(Exception e){
    	}
    	return ok(returnJSONArray.toJSONString());
    }

    //return a list of staff based on the search input of staff information
    public static Result filterStaffListByStaffInfo(String input) {
    	JSONArray returnJSONArray = new JSONArray();
    	Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String searchCriteria = (String)inputJson.get("searchCriteria");
		
		ArrayList<Staff> staffList = StaffManager.filterStaffListByStaffInfo(searchCriteria);
		for(int i=0;i<staffList.size();i++){
			Staff s = staffList.get(i);
			JSONObject returnJson = new JSONObject();
			returnJson.put("maxProjectNum",s.getMaxProjectNum());
        	returnJson.put("status",s.getStatus());
        	returnJson.put("email",s.getEmail());
        	returnJson.put("staffID",s.getStaffID());
        	returnJson.put("role",s.getRole());
        	returnJson.put("name",s.getName());
			returnJSONArray.add(returnJson);
		}
    	return ok(returnJSONArray.toJSONString());
    }
    
  //return a list of staff based on the search input of skill name
    public static Result filterStaffListBySkill(String input) {
    	JSONArray returnJSONArray = new JSONArray();
    	Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String skill = (String)inputJson.get("skill");
		
		ArrayList<Staff> staffList = StaffManager.filterStaffListBySkill(skill);
		for(int i=0;i<staffList.size();i++){
			Staff s = staffList.get(i);
			JSONObject returnJson = new JSONObject();
			returnJson.put("maxProjectNum",s.getMaxProjectNum());
        	returnJson.put("status",s.getStatus());
        	returnJson.put("email",s.getEmail());
        	returnJson.put("staffID",s.getStaffID());
        	returnJson.put("role",s.getRole());
        	returnJson.put("name",s.getName());
			returnJSONArray.add(returnJson);
		}
    	return ok(returnJSONArray.toJSONString());
    }

}
