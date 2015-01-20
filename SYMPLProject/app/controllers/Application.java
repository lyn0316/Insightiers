package controllers;

import play.*;
import play.mvc.*;

import java.text.DateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import DAO.*;
import entity.*;
import views.html.*;

import com.eaio.uuid.UUID;

public class Application extends Controller {
		
    public static Result login() {
        return ok(login.render());
    }

    //this method serves to validate whether the UUID has expired
    public static Result resetPassword(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	
    	UUID u = new UUID(input);
    	String accountID = AccountURLManager.retrieveAccountIdByURL(input);
    	if(accountID==null){
    		return ok(login.render());
    	}
    	
    	String tokenTimeString = AccountURLManager.retrieveTimedByURL(input);
    	long tokenTime = Date.parse(tokenTimeString);
    	Date currentDate = new Date();
    	long currentTime = currentDate.getTime();
    	if((currentTime - tokenTime) <= (3 * 60 * 60 * 1000)){
    		session("accountID", accountID);
 //   		return ok(returnJson.toStringJSON());
    		return ok(resetPassword.render());
    	}else{
    		return ok(timeOut.render());
    	}
    }
    
    //this method takes accountID and new password from resetPassword page and process the password reset request
    public static Result reset(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj=JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String accountID = session("accountID");
    		String password = (String)inputJson.get("newPassword");
    		Account account = AccountManager.retrieveAccountByID(accountID);
    		if ( accountID != null && password != null ) {
            	AccountManager.resetPassword(accountID, password);
            	returnJson.put("status",true);
            }else{
            	returnJson.put("status",false);
            }
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    public static Result sendResetPasswordEmail(String input) {
    	UUID u = new UUID();
    	Long time = u.getTime();
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj=JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String email = (String)inputJson.get("email");
    		Account account = AccountManager.retrieveAccountByEmail(email);
    		if(account!=null){
    			String accountID = account.getAccountId();
    			//not store the newly created UUID together with accountID and current time into database 
    			Date currentDate = new Date();
    			AccountURLManager.populateAccountURLDB(u.toString(), accountID , currentDate.toGMTString());
    			EmailController.contactUsSendEmail(email, u);
    			returnJson.put("status",true);
    		}else{
    			returnJson.put("status",false);
    		}
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
    
    public static Result authentication(String input) {
    	JSONObject returnJson = new JSONObject(); 
    	try{
    		Object inputObj=JSONValue.parse(input);
    		JSONObject inputJson = (JSONObject)inputObj;
    		String accountID = (String)inputJson.get("loginUsername");
    		String password = (String)inputJson.get("loginPass");
    		Account account = AccountManager.retrieveAccountByID(accountID);
    		session("accountID", accountID);
    		if ( account != null && account.getPassword().equals(password)) {
            	String email = account.getEmailAddress();
            	String type = account.getAccountType();
            	String name = account.getName();
            	returnJson.put("status",true);
            	returnJson.put("email",email);
            	returnJson.put("accountID",accountID);
            	returnJson.put("type",type);
            	returnJson.put("name",name);
            }else{
            	returnJson.put("status",false);
            }
    	}catch(Exception e){
    		returnJson.put("status",false);
    	}
    	return ok(returnJson.toJSONString());
    }
  
    public static Result logout(){
    	session().clear();
    	return ok(login.render());
    }
    
    public static Result sessionProtection() {
    	return ok("muyoua");
    	/*JSONObject returnJson = new JSONObject();              
    	String accountID = session("accountID");
    	if(accountID==null){
   			returnJson.put("status",false);
        }else{
           	returnJson.put("status",true);
        }
    	return ok(returnJson.toJSONString());*/
    }
    
    
    

}
