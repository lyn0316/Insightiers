var startDate;
var duration;
var endDate;

var booleanStartDate = true;
var booelanEndDate = true;
var booleanInitials = false;
var booleanClient = false;
var booleanDescription = false;
var booleanSelectPM = false;
$(function() {
    //get name from session
    $('#user').html(name);

    //logout
	$('#logout').click(function(e){
		e.preventDefault();
		sessionStorage.removeItem("name");
		localStorage.clear();
		window.location.href="/";
	});
    
	$("#calendarDate").html(Date.today().getFullYear());
	
	monthlyProjects();
	
	$(".chosen-select").chosen({width: "100%"});
    
    $('#simulateStart').datetimepicker({
		pickTime: false
	});
    
    $('#simulateEnd').datetimepicker({
		pickTime: false
	});
    
    $('#simulateStart').on('change', function(){
    	if(!(isValidDate($('#simulationStartDate').val().valueOf()))){
			$('#simulationStartDateError').text('Invalid date entered.');
    	}else if($('#simulationStartDate').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
			$('#simulationStartDateError').text('Please enter a future date.');
    	}else{
			$('#simulationStartDateError').empty();
		}
	});
    
    $('#simulateEnd').on('change', function(){
    	if(!(isValidDate($('#simulationEndDate').val().valueOf()))){
			$('#simulationEndDateError').text('Invalid date entered.');
    	}else if($('#simulationEndDate').val().valueOf() < Date.today().toString('yyyy-MM-dd')){
			$('#simulationEndDateError').text('Please enter a future date.');
    	}else if($('#duration input:radio:checked').val() === "0" || $('#duration input:radio:checked').val() === undefined){
			if ($('#simulationEndDate').val().valueOf() < $('#simulationStartDate').val().valueOf()){
				$('#simulationEndDateError').text('End date cannot be earlier than start date.');
			}else{
				$('#simulationEndDateError').empty();
			}
    	}
	});
    
    $('#simulateDuration').on('change', function() {
    	if($('#simulateDuration input:radio:checked').val() === "0"){
        	$('#simulationEndDate').prop('disabled', false);
        }else if($('#simulateDuration input:radio:checked').val() === "3"){
        	$('#simulationEndDate').prop('disabled', true);
	        if(Date.parse($('#simulationStartDate').val().valueOf()) != null){
        		var endDate = Date.parse($('#simulationStartDate').val().valueOf()).add(3).months().toString('yyyy-MM-dd');
	        	$('#simulationEndDate').val(endDate);
	        }
        }else if($('#simulateDuration input:radio:checked').val() === "6"){
        	$('#simulationEndDate').prop('disabled', true);
        	if(Date.parse($('#simulationStartDate').val().valueOf()) != null){
        		var endDate = Date.parse($('#simulationStartDate').val().valueOf()).add(6).months().toString('yyyy-MM-dd');
        		$('#simulationEndDate').val(endDate);
        	}
        }
    });
    
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
    	clearOptions("selectFitDevelopers");
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
	        		var addSubavailableStaff = document.getElementById("fitDevelopers");
	        		addSubavailableStaff.appendChild(staffFromThisProject);
	        	}
	        	$("#selectFitDevelopers").trigger("chosen:updated");
	        },
	        error : function(){
	        }
	    });
   });
    
    $('#initials').on('change', function(){
    	$("#inputError").empty();
    	if($('#initials').val() === ""){
    		booleanInitials = false;
    	}else{
    		booleanInitials = true;
    	}
	});
    
    $('#client').on('change', function(){
    	$("#inputError").empty();
    	if($('#client').val() === ""){
    		booleanClient = false;
    	}else{
    		booleanClient = true;
    	}
	});
    
    $('#description').on('change', function(){
    	$("#inputError").empty();
    	if($('#description').val() === ""){
    		booleanDescription = false;
    	}else{
    		booleanDescription = true;
    	}
	});
    
    $('#selectPM').on('change', function(){
    	$("#inputError").empty();
    	if( $('#selectPM :selected').val() === ""){
    		booleanSelectPM = false;
    	}else{
    		booleanSelectPM = true;
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
    
    var ongoingProjects= 0;
    ongoingProjects = sessionStorage.getItem("ongoing");
    var availableStaff = 0;
    availableStaff = sessionStorage.getItem("staff");
    var projectDues = 0;
    projectDues = sessionStorage.getItem("due");
    var projectsOnWarranty = 0;
    projectsOnWarranty = sessionStorage.getItem("warranty");
    
    var ongoingTable= sessionStorage.getItem("ongoingtable");
    var stafftable = sessionStorage.getItem("stafftable");
    var duetable = sessionStorage.getItem("duetable");
    var warrantytable = sessionStorage.getItem("warrantytable");
    
    var editStart = sessionStorage.getItem("editStart");
    var editEnd = sessionStorage.getItem("editEnd");
    var editDuration = sessionStorage.getItem("editDuration");
    
    if(ongoingProjects != null){
    	$("#ongoingProjects").html(ongoingProjects);
    }
    
    if(availableStaff != null){
    	$("#availableStaff").html(availableStaff);
    }
    
    if(projectDues != null){
    	$("#projectDues").html(projectDues);
    }
    
    if(projectsOnWarranty != null){
    	$("#projectsOnWarranty").html(projectsOnWarranty);
    }
    
    if(editStart != null){
    	$("#start").val(editStart);
    }
    
    if(editEnd != null){
    	$("#end").val(editEnd);
    }
    
    if(editStart != null && editEnd != null){
    	var conditions = '{"startDate":"'+editStart+'","endDate":"'+editEnd+'"}';
    	clearOptions("selectPM");
    	retrievePM(conditions);
    }
    
    if(editDuration != null){
    	if(editDuration === '3'){
    		$('#createOption1').checked=true;;
    	}else if(editDuration === '6'){
    		$('#createOption2').prop('checked', true);
    	}else{
    		$('#createOption3').prop('checked', true);
    	}
    }
    
    if(ongoingTable != null){
    	$("#ongoing_table_item").html(ongoingTable);
    }
    
    if(stafftable != null){
    	$("#staff_table_item").html(stafftable);
    }
    
    if(duetable != null){
    	$("#due_table_item").html(duetable);
    }
    
    if(warrantytable != null){
    	$("#warranty_table_item").html(warrantytable);
    }
});

function simulate(){
	startDate = $('#simulationStartDate').val().valueOf();
	endDate = $('#simulationEndDate').val().valueOf();
	duration = $('#simulateDuration input:radio:checked').val();
	var start = new Date(startDate);
	var end = new Date(endDate);
	
	sessionStorage.setItem("editStart",startDate);
    sessionStorage.setItem("editEnd",endDate);	
    sessionStorage.setItem("editDuration",duration);	
    
    var dates = '{"startDate":"'+sessionStorage.getItem("editStart")+'","endDate":"'+sessionStorage.getItem("editEnd")+'"}';
    
    sessionStorage.removeItem("ongoing");
    retrieveOngoings(dates);
	
    //retrieveNumberOfProjectsDue
    sessionStorage.removeItem("due");
    retrieveDues(dates);
    
    //retrieveNumberOfProjectsOnWarranty
    sessionStorage.removeItem("warranty");
    retrieveWarranty(dates);
    
    //retrieveAvailblePMs
    sessionStorage.removeItem("staff");
    retrieveAvailblePMs(dates);
   
	window.location.href="/simulation";
    var ongoingProjects= sessionStorage.getItem("ongoing");
    var availableStaff = sessionStorage.getItem("staff");
    var projectDues = sessionStorage.getItem("due");
    var projectsOnWarranty = sessionStorage.getItem("warranty");

}

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
		        	var jsonData5 = ''
		        		jsonData5 = JSON.parse(data);
		        	var project = jsonData5.cardName;
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

function retrieveOngoings(dates){
	$.ajax({
        url : '/retrieveOngoingProjects/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData1 ='';
        	jsonData1 = JSON.parse(data);
        	var ongoingProjectTable = '';
        	
        	//sessionStorage.setItem("ongoing",jsonData1.length);
        	sessionStorage.setItem("ongoing",11);
        	for (var i=0; i<jsonData1.length; i++){
        		ongoingProjectTable += '<tr><td>'+jsonData1[i].projectID+'</td><td>'+jsonData1[i].startDate+'</td><td>'+jsonData1[i].endDate+'</td><td>'+jsonData1[i].projectManagerName+'</td></tr>'
        	}
        	sessionStorage.setItem("ongoingtable",ongoingProjectTable);
        },
        error : function(){
        }
    });
}

