$( document ).ready(function() {
	
	var validatePassword = false;
	var validateEmail = false;
	
	$("#createUserForm #confirmPassword").change(function() {
		
		
		var confirmPassword = $("#createUserForm #confirmPassword").val();
		var password = $("#createUserForm #password").val();
		
		if(!(password == confirmPassword)) {
			alert('Password Mismatch');
			validatePassword = false;
		}else {
			validatePassword = true;
		}
		checkFlags();
	});
	
	function checkFlags() {
		
		if(validatePassword && validateEmail) {
			$("#createUserForm #CreateUserSubmitBtn").removeAttr('disabled');
		}else {
			$("#createUserForm #CreateUserSubmitBtn").attr("disabled", "disabled");
		}
		
	}
	
	
	$("#createUserForm #email").change(function() {
		
		
		var mailId = $("#createUserForm #email").val();
		var emailRegEx = /^[A-Z0-9._%+-]+@([A-Z0-9-]+\.)+[A-Z]{2,4}$/i;
		if (emailRegEx.test(mailId)) {
			ajaxValidateEmail(mailId);
		}
	});
	
	function ajaxValidateEmail(mailId){
		
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/SuperAdmin/Secured/Validate/Email",
            data: JSON.stringify(mailId),
            dataType: 'json',
            cache: false,
            timeout: 600000,
			success : function(result) {
				if(result.status == "Done") {
					alert("Entered Email Already Exists");
					validateEmail = false;
				}
				else if(result.status == "OK") {
					validateEmail = true;
				}
				checkFlags();
			},
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
        });
	}
	
	
})