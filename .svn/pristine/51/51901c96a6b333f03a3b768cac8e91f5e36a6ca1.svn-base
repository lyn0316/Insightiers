/* Morris.js Charts */
$(document).ready(function() {
	
	//get name from session
	$('#user').html(name);
	
	//logout
    $('#logout').click(function(e){
		e.preventDefault();
		sessionStorage.removeItem("name");
		window.location.href="/";
	});
    
    //retrieveNumberOfOngoingProjects
    var date = Date.today().toString('yyyy-MM-dd');
    var dates = '{"startDate":"'+date+'","endDate":"'+date+'"}';
    $.ajax({
        url : '/retrieveOngoingProjects/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$('#ongoingProjects').html(jsonData.length);
        },
        error : function(){
        }
    });
	
    //retrieveNumberOfProjectsDue
    $.ajax({
        url : '/retrieveProjectsDue/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$('#projectDues').html(jsonData.length);
        },
        error : function(){
        }
    });
    
    //retrieveNumberOfProjectsOnWarranty
    $.ajax({
        url : '/retrieveProjectsOnWarranty/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$('#projectsOnWarranty').html(jsonData.length);
        },
        error : function(){
        }
    });
    
  //retrieveAvailblePMs
    $.ajax({
        url : '/RetrieveAvailableAndUnavailablePM/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$('#availablePMs').html(jsonData.availablePMList.length);
        },
        error : function(){
        }
    });
    
	//company project intake overview 
    var year = Date.today().toString("yyyy");
    $.ajax({
        url : '/retrieveAverageProjectNumberPerMonth/'+'{"year":"'+year+'"}',
        type : 'GET',
        success : function(data){
        	var jsonData = JSON.parse(data);
        	var projectsPerMonth = [];
        	for (var i = 0; i < jsonData.length; i=i+1){
        		var project = {
        			code:jsonData[i].month,
        			Avg_No: jsonData[i].number
        		};
        		projectsPerMonth.push(project);
        	}
        	
        	var line = new Morris.Line({
    	        element: 'project-overview-chart',
    	        resize: true,
    	        data: projectsPerMonth,
    	        xkey: ['code'],
    	        ykeys: ['Avg_No'],
    	        lineColors: ['000000'],
    	        pointFillColors: ['A52A2A'],
    	        labels: ['Avg_No'],
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
    
    //numOfProjectByPM
    var chart;
    var legend;
    var selected;
    
    $.ajax({
        url : '/retrieveNumberOfProjectsByPM',
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	var projectsUnderPM = [];
        	for (var i = 0; i < arr.length; i=i+1){
        		var number = {
        			PMName:arr[i].PMName,
        			number: arr[i].number
                };
        		projectsUnderPM.push(number);
        	}
        	
        	var line = new Morris.Bar({
    	        element: 'PM-chart',
    	        resize: true,
    	        data: projectsUnderPM,
    	        xkey: ['PMName'],
    	        ykeys: ['number'],
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
		}	
	});
      
    //numOfStaffWithSkill
    $.ajax({
        url : '/retrieveNumberOfStaffWithSkill',
        type : 'GET',
        success : function(data){
        	var arr = JSON.parse(data);
        	var staffWithSkill =[];
        	for (var i = 0; i < arr.length; i=i+1){
        		var staff = {
        			"skill":arr[i].skillName,
    	    		"Num": arr[i].number
        		};
        		staffWithSkill.push(staff);
        	}
        	var chart = AmCharts.makeChart(
        		"bar-chart", {
	    	    	"type": "serial",
	    	    	"pathToImages": "http://cdn.amcharts.com/lib/3/images/",
	    	    	"categoryField": "skill",
	    	    	"rotate": true,
	    	    	"startDuration": 1,
	    	    	"categoryAxis": {
	    	    		"gridPosition": "start",
	    	    		"position": "left"
	    	    	},
	    	    	"trendLines": [],
	    	    	"graphs": [
	    	    		{
	    	    			"balloonText": "Number of Staff:[[value]]",
	    	    			"fillAlphas": 0.8,
	    	    			"id": "AmGraph-1",
	    	    			"lineAlpha": 0.2,
	    	    			"title": "Num",
	    	    			"type": "column",
	    	    			"valueField": "Num"
	    	    		}
	    	    		
	    	    	],
	    	    	"guides": [],
	    	    	"valueAxes": [
	    	    		{
	    	    			"id": "ValueAxis-1",
	    	    			"position": "top",
	    	    			"axisAlpha": 0
	    	    		}
	    	    	],
	    	    	"allLabels": [],
	    	    	"amExport": {
	    	    		"right": 20,
	    	    		"top": 20
	    	    	},
	    	    	"balloon": {},
	    	    	"titles": [],
	    	    	"dataProvider": staffWithSkill
        		});
        },
        error : function(){
        }
    });
    
    //scatter chart
    var line = new Morris.Line({
        element: 'scatter-chart',
        resize: true,
        data: [
            {code: "PR1405002BBB", Score: 100},
            {code: "PR1406001CCC", Score: 80},
            {code: "PR1407001DDD", Score: 90},
            {code: "PR1408001EEE", Score: 100},
            {code: "PR1409001FFF", Score: 100},
            {code: "PR1410001GGG", Score: 100},
            {code: "PR1410002NUS", Score: 110},
            {code: "PR1410003SMU", Score: 100},
            {code: "PR1410004NTU", Score: 120}
        ],
        xkey: ['code'],
        ykeys: ['Score'],
        lineWidth:0,
        hideHover: 'auto',
        gridTextColor: "A52A2A",
        gridStrokeWidth: 0.1,
        pointSize: 4,
        labels: ['Score'],
        pointStrokeColors: ["A52A2A"],
        gridTextFamily: "Open Sans",
        gridTextSize: 10,
        grid:true,
        goals:[90,110],
        goalStrokeWidth:3,
        goalLineColors:["FF0000"],
        xLabelAngle: 30,
        ymax:160,
        ymin:40,
        parseTime: false
    });
    
    $('.box ul.nav a').on('shown.bs.tab', function(e) {
        area.redraw();
        donut.redraw();
    });
});