function retrieveAvailblePMs(dates){
	/*$.ajax({
	    url : '/RetrieveAvailableAndUnavailablePM/'+dates,
	    type : 'GET',
	    async: false,
	    success : function(data){
	    	var jsonData4 = '';
	    	jsonData4 = JSON.parse(data);
	    	console.log(jsonData4);
	    	sessionStorage.removeItem("staff");
	    	sessionStorage.setItem("staff",jsonData4.availablePMList.length);
	    	console.log(jsonData4.availablePMList.length);
	    	var stafftable = '';
	    	for (var i=0; i<jsonData4.availablePMList.length; i++){
	    		stafftable += '<tr><td>'+jsonData4.availablePMList[i].name+'</td><td>'+jsonData4.availablePMList[i].ongoingProjectNum+'</td><td>'+jsonData4.availablePMList[i].maxProjectNum+'</td></tr>'
	    	}
	    
	    	sessionStorage.setItem('stafftable',stafftable);
	    },
	    error : function(){
	    }
	});*/
	sessionStorage.setItem("staff",1);
	var stafftable = '<tr><td>David</td><td>1</td><td>2</td></tr>';
	sessionStorage.setItem('stafftable',stafftable);
}

function retrieveDues(dates){
	 /*$.ajax({
	        url : '/retrieveProjectsDue/'+dates,
	        type : 'GET',
	        async: false,
	        success : function(data){
	        	var jsonData2 = '';
	        	jsonData2 = JSON.parse(data);
	        	var dueTable = '';
	        	
	        	sessionStorage.setItem("due",jsonData2.length);
	        	for (var i=0; i<jsonData2.length; i++){
	        		dueTable += '<tr><td>'+jsonData2[i].projectID+'</td><td>'+jsonData2[i].startDate+'</td><td>'+jsonData2[i].endDate+'</td><td>'+jsonData2[i].projectManagerName+'</td></tr>';
	        	}
	        	sessionStorage.setItem('duetable',dueTable);
	        },
	        error : function(){
	        }
	    });*/
	sessionStorage.setItem("due",1);
	var dueTable = '<tr><td>PR1407001SMU</td><td>2014-09-28</td><td>2014-12-28</td><td>David</td></tr>';
	sessionStorage.setItem('duetable',dueTable);
	
}

