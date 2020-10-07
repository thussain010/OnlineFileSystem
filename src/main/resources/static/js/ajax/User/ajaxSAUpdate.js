$( document ).ready(function() {
	
    $("#updateRoleForm").submit(function(event) {
		event.preventDefault();
		ajaxUpdateRoles();
	});
    
    

    function ajaxUpdateRoles(){
    	
        var updateRoleDto = {};
        
        var updateRoleForm = document.getElementById('updateRoleForm');
        var length = document.getElementById('RoleSetLength');
        
        
        
        var roleDtoArray = [];
        
        for (var i = 0; i < length.value; i++) {
        	
        	var privilegesList = [];
        	var roleDto = {};
        	
            roleDto['roleName'] = updateRoleForm.elements['roleDto['+i+'].roleName'].value;
        	var TempPriv = updateRoleForm.elements['roleDto['+i+'].privilegesList'];
        	
        	for(j=0;j<TempPriv.length;j++) {
        		if(TempPriv[j].checked) {
        			privilegesList.push(TempPriv[j].value);
        		}
        	}
        	roleDto['privilegesList'] = privilegesList;
        	roleDtoArray.push(roleDto);
        	
        }
        
        
        updateRoleDto['roleDto'] = roleDtoArray;
        
        console.log(updateRoleDto);
        
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/SuperAdmin/Secured/Update/Role",
            data: JSON.stringify(updateRoleDto),
            dataType: 'json',
            cache: false,
            timeout: 600000,
			success : function(result) {
				alert('Roles Updated');
			},
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
    	
    }
    
	
})