(function($) {

    "use strict";
    var options = {
        events_source: [],
        view: 'week',
        tmpl_path: '/assets/tmpls/',
        tmpl_cache: false,
        day: Date.today().toString('yyyy-MM-dd'),
        onAfterEventsLoad: function(events) {
            if (!events) {
                return;
            }
            var list = $('#eventlist');
            list.html('');

            $.each(events, function(key, val) {
                $(document.createElement('li'))
                    .html('<a href="' + val.url + '">' + val.title + '</a>')
                    .appendTo(list);
            });
        },
        onAfterViewLoad: function(view) {
            $('#calendarDate').text(this.getTitle());
            $('.btn-group button').removeClass('active');
            $('button[data-calendar-view="' + view + '"]').addClass('active');
        },
        classes: {
            months: {
                general: 'label'
            }
        }
    };

    //retrieve staff task
    $.ajax({
        url: '/retrieveAllSubproject/' + staffID,
        type: 'GET',
        success: function(data) {
            var jsonData = JSON.parse(data);
            for (var i = 0; i < jsonData.length; i = i + 1) {
                var name = jsonData[i].subprojectName;

                var plannedStart = jsonData[i].subprojectStartDate;
                var plannedEnd = jsonData[i].subprojectStartDate;
                var plannedStartMilliseconds = new Date(plannedStart).getTime();
                var plannedEndMilliseconds = new Date(plannedEnd).getTime();

                var actualStart = jsonData[i].actualStartDate;
                var actualEnd = jsonData[i].actualEndDate;

                var subproject;

                if (actualStart == "this subproject has not started") {
                	var random = Math.random();
                    if(random < 0.2){
                    	subproject = {
                    			"title": name,
                                "class": "event-warning",
                                "start": plannedStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.4){
                    	subproject = {
                    			"title": name,
                                "class": "event-special",
                                "start": plannedStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.6){
                    	subproject = {
                    			"title": name,
                                "class": "event-info",
                                "start": plannedStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.8){
                    	subproject = {
                    			"title": name,
                                "class": "event-important",
                                "start": plannedStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.8){
                    	subproject = {
                    			"title": name,
                                "class": "event-inverse",
                                "start": plannedStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    } 
                } else if (actualEnd == "this subproject has not ended") {
                	var actualStartMilliseconds = actualStart.getTime();
                	var random = Math.random();
                    if(random < 0.2){
                    	subproject = {
                    			"title": name,
                                "class": "event-warning",
                                "start": actualStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.4){
                    	subproject = {
                    			"title": name,
                                "class": "event-special",
                                "start": actualStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.6){
                    	subproject = {
                    			"title": name,
                                "class": "event-info",
                                "start": actualStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.8){
                    	subproject = {
                    			"title": name,
                                "class": "event-important",
                                "start": actualStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.8){
                    	subproject = {
                    			"title": name,
                                "class": "event-inverse",
                                "start": actualStartMilliseconds,
                                "end": plannedEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    } 
                } else {
                	var actualStartMilliseconds = actualStart.getTime();
                    var actualEndMilliseconds = actualEnd.getTime();
                    var random = Math.random();
                    if(random < 0.2){
                    	subproject = {
                    			"title": name,
                                "class": "event-warning",
                                "start": actualStartMilliseconds,
                                "end": actualEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.4){
                    	subproject = {
                    			"title": name,
                                "class": "event-special",
                                "start": actualStartMilliseconds,
                                "end": actualEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.6){
                    	subproject = {
                    			"title": name,
                                "class": "event-info",
                                "start": actualStartMilliseconds,
                                "end": actualEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.8){
                    	subproject = {
                    			"title": name,
                                "class": "event-important",
                                "start": actualStartMilliseconds,
                                "end": actualEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }else if(random < 0.8){
                    	subproject = {
                    			"title": name,
                                "class": "event-inverse",
                                "start": actualStartMilliseconds,
                                "end": actualEndMilliseconds
                            };
                            options.events_source.push(subproject);
                    }
                }
            }

            var calendar = $('#calendar').calendar(options);

            $('.btn-group button[data-calendar-nav]').each(function() {
                var $this = $(this);
                $this.click(function() {
                    calendar.navigate($this.data('calendar-nav'));
                });
            });

            $('.btn-group button[data-calendar-view]').each(function() {
                var $this = $(this);
                $this.click(function() {
                    calendar.view($this.data('calendar-view'));
                });
            });

            $('#first_day').change(function() {
                var value = $(this).val();
                value = value.length ? parseInt(value) : null;
                calendar.setOptions({
                    first_day: value
                });
                calendar.view();
            });
        },
        error: function() {}
    });

    //filterProjects
    $.fn.extend({
        filterTable: function() {
            return this.each(function() {
                $(this).on('keyup', function(e) {
                    $('.filterTable_no_results').remove();
                    var $this = $(this),
                        search = $this.val().toLowerCase(),
                        target = $this.attr('data-filters'),
                        $target = $(target),
                        $rows = $target.find('tbody tr');
                    if (search == '') {
                        $rows.show();
                    } else {
                        $rows.each(function() {
                            var $this = $(this);
                            $this.text().toLowerCase().indexOf(search) === -1 ? $this.hide() : $this.show();
                        })
                        if ($target.find('tbody tr:visible').size() === 0) {
                            var col_count = $target.find('tr').first().find('td').size();
                            var no_results = $('<tr class="filterTable_no_results"><td colspan="' + col_count + '">No results found</td></tr>')
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
        url: '/retrieveByID/' + staffID,
        type: 'GET',
        success: function(data) {
            var jsonData = JSON.parse(data);
            $("#staffName").html("&nbsp;" + jsonData.name);
            $("#staffRole").html("&nbsp;" + jsonData.role);
            $("#staffEmail").html("&nbsp;" + jsonData.email);
            $("#maxProject").html("&nbsp;" + jsonData.maxProjectNum);
        },
        error: function() {}
    });

    //retrieve all staff
    $.ajax({
        url: '/retrieveAll',
        type: 'GET',
        success: function(data) {
            var arr = JSON.parse(data);
            for (var i = 0; i < arr.length; i = i + 1) {
                $("#table_staff").append('<tr><td><h4 align="center"><strong><a href=/staff?staffID{"staffID":"' + arr[i].staffID + '"} style="color: #2c3e50; text-decoration: none">' + arr[i].name + '</a></strong></h4></td></tr><hr>');
            }
        },
        error: function() {}
    });


    //retrieve all relate projects list detail
    var count = 0;
    $.ajax({
        url: '/retrieveProjectByStaff/' + staffID,
        type: 'GET',
        success: function(data) {
            var arr = JSON.parse(data);
            count = arr.length;
            for (var j = 0; j < arr.length; j = j + 1) {
                $('#table_item').append('<tr id="' + arr[j].projectID + '"></tr>');
                $('#' + arr[j].projectID).html('<td><a href=/project?projectID{"projectID":"' + arr[j].projectID + '"}>' + arr[j].projectID + "</a></td><td>" + arr[j].startDate + "</td><td>" + arr[j].endDate + "</td><td>" + arr[j].projectManagerName + "</td>");
            }
        },
        error: function() {}
    });
    
    //logout
    $('#logout').click(function(e) {
        e.preventDefault();
        sessionStorage.removeItem("name");
        localStorage.clear();
        window.location.href = "/";
    });
})(jQuery);

function searchStaff(){
	$("#table_staff").html("");
		if($("#searchStaff").text() === "Staff"){
			var searchCriteria = '{"searchCriteria":"'+$("#searchInput").val()+'"}';
			$.ajax({
		        url: '/filterStaffListByStaffInfo/' + searchCriteria,
		        type: 'GET',
		        success: function(data) {
		            var arr = JSON.parse(data);
		            for (var i = 0; i < arr.length; i = i + 1) {
		                $("#table_staff").append('<tr><td><h4 align="center"><strong><a href=/staff?staffID{"staffID":"' + arr[i].staffID + '"} style="color: #2c3e50; text-decoration: none">' + arr[i].name + '</a></strong></h4></td></tr><hr>');
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