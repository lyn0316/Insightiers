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
});



$(function(){
	var count=0;
	//retrieve all account list detail
	$.ajax({
        url : '/retrieveAll',
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	count = arr.length;
        	for (var j = 0; j < arr.length; j=j+1){
        		$('#staff_table_item').append('<tr id="account'+j+'"></tr>');
        		$('#account'+j).html("<td id='newstaffID"+j+"'>"+ arr[j].staffID+"</td><td id='newMaxProj"+j+"'>"+ arr[j].maxProjectNum+"</td><td id='newName"+j+"'>"+ arr[j].name+"</td><td id='newRole"+j+"'>"+ arr[j].role+"</td><td id='newEmail"+j+"'>"+ arr[j].email+"</td><td id='newStatus"+j+"'>"+ arr[j].status+"</td><td><p><button class='btn btn-primary btn-xs' data-title='Edit' data-toggle='modal' data-target='#edit' data-placement='top' rel='tooltip' onclick=editStaff('" + j + "')><span class='glyphicon glyphicon-pencil'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Delete' data-toggle='modal' data-target='#delete' data-placement='top' rel='tooltip' onclick=deleteStaff('"+arr[j].staffID+"')><span class='glyphicon glyphicon-trash'></span></button></p></td>");
        	}
        },
        error : function(){
        }
    });
	
	//add row
	 $("#add_row").click(function(){
		 var i = count;
		  $('#dev-table').append('<tr id="account'+i+'"></tr>');
	      $('#account'+i).html("<td><input name='staffID' type='text' placeholder='' class='form-control input-md' id='newstaffID"+i+"'disabled/> </td><td><input name='MaxProj' type='text' placeholder='Max Project' class='form-control input-md' id='newMaxProj"+i+"'/> </td><td><input  name='Name' type='text' placeholder='Name'  class='form-control input-md' id='newName"+i+"'></td><td><select id='newRole"+i+"'><option value='Project Manager'>Project Manager</option><option value='Developer'>Developer</option></select></td><td><input  name='Email' type='email' placeholder='Email'  class='form-control input-md' id='newEmail"+i+"'></td><td><input name='Status' type='text' placeholder='active' class='form-control input-md' id='newStatus"+i+"'disabled/> </td><td><p><button class='btn btn-primary btn-xs' data-title='Save' data-toggle='modal' data-target='#save' data-placement='top' rel='tooltip' onclick=CreateStaff("+i+")><span class='glyphicon glyphicon-floppy-saved'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Cancel' data-toggle='modal' data-target='#cancel' data-placement='top' rel='tooltip'onclick=deleteStaff('#staffID"+i+"')><span class='glyphicon glyphicon-remove'></span></button></p></td>");
	      i++; 
	 });

});

function CreateStaff(accountRow){
	
	var name = $("#newName"+accountRow).val();
	var max = $("#newMaxProj"+accountRow).val();
	var role = $("#newRole"+accountRow).val();
	var email = $("#newEmail"+accountRow).val();
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	
	if(max === ""){
		$("#createInputError").html("Please enter a max project number");
	}else if(max < 0){
		$("#createInputError").html("Please enter a valid max project number");
	}else if(name === ""){
		$("#createInputError").html("Please enter a name");
	}else if(role === ""){
		$("#createInputError").html("Please enter a role");
	}else if(email === ""){
		$("#createInputError").html("Please enter an email address");
	}else if (!filter.test(email)){
    	//check email validation
    	$("#createInputError").html("Please enter a valid email address");
    }else{
		var credentials = '{"name" : "' + name +'","maxProjectNum" : "'+max+'","role":"'+role+'","email":"'+email+'"}';
		$.ajax({
	        url : '/createStaff/'+ credentials,
	        type : 'GET',
	        success : function(data){
	        	window.location.href="/adminStaff";
	        },
	        error : function(){
	        }
	    });
	}
}

var deleteItem;
function deleteStaff(staffID){
	deleteItem = staffID;
}

function excuteDelete(){
	var credentials = '{"staffID" : "' + deleteItem +'"}';
	$.ajax({
        url : '/deactivateStaff/'+ credentials,
        type : 'GET',
        success : function(data){
        	window.location.href="/adminStaff";
        },
        error : function(){
        }
    });
}