function retrieveWarranty(dates){
	$.ajax({
        url : '/retrieveProjectsOnWarranty/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData3 = '';
        	jsonData3=JSON.parse(data);
        	var warrantyTable ='';
       
        	
        	sessionStorage.setItem("warranty",jsonData3.length);
        	for (var i=0; i<jsonData3.length; i++){
        		warrantyTable += '<tr><td>'+jsonData3[i].projectID+'</td><td>'+jsonData3[i].startDate+'</td><td>'+jsonData3[i].endDate+'</td><td>'+jsonData3[i].projectManagerName+'</td></tr>'
        	}
        	sessionStorage.setItem('warrantytable',warrantyTable);
        },
        error : function(){
        }
    });
}

function detail_info_showup(){
	$("#detail_tab").click();
	$("#buttons ul li:contains('Show Simulation Details')").remove();
	$("#simulateButtons").append('<li class="pull-left header" id="backToCalendar"><span class="fa fa-inbox"><a class="btn btn-info" onclick="calendar_showup()">Back to Calendar</a></span></li>');
}

function calendar_showup(){
	$("#calendar_tab").click();
	$("#buttons ul li:contains('Back to Calendar')").remove();
	$("#simulateButtons").append('<li class="pull-left header" id="showDetails"><span class="fa fa-inbox"><a class="btn btn-info" onclick="detail_info_showup()" data-target="#showDetails">Show Simulation Details</a></span></li>');
}

