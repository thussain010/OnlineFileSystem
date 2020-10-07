$( document ).ready(function() {

	
	function configureTrash(object) {
		
		if(object.folders.length == 0 && object.files.length == 0) {
			
			$('#TrashSystemDiv').html('<h2 style="text-align: center;">&#x1F601; Trash is Empty</h2>');
	    	return;
		}
		
		var foldershtml = '';
		var filehtml = '';
    	$.each(object.folders, function(index, folder) {
    		foldershtml += '<button id="Folder_'+ folder.folderId +'" class="TreeFolder FolderIcon col-sm-2 Trash-Folder-context-menu" value="'+ folder.folderId +'"><i class="fa fa-folder faIconsStyle"></i> '+ folder.folderName +'</button>';
    	});

    	$.each(object.files, function(index, file) {
    		filehtml += '<button  id="File_'+file.documentId +'" class="TreeFile FolderIcon col-sm-2 Trash-File-context-menu" value="'+ file.documentId +'"><i class="fa fa-file faIconsStyle"></i> '+ file.fileName +'</button>';
    	});
    	$('#TrashSystemDiv').html(foldershtml + filehtml);
	}
	
	
	$('#TrashBtn').click(function (event) {
		
		event.preventDefault();
		ajaxTrashSystem();
	});
	
	function ajaxTrashSystem() {
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/get/Trash",
            success : function(result) {
            	configureTrash(result.obj);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
		
	}
	
	
	$('#DeleteAllPermanantBtn').click(function (event) {
		
		event.preventDefault();
		if(confirm('Want to Delete All Files & Folders Permanent?'))
			DeleteAllPermanant();
		
		
	});
	
	function DeleteAllPermanant() {
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Delete/Permanent/All",
            success : function(result) {
            	alert(result.message);
            	ajaxTrashSystem();
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	$('#RestoreAllBtn').click(function (event) {
		
		event.preventDefault();
		if(confirm('Want to Restore All Files & Folders?'))
			RestoreAll();
	});
	
	function RestoreAll() {

		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Restore/All",
            success : function(result) {
            	alert(result.message);
            	ajaxTrashSystem();
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}

	$('#TrashSystemDiv').on('contextmenu','button.Trash-Folder-context-menu',  function() {
	    
		
    	$.contextMenu({
	    	selector: '.Trash-Folder-context-menu',
	        callback: function(key, options) {
	        	
	        	var folderId = this[0].value;
	           	if(key == 'restore' && confirm('Want To Restore Folder?')) {
	           		RestoreFolder(folderId);
	           	}
	           	else if(key == 'permanantDelete' && confirm('Want To Permanently Delete Folder?')) {
            		PermanantDeleteFolder(folderId);
            	}
	           	else if(key == 'properties') {
	           		showFolderProperties(folderId);
	           	}
	        },
	        items: {
	        	"restore": {name: "Restore", icon: "fa-undo"},
	            "permanantDelete": {name: "Permanant Delete", icon: "fa-times"},
	            "sep1": "---------",
	            "properties": {name: "Properties", icon: 'fa-info-circle'},
	        }
	    });
	});
	
	
	RestoreFolder = function (folderId) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Restore/Folder/" + folderId,
            success : function(result) {
            	$('#TrashSystemDiv #Folder_' + folderId).remove();
            	alert(result.message);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	
	PermanantDeleteFolder = function (folderId) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Delete/Permanent/Folder/" + folderId,
            success : function(result) {
            	$('#TrashSystemDiv #Folder_' + folderId).remove();
            	alert(result.message);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	
	$('#TrashSystemDiv').on('contextmenu','button.Trash-File-context-menu',  function() {
	    
		
    	$.contextMenu({
	    	selector: '.Trash-File-context-menu',
	        callback: function(key, options) {
	        	
	        	var fileId = this[0].value;
	           	if(key == 'restore' && confirm('Want To Restore File?')) {
	           		RestoreFile(fileId);
	           	}
	           	else if(key == 'permanantDelete' && confirm('Want To Permanently Delete File?')) {
            		PermanantDeleteFile(fileId);
            	}
	           	else if(key == 'properties') {
            		showFileProperties(fileId);
            	}
	        },
	        items: {
	        	"restore": {name: "Restore", icon: "fa-undo"},
	            "permanantDelete": {name: "Permanant Delete", icon: "fa-times"},
	            "sep1": "---------",
	            "properties": {name: "Properties", icon: 'fa-info-circle'},
	        }
	    });
	});
	
	
	
	RestoreFile = function (fileId) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Restore/File/"+ fileId,
            success : function(result) {
            	$('#TrashSystemDiv #File_' + fileId).remove();
            	alert(result.message);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	
	PermanantDeleteFile = function (fileId) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Delete/Permanent/File/"+ fileId,
            success : function(result) {
            	$('#TrashSystemDiv #File_' + fileId).remove();
            	alert(result.message);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	
	window.MoveFileToTrash = function (fileId) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Move/Trash/File/"+ fileId,
            success : function(result) {
            	if(result.status == 'Invalid') {
            		alert(result.message);
            		return;
            	}
            	$('#FileSystemDiv #File_' + fileId).remove();
            	alert(result.message);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	window.MoveFolderToTrash = function (folderId) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Move/Trash/Folder/" + folderId,
            success : function(result) {
            	if(result.status == 'Invalid') {
            		alert(result.message);
            		return;
            	}
            	$('#FileSystemDiv #Folder_' + folderId).remove();
            	alert(result.message);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	
});