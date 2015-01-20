var type="";

var displayname = $("#inputName").html();
var pw = $("#inputPassword").html();
var email = $("#inputEmail").html();

$(function(){
	//get name from session
	$('#user').html(name);
	 
	//logout
	$('#logout').click(function(e){
		e.preventDefault();
		sessionStorage.removeItem("name");
		localStorage.clear();
		window.location.href="/";
	});
	    
	//retrieve account detail
	
	var credentials = '{"accountID" : "' + accountID +'"}';
	$.ajax({
        url : '/retrieveAccountByID/'+ credentials,
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	var name = arr.name;
        	var email = arr.email;
        	var pw = arr.password;
        	type = arr.accountType;
        	$("#inputName").text(name);
        	$("#inputEmail").text(email);
        	$("#inputPassword").text(pw);
        	
        },
        error : function(){
        }
    });
	
	 $("#editbutton").click(function(){
		 	var displayname = $("#inputName").html();
			var pw = $("#inputPassword").html();
			var email =$("#inputEmail").html();
			console.log(displayname);
			console.log("hsaohdshiuahfa");
			
			if ((displayname !="") && (email!="") && (pw!="")){
				$("#displayName").html("<input type='text' class='form-control' id='inputName' value='"+displayname+"'>");
				$("#displayEmail").html("<input type='text' class='form-control' id='inputEmail' value='"+email+"'>");
				$("#displayPassword").html("<input type='text' class='form-control' id='inputPassword' value='"+pw+"'>");
				$("#displayButton").html("<a type='submit' class='btn btn-primary' onclick='saveInfo()'>Save</a>");
				$("#displayButton").append("<a type='submit' class='btn btn-primary' onclick='cancel()'>Cancel</a>");
	 		}
	 });
	
	
	
});



function cancel(){
	window.location.href = "/editAccount";
}


function saveInfo(){
	var displayname = $("#inputName").val();
	var email = $("#inputEmail").val();
	var pw = $("#inputPassword").val();
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

	
	if(displayname === ""){
		$("#createInputError").html("Please enter a name");
	}else if(pw === ""){
		$("#createInputError").html("Please enter a passwork");
	}else if(email === ""){
		$("#createInputError").html("Please enter an email address");
	}else if (!filter.test(email)){
	   	//check email validation
		
	    $("#createInputError").html("Please enter a valid email address");
	}else{
	
		var credentials = '{"accountID" : "' + accountID +'", "name"  : "'+ displayname +'","emailaddress"  : "'+ email +'","accountType"  : "'+ type +'","password"  : "'+ pw+'"}';
		$.ajax({
	        url : '/updateAccount/'+ credentials,
	        type : 'GET',
	        success : function(data){
	        	window.location.href = "/editAccount";
	        },
	        error : function(){
	        }
	    });
	}

}

