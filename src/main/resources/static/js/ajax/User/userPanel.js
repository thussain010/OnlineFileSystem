$(document).ready(
		function() {
			
			var userType = $('#userTypeDetailInp').val();
			
			if(userType == 'User'){
				
			}else{
				ajaxGetAllLogs();
				$('#homebtn').click(function(event) {

					event.preventDefault();
					ajaxGetAllLogs();
				});

				function ajaxGetAllLogs() {
					$.ajax({
						type : "GET",
						url : "/SuperAdmin/Secured/Log/Reports",
						success : function(result) {
							showAllLogs(result.obj)
						},
						error : function(e) {
							alert("Error!")
							console.log("ERROR: ", e);
						}
					});
				}
				
				function showAllLogs(allLogs) {
					$("#allLogs").empty();
					var tabledata="<table align='center' class='table table-hover'><thead><tr><th>S. No.</th><th>User</th><th>Date/Time</th><th>Report</th></tr></thead>";
					for (var i = 0; i < allLogs.length; i++) {
						tabledata+='<tbody><tr><td>'+(i+1)+'</td>'+'<td>'+allLogs[i].username+'</td>'+'<td>'+allLogs[i].date+'</td>'+'<td>'+allLogs[i].remarks+'</td></tr></tbody>';
					}
					tabledata+="</table>";
					$('#allLogs').append(tabledata);
				}
			}
			
			
			
			

			$('#ViewUserBtn').click(function(event) {

				event.preventDefault();
				ajaxGetAllUserData();
			});

			function ajaxGetAllUserData() {
				$.ajax({
					type : "GET",
					url : "/SuperAdmin/Secured/SuperAdminPanel/AllUserDetails",
					success : function(result) {
						showAllUserDetails(result.obj)
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

			function showAllUserDetails(allUsers) {
				$("#showAllUsers").empty();
				var tabledata="<table align='center' class='table table-hover'><thead><tr><th>Name</th><th>Email</th><th>Mobile Number</th></tr></thead>";
				for (var i = 0; i < allUsers.length; i++) {
					tabledata+='<tbody><tr><td>'+allUsers[i].name+'</td>'+'<td>'+allUsers[i].email+'</td>'+'<td>'+allUsers[i].mobileNumber+'</td></tr></tbody>';
				}
				tabledata+="</table>";
				$('#showAllUsers').append(tabledata);
			}

			$('#UpdateProfileTabBtn').click(function(event) {

				event.preventDefault();
				ajaxGetUserData();
			});

			$('#ProfileTabBtn').click(function(event) {

				event.preventDefault();
				ajaxGetUserData();
			});

			function ajaxGetUserData() {

				var userType = $('#userTypeDetailInp').val();

				if (userType == 'User')
					var uri = '/User/Secured/UserPanel/Details';
				else
					var uri = '/SuperAdmin/Secured/SuperAdminPanel/Details';

				$.ajax({
					type : "GET",
					url : uri,
					success : function(result) {
						configUserDetails(result.obj)
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

			function configUserDetails(user) {

				$('.nameInp').val(user.name);
				$('.emailInp').val(user.email);
				$('.mobileNumberInp').val(user.mobileNumber);

			}

			if ((window.location.pathname)
					.includes('/Folder/Secured/Shared/Folder/')) {

				$('#home').removeClass('active');
				$('#FilesSystem').addClass('active');

				ajaxGetUserData();

				setTimeout(function() {
					ajaxGetFolders($('#hiddenFolderIdInp').val());
				}, 1);

			}

			$("#UpdateProfileForm").submit(function(event) {
				event.preventDefault();
				if (confirm('You want to Update Profile?'))
					ajaxUpdateUser();
			});

			function ajaxUpdateUser() {

				var updateUserForm = new FormData($('#UpdateProfileForm')[0]);
				
				$.ajax({
					type : "PUT",
					url : "/Update/Secured/Profile",
					data : updateUserForm,
					processData : false,
					contentType : "application/json",
					cache : false,
					timeout : 600000,
					success : function(result) {
						alert(result.message);
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

			$("#UpdatePasswordForm").submit(
					function(event) {
						event.preventDefault();
						if ($('#newPassword').val() == $('#confirmPassword')
								.val()
								&& confirm('You want to Change Password?'))
							ajaxUpdatePassword();
						else
							alert('Password Fields Must Match');
					});

			function ajaxUpdatePassword() {

				var updatePassword = new FormData($('#UpdatePasswordForm')[0]);

				$.ajax({
					type : "POST",
					url : "/Update/Secured/Password",
					data : updatePassword,
					processData : false,
					contentType : false,
					cache : false,
					timeout : 600000,
					success : function(result) {
						alert(result.message);
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

		});