(function(){
   'use strict';
	var $ = jQuery;
	$.fn.extend({
		filterTable: function(){
			return this.each(function(){
				$(this).on('keyup', function(e){
					$('.filterTable_no_results').remove();
					var $this = $(this), search = $this.val().toLowerCase(), target = $this.attr('data-filters'), $target = $(target), $rows = $target.find('tbody tr');
					if(search == '') {
						$rows.show(); 
					} else {
						$rows.each(function(){
							var $this = $(this);
							$this.text().toLowerCase().indexOf(search) === -1 ? $this.hide() : $this.show();
						})
						if($target.find('tbody tr:visible').size() === 0) {
							var col_count = $target.find('tr').first().find('td').size();
							var no_results = $('<tr class="filterTable_no_results"><td colspan="'+col_count+'">No results found</td></tr>')
							$target.find('tbody').append(no_results);
						}
					}
				});
			});
		}
	});
	$('[data-action="filter"]').filterTable();
})(jQuery);

$(function(){
	//get name from session
	$('#user').html(name);
	
    // attach table filter plugin to inputs
	$('[data-action="filter"]').filterTable();
	
	$('.accountContainer').on('click', '.panel-heading span.filter', function(e){
		var $this = $(this), 
				$panel = $this.parents('.panel');
		
		$panel.find('.panel-body').slideToggle();
		if($this.css('display') != 'none') {
			$panel.find('.panel-body input').focus();
		}
	});
	$('[data-toggle="tooltip"]').tooltip();
	
	
	 
	//logout
	$('#logout').click(function(e){
		e.preventDefault();
		sessionStorage.removeItem("name");
		localStorage.clear();
		window.location.href="/";
	});
	    
	var count=0;
	//retrieve all account list detail
	$.ajax({
        url : '/retrieveAllAccounts',
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	count = arr.length;
        	for (var j = 0; j < arr.length; j=j+1){
        		$('#acc_table_item').append('<tr id="account'+j+'"></tr>');
        		$('#account'+j).html("<td id='name"+j+"'>"+ arr[j].name+"</td><td id='username"+j+"'>"+ arr[j].accountID+"</td><td id='password"+j+"'>"+ arr[j].password+"</td><td id='email"+j+"'>"+ arr[j].email+"</td><td id='accType"+j+"'>"+ arr[j].accountType+"</td><td><p><button class='btn btn-primary btn-xs' onclick=editAcc('"+j+"')><span class='glyphicon glyphicon-pencil'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Delete' data-toggle='modal' data-target='#delete' data-placement='top' rel='tooltip' onclick=deleteAcc('"+arr[j].accountID+"')><span class='glyphicon glyphicon-trash'></span></button></p></td>");
        	}
        },
        error : function(){
        }
    });
	
	//add row
	 $("#add_row").click(function(){
		 var i = count;
		  $('#dev-table').append('<tr id="account'+i+'"></tr>');
	      $('#account'+i).html("<td><input name='Name' type='text' placeholder='Name' class='form-control input-md' id='newName"+i+"'/> </td><td><input name='Username' type='text' placeholder='Username' class='form-control input-md' id='newUserName"+i+"'/> </td><td><input  name='Password' type='text' placeholder=''  class='form-control input-md' id='newPassword"+i+"' disabled/></td><td><input  name='Email' type='text' placeholder='Email'  class='form-control input-md' id='newEmail"+i+"'></td><td><select id='newAcctype"+i+"'><option value='Project Manager'>Project Manager</option><option value='Administrator'>Administrator</option><option value='Client'>Client</option></select></td><td><p><button class='btn btn-primary btn-xs' onclick=createAcc('"+i+"')><span class='glyphicon glyphicon-floppy-saved'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Cancel' data-toggle='modal' data-target='#cancel' data-placement='top' rel='tooltip'><span class='glyphicon glyphicon-remove'></span></button></p></td>");
	      i++; 
	    
	 });

});

function createAcc(accountRow){
	console.log(accountRow);
	var name = $("#newName"+accountRow).val();
	var username = $("#newUserName"+accountRow).val();
	var email = $("#newEmail"+accountRow).val();
	var type = $("#newAcctype"+accountRow).val();
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	console.log(name+username+email+type);
	if(name === ""){
		$("#createInputError").html("Please enter a name");
	}else if(username === ""){
		$("#createInputError").html("Please enter a userName");
	}else if(email === ""){
		$("#createInputError").html("Please enter an email address");
	}else if (!filter.test(email)){
    	//check email validation
    	$("#createInputError").html("Please enter a valid email address");
    }else if(type === ""){
		$("#createInputError").html("Please enter an account type");
	}else{
    	var credentials = '{"accountID" : "' + username +'","name":"'+name+'","emailAddress":"'+email+'","accountType":"'+type+'"}';
    	//console.log(credentials);
    	$.ajax({
	        url : '/createAccount/'+ credentials,
	        type : 'GET',
	        success : function(data){
	        	window.location.href="/adminAccount";
	        },
	        error : function(){
	        }
	    });
	}
}