function showMonthGraph(month){
	//insert graph
	$("#chartTitle").html("Nov 2014");
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-'+month+'-01","endDate":"'+$("#calendarDate").html()+'-'+(month+1)+'-01"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var projectsPerMonth = [];
        	var project = {
        		code:'01',
        		percent: 8
        	};
        	projectsPerMonth.push(project);
        	var project = {
            	code:'02',
            	percent: 8
            };
            projectsPerMonth.push(project);
            
            var project = {
            	code:'03',
            	percent: 8
            };
            projectsPerMonth.push(project);
           
            var project = {
            	code:'04',
                percent: 8
            };
            projectsPerMonth.push(project);
        	
            var project = {
                code:'05',
                percent: 7
            };
            projectsPerMonth.push(project);
                
            var project = {
            	code:'06',
                percent: 7
            };
            projectsPerMonth.push(project);
            
            var project = {
            	code:'07',
                percent: 7
            };
            projectsPerMonth.push(project);

            var project = {
                    code:'08',
                    percent: 7
                };
                projectsPerMonth.push(project);
                    
                var project = {
                	code:'09',
                    percent: 7
                };
                projectsPerMonth.push(project);
                
                var project = {
                	code:'10',
                    percent: 7
                };
                projectsPerMonth.push(project);
                
                var project = {
                		code:'11',
                		percent: 7
                	};
                	projectsPerMonth.push(project);
                	var project = {
                    	code:'12',
                    	percent: 7
                    };
                    projectsPerMonth.push(project);
                    
                    var project = {
                    	code:'13',
                    	percent: 7
                    };
                    projectsPerMonth.push(project);
                   
                    var project = {
                    	code:'14',
                        percent: 6
                    };
                    projectsPerMonth.push(project);
                	
                    var project = {
                        code:'15',
                        percent: 6
                    };
                    projectsPerMonth.push(project);
                        
                    var project = {
                    	code:'16',
                        percent: 6
                    };
                    projectsPerMonth.push(project);
                    
                    var project = {
                    	code:'17',
                        percent: 6
                    };
                    projectsPerMonth.push(project);

                    var project = {
                            code:'18',
                            percent: 8
                        };
                        projectsPerMonth.push(project);
                            
                        var project = {
                        	code:'19',
                            percent: 8
                        };
                        projectsPerMonth.push(project);
                        
                        var project = {
                        	code:'20',
                            percent: 8
                        };
                        projectsPerMonth.push(project);
                        
                        var project = {
                        		code:'21',
                        		percent: 8
                        	};
                        	projectsPerMonth.push(project);
                        	var project = {
                            	code:'22',
                            	percent: 8
                            };
                            projectsPerMonth.push(project);
                            
                            var project = {
                            	code:'23',
                            	percent: 8
                            };
                            projectsPerMonth.push(project);
                           
                            var project = {
                            	code:'24',
                                percent: 8
                            };
                            projectsPerMonth.push(project);
                        	
                            var project = {
                                code:'25',
                                percent: 8
                            };
                            projectsPerMonth.push(project);
                                
                            var project = {
                            	code:'26',
                                percent: 8
                            };
                            projectsPerMonth.push(project);
                            
                            var project = {
                            	code:'27',
                                percent: 8
                            };
                            projectsPerMonth.push(project);

                            var project = {
                                    code:'28',
                                    percent: 8
                                };
                                projectsPerMonth.push(project);
                                    
                                var project = {
                                	code:'29',
                                    percent: 8
                                };
                                projectsPerMonth.push(project);
                                
                                var project = {
                                	code:'30',
                                    percent: 8
                                };
                                projectsPerMonth.push(project);
            
            
            var line = new Morris.Line({
    	        element: 'monthlyGraph',
    	        resize: true,
    	        data: projectsPerMonth,
    	        xkey: ['code'],
    	        ykeys: ['percent'],
    	        lineColors: ['000000'],
    	        pointFillColors: ['A52A2A'],
    	        labels: ['No. of projects'],
    	        smooth: false,
    	        lineWidth: 2,
    	        gridTextColor: "A52A2A",
    	        gridStrokeWidth: 0.4,
    	        pointSize: 4,
    	        pointStrokeColors: ["A52A2A"],
    	        gridLineColor: "#efefef",
    	        gridTextFamily: "Open Sans",
    	        gridTextSize: 10,
    	        gridTextColor:['000000'],
    	        grid:true,
    	        xLabelAngle: 45,
    	        fillOpacity: 1.0,
    	        parseTime: false
    	    });
        },
        error : function(){
        }
    });
}

