package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import com.eaio.uuid.UUID;


public class EmailController extends Controller {

    public static Result contactUsSendEmail(String emailaddress, UUID uuid) {

        	MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
            mail.setSubject("[Reset Password]");
            mail.setRecipient(emailaddress);
            mail.setFrom("Insightiers <insightiers@gmail.com>");
            
            mail.send("http://localhost:9000/resetPassword/"+uuid.toString());
            return redirect("/");
    }
}

