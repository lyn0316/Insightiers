var booleanStartDate = false;
var booelanEndDate = false;
var booleanInitials = false;
var booleanClient = false;
var booleanDescription = false;
var booleanSelectPM = false;

var booleanEditStartDate = false;
var booelanEditEndDate = false;
var booleanEditInitials = false;
var booleanEditClient = false;
var booleanEditDescription = false;
var booleanEditSelectPM = false;

var booleanAddStartDate = false;
var booleanAddEndDate = false;
var booleanAddTaskName = false;
var booleanAddCard_taskType = false;

var booleanEditTaskName = false;
var booelanEditTaskDescription = false;
var booleanEditTaskType = false;
var booleanEditTaskStartDate =false;
var booleanEditTaskEndDate = false;

$(document).ready(function() {
	//get name from session
	$('#user').html(name);
	
	//select
	$('#createProject').on('shown.bs.modal', function () {
		$('.chosen-select', this).chosen();
	});
	
	//logout
	$('#logout').click(function(e){
		e.preventDefault();
		sessionStorage.removeItem("name");
		localStorage.clear();
		window.location.href="/";
	});
	
	$(".chosen-select").chosen({width: "100%"});
	
	$.ajax({
        url : '/retrieveAllProjects',
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	for (var i = 0; i < arr.length; i=i+1){
        		 $("#table_project").append('<div><h4><strong><a href=/project?projectID{"projectID":"'+ arr[i].projectId +'"} style="color: #2c3e50">'+arr[i].projectId+'</a></strong></h4><span class="glyphicon glyphicon-lock"></span><span id="sidebarInitials">'+(arr[i].projectId).substring(9,12)+'</span><span class="glyphicon glyphicon-user"><span id="sidebarInitials">'+arr[i].projectManagerName+'</span></span><div><span class="glyphicon glyphicon-calendar"><span id="sidebarInitials"><small>'+arr[i].projectStartDate+' - '+arr[i].projectEndDate+'</span></small></span></div></div><hr>');
            }
        },
        error : function(){
        }
    });
	
	//display developers
	$.ajax({
        url : '/RetrieveStaffByProject/'+projectID,
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
        		if(arr.staffFromThisProject[i].role === "Developer"){
        			$("#developerInfo").html(arr.staffFromThisProject[i].name);
        		}
        	}
        },
        error : function(){
        }
    });
	
	//scatter chart
	var line = new Morris.Line({
	    element: 'scatter-chart',
	    resize: true,
	    data: [
	        {code: "", Score: 40}
	        
	    ],
	    xkey: ['code'],
	    ykeys: ['Score'],
	    lineWidth:0,
	    hideHover: 'auto',
	    gridTextColor: "A52A2A",
	    gridStrokeWidth: 0.4,
	    pointSize: 4,
	    labels: ['Score'],
	    pointStrokeColors: ["A52A2A"],
	    gridTextFamily: "Open Sans",
	    gridTextSize: 10,
	    grid:true,
	    xLabelAngle: 0,
	    ymax:160,
	    ymin:40,
	    parseTime: false
	});
    
    //date time picker
    $('#startDate').datetimepicker({
		pickTime: false
	});
    
    $('#endDate').datetimepicker({
		pickTime: false
	});
    
    $('#startDate').on('change', function(){
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
    	//var conditions = '{"startDate":"'+$('#start').val().valueOf()+'","endDate":"'+$('#end').val().valueOf()+'"}';
    	//clearOptions("selectPM");
    	//retrievePM(conditions);
	});
    
    $('#endDate').on('change', function(){
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
    	//var conditions = '{"startDate":"'+$('#start').val().valueOf()+'","endDate":"'+$('#end').val().valueOf()+'"}';
    	//clearOptions("selectPM");
    	//retrievePM(conditions);
	});
    
    $('#duration').on('change', function() {
    	if ($('#end').val().valueOf() < $('#start').val().valueOf()){
    		booelanEndDate = false;
			$('#dateError').text('End date cannot be earlier than start date.');
		}else{
			booelanEndDate = true;
			$('#dateError').empty();
		}
    	
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
        //var conditions = '{"startDate":"'+$('#start').val().valueOf()+'","endDate":"'+$('#end').val().valueOf()+'"}';
        //clearOptions("selectPM");
        //retrievePM(conditions);
    });
    
    $('#skillsetRequired').on('change', function() {
    	/*clearOptions("selectStaff");
    	var skills = $("#skillsetRequired").val();
    	var skillsJson = '{"startDate":"' + $('#start').val().valueOf()+'","endDate":"'+$('#end').val().valueOf()+'","skills":%5B';
    	//skillsJson = {"startDate":"2010-10-10","endDate":"2010-10-20","skills":["HTML","JS"]}
	    if(skills != null){
	    	if(skills.length === 1){
	    		skillsJson += '"'+skills+'"%5D}'
	    	}else{
	    		for(var i=0;i<skills.length-1;i++){
		    		skillsJson += '"'+skills[i]+'",';
		    	}
	    		skillsJson += '"'+skills[skills.length-1]+'"%5D}'
	    	}
	    }
	   retrieveAvailableStaffBySkills(skillsJson);*/
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
    
    function retrievePM(conditions){
    	$.ajax({
	        url : '/RetrieveStaffByProject/'+conditions,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr.staffFromThisProject[i].name;
	        		staffFromThisProject.value = arr.staffFromThisProject[i].staffID;
	        		var addSubavailableStaff = document.getElementById("availablePM");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	for (var i = 0; i < arr.staffOutsideThisProject.length; i=i+1){
	        		var staffOutsideThisProject = document.createElement("option");
	        		staffOutsideThisProject.text = arr.staffOutsideThisProject[i].name;
	        		staffOutsideThisProject.value = arr.staffOutsideThisProject[i].staffID;
	        		var addSubunavailableStaff = document.getElementById("unavailablePM");
	        		addSubunavailableStaff.appendChild(staffOutsideThisProject);
	        	}
	        	$("#selectPM").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
	
    //retrieveAvailableStaffBySkills - edit
    function retrieveEditProjectDevelopers(skillsJson){
    	$.ajax({
	        url : '/RetrieveStaffByProject/'+skillsJson,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr.staffFromThisProject[i].name;
	        		staffFromThisProject.value = arr.staffFromThisProject[i].staffID;
	        		var addSubavailableStaff = document.getElementById("editDevelopersFit");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	$("#editFitDevelopers").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
    
    //retrieveAvailableStaffBySkills
    function retrieveProjectDevelopers(skillsJson){
    	$.ajax({
	        url : '/RetrieveStaffByProject/'+skillsJson,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr.staffFromThisProject[i].name;
	        		staffFromThisProject.value = arr.staffFromThisProject[i].staffID;
	        		var addSubavailableStaff = document.getElementById("developersFit");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	$("#selectFitDevelopers").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
    
    //edit
    $('#editStartDate').datetimepicker({
		pickTime: false
	});
    
    $('#editEndDate').datetimepicker({
		pickTime: false
	});
    
    $('#editStartDate').on('change', function(){
    	if(!(isValidDate($('#editStart').val().valueOf()))){
    		booleanEditStartDate = false;
			$('#editStartDateError').text('Invalid date entered.');
    	}else if($('#editStart').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
    		booleanEditStartDate = false;
			$('#editStartDateError').text('Please enter a future date.');
    	}else{
    		booleanEditStartDate = true;
			$('#editStartDateError').empty();
		}
    	var conditions = '{"startDate":"'+$('#editStart').val().valueOf()+'","endDate":"'+$('#editEnd').val().valueOf()+'"}';
        clearOptions("editSelectPM");
        retrieveEditPM(conditions);
	});
    
    $('#editEndDate').on('change', function(){
    	if(!(isValidDate($('#editEnd').val().valueOf()))){
    		booelanEditEndDate = false;
			$('#editEndDateError').text('Invalid date entered.');
    	}else if($('#editEnd').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
    		booelanEditEndDate = false;
			$('#editEndDateError').text('Please enter a future date.');
    	}else if($('#editDuration input:radio:checked').val() === "0" || $('#editDuration input:radio:checked').val() === undefined){
			if ($('#editEnd').val().valueOf() < $('#editStart').val().valueOf()){
				booelanEditEndDate = false;
				$('#editEndDateError').text('End date cannot be earlier than start date.');
			}else{
				booelanEditEndDate = true;
				$('#editEndDateError').empty();
			}
    	}else{
    		booelanEditEndDate = true;
			$('#editEndDateError').empty();
		}
    	var conditions = '{"startDate":"'+$('#editStart').val().valueOf()+'","endDate":"'+$('#editEnd').val().valueOf()+'"}';
        clearOptions("editSelectPM");
        retrieveEditPM(conditions);
	});
    
    $('#editDuration').on('change', function() {
    	if($('#editDuration input:radio:checked').val() === "0"){
        	$('#editEnd').prop('disabled', false);
        }else if($('#editDuration input:radio:checked').val() === "3"){
        	$('#editEnd').prop('disabled', true);
	        if(Date.parse($('#editStart').val().valueOf()) != null){
        		var endDate = Date.parse($('#editStart').val().valueOf()).add(3).months().toString('yyyy-MM-dd');
        		booelanEditEndDate = true;
	        	$('#editEnd').val(endDate);
	        }
        }else if($('#editDuration input:radio:checked').val() === "6"){
        	$('#editEnd').prop('disabled', true);
        	if(Date.parse($('#editStart').val().valueOf()) != null){
        		var endDate = Date.parse($('#editStart').val().valueOf()).add(6).months().toString('yyyy-MM-dd');
        		booelanEditEndDate = true;
        		$('#editEnd').val(endDate);
        	}
        }
    	var conditions = '{"startDate":"'+$('#editStart').val().valueOf()+'","endDate":"'+$('#editEnd').val().valueOf()+'"}';
        clearOptions("editSelectPM");
        retrieveEditPM(conditions);
    });
    
    function retrieveEditPM(conditions){
    	$.ajax({
	        url : '/RetrieveAvailableAndUnavailablePM/'+conditions,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	console.log(arr);
	        	for (var i = 0; i < arr.availablePMList.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr.availablePMList[i].name+"---"+arr.availablePMList[i].ongoingProjectNum+"/"+arr.availablePMList[i].maxProjectNum;
	        		staffFromThisProject.value = arr.availablePMList[i].staffID;
	        		var addSubavailableStaff = document.getElementById("editAvailablePM");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	for (var i = 0; i < arr.unavailablePMList.length; i=i+1){
	        		var staffOutsideThisProject = document.createElement("option");
	        		staffOutsideThisProject.text = arr.unavailablePMList[i].name+"---"+arr.unavailablePMList[i].ongoingProjectNum+"/"+arr.unavailablePMList[i].maxProjectNum;
	        		staffOutsideThisProject.value = arr.unavailablePMList[i].staffID;
	        		var addSubunavailableStaff = document.getElementById("editUnavailablePM");
	        		addSubunavailableStaff.appendChild(staffOutsideThisProject);
	        	}
	        	$("#editSelectPM").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    }
    
    $('#editInitials').on('change', function(){
    	if($('#editInitials').val() === ""){
    		booleanEditInitials = false;
    	}else{
    		booleanEditInitials = true;
    	}
	});
    
    $('#editClient').on('change', function(){
    	if($('#editClient').val() === ""){
    		booleanEditClient = false;
    	}else{
    		booleanEditClient = true;
    	}
	});
    
    $('#editDescription').on('change', function(){
    	if($('#editDescription').val() === ""){
    		booleanEditDescription = false;
    	}else{
    		booleanEditDescription = true;
    	}
	});
    
    $('#editSkillsetRequired').on('change', function() {
    	clearOptions("editFitDevelopers");
    	var skillList = $('#editSkillsetRequired').chosen().val();
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
	    
	    $.ajax({
	        url : '/RetrieveStaffBySkills/'+retrieveStaffByProject,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr[i].name;
	        		staffFromThisProject.value = arr[i].staffID;
	        		var addSubavailableStaff = document.getElementById("editDevelopersFit");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	$("#editFitDevelopers").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
    });
    
    $('#editSelectPM').on('change', function(){
    	if( $('#editSelectPM :selected').val() === ""){
    		booleanEditSelectPM = false;
    	}else{
    		booleanEditSelectPM = true;
    	}
	});
    
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
    
    //subproject
    //retrieveStaffbyProject
    $('#subskillsetRequired').on('change', function() {
    	clearOptions("addSubselectStaff");
    	var skillList = $('#subskillsetRequired').chosen().val();
	    var retrieveStaffByProject = '{"projectID":"'+projectID.substring(14,26)+'"';
	    if(skillList != null){
	    	retrieveStaffByProject += ',"skills":%5B';
	    	for(var i=0; i<skillList.length-1; i++){
		    	retrieveStaffByProject += '"'+skillList[i]+'",';
			}
	    	retrieveStaffByProject += '"'+skillList[skillList.length-1]+'"%5D}';
		}else{
			retrieveStaffByProject += '}';
		}
	    
	    $.ajax({
	        url : '/RetrieveStaffByProject/'+retrieveStaffByProject,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr.staffFromThisProject[i].name;
	        		staffFromThisProject.value = arr.staffFromThisProject[i].staffID;
	        		var addSubavailableStaff = document.getElementById("addSubavailableStaff");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	for (var i = 0; i < arr.staffOutsideThisProject.length; i=i+1){
	        		var staffOutsideThisProject = document.createElement("option");
	        		staffOutsideThisProject.text = arr.staffOutsideThisProject[i].name;
	        		staffOutsideThisProject.value = arr.staffOutsideThisProject[i].staffID;
	        		var addSubunavailableStaff = document.getElementById("addSubunavailableStaff");
	        		addSubunavailableStaff.appendChild(staffOutsideThisProject);
	        	}
	        	$("#addSubselectStaff").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
   });
    
  //retrieveStaffbyProject -- edit task
    $('#editSubskillsetRequired').on('change', function() {
    	clearOptions("edit_subselectStaff");
    	var skillList = $('#editSubskillsetRequired').chosen().val();
	    var retrieveStaffByProject = '{"projectID":"'+projectID.substring(14,26)+'"';
	    if(skillList != null){
	    	retrieveStaffByProject += ',"skills":%5B';
	    	for(var i=0; i<skillList.length-1; i++){
		    	retrieveStaffByProject += '"'+skillList[i]+'",';
			}
	    	retrieveStaffByProject += '"'+skillList[skillList.length-1]+'"%5D}';
		}else{
			retrieveStaffByProject += '}';
		}
	    $.ajax({
	        url : '/RetrieveStaffByProject/'+retrieveStaffByProject,
	        type : 'GET',
	        success : function(data){
	        	var arr = JSON.parse(data);
	        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
	        		var staffFromThisProject = document.createElement("option");
	        		staffFromThisProject.text = arr.staffFromThisProject[i].name;
	        		staffFromThisProject.value = arr.staffFromThisProject[i].staffID;
	        		var editSubavailableStaff = document.getElementById("editSubavailableStaff");
	        		editSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	for (var i = 0; i < arr.staffOutsideThisProject.length; i=i+1){
	        		var staffOutsideThisProject = document.createElement("option");
	        		staffOutsideThisProject.text = arr.staffOutsideThisProject[i].name;
	        		staffOutsideThisProject.value = arr.staffOutsideThisProject[i].staffID;
	        		var editSubunavailableStaff = document.getElementById("editSubunavailableStaff");
	        		editSubunavailableStaff.appendChild(staffOutsideThisProject);
	        	}
	        	$("#edit_subselectStaff").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
   });
    
	$('#addStartDate').datetimepicker({
		pickTime: false
	});
	
	$('#addEndDate').datetimepicker({
		pickTime: false
	});
	
	$('#editTaskStartDate').datetimepicker({
		pickTime: false
	});
	
	$('#editTaskEndDate').datetimepicker({
		pickTime: false
	});
	
	$('#addCard_taskType').on('change', function() {
		console.log($('#addCard_taskType :selected').val());
		if($('#addCard_taskType :selected').val() === "noncoding"){
			$('#addSubselectStaff option:selected').val('1');
		}
	});
	
	$('#addStartDate').on('change', function(){
		if(!(isValidDate($('#addStart').val().valueOf()))){
			booleanAddStartDate = false;
			$('#addCardStartDateError').text('Invalid date entered.');
		}else if($('#addStart').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
			booleanAddStartDate = false;
			$('#addCardStartDateError').text('Please enter a future date.');
		}else if(Date.parse($("#startInfo").html()).toString('yyyy-MM-dd') > $('#addStart').val().valueOf()){
			booleanAddStartDate = false;
			$('#addCardStartDateError').text('Task cannot start before the project start date.');
		}else if(Date.parse($("#endInfo").html()).toString('yyyy-MM-dd') < $('#addStart').val().valueOf()){
			booleanAddStartDate = false;
			$('#addCardStartDateError').text('Task start date cannot be later than the project end date.');
		}/*else if($('#addCardPredecessorTask :selected').val() != ""){
			if(Date.parse($("#addCardPredecessorTask").val().substring(0,10)).toString("yyyy-MM-dd") > $('#addStart').val().valueOf()){
				booleanAddStartDate = false;
				console.log();
				$('#addCardStartDateError').text('Task cannot start before the end date of predecessor tasks.');
			}else{
				booleanAddStartDate = true;
				$('#addCardStartDateError').empty();
			}
		}*/else{
			booleanAddStartDate = true;
			$('#addCardStartDateError').empty();
		}
	});
	
	$('#addEndDate').on('change', function(){
		if(!(isValidDate($('#addEnd').val().valueOf()))){
			booleanAddEndDate = false;
			$('#addCardEndDateError').text('Invalid date entered.');
		}else if($('#addEnd').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
			booleanAddEndDate = false;
			$('#addCardEndDateError').text('Please enter a future date.');
		}else if($('#addStart').val().valueOf() > $('#addEnd').val().valueOf()){
			booleanAddEndDate = false;
			$('#addCardEndDateError').text('Task end date cannot earlier than the task start date.');
		}else{
			booleanAddEndDate = true;
			$('#addCardEndDateError').empty();
		}
	});
	
	$('#addTaskName').on('change', function(){
    	if($('#addTaskName').val() === ""){
    		booleanAddTaskName = false;
    	}else{
    		booleanAddTaskName = true;
    	}
	});
    
    $('#addCard_taskType').on('change', function(){
    	if( $('#addCard_taskType :selected').val() === ""){
    		booleanAddCard_taskType = false;
    	}else{
    		booleanAddCard_taskType = true;
    	}
	});
	
	$('#editTaskStartDate').on('change', function(){
		if(!(isValidDate($('#editTaskStart').val().valueOf()))){
			$('#editCardStartDateError').text('Invalid date entered.');
		}else if($('#editTaskStart').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
			$('#editCardStartDateError').text('Please enter a future date.');
		}else if(Date.parse($("#startInfo").html()).toString('yyyy-MM-dd') > $('#editTaskStart').val().valueOf()){
			booleanAddStartDate = false;
			$('#editCardStartDateError').text('Task cannot start before the project start date.');
		}else if(Date.parse($("#endInfo").html()).toString('yyyy-MM-dd') < $('#editTaskStart').val().valueOf()){
			booleanAddStartDate = false;
			$('#editCardStartDateError').text('Task start date cannot be later than the project end date.');
		}else if($('#addCardPredecessorTask :selected').val() != ""){
			if(Date.parse($("#addCardPredecessorTask").val().substring(0,10)).toString("yyyy-MM-dd") > $('#editTaskStart').val().valueOf()){
				booleanAddStartDate = false;
				$('#editCardStartDateError').text('Task cannot start before the end date of predecessor tasks.');
			}else{
				booleanAddStartDate = true;
				$('#editCardStartDateError').empty();
			}
		}else{
			booleanAddStartDate = true;
			$('#editCardStartDateError').empty();
		}
	});
	
	$('#editTaskEndDate').on('change', function(){
		if(!(isValidDate($('#editTaskEnd').val().valueOf()))){
			$('#editCardEndDateError').text('Invalid date entered.');
		}else if($('#editTaskEnd').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
			$('#editCardEndDateError').text('Please enter a future date.');
		}else{
			$('#editCardEndDateError').empty();
		}
	});
	
	$.ajax({
        url : '/RetrieveStaffByProject/'+'{"projectID":"'+projectID.substring(14,26)+'","skills":%5B%5D}',
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	//add task
        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
        		var staffFromThisProject = document.createElement("option");
        		staffFromThisProject.text = arr.staffFromThisProject[i].name;
        		staffFromThisProject.value = arr.staffFromThisProject[i].staffID;
        		var addSubavailableStaff = document.getElementById("addSubavailableStaff");
        		addSubavailableStaff.appendChild(staffFromThisProject);
        	}
        	for (var i = 0; i < arr.staffOutsideThisProject.length; i=i+1){
        		var staffOutsideThisProject = document.createElement("option");
        		staffOutsideThisProject.text = arr.staffOutsideThisProject[i].name;
        		staffOutsideThisProject.value = arr.staffOutsideThisProject[i].staffID;
        		var addSubunavailableStaff = document.getElementById("addSubunavailableStaff");
        		addSubunavailableStaff.appendChild(staffOutsideThisProject);
        	}
        	$("#addSubselectStaff").trigger("chosen:updated");
        	
        	//edit task
        	for (var i = 0; i < arr.staffFromThisProject.length; i=i+1){
        		var staffFromThisProject = document.createElement("option");
        		staffFromThisProject.text = arr.staffFromThisProject[i].name;
        		staffFromThisProject.value = arr.staffFromThisProject[i].staffID;
        		var editSubavailableStaff = document.getElementById("editSubavailableStaff");
        		editSubavailableStaff.appendChild(staffFromThisProject);
        	}
        	for (var i = 0; i < arr.staffOutsideThisProject.length; i=i+1){
        		var staffOutsideThisProject = document.createElement("option");
        		staffOutsideThisProject.text = arr.staffOutsideThisProject[i].name;
        		staffOutsideThisProject.value = arr.staffOutsideThisProject[i].staffID;
        		var editSubunavailableStaff = document.getElementById("editSubunavailableStaff");
        		editSubunavailableStaff.appendChild(staffOutsideThisProject);
        	}
        	$("#edit_subselectStaff").trigger("chosen:updated");
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
		        	window.location.href = '/project?projectID{"projectID":"'+project+'"}';
		        },
		        error : function(){
		        }
		    });
	}else{
		$("#inputError").text("Please fill in all the required fields correctly.");
	}
}

function editProject(){
	projectID = projectID.substring(14,26);
	var projectInitials = $("#editInitials").val();
	var projectDescription = $("#editDescription").val();
	var projectStartDate = $('#editStart').val().valueOf();
	var	projectEndDate = $('#editEnd').val().valueOf();
	var client = $('#editClient').val().valueOf();
	var ProjectManager = $('#editSelectPM :selected').val();
	var staffList = $('#editSelectStaff').chosen().val();
	
	if(true){
		$('#editInputError').empty();
		var projectDetails = '{"projectID":"'+projectID+'","projectDescription":"'+projectDescription+'","projectStartDate":"'+projectStartDate+'","projectEndDate":"'+projectEndDate+'","client":"'+client+'","projectManager":"'+ProjectManager+'","staffList":%5B';
		if(staffList != null){
			for(var i=0; i<staffList.length-1; i++){
				projectDetails += '"'+staffList[i]+'",';
			}
			projectDetails += '"'+staffList[staffList.length-1]+'"%5D}';
		}else{
			projectDetails += '%5D}';
		}
		
		
		$.ajax({
			
		        url : '/updateProject/'+projectDetails,
		        type : 'GET',
		        success : function(data){
		        	var jsonData = JSON.parse(data);
		        	var project = jsonData.cardName;
		        	
		        	var cardId = jsonData.cardID;

		        	Trello.authorize({
		    			type: 'popup',
		    			scope: { read: true, write: true, account: true},
		    			interactive: true,
		    	 	expiration: "never",
		    	 	persist: "true",
		    	 	success: function() {
		    	 		
		    	 		Trello.put("cards/"+cardId+"/",{ name: jsonData.cardName, desc: jsonData.Desc,  idList: "542bcfbd9d1c4dc205159ebd", due:new Date(jsonData.dueDate), urlSource: null});
		    	 		
		    	 	}
		    		});
		        	
		        	window.setTimeout(function(){
		        		window.location.href = '/project?projectID{"projectID":"'+project+'"}';
	        		}, 1500);	
		        	
		        },
		        error : function(){
		        }
		    });
	}else{
		$("#editInputError").text("Please fill in all the required fields correctly.");
	}
}

function closeProject(){
	$("#statusInfo").html("warranty");
	$("#actualendInfo").html(Date.today().toString('yyyy-MM-dd'));
	$('#endProject').modal('hide');
}

//sub
function createSubproject(){
	var subStart = $('#addStart').val().valueOf();
	var subEnd = $('#addEnd').val().valueOf();
	var taskname = $('#addTaskName').val();
	var taskDescription = $('#addTaskDescription').val();
	var type = $('#addCard_taskType :selected').val();
	var staffList = $('#addSubselectStaff').chosen().val();
	
	if(booleanAddStartDate === true && booleanAddEndDate === true && booleanAddTaskName === true && booleanAddCard_taskType === true){
		$('#addCardError').empty();
		var subprojectDetails = '{"projectID":"'+projectID.substring(14,26)+'","subprojectName":"'+taskname+'","subprojectDescription":"'+taskDescription+'","subprojectType":"'+type+'","predecessorID":"0","subprojectStartDate":"'+subStart+'","subprojectEndDate":"'+subEnd+'","staffList":%5B';
		if(staffList != null){
			for(var i=0; i<staffList.length-1; i++){
				subprojectDetails += '"'+staffList[i]+'",';
			}
			subprojectDetails += '"'+staffList[staffList.length-1]+'"%5D}';
		}else{
			subprojectDetails += '%5D}';
		}
        
        $.ajax({
	        url : '/createSubproject/'+subprojectDetails,
	        type : 'GET',
	        success : function(data){
	        	var jsonData = JSON.parse(data);
	        	var project = jsonData.cardName;
	        	
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
		        			   var condition = '{"subprojectCardID":"'+trelloCardId+'","subprojectCardName":"'+jsonData.cardName+'"}';
		        			 $.ajax({
		        		        url : '/updateSubprojectCardId/'+condition,
		        		        type : 'GET',
		        		        async: false,
		        		        success : function(data){
		        		        	window.location.href='/project?projectID'+projectID;
		        		        }
		        		    });
		        		}, 1500);	
		        		
         	    	}
         		});
	        	
	        },
	        error : function(){
	        }
	    });
        
	}else{
		$("#addCardError").text("Please fill in all the required fields correctly.");
	}
}

function editCard(name,plannedStart,plannedEnd,type,description,subID){
	$('#editTaskName').val(name);
	$('#editTaskDescription').val(description);
	$('#editTaskStart').val(plannedStart);
	$('#editTaskEnd').val(plannedEnd);
	$('#editTaskType').val(type);
    $('#editTaskType').trigger("chosen:updated");
    //$('#edit_subselectStaff').val(staffList);
    //$('#edit_subselectStaff').trigger("chosen:updated");
    $('#editSubprojectBtn').click( function() { editSubproject(subID) });
}

function editSubproject(subID){
	var subStart = $('#editTaskStart').val().valueOf();
	var subEnd = $('#editTaskEnd').val().valueOf();
	var taskname = $('#editTaskName').val();
	var taskDescription = $('#editTaskDescription').val();
	var type = $('#editTaskType :selected').val();
	var staffList = $('#edit_subselectStaff').chosen().val();
		
	if(true){
		var subprojectDetails = '{"projectID":"'+projectID.substring(14,26)+'","subprojectID":"'+subID+'","subprojectName":"'+taskname+'","subprojectDescription":"'+taskDescription+'","subprojectType":"'+type+'","predecessorID":"0","subprojectStartDate":"'+subStart+'","subprojectEndDate":"'+subEnd+'","staffList":%5B';
		if(staffList != null){
			for(var i=0; i<staffList.length-1; i++){
				subprojectDetails += '"'+staffList[i]+'",';
			}
			subprojectDetails += '"'+staffList[staffList.length-1]+'"%5D}';
		}else{
			subprojectDetails += '%5D}';
		}

        $.ajax({
			
        	 url : '/updateSubproject/'+subprojectDetails,
	        type : 'GET',
	        success : function(data){
	        	var jsonData = JSON.parse(data);
	        	var project = jsonData.cardName;
	        	
	        	var cardId = jsonData.subprojectCardID;
	        	
	        	Trello.authorize({
	    			type: 'popup',
	    			scope: { read: true, write: true, account: true},
	    			interactive: true,
	    	 	expiration: "never",
	    	 	persist: "true",
	    	 	success: function() {
	    	 		Trello.put("cards/"+cardId+"/",{ name: jsonData.cardName, desc: jsonData.Desc,  idList: "542bcfbd9d1c4dc205159ebd", due:new Date(jsonData.dueDate), urlSource: null});
	    	 		
	    	 	}
	    		});
	        	
	        	window.setTimeout(function(){
	        		window.location.href='/project?projectID'+projectID;
        		}, 1500);	
	        	
	        },
	        error : function(){
	        }
	    });
        
	}else{
		$("#editCardError").text("Please fill in all the required fields correctly.");
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


function synWithTrello(){
	
	Trello.authorize({
			type: 'popup',
			scope: { read: true, write: true, account: true},
			interactive: true,
	    	expiration: "never",
	    	persist: "true",
	    	success: function() {
	    		info = Trello.get("boards/542bcfbd9d1c4dc205159ebc/cards?fields=id,desc,idList,name,dateLastActivity");
	    		
	    		window.setTimeout(function(){
	    			
	    			$.ajax({
	    				
				        url : '/updateFromTrello/%5B'+JSON.stringify(info.responseJSON).substr(1,JSON.stringify(info.responseJSON).length-2)+"%5D",
				        type : 'GET',
				        success : function(data){
				        	var arr = JSON.parse(data);
				        	
				        	//window.location.href = '/project?projectID{"projectID":"'+project+'"}';
				        	//var statusChange = jsonData.statusUpdateLog;
				        	console.log(arr.statusUpdateLog);
				        	
				        	if (arr.statusUpdateLog.length > 0){
				        		$("#approvalListTable").append("<tr><th>Task Name</th><th>Status From</th><th>Status To</th><th>Action</th></tr>")
				        	}
				        	
				        	if (arr.descUpdateLog.length > 0){
				        		$("#descChangeListTable").append("<tr><th>Task Name</th><th>Orig Desc</th><th>New Desc</th></tr>")
				        	}
				        	
				        	
				        	for (var i = 0; i < arr.statusUpdateLog.length; i=i+1){
				        		if (arr.statusUpdateLog[i].subprojectName != null ){
				        			$("#approvalListTable").append("<tr><td>"+arr.statusUpdateLog[i].subprojectName+"</td><td>"+arr.statusUpdateLog[i].originalStatus+"</td><td>"+arr.statusUpdateLog[i].newStatus+"</td><td><button type='button' class='btn btn-primary btn-xs' onclick=approvalUpdateFromTrello('"+arr.statusUpdateLog[i].projectId+"','"+arr.statusUpdateLog[i].type+"','"+arr.statusUpdateLog[i].newStatus+"','"+arr.statusUpdateLog[i].subprojectId+"')>Approval</button> <button type='button' class='btn btn-danger btn-xs' onclick=rejectUpdateFromTrello('"+arr.statusUpdateLog[i].cardId+"','"+arr.statusUpdateLog[i].originalIdList+"')>Rejected</button></td></tr>");
				        		}
				        	}
				        	
				        	for (var j = 0; j < arr.descUpdateLog.length; j=i+1){
				        			$("#descChangeListDetail").append("<tr><td>"+arr.descUpdateLog[j].projectId+"</td><td>"+arr.descUpdateLog[j].originalDesc+"</td><td>"+arr.descUpdateLog[j].newDesc+"</td></tr>");
				        	}
				        	
				        	
				        	$('#approvalList').modal('show');
				        	
				        },
				        error : function(){
				        }
				    });
        			
        		}, 2000);	
	    		
	    		//member = Trello.get("boards/542bcfbd9d1c4dc205159ebc/members");
	    		//console.log(member);
	    	}
		});
	
}

function approvalUpdateFromTrello(projectId,type,newStatus,subprojectId){
	console.log("I am here "+ projectId);
	var condition = '{"projectId":"'+projectId+'","type":"'+type+'","newStatus":"'+newStatus+'","subprojectId":"'+subprojectId+'"}';
	$.ajax({
        url : '/updateStatusFromTrello/'+condition,
        type : 'GET',
        success : function(data){
        	 
        },
        error : function(){
        }
    });
}

function rejectUpdateFromTrello(cardId,originalIdList){
	Trello.authorize({
		type: 'popup',
		scope: { read: true, write: true, account: true},
		interactive: true,
 	expiration: "never",
 	persist: "true",
 	success: function() {
 		
 		Trello.put("cards/"+cardId+"/",{idList: originalIdList});
 		
 	}
	});
	
	window.setTimeout(function(){
		window.location.reload();
	}, 1000);
}



function createCard(){
	if (localStorage.trello_token == undefined ||  localStorage.trello_token == "undefined"){
 		Trello.authorize({
 			type: 'popup',
 			scope: { read: true, write: true, account: true},
 			interactive: true,
 	    	expiration: "never",
 	    	persist: "true",
 	    	success: function() {
 	    		Trello.post("cards", { name: "Lidan is a PIG", desc: "this is a test",  idList: "542bcfbd9d1c4dc205159ebd", due: new Date("December 25, 2015 12:00:00"), urlSource: null});
 	    	}
 		});
 	}else{
 		Trello.post("cards", { name: "hhhhhhh", desc: "this is a test",  idList: "542bcfbd9d1c4dc205159ebd", due: new Date("December 25, 2015 12:00:00"), urlSource: null});
 	}
}

function updateCard(){
	Trello.authorize({
			type: 'popup',
			scope: { read: true, write: true, account: true},
			interactive: true,
	 	expiration: "never",
	 	persist: "true",
	 	success: function() {
	 		var info = Trello.put("cards/54ae7e131c58c99cb166dfb1/",{name:"lidan shi ge zhu"});
	 		
		
	 	}
		});
}
