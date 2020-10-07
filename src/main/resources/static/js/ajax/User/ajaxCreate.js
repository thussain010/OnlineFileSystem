$( document ).ready(function() {
	
    $("#createUserForm").submit(function(event) {
		event.preventDefault();
		if($('#createUserForm #password').val() == $('#createUserForm #confirmPasswordId').val())
			ajaxCreateUser();
		else alert('Password Fields Must Match');
	});
    
    
    function ajaxCreateUser(){
    	
        var createUserForm = new FormData($('#createUserForm')[0]);

        
        $.ajax({
            type: "POST",
            url: "/SuperAdmin/Secured/Create/New/User",
            data: createUserForm,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
			success : function(result) {
				if(result.status == 'Done' || result.status == 'Error' ) {
					alert(result.message);
				} else {
					alert('Internal Error User Can\'t be Created');
				}
			},
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
    	resetCUForm();
    }
    
    function resetCUForm(){
    	document.getElementById("createUserForm").reset();
    }
    
    
    
    
    $("#createRoleForm").submit(function(event) {
		event.preventDefault();
		ajaxCreateRole();
	});
    
    function ajaxCreateRole(){
    	
    	var createRoleForm = new FormData($('#createRoleForm')[0]);
        $.ajax({
            type: "POST",
            url: "/SuperAdmin/Secured/Create/New/Role",
            data: createRoleForm,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
			success : function(result) {
				if(result.status == 'Done') {
					alert(result.message);
				} else if(result.status= "Duplicate") {
					alert(result.message);
				}
			},
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
    	resetCRForm();
    }
    
    function resetCRForm(){
    	document.getElementById("createRoleForm").reset();
    }
    
})