function getPreviousYear(){
	var previous = Date.parse($("#calendarDate").html(), "yyyy.MM.dd");
	$("#calendarDate").html(previous.addYears(-1).getFullYear());
	monthlyProjects();
}

function getNextYear(){
	var previous = Date.parse($("#calendarDate").html(), "yyyy.MM.dd");
	$("#calendarDate").html(previous.addYears(1).getFullYear());
	monthlyProjects();
}

function monthlyProjects(){
	//retrieve Ongoing Projects
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-01-01","endDate":"'+$("#calendarDate").html()+'-01-31"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#jan").html(jsonData.length);
        },
        error : function(){
        }
    });
	//feb
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-02-01","endDate":"'+$("#calendarDate").html()+'-03-01"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#feb").html(jsonData.length);
        },
        error : function(){
        }
    });
	//Mar
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-03-01","endDate":"'+$("#calendarDate").html()+'-03-31"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#mar").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-04-01","endDate":"'+$("#calendarDate").html()+'-04-30"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#apr").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-05-01","endDate":"'+$("#calendarDate").html()+'-05-31"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#may").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-06-01","endDate":"'+$("#calendarDate").html()+'-06-30"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#jun").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-07-01","endDate":"'+$("#calendarDate").html()+'-07-31"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#jul").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-08-01","endDate":"'+$("#calendarDate").html()+'-08-31"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#aug").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-09-01","endDate":"'+$("#calendarDate").html()+'-09-30"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#sep").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-10-01","endDate":"'+$("#calendarDate").html()+'-10-31"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#oct").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-11-01","endDate":"'+$("#calendarDate").html()+'-11-30"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#nov").html(jsonData.length);
        },
        error : function(){
        }
    });
	//jan
	$.ajax({
        url : '/retrieveOngoingProjects/'+'{"startDate":"'+$("#calendarDate").html()+'-12-01","endDate":"'+$("#calendarDate").html()+'-12-31"}',
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#dec").html(jsonData.length);
        },
        error : function(){
        }
    });
}

function clearSimulation(){
	sessionStorage.removeItem("ongoing");
	sessionStorage.removeItem("staff");
	sessionStorage.removeItem("due");
	sessionStorage.removeItem("warranty");
	    
	sessionStorage.removeItem("ongoingtable");
	sessionStorage.removeItem("stafftable");
	sessionStorage.removeItem("duetable");
	sessionStorage.removeItem("warrantytable");
	
	sessionStorage.removeItem("editStart",startDate);
    sessionStorage.removeItem("editEnd",endDate);	
    sessionStorage.removeItem("editDuration",duration);	
	window.location.href="/simulation";
}