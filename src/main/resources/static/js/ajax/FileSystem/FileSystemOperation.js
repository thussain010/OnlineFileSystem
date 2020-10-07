$(document).ready(
		function() {
			
			$('#FileSystemDiv').on(
					'contextmenu',
					'button.Folder-context-menu',
					function() {
						$.contextMenu({
							selector : '.Folder-context-menu',
							callback : function(key, options) {
								var fileordir = "dir";
								var folderId = this[0].value;
								if (key == 'download') {
									DownloadFolder(folderId);
								} else if (key == 'shareLink') {
									ShareFolderLink(folderId);
								} else if (key == 'downloadLink') {
									ShareDownloadFolderLink(folderId);
								} else if (key == 'delete'
										&& confirm('Move Folder To bin?')) {
									DeleteFolder(folderId);
								} else if (key == 'properties') {
									showFolderProperties(folderId);
								}else{
									$.ajax({
										type: "GET",
										url: "/Folder/Secured/Tree/Folder/Current/"+folderId,
										success: function(result){
											var lockOpt=result.obj.isLocked;
											if(key=='lock'){
												if(lockOpt=='true'){
													alert("Folder is already Locked");
												}else{
													lockItem(folderId, "folder");
												}
											}else if(key=='unlock'){
												if(lockOpt=='false' || lockOpt=="" || lockOpt == null){
													alert("Folder is already Unlocked");
												}else{
													unlockItem(folderId, "folder");
												}
											}
										},
										error: function(error){
											console.log(error);
										}
									});
									
								}
							},
							items : {
								"download" : {
									name : "Download",
									icon : "fa-cloud-download"
								},
								"share" : {
									name : "Share",
									icon : "fa-share",

									items : {
										"shareLink" : {
											"name" : "Share Link",
											icon : 'fa-link'
										},
										"downloadLink" : {
											"name" : "Share Download Link",
											icon : 'fa-download'
										},
									}
								},
								"delete" : {
									name : "Delete",
									icon : "delete"
								},
								"sep1" : "---------",
								"properties" : {
									name : "Properties",
									icon : 'fa-info-circle'
								},
								"lock" : {
									name : "Lock",
									icon : 'fa-lock'
								},
								"unlock" : {
									name : "Unlock",
									icon : 'fa-unlock-alt'
								},
							}
						});
					});

			$('#FileSystemDiv').on(
					'contextmenu',
					'button.File-context-menu',
					function() {

						$.contextMenu({
							selector : '.File-context-menu',
							callback : function(key, options) {

								var fileordir = "file";
								var fileId = this[0].value;
								if (key == 'download') {
									DownloadFile(fileId);
								} else if (key == 'shareLink') {
									ShareFileLink(fileId);
								} else if (key == 'delete'
										&& confirm('Move File To bin?')) {
									DeleteFile(fileId);
								} else if (key == 'properties') {
									showFileProperties(fileId);
								} else{
									$.ajax({
										type: "GET",
										url: "/Folder/Secured/Tree/Folder/File/Current/"+fileId,
										success: function(result){
											var lockOpt=result.obj.isLocked;
											if(key=='lock'){
												if(lockOpt=='true'){
													alert("File is already Locked");
												}else{
													lockItem(fileId, "file");
												}
											}else if(key=='unlock'){
												if(lockOpt=='false' || lockOpt=="" || lockOpt == null){
													alert("File is already Unlocked");
												}else{
													unlockItem(fileId, "file");
												}
											}
										},
										error: function(error){
											console.log(error);
										}
									});
									
								}
							},
							items : {
								"download" : {
									name : "Download",
									icon : "fa-cloud-download"
								},
								"shareLink" : {
									name : "Share",
									icon : 'fa-link'
								},
								"delete" : {
									name : "Delete",
									icon : "delete"
								},
								"sep1" : "---------",
								"properties" : {
									name : "Properties",
									icon : 'fa-info-circle'
								},
								"lock" : {
									name : "Lock",
									icon : 'fa-lock'
								},
								"unlock" : {
									name : "Unlock",
									icon : 'fa-unlock-alt'
								},
							}
						});
					});

			window.lockItem = function(itemId, fileordir) {
				$('#folderlock').html(
						"<table>" +
								"<tr><td><input type='hidden' id='itemId' value='" + itemId+"'/></td></tr>" +
										"<tr><td><input type='hidden' id='fileordir' value='" + fileordir+"'/></td></tr>"
									+"<tr>"
									+"	<td><input type='password' class='form-control' name='key1' placeholder='********' id='key1' /></td>"
									+"</tr>"
									+"<tr>"
									+"	<td><input type='password' class='form-control' name='key2' placeholder='********' id='key2'></td>"
									+"</tr>"
									+"<tr>"
									+"	<td><input type='submit' class='btn btn-info' value='Lock' id='lockbtn'></td>"
									+"</tr>"
									+"</table>");
				$('#LockModal').modal('show');
			}

			$('#folderlock').submit(function(e) {
				e.preventDefault();
				var fileordir=$('#fileordir').val();
				var formData = {
					"itemId" : $('#itemId').val(),
					"key1" : $('#key1').val(),
					"key2" : $('#key2').val()
				};
				if(fileordir=="folder"){
					$.ajax({
						type : "PUT",
						processData : false,
						cache : false,
						contentType : "application/json",
						url : "/Folder/Secured/Lock/Folder",
						data : JSON.stringify(formData),
						success : function(result) {
							alert('Folder Locked successfully');
						},
						error : function(error) {
							alert("An ERROR occured");
							console.log(error);
						}
					});
				}else if(fileordir=="file"){
					$.ajax({
						type : "PUT",
						processData : false,
						cache : false,
						contentType : "application/json",
						url : "/Folder/Secured/Lock/File",
						data : JSON.stringify(formData),
						success : function(result) {
							alert('File Locked successfully');
						},
						error : function(error) {
							alert("An ERROR occured");
							console.log(error);
						}
					});
				}
				
			});

			window.unlockItem = function(itemId, fileordir) {
				$('#folderunlock').html(
						"<table>" +
						"<tr><td><input type='hidden' id='itemIdUnlock' value='" + itemId+"'/></td></tr>" +
						"<tr><td><input type='hidden' id='fileordir' value='" + fileordir+"'/></td></tr>"
						+"<tr>"
						+"	<td><input type='password' class='form-control' name='key' placeholder='********' id='key' /></td>"
						+"</tr>"
						+"<tr>"
						+"	<td><input type='submit' class='btn btn-info' value='Unlock' id='unlockbtn'></td>"
						+"</tr>"
						+"</table>");
				$('#UnlockModal').modal('show');
			}
			
			$('#folderunlock').submit(function(e) {
				e.preventDefault();
				var key=$('#key').val();
				var itemIdUnlock=$('#itemIdUnlock').val();
				var fileordir=$('#fileordir').val();
				ajaxUnlockSubmit(itemIdUnlock, key, fileordir);
			});
			
			function ajaxUnlockSubmit(itemIdUnlock, key, fileordir){
				
				if(fileordir=="file"){
					$.ajax({
						type: "GET",
						url: "/Folder/Secured/Tree/Folder/File/Current/"+itemIdUnlock,
						success: function(result){
							var folder=result.obj;
							if(folder.key==key){
							$.ajax({
								type : "PUT",
								processData : false,
								cache : false,
								contentType : "application/json",
								url : "/Folder/Secured/Unlock/Folder/File/"+itemIdUnlock,
								data : JSON.stringify(folder),
								success : function(result) {
									alert('Item Unlocked successfully');
								},
								error : function(error) {
									alert("An ERROR occured");
									console.log(error);
								}
							});
							}else{
								alert("Key is Incorrect");
							}
							
						},
						error: function(error){
							alert("An ERROR occured");
						}
					});
				}else if(fileordir=="folder"){
					$.ajax({
						type: "GET",
						url: "/Folder/Secured/Tree/Folder/Current/"+itemIdUnlock,
						success: function(result){
							var folder=result.obj;
							if(folder.key==key){
							$.ajax({
								type : "PUT",
								processData : false,
								cache : false,
								contentType : "application/json",
								url : "/Folder/Secured/Unlock/Folder/"+itemIdUnlock,
								data : JSON.stringify(folder),
								success : function(result) {
									alert('Item Unlocked successfully');
								},
								error : function(error) {
									alert("An ERROR occured");
									console.log(error);
								}
							});
							}else{
								alert("Key is Incorrect");
							}
							
						},
						error: function(error){
							alert("An ERROR occured");
						}
					});
				}
				
			}
			
			window.DownloadFile = function(fileId) {

				window.open("/Folder/Secured/Download/File/" + fileId,
								'_blank');
			}

			window.ShareFileLink = function(fileId) {

				$('#LinkPopUpHeader').html('Share File Link');

				$.ajax({
					type : "GET",
					url : "/Folder/Secured/Sharable/File/" + fileId,
					success : function(result) {

						if (result.status == 'Invalid') {
							alert(result.message);
							return;
						}
						$('#LinkPopupAnchor').html(
								'localhost:8080/Folder/Secured/Shared/File/'
										+ result.obj.link);
						$('#LinkPopupDiv').dialog({
							height : 'auto',
							width : 500,
							modal : true,
							resizable : true,
							dialogClass : 'no-close success-dialog'
						});

					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

			DeleteFile = function(fileId) {

				MoveFileToTrash(fileId);

			}

			window.showFileProperties = function(fileId) {

				$.ajax({
					type : "GET",
					url : "/Folder/Secured/get/Properties/File/" + fileId,
					success : function(result) {

						var fileProps = '';
						fileProps += 'Type: File </br>';
						fileProps += 'File Name: ' + result.obj.fileName
								+ ' </br>';
						fileProps += 'File Path: ' + result.obj.filePath
								+ ' </br>';
						fileProps += 'File Size: ' + result.obj.fileSize
								+ ' </br>';
						fileProps += 'Created Date: ' + result.obj.createdDate
								+ ' </br>';

						$('#PropertiesPara').html(fileProps);
						$('#PropertiesDiv').dialog({
							height : 'auto',
							width : 500,
							modal : true,
							resizable : true,
							dialogClass : 'no-close success-dialog'
						});

					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});

			}

			/* Folder */

			window.DownloadFolder = function(folderId) {

				window.open("/Folder/Secured/Download/Folder/" + folderId,
						'_blank');

			}

			window.ShareDownloadFolderLink = function(folderId) {

				$('#LinkPopUpHeader').html('Share Folder Link');

				$.ajax({
					type : "GET",
					url : "/Folder/Secured/Sharable/Folder/Download/Link/"
							+ folderId,
					success : function(result) {

						if (result.status == 'Invalid') {
							alert(result.message);
							return;
						}
						$('#LinkPopupAnchor').html(
								'localhost:8080/Folder/Secured/Download/Shared/Folder/'
										+ result.obj.link);
						$('#LinkPopupDiv').dialog({
							height : 'auto',
							width : 500,
							modal : true,
							resizable : true,
							dialogClass : 'no-close success-dialog'
						});

					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

			window.ShareFolderLink = function(folderId) {

				$('#LinkPopUpHeader').html('Share Download Folder Link');

				$.ajax({
					type : "GET",
					url : "/Folder/Secured/Sharable/Folder/" + folderId
							+ "/Access/Link",
					success : function(result) {

						if (result.status == 'Invalid') {
							alert(result.message);
							return;
						}
						$('#LinkPopupAnchor').html(
								'localhost:8080/Folder/Secured/Shared/Folder/'
										+ result.obj.link);
						$('#LinkPopupDiv').dialog({
							height : 'auto',
							width : 500,
							modal : true,
							resizable : true,
							dialogClass : 'no-close success-dialog'
						});

					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

			DeleteFolder = function(folderId) {
				MoveFolderToTrash(folderId);
			}

			window.showFolderProperties = function(folderId) {

				$.ajax({
					type : "GET",
					url : "/Folder/Secured/get/Properties/Folder/" + folderId,
					success : function(result) {

						var folderProps = '';
						folderProps += 'Type: Folder </br>';
						folderProps += 'Folder Name: ' + result.obj.folderName
								+ ' </br>';
						folderProps += 'Folder Path: ' + result.obj.folderPath
								+ ' </br>';
						folderProps += 'Folder Size: ' + result.obj.folderSize
								+ ' </br>';
						folderProps += 'Total Items: ' + result.obj.subFileNum
								+ ' </br>';
						folderProps += 'Created Date: '
								+ result.obj.createdDate + ' </br>';

						$('#PropertiesPara').html(folderProps);
						$('#PropertiesDiv').dialog({
							height : 'auto',
							width : 500,
							modal : true,
							resizable : true,
							dialogClass : 'no-close success-dialog'
						});

					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});

			}

		});