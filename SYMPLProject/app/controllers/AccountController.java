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


public class AccountController extends Controller {
    
	public static Result updateAccount(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String accountID = (String)inputJson.get("accountID");
    		String name = (String)inputJson.get("name");
    		String password = (String)inputJson.get("password");
    		String email = (String)inputJson.get("emailaddress");
    		String accountType = (String)inputJson.get("accountType");
    		boolean update = AccountManager.updateAccount(accountID, name, password, email, accountType);
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
    
    public static Result createAccount(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String accountID = (String)inputJson.get("accountID");
    		String password = AccountManager.generatePassword();
    		String name = (String)inputJson.get("name");
    		String email = (String)inputJson.get("emailAddress");
    		String accountType = (String)inputJson.get("accountType");
    		boolean create = AccountManager.populateAccountDB(accountID, password, name, email, accountType);
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
    
    public static Result deleteAccount(String input){
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj = JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String accountID = (String)inputJson.get("accountID");
    		boolean deleteAcc = AccountManager.deleteAccount(accountID);
    		if ( deleteAcc = true) {
    			returnJson.put("status",true);
            }else{
            	returnJson.put("status", false);
            }
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    public static Result retrieveAllAccounts() {
    	JSONArray returnJSONArray = new JSONArray(); 
		ArrayList<Account> accountList = AccountManager.retrieveAll();
		for(int i=0;i<accountList.size();i++){
			Account a = accountList.get(i);
			JSONObject returnJson = new JSONObject();
			returnJson.put("accountID",a.getAccountId());
        	returnJson.put("accountType",a.getAccountType());
        	returnJson.put("email",a.getEmailAddress());
        	returnJson.put("name",a.getName());
        	returnJson.put("password",a.getPassword());
			returnJSONArray.add(returnJson);
		}
    	return ok(returnJSONArray.toJSONString());
    }
    
    public static Result retrieveAccountByID(String input) {
    	JSONObject returnJson = new JSONObject();
    	Object inputObj = JSONValue.parse(input);
		JSONObject inputJson = (JSONObject)inputObj;
		String accountID = (String)inputJson.get("accountID");
		Account account = AccountManager.retrieveAccountByID(accountID);
		returnJson.put("accountID",account.getAccountId());
    	returnJson.put("accountType",account.getAccountType());
    	returnJson.put("email",account.getEmailAddress());
    	returnJson.put("name",account.getName());
    	returnJson.put("password",account.getPassword());
    	return ok(returnJson.toJSONString());
    }
}
