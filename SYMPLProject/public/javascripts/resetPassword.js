$(function(){
	//password validation
	$('.input-group input[required], .input-group textarea[required], .input-group select[required]').on('keyup change', function() {
		var $form = $(this).closest('form'),
            $group = $(this).closest('.input-group'),
			$addon = $group.find('.input-group-addon'),
			$icon = $addon.find('span');
		
		if($("#newPassword").val() === $("#confirmPassword").val()){
			$("#output").removeClass(' alert alert-danger');
			$("#output").empty("");
			$addon.removeClass('danger');
			$addon.addClass('success');
			$icon.attr('class', 'glyphicon glyphicon-ok');
			
			 //confirm button
		    $('button[name="continue"]').click(function(e){
		    		var newPassword = $("#confirmPassword").val();
		    		e.preventDefault();
		    		//get new password
		    		//call function to reset password
		    		var password = '{"newPassword"  : "'+ newPassword +'"}';
		    		$.ajax({
		                url : '/reset/' + password,
		                type : 'GET',
		                success : function(data){
		                	if(JSON.parse(data).status == true){
		                		window.location.href = "/";
		                	}
		                },
		                error : function(){
		                }
		            });
			});
			
		}else{
			if($("#newPassword").val().length <= $("#confirmPassword").val().length){
				$addon.removeClass('success');
				$addon.addClass('danger');
				$icon.attr('class', 'glyphicon glyphicon-remove');
				$("#output").addClass("alert alert-danger animated fadeInUp").html("Password does not match!");
			}
			
		}
	});
	
    //confirm button
    $('button[name="continue"]').click(function(e){
    		var newPassword = $("#confirmPassword").val();
    		e.preventDefault();
    		//get new password
    		//call function to reset password
    		if($("#newPassword").val() === $("#confirmPassword").val()){
	    		var password = '{"newPassword"  : "'+ newPassword +'"}';
	    		$.ajax({
	                url : '/reset/' + password,
	                type : 'GET',
	                success : function(data){
	                	if(JSON.parse(data).status == false){
	                		window.location.href = "/";
	                	}else{
	                		window.location.href = "/";
	                	}
	                },
	                error : function(){
	                }
	            });
    		}else{
    			$("#output").addClass("alert alert-danger animated fadeInUp").html("Password does not match!");
    		}
	});    $('button[name="cancel"]').click(function(e){
		e.preventDefault();
		//direct to login page
		window.location.href = "/";
	});
});