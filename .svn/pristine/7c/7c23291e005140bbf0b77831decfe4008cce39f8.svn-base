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
	
	//get name from session
	$('#user').html(name);
	
	$("#searchMenu").on('click', 'li a', function(){
		var selText = $(this).text();
		$("#searchStaff").html(selText+' <span class="caret"></span>');
    });
	
	//retrieve staff details
	$.ajax({
        url : '/retrieveByID/' + staffID,
        type : 'GET',
        success : function(data){
        	var jsonData = JSON.parse(data);
        	$("#staffName").html("&nbsp;"+jsonData.name);
        	$("#staffRole").html("&nbsp;"+jsonData.role);
        	$("#staffEmail").html("&nbsp;"+jsonData.email);
        	$("#maxProject").html("&nbsp;"+jsonData.maxProjectNum);
        },
        error : function(){
        }
    });
	
	//retrieve all staff
	$.ajax({
        url : '/retrieveAll',
        type : 'GET',
        async: false,
        success : function(data){
        	var arr = JSON.parse(data);
        	for (var i = 0; i < arr.length; i=i+1){
        		$("#table_staff").append('<tr><td><h4 align="center"><strong><a href=/staff?staffID{"staffID":"'+ arr[i].staffID +'"} style="color: #2c3e50; text-decoration: none">'+arr[i].name+'</a></strong></h4></td></tr>');
            }
        },
        error : function(){
        }
    });
	
	//logout
    $('#logout').click(function(e){
		e.preventDefault();
		sessionStorage.removeItem("name");
		window.location.href="/";
	});
    
    //ongoingTasks
    retrieveOngoings();
})(jQuery);

function retrieveOngoings(){
	var date = Date.today().toString('yyyy-MM-dd');
    var dates = '{"startDate":"'+date+'","endDate":"'+date+'"}';
	$.ajax({
        url : '/retrieveAllOngoingTasksByStaff/'+dates,
        type : 'GET',
        async: false,
        success : function(data){
        	var jsonData = '';
        	jsonData = JSON.parse(data);
        	var ongoingTasks = '';
        	for (var i=0; i<jsonData.length;i++){
        		ongoingTasks += '<tr><td>'+jsonData[i].staffName+'</td><td>'+jsonData[i].role+'</td>';
        		if(jsonData[i].ongoingTasks != []){
        			ongoingTasks += '<td>';
	        		for (var j=0; j<(jsonData[i].ongoingTasks).length;j++){
	        			ongoingTasks += ((jsonData[i].ongoingTasks)[j]).subprojectName + ",";
	        		}
	        		ongoingTasks += '</td>';
        		}
        		ongoingTasks += '<tr>';
        	}
        	$('#ongoing_tasks').html(ongoingTasks);
        },
        error : function(){
        }
    });
}

function searchStaff(){
	if($("#searchStaff").text() === "Staff"){
		var searchCriteria = '{"searchCriteria":"'+$("#searchInput").val()+'"}';
		$.ajax({
	        url: '/filterStaffListByStaffInfo/' + searchCriteria,
	        type: 'GET',
	        success: function(data) {
	            var arr = JSON.parse(data);
	            for (var i = 0; i < arr.length; i = i + 1) {
	                $("#table_staff").html('<tr><td><h4 align="center"><strong><a href=/staff?staffID{"staffID":"' + arr[i].staffID + '"} style="color: #2c3e50; text-decoration: none">' + arr[i].name + '</a></strong></h4></td></tr><hr>');
	            }
	        },
	        error: function() {}
	    });
	}else{
		var searchCriteria = '{"skill":"'+$("#searchInput").val()+'"}';
		$.ajax({
	        url: '/filterStaffListBySkill/' + searchCriteria,
	        type: 'GET',
	        success: function(data) {
	            var arr = JSON.parse(data);
	            for (var i = 0; i < arr.length; i = i + 1) {
	                $("#table_staff").html('<tr><td><h4 align="center"><strong><a href=/staff?staffID{"staffID":"' + arr[i].staffID + '"} style="color: #2c3e50; text-decoration: none">' + arr[i].name + '</a></strong></h4></td></tr><hr>');
	            }
	        },
	        error: function() {}
	    });
	}
}