function editAcc(accountRow){
	var name = $("#name"+accountRow).html();
	var username = $("#username"+accountRow).html();
	var email = $("#email"+accountRow).html();
	var type = $("#accType"+accountRow).html();
	var password = $("#password"+accountRow).html();
	$('#account'+accountRow).html("<td><input name='Name' type='text' value='"+name+"' class='form-control input-md' id='newName"+accountRow+"'/> </td><td><input name='Username' type='text' value='"+username+"' class='form-control input-md' id='newUserName"+accountRow+"'disabled/> </td><td><input  name='Password' type='text' value='"+password+"'  class='form-control input-md' id='newPassword"+accountRow+"'/></td><td><input  name='Email' type='text' value='"+email+"'  class='form-control input-md' id='newEmail"+accountRow+"'></td>");
	
	if(type === "Project Manager"){
		$('#account'+accountRow).append("<td><select id='newAcctype"+accountRow+"'><option value='Project Manager' selected>Project Manager</option><option value='Administrator'>Administrator</option><option value='Client'>Client</option></select></td><td><p><button class='btn btn-primary btn-xs' onclick=updateAcc('" +accountRow+ "')><span class='glyphicon glyphicon-floppy-saved'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Cancel' data-toggle='modal' data-target='#cancel' data-placement='top' rel='tooltip'><span class='glyphicon glyphicon-remove'></span></button></p></td>");
	}else if(type === "Client"){
		$('#account'+accountRow).append("<td><select id='newAcctype"+accountRow+"'><option value='Project Manager'>Project Manager</option><option value='Administrator'>Administrator</option><option value='Client' selected>Client</option></select></td><td><p><button class='btn btn-primary btn-xs' onclick=updateAcc('" +accountRow+ "')><span class='glyphicon glyphicon-floppy-saved'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Cancel' data-toggle='modal' data-target='#cancel' data-placement='top' rel='tooltip'><span class='glyphicon glyphicon-remove'></span></button></p></td>");
	}else if(type === "Administrator"){
		$('#account'+accountRow).append("<td><select id='newAcctype"+accountRow+"'><option value='Project Manager'>Project Manager</option><option value='Administrator' selected>Administrator</option><option value='Client'>Client</option></select></td><td><p><button class='btn btn-primary btn-xs' onclick=updateAcc('" +accountRow+ "')><span class='glyphicon glyphicon-floppy-saved'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Cancel' data-toggle='modal' data-target='#cancel' data-placement='top' rel='tooltip'><span class='glyphicon glyphicon-remove'></span></button></p></td>");
	}
}

function updateAcc(accountRow){
	var name = $("#newName"+accountRow).val();
	var username = $("#newUserName"+accountRow).val();
	var email = $("#newEmail"+accountRow).val();
	var type = $("#newAcctype"+accountRow).val();
	var password = $("#newPassword"+accountRow).val();
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	
	if(name === ""){
		$("#createInputError").html("Please enter a name");
	}else if(username === ""){
		$("#createInputError").html("Please enter a userName");
	}else if(password === ""){
		$("#createInputError").html("Please enter a password");
	}else if(email === ""){
		$("#createInputError").html("Please enter an email address");
	}else if (!filter.test(email)){
    	//check email validation
    	$("#createInputError").html("Please enter a valid email address");
    }else if(type === ""){
		$("#createInputError").html("Please enter an account type");
	}else{
		var credentials = '{"accountID" : "' + username +'","name":"'+name+'","emailaddress":"'+email+'","accountType":"'+type+'","password":"'+password+'"}';
		$.ajax({
	        url : '/updateAccount/'+credentials,
	        type : 'GET',
	        success : function(data){
	        	var status  = JSON.parse(data).status;
	        	if(status){
	        		$('#account'+accountRow).html("<td id='name"+accountRow+"'>"+ name+"</td><td id='username"+accountRow+"'>"+ username+"</td><td id='password"+accountRow+"'>"+ password +"</td><td id='email"+accountRow+"'>"+ email+"</td><td id='accType"+accountRow+"'>"+type+"</td><td><p><button class='btn btn-primary btn-xs' onclick=editAcc('"+accountRow+"')><span class='glyphicon glyphicon-pencil'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Delete' data-toggle='modal' data-target='#delete' data-placement='top' rel='tooltip' onclick=deleteAcc('"+username+"')><span class='glyphicon glyphicon-trash'></span></button></p></td>");
	        		window.location.href="/adminAccount";
	        	}
	        },
	        error : function(){
	        }
	    });
	}
}

var deleteItem;
function deleteAcc(accountID){
	deleteItem = accountID; 
}

function excuteDelete(){
	var credentials = '{"accountID" : "' + deleteItem +'"}';
	$.ajax({
        url : '/deleteAccount/'+ credentials,
        type : 'GET',
        success : function(data){
        	window.location.href="/adminAccount";
        },
        error : function(){
        }
 });
}