function editStaff(accountRow){
	var staffid = $("#newstaffID"+accountRow).html();
	var name = $("#newName"+accountRow).html();
	var max = $("#newMaxProj"+accountRow).html();
	var role = $("#newRole"+accountRow).html();
	var email = $("#newEmail"+accountRow).html();
	var status = $("#newStatus"+accountRow).html();
	$('#account'+accountRow).html("<td><input name='staffID' type='text' value='"+staffid+"' class='form-control input-md' id='newstaffID"+accountRow+"' disabled/> </td><td><input name='MaxProj' type='text' value='"+max+"' class='form-control input-md' id='newMaxProj"+accountRow+"'/> </td><td><input  name='Name' type='text' value='"+name+"'  class='form-control input-md' id='newName"+accountRow+"'></td>");
	if(role === "Project Manager"){
		$('#account'+accountRow).append("<td><select id='newRole"+accountRow+"'><option value='Project Manager' selected>Project Manager</option><option value='Developer'>Developer</option></select></td><td><input  name='Email' type='email' value='"+email+"' class='form-control input-md' id='newEmail"+accountRow+"'></td>");
	}else if(role === "Developer"){
		$('#account'+accountRow).append("<td><select id='newRole"+accountRow+"'><option value='Project Manager'>Project Manager</option><option value='Developer' selected>Developer</option></select></td><td><input  name='Email' type='email' value='"+email+"' class='form-control input-md' id='newEmail"+accountRow+"'></td>");
	}
	if(status === "active"){
		$('#account'+accountRow).append("<td><select id='newStatus"+accountRow+"'><option value='active' selected>active</option>/><option value='deactivated'>deactivated</option>/></select></td><td><p><button class='btn btn-primary btn-xs' data-title='Save' data-toggle='modal' data-target='#save' data-placement='top' rel='tooltip' onclick=updateStaff('" + accountRow + "')><span class='glyphicon glyphicon-floppy-saved'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Cancel' data-toggle='modal' data-target='#cancel' data-placement='top' rel='tooltip'><span class='glyphicon glyphicon-remove'></span></button></p></td>");
	}else if(status === "deactivated"){
		$('#account'+accountRow).append("<td><select id='newStatus"+accountRow+"'><option value='active'>active</option>/><option value='deactivated' selected>deactivated</option>/></select></td><td><p><button class='btn btn-primary btn-xs' data-title='Save' data-toggle='modal' data-target='#save' data-placement='top' rel='tooltip' onclick=updateStaff('" + accountRow + "')><span class='glyphicon glyphicon-floppy-saved'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Cancel' data-toggle='modal' data-target='#cancel' data-placement='top' rel='tooltip'><span class='glyphicon glyphicon-remove'></span></button></p></td>");
	}
}

function updateStaff(accountRow){
	var staffID = $("#newstaffID"+accountRow).val();
	var maxProjectNum = $("#newMaxProj"+accountRow).val();
	var name = $("#newName"+accountRow).val();
	var role = $("#newRole"+accountRow).val();
	var email = $("#newEmail"+accountRow).val();
	var staffStatus = $("#newStatus"+accountRow).val();
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	
	if(maxProjectNum === ""){
		$("#createInputError").html("Please enter a max project number");
	}else if(name === ""){
		$("#createInputError").html("Please enter a name");
	}else if(role === ""){
		$("#createInputError").html("Please enter a role");
	}else if(email === ""){
		$("#createInputError").html("Please enter an email address");
	}else if (!filter.test(email)){
    	//check email validation
    	$("#createInputError").html("Please enter a valid email address");
    }else if(staffStatus === ""){
		$("#createInputError").html("Please enter a status");
	}else{
		var credentials = '{"name" : "' + name +'","maxProjectNum" : "'+maxProjectNum+'","role":"'+role+'","email":"'+email+'","staffID":"'+staffID+'","status":"'+staffStatus+'"}';
		$.ajax({
	        url : '/updateStaff/'+credentials,
	        type : 'GET',
	        success : function(data){
	        	var status  = JSON.parse(data).status;
	        	if(status){
	        		$('#account'+accountRow).html("<td id='newstaffID"+accountRow+"'>"+staffID+"</td><td id='newMaxProj"+accountRow+"'>"+ maxProjectNum+"</td><td id='newName"+accountRow+"'>"+name+"</td><td id='newRole"+accountRow+"'>"+role+"</td><td id='newEmail"+accountRow+"'>"+email+"</td><td id='newStatus"+accountRow+"'>"+staffStatus+"</td><td><p><button class='btn btn-primary btn-xs' data-title='Edit' data-toggle='modal' data-target='#edit' data-placement='top' rel='tooltip' onclick=editStaff('" + accountRow + "')><span class='glyphicon glyphicon-pencil'></span></button></p></td><td><p><button class='btn btn-danger btn-xs' data-title='Delete' data-toggle='modal' data-target='#delete' data-placement='top' rel='tooltip' onclick=deleteStaff('"+staffID+"')><span class='glyphicon glyphicon-trash'></span></button></p></td>");
	        		window.location.href="/adminStaff";
	        	}
	        },
	        error : function(){
	        }
	    });
    }
}
