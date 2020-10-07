$( document ).ready(function() {

	$("#CreateUserBtn").click(function(event) {
		event.preventDefault();
		ajaxGetRoles('#createUserRoleSelectDiv');
	});
	
	
	$("#CreateNewPriv").click(function(event) {
		event.preventDefault();
		ajaxGetRoles('#roleSelectDiv');
	});
	
	
	function ajaxGetRoles(id){
    	
        $.ajax({
            type: "GET",
            url: "/Repo/Secured/get/Roles",
            success : function(result) {
            	var role;
            	var options='<br/>';
            	$.each(result.obj, function(index, role) {
            		options += '<option value="' + role.role +'">'+ role.role +'</option><br/>';
            	});
            	
            	
            	$(id).html('<select class="selectpicker" data-width="100%" name="roleName" id="roleName" multiple required>' 
                                        		+ options
                                        		+'</select>');
            	
            	if(id == '#createUserRoleSelectDiv') {
            		$("#createUserRoleSelectDiv #roleName").prop("multiple", "");
            	}
            	
                $('.selectpicker').selectpicker('render');
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
    }

	
	

	

	$("#updateRoleBtn").click(function(event) {
		event.preventDefault();
		ajaxUpdateRole();
	});
	
	function ajaxUpdateRole(){
    	
        $.ajax({
            type: "GET",
            url: "/Repo/Secured/get/Update/Roles",
            success : function(result) {
            	
            	var tableHeader = '\n<th>Role</th>\n';
            	var tableBody = '\n';
            	$('#RoleSetLengthDiv').html('<input type="hidden" value="'+ result.obj.roles.length +'" id="RoleSetLength">');
            	$.each(result.obj.privileges , function (index, priv) {
            		
            		tableHeader += '\n<th>'+ priv.privilegeName +'</th>\n';
            	});
            	
            	
            	$('#updateRoleHeader').html(	'<tr>\n'
            									+ tableHeader
            									+'\n</tr>');
            	
            	tableBody = '';
            	
            	$.each(result.obj.roles , function (roleIndex, role) {
            		var flag = false;
            		tableBody += '\n<tr>';
            		tableBody += '\n<td>'+ role.role + '<input type="hidden" name="roleDto['+ roleIndex +'].roleName" value="'+ role.role +'">' +'</td>';
            		
            		$.each(result.obj.privileges , function (index, priv) {
    	            	
            			$.each(role.privilegesId , function (index, rolePrivId) {
	            		
	            			if(rolePrivId == priv.id) {
	            				flag = true;
	            				return false;
	            			}
	            			
	            		});
            			if(flag == true) {
            				tableBody += '\n<td><input type="checkbox" name="roleDto['+ roleIndex +'].privilegesList" value="'+ priv.privilegeName +'" checked> </td>';
            			}
            			else {
            				tableBody += '\n<td><input type="checkbox" name="roleDto['+ roleIndex +'].privilegesList" value="'+ priv.privilegeName +'"> </td>';
            			}
            			flag=false;
            		});	
            		
            		tableBody += '</tr>';
            	
            	});
            	$('#updateRoleBody').html(tableBody);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
    }

	
	
	
	$("#createRoleBtn").click(function(event) {
		event.preventDefault();
		ajaxGetPrivileges('#selectPrivilegesDiv');
	});
	
	
	function ajaxGetPrivileges(id) {
    	
        $.ajax({
            type: "GET",
            url: "/Repo/Secured/get/Privileges",
            success : function(result) {
            	var options='<br/>';
            	
            	$.each(result.obj, function(index, priv) {
            		options += '<option value="' + priv.privilegeName +'">'+ priv.privilegeName +'</option><br/>';
            	});
            	
            	$(id).html('<select class="selectpicker" data-width="100%" name="privilegesList" id="privilegesList" multiple required>' 
                                        		+ options
                                        		+'</select>');

                $('.selectpicker').selectpicker('render');
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
    }
})
