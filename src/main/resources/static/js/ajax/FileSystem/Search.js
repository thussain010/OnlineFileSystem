$( document ).ready(function() {

	$('#SearchInFileSystemInp').on('keypress click', function(e) {
		if (e.which === 13) {
			
			if(this.value == '')
				setTimeout(function() {
					ajaxGetBaseFileSystem('#FileSystemDiv');
				}, 1);
			
			else {
				//$('#FileSysParentFolderId').val();
				$.LoadingOverlay("show");
				ajaxSearchFileOrFolder(this.value, $('#FileSysParentFolderId').val());
			}
			
		}
	});
	
	function ajaxSearchFileOrFolder(searchString, folderId) {
		
		$.ajax({
            type: "GET",
            url: '/Folder/Secured/Folder/'+ folderId +'/Search/' + searchString,
            success : function(result) {
            	$.LoadingOverlay("hide");
            	$('#FileSystemAddressBar').html('> Search Results ? ' + '\''+searchString +'\'' +'&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Total Results: <span title="Files: '+result.obj.files.length+' , Folders: '+result.obj.folders.length +'">' + (result.obj.folders.length + result.obj.files.length));
            	$('#FileSystemBackButton').val(folderId);
            	CreateSearchDiv(result.obj, searchString);
            },
			error : function(e) {
				$.LoadingOverlay("hide");
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
		
	}
	
	function CreateSearchDiv(object, searchString) {
		
		if(object.folders.length == 0 && object.files.length == 0) {
			
			$('#FileSystemDiv').html('<h2 style="text-align: center;">&#x1F622; No Result Found for : '+ searchString+'</h2>');
	    	return;
		}
		
		var foldershtml = '';
		var filehtml = '';
    	$.each(object.folders, function(index, folder) {
    		foldershtml += '<button id="Folder_'+ folder.folderId +'" class="TreeFolder FolderIcon col-sm-2 Folder-context-menu" value="'+ folder.folderId +'"><i class="fa fa-folder faIconsStyle"></i> '+ folder.folderName +'</button>';
    	});

    	$.each(object.files, function(index, file) {
    		filehtml += '<button  id="File_'+file.documentId +'" class="TreeFile FolderIcon col-sm-2 File-context-menu" value="'+ file.documentId +'"><i class="fa fa-file faIconsStyle"></i> '+ file.fileName +'</button>';
    	});
    	$('#FileSystemDiv').html(foldershtml + filehtml);
	}
	
});