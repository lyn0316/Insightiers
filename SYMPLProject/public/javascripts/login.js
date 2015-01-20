$(function(){
	//get username & password from submit form
	var loginUsername = $("input[name=loginUsername]");
	var loginPass = $("input[name=loginPass]");
	var recoverEmail = $("input[name=loginEmail]");
	
	$('button[name="signIn"]').click(function(e){
		e.preventDefault();
		//authenticate
		var credentials = '{"loginUsername" : "' + loginUsername.val() +'", "loginPass"  : "'+ loginPass.val() +'"}';
		$.ajax({
            url : '/authentication/' + credentials,
            type : 'GET',
            success : function(data){
            	var dataJson = JSON.parse(data);
            	if(dataJson.status === false){
            		$("#output").addClass("alert alert-danger animated fadeInUp").html("Invalid username/password");
            	}else{
            		window.location.href = "/dashboard";
            		sessionStorage.setItem("name",dataJson.name);
            		sessionStorage.setItem("accountID",dataJson.accountID);
            	}
            },
            error : function(){
            }
        });
	});
	
	// Checking for CSS 3D transformation support
	$.support.css3d = supportsCSS3D();
	
	var formContainer = $('#formContainer');
	
	// Listening for clicks on the ribbon links
	$('.flipLink').click(function(e){
		$("#output").removeClass("alert alert-danger animated fadeInUp");
		$("#output").empty("");
		
		//$("#output").hide();
		 
		// Flipping the forms
		formContainer.toggleClass('flipped');
		
		// If there is no CSS3 3D support, simply
		// hide the login form (exposing the recover one)
		if(!$.support.css3d){
			$('#login').toggle();
		}
		e.preventDefault();
	});
	
	formContainer.find('form').submit(function(e){
		// Preventing form submissions. If you implement
		// a backend, you might want to remove this code
		e.preventDefault();
		
	});
	
	
	// A helper function that checks for the 
	// support of the 3D CSS3 transformations.
	function supportsCSS3D() {
		var props = [
			'perspectiveProperty', 'WebkitPerspective', 'MozPerspective'
		], testDom = document.createElement('a');
		  
		for(var i=0; i<props.length; i++){
			if(props[i] in testDom.style){
				return true;
			}
		}
		
		return false;
	}
	
	
	$('button[name="recoverPass"]').click(function(e){
		e.preventDefault();
        //little validation just to check username
        if (recoverEmail.val() != "") {
        	var email = '{"email" : "' + recoverEmail.val() +'"}';
    		
        	$.ajax({
                url : '/sendResetPasswordEmail/' + email,
                type : 'GET',
                success : function(data){
                	var dataJson = JSON.parse(data);
                	if(dataJson.status === true){
                		 $("#output").removeClass('alert-danger');
                		 $('button[name="recoverPass"]').html("Resend")
                		 $("#reminder").addClass("palette-paragraph").html("A reset password email has been sent successfully. Please click on 'Resend' button if you did not receive the email.")
                	}else{
                		$("#reminder").addClass("palette-paragraph").html("Invalid email address!")
                		
                	}
                },
                error : function(){
                }
            });
            
        } else {
            //remove success mesage replaced with error message
            $("#output").removeClass(' alert alert-success');
            $("#output").addClass("alert alert-danger animated fadeInUp").html("<span style='text-transform:uppercase'>sorry enter an email address</span>");
        }
        //console.log(textfield.val());
	});
	
	
});
