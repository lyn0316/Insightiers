var booleanStartDate = false;
var booelanEndDate = false;
var booleanInitials = false;
var booleanClient = false;
var booleanDescription = false;
var booleanSelectPM = false;

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

var createStatus = false;

$(document).ready(function() {
	//get name from session
	$('#user').html(name);
    
	//logout
    $('#logout').click(function(e){
		e.preventDefault();
		sessionStorage.removeItem("name");
		window.location.href="/";
	});
	
	$.ajax({
        url : '/retrieveAllProjects',
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	for (var i = 0; i < arr.length; i=i+1){
        		$("#table_project").append('<div><h4><strong><a href=/project?projectID{"projectID":"'+ arr[i].projectId +'"} style="color: #2c3e50">'+arr[i].projectId+'</a></strong></h4><span class="glyphicon glyphicon-lock"></span><span id="sidebarInitials">'+(arr[i].projectId).substring(9,12)+'</span><span class="glyphicon glyphicon-user"><span id="sidebarInitials">'+arr[i].projectManagerName+'</span></span><div><span class="glyphicon glyphicon-calendar"><span id="sidebarInitials"><small>'+arr[i].projectStartDate+' — '+arr[i].projectEndDate+'</span></small></span></div></div><hr>');
            }
        },
        error : function(){
        }
    });
	
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
	
	$('#createProject').on('shown.bs.modal', function () {
		$('.chosen-select', this).chosen();
	});
	
	$(".chosen-select").chosen({width: "100%"});
	
	//date time picker
    $('#startDate').datetimepicker({
		pickTime: false
	});
    
    $('#endDate').datetimepicker({
		pickTime: false
	});
    
    $('#startDate').on('change', function(){
    	$("#inputError").empty();
    	if(!(isValidDate($('#start').val().valueOf()))){
    		booleanStartDate = false;
			$('#startDateError').text('Invalid date entered.');
    	}else if($('#start').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
    		booleanStartDate = false;
			$('#startDateError').text('Please enter a future date.');
    	}else{
    		booleanStartDate = true;
			$('#startDateError').empty();
		}
    	var conditions = '{"startDate":"'+$('#start').val().valueOf()+'","endDate":"'+$('#end').val().valueOf()+'"}';
    	clearOptions("selectPM");
    	retrievePM(conditions);
	});
    
    $('#endDate').on('change', function(){
    	$("#inputError").empty();
    	if(!(isValidDate($('#end').val().valueOf()))){
    		booelanEndDate = false;
			$('#endDateError').text('Invalid date entered.');
    	}else if($('#end').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
    		booelanEndDate = false;
			$('#endDateError').text('Please enter a future date.');
    	}else if($('#duration input:radio:checked').val() === "0" || $('#duration input:radio:checked').val() === undefined){
			if ($('#end').val().valueOf() < $('#start').val().valueOf()){
				booelanEndDate = false;
				$('#endDateError').text('End date cannot be earlier than start date.');
			}else{
				booelanEndDate = true;
				$('#endDateError').empty();
			}
    	}else{
    		booelanEndDate = true;
			$('#endDateError').empty();
		}
    	var conditions = '{"startDate":"'+$('#start').val().valueOf()+'","endDate":"'+$('#end').val().valueOf()+'"}';
    	clearOptions("selectPM");
    	retrievePM(conditions);
	});
    
    $('#duration').on('change', function() {
    	$("#inputError").empty();
    	if($('#duration input:radio:checked').val() === "0"){
        	$('#end').prop('disabled', false);
        }else if($('#duration input:radio:checked').val() === "3"){
        	$('#end').prop('disabled', true);
	        if(Date.parse($('#start').val().valueOf()) != null){
        		var endDate = Date.parse($('#start').val().valueOf()).add(3).months().toString('yyyy-MM-dd');
        		booelanEndDate = true;
	        	$('#end').val(endDate);
	        }
        }else if($('#duration input:radio:checked').val() === "6"){
        	$('#end').prop('disabled', true);
        	if(Date.parse($('#start').val().valueOf()) != null){
        		var endDate = Date.parse($('#start').val().valueOf()).add(6).months().toString('yyyy-MM-dd');
        		booelanEndDate = true;
        		$('#end').val(endDate);
        	}
        }
    	var conditions = '{"startDate":"'+$('#start').val().valueOf()+'","endDate":"'+$('#end').val().valueOf()+'"}';
    	clearOptions("selectPM");
    	retrievePM(conditions);
    });
    
    function retrievePM(conditions){
    	$.ajax({
	        url : '/RetrieveAvailableAndUnavailablePM/'+conditions,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.availablePMList.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr.availablePMList[i].name +"---"+arr.availablePMList[i].ongoingProjectNum+"/"+arr.availablePMList[i].maxProjectNum;
	        		staffFromThisProject.value = arr.availablePMList[i].staffID;
	        		var addSubavailableStaff = document.getElementById("availablePM");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	for (var i = 0; i < arr.unavailablePMList.length; i=i+1){
	        		var staffOutsideThisProject = document.createElement("option");
	        		staffOutsideThisProject.text = arr.unavailablePMList[i].name +"---"+arr.availablePMList[i].ongoingProjectNum+"/"+arr.availablePMList[i].maxProjectNum;
	        		staffOutsideThisProject.value = arr.unavailablePMList[i].staffID;
	        		var addSubunavailableStaff = document.getElementById("unavailablePM");
	        		addSubunavailableStaff.appendChild(staffOutsideThisProject);
	        	}
	        	$("#selectPM").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
    
    $('#skillsetRequired').on('change', function() {
    	clearOptions("selectStaff");
    	var skillList = $('#skillsetRequired').chosen().val();
	    var retrieveStaffByProject = '{';
	    if(skillList != null){
	    	retrieveStaffByProject += '"skills":%5B';
	    	for(var i=0; i<skillList.length-1; i++){
		    	retrieveStaffByProject += '"'+skillList[i]+'",';
			}
	    	retrieveStaffByProject += '"'+skillList[skillList.length-1]+'"%5D}';
		}else{
			retrieveStaffByProject += '}';
		}
	    console.log(retrieveStaffByProject);
	    
	    $.ajax({
	        url : '/RetrieveStaffBySkills/'+retrieveStaffByProject,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr[i].name;
	        		staffFromThisProject.value = arr[i].staffID;
	        		var addSubavailableStaff = document.getElementById("availableStaff");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	$("#selectStaff").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    });
    
    $('#initials').on('change', function(){
    	if($('#initials').val() === ""){
    		booleanInitials = false;
    	}else{
    		booleanInitials = true;
    	}
	});
    
    $('#client').on('change', function(){
    	if($('#client').val() === ""){
    		booleanClient = false;
    	}else{
    		booleanClient = true;
    	}
	});
    
    $('#description').on('change', function(){
    	if($('#description').val() === ""){
    		booleanDescription = false;
    	}else{
    		booleanDescription = true;
    	}
	});
    
    $('#selectPM').on('change', function(){
    	if( $('#selectPM :selected').val() === ""){
    		booleanSelectPM = false;
    	}else{
    		booleanSelectPM = true;
    	}
	});
    
    function retrieveAvailablePM(conditions){
	    $.ajax({
	        url : '/retrieveAvailablePM/'+conditions,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.length; i=i+1){
	        		var option = document.createElement("option");
	        		option.text = arr[i].name+"--------------------------------"+arr[i].ongoingProjectNum+"/"+arr[i].maxProjectNum;
	        		var select = document.getElementById("availablePM");
	        		select.appendChild(option);
	        	}
	        	console.log(document.getElementById("availablePM"));
	        	$("#selectPM").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
    
    function retrieveUnavailablePM(){
	    $.ajax({
	        url : '/retrieveUnavailablePM/'+conditions,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.length; i=i+1){
	        		var option = document.createElement("option");
	        		option.text = arr[i].name+"--------------------------------"+arr[i].ongoingProjectNum+"/"+arr[i].maxProjectNum;
	        		var select = document.getElementById("unavailablePM");
	        		select.appendChild(option);
	        	}
	        	console.log(document.getElementById("unavailablePM"));
	        	$("#selectPM").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
	
    //retrieveAvailableStaffBySkills
    function retrieveAvailableStaffBySkills(skillsJson){
    	var skills = skillsJson;
	    $.ajax({
	        url : '/retrieveAvailableStaffBySkills/'+skills,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.length; i=i+1){
	        		var option = document.createElement("option");
	        		option.text = arr[i].name+"--------------------------------"+arr[i].ongoingProjectNum+"/"+arr[i].maxProjectNum;
	        		var select = document.getElementById("availableStaff");
	        		select.appendChild(option);
	        	}
	        	console.log(document.getElementById("availableStaff"));
	        	$("#selectStaff").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
    
  //clear options
    function clearOptions(id){
		document.getElementById(id).options.length = 1;
	}
    
    function isValidDate(date){
        var matches = /(\d{4})[-\-](\d{2})[-\-](\d{2})/.exec(date);
        if (matches == null) return false;
        var day = matches[3];
        var month = matches[2] - 1;
        var year = matches[1];
        var composedDate = new Date(year, month, day);
        return composedDate.getDate() == day &&
                 composedDate.getMonth() == month &&
                 composedDate.getFullYear() == year;
    }
	
    //retrieveOngoingProjects
    var date = Date.today().toString('yyyy-MM-dd');
    var dates = '{"startDate":"'+date+'","endDate":"'+date+'"}';
    $.ajax({
        url : '/retrieveOngoingProjects/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	var ongoingProjects = "<tr><td><strong>Ongoing Projects</strong></td></tr>";
        	for (var i=0; i<jsonData.length;i++){
        		ongoingProjects += '<tr><td></td><td ><a href=/project?projectID{"projectID":"'+ jsonData[i].projectID +'"} style="color: #2c3e50">'+jsonData[i].projectID+'</td><td>'+jsonData[i].startDate+'</td><td>'+jsonData[i].endDate+'</td><td><a href=/staff?staffID{"staffID":"' + jsonData[i].pmID + '"} style="color: #2c3e50;">'+jsonData[i].projectManagerName+'</td></tr>'
        	}
        	$('#ongoing_table_item').append(ongoingProjects);
        },
        error : function(){
        }
    });
    
    $.ajax({
        url : '/retrieveProjectsDue/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	var dueProjects = "<tr><td><strong>Projects Due</strong></td></tr>";
        	for (var i=0; i<jsonData.length;i++){
        		dueProjects += '<tr><td></td><td ><a href=/project?projectID{"projectID":"'+ jsonData[i].projectID +'"} style="color: #2c3e50">'+jsonData[i].projectID+'</td><td>'+jsonData[i].startDate+'</td><td>'+jsonData[i].endDate+'</td><td><a href=/staff?staffID{"staffID":"' + jsonData[i].pmID + '"} style="color: #2c3e50;">'+jsonData[i].projectManagerName+'</td></tr>'
        	}
        	$('#ongoing_table_item').append(dueProjects);
        },
        error : function(){
        }
    });
    
    $.ajax({
        url : '/retrieveProjectsOnWarranty/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	var warrantyProjects = "<tr><td><strong>Projects on Warranty</strong></td></tr>";
        	for (var i=0; i<jsonData.length;i++){
        		warrantyProjects += '<tr><td></td><td ><a href=/project?projectID{"projectID":"'+ jsonData[i].projectID +'"} style="color: #2c3e50">'+jsonData[i].projectID+'</td><td>'+jsonData[i].startDate+'</td><td>'+jsonData[i].endDate+'</td><td><a href=/staff?staffID{"staffID":"' + jsonData[i].pmID + '"} style="color: #2c3e50;">'+jsonData[i].projectManagerName+'</td></tr>'
        	}
        	$('#ongoing_table_item').append(warrantyProjects);
        },
        error : function(){
        }
    });
});

//createProject
function createProjectInfo(){
	var projectInitials = $("#initials").val();
	var projectDescription = $("#description").val();
	var projectStartDate = $('#start').val().valueOf();
	var	projectEndDate = $('#end').val().valueOf();
	var client = $('#client').val().valueOf();
	var ProjectManager = $('#selectPM :selected').val();
	var staffList = $('#selectStaff').chosen().val();
	
	if(booleanStartDate === true && booelanEndDate === true && booleanInitials === true && booleanClient === true && booleanDescription === true && booleanSelectPM === true){
		$('#inputError').empty();
		var projectDetails = '{"projectInitials":"'+projectInitials+'","projectDescription":"'+projectDescription+'","projectStartDate":"'+projectStartDate+'","projectEndDate":"'+projectEndDate+'","client":"'+client+'","projectManager":"'+ProjectManager+'","staffList":%5B';
		if(staffList != null){
			for(var i=0; i<staffList.length-1; i++){
				projectDetails += '"'+staffList[i]+'",';
			}
			projectDetails += '"'+staffList[staffList.length-1]+'"%5D}';
		}else{
			projectDetails += '%5D}';
		}
		
		$.ajax({
		        url : '/createProject/'+projectDetails,
		        type : 'GET',
		        success : function(data){
		        	var jsonData = JSON.parse(data);
		        	var project = jsonData.cardName;
		        	$('#timeline').append('<div id="timelineBar"><div id="draggable" class="ui-widget-content"><p>Drag me to my target</p></div></div>');
		        	
		        	//if (localStorage.trello_token == undefined ||  localStorage.trello_token == "undefined"){
		        		Trello.authorize({
		         			type: 'popup',
		         			scope: { read: true, write: true, account: true},
		         			interactive: true,
		         	    	expiration: "never",
		         	    	persist: "true",
		         	    	success: function() {
		         	    		var info = Trello.post("cards", { name: jsonData.cardName, desc: jsonData.Desc,  idList: "542bcfbd9d1c4dc205159ebd", due:new Date(jsonData.dueDate), urlSource: null});
		         	    		
				        		window.setTimeout(function(){
				        			var trelloCardId = info.responseJSON.id;
				        			   var condition = '{"id":"'+trelloCardId+'","cardName":"'+jsonData.cardName+'"}';
				        			 $.ajax({
				        		        url : '/updateProjectCardId/'+condition,
				        		        type : 'GET',
				        		        async: false,
				        		        success : function(data){
				        		        	 	window.location.href = '/project?projectID{"projectID":"'+project+'"}';
				        		        }
				        		    });
				        		}, 1500);	
				        		
		         	    	}
		         		});
		        		
//		         	}else{
//		         		alert(3);
//		         		Trello.post("cards", { name: jsonData.cardName, desc: jsonData.Desc,  idList: "542bcfbd9d1c4dc205159ebd", due:new Date(jsonData.dueDate), urlSource: null});
//         	    		window.setTimeout(function(){
//         	    			window.location.href = '/project?projectID{"projectID":"'+project+'"}';
//         	    		}, 500);
//		         	}
		        },
		        error : function(){
		        }
		    });
	}else{
		$("#inputError").text("Please fill in all the required fields correctly.");
	}
}

function searchProject(){
	$("#table_project").html("");
	var searchCriteria = '{"projectInfo":"'+$("#searchInput").val()+'"}';
	$.ajax({
        url : '/filterProjectListByProjectInfo/'+searchCriteria,
        type : 'GET',
        success : function(data){
        	 var arr = JSON.parse(data);
	         for (var i = 0; i < arr.length; i = i + 1) {
	        	 $("#table_project").append('<div><h4><strong><a href=/project?projectID{"projectID":"'+ arr[i].projectID +'"} style="color: #2c3e50">'+arr[i].projectID+'</a></strong></h4><span class="glyphicon glyphicon-lock"></span><span id="sidebarInitials">'+(arr[i].projectID).substring(9,12)+'</span><span class="glyphicon glyphicon-user"><span id="sidebarInitials">'+arr[i].projectManagerName+'</span></span><div><span class="glyphicon glyphicon-calendar"><span id="sidebarInitials"><small>'+arr[i].startDate+' - '+arr[i].endDate+'</span></small></span></div></div><hr>');
	         }
        },
        error : function(){
        }
    });
}




