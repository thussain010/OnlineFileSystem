$( document ).ready(function() {
	
	$("#FileSystemBtn").click(function(event) {
		event.preventDefault();
		ajaxGetBaseFileSystem('#FileSystemDiv');
	});
	
	window.ajaxGetBaseFileSystem = function(id) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/get/Base/FileSystem",
            success : function(result) {
            	
            	$('#FileSystemAddressBar').html('> <button class="AddressBarAccessBtn" value="'+ result.obj.folderId +'">root</button>');
            	
            	$('#FileSystemBackButton').val(result.obj.parentFolderId);
            	$('#FileSystemDirectoryAddress').val('/');
            	$('#FileSysParentFolderId').val(result.obj.folderId);
            	CreateDivFileSystem(id, result.obj);
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	$('#FileSystemBackButton').click(function() {
		
		if(!this.value.includes('ROOT'))
			ajaxGetFolders(this.value);
		else {
			alert('You Are at root');
			ajaxGetBaseFileSystem('#FileSystemDiv');
		}
	});
	
	function CreateDivFileSystem(id, parentFolder) {
		
		var subFolders = '';
		var subFiles = '';
    	$.each(parentFolder.subFolder, function(index, folder) {
    		if(folder.isTrashed == 'true')
    			return;
    		subFolders += '<button id="Folder_'+ folder.folderId +'" class="TreeFolder FolderIcon col-sm-2 Folder-context-menu" value="'+ folder.folderId +'"><i class="fa fa-folder faIconsStyle"></i> '+ folder.folderName +'</button>';
    	});

    	$.each(parentFolder.subDocumentFile, function(index, file) {
    		
    		if(file.isTrashed == 'true')
    			return;
    		subFiles += '<button id="File_'+file.documentId +'" class="TreeFile FolderIcon col-sm-2 File-context-menu" value="'+ file.documentId +'"><i class="fa fa-file faIconsStyle"></i> '+ file.documentName +'</button>';
    	});
    	$(id).html(subFolders + '\n' + subFiles);
    	
	}
	
	
	
	$("#FileSystemDiv").on('dblclick', 'button' , function(event){
		var id = this.value;
		var btnClass = $(this).attr('class');
		if(btnClass.includes("TreeFile")) {
			$.ajax({
				type: "GET",
				url: "/Folder/Secured/Tree/Folder/File/Current/"+id,
				success: function(result){
					var isLocked=result.obj.isLocked;
					if(isLocked=="true"){
						alert("Unlock this File to open or download!");
					}else{
						ajaxGetFile(id);
					}
				},
				error: function(error){
					console.log(error);
				}
			});
	    }else if(btnClass.includes("TreeFolder")){
	    	$.ajax({
				type: "GET",
				url: "/Folder/Secured/Tree/Folder/Current/"+id,
				success: function(result){
					var isLocked=result.obj.isLocked;
					if(isLocked=="true"){
						alert("Unlock this folder to open!");
					}else{
					    	ajaxGetFolders(id);
					}
				},
				error: function(error){
					console.log(error);
				}
			});
	    }
		
	    
	});
	
	window.ajaxGetFolders = function(id) {
		
		$.ajax({
            type: "GET",
            url: "/Folder/Secured/Tree/Folder/" + id,
            success : function(result) {
            	
            	$('#FileSystemBackButton').val(result.obj.folder.parentFolderId);
            	attachFileSystemAddressBar(result.obj.folder, result.obj.parentMap);
            	$('#FileSystemDirectoryAddress').val(result.obj.folder.folderPath);
            	$('#FileSysParentFolderId').val(result.obj.folder.folderId);
            	CreateDivFileSystem('#FileSystemDiv', result.obj.folder);
            
            },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
	}
	
	function attachFileSystemAddressBar(folder, parentMap) {
		
		var val = '';
		
		$.each(parentMap, function(key, value) {
			val += ' > ' + '<button class="AddressBarAccessBtn" value="'+ value +'">'+key+'</button>';
		});
		
    	$('#FileSystemAddressBar').html(val);

	}

	$("#FileSystemAddressBar").on('click', 'button.AddressBarAccessBtn' , function(event){
		event.preventDefault();
		ajaxGetFolders(this.value);
	});
	
	
	
	function ajaxGetFile(fileId) {
		window.open("/Folder/Secured/Tree/File/" + fileId, '_blank');
	}
	
})