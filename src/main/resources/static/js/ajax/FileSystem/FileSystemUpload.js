$( document ).ready(function() {
	
	var FileSystemId = '#FileSystemDiv';
	
	
	$("#createFolderForm").submit(function(event) {
		event.preventDefault();
		ajaxCreateFolder();
	});
    
    function ajaxCreateFolder(){
    	var createFolderDto = {};
    	createFolderDto['folderName'] = $('#createFolderForm #folderName').val();
    	createFolderDto['folderAddress'] = $('#FileSystemDirectoryAddress').val();
    	createFolderDto['parentFolderId'] = $('#FileSysParentFolderId').val();
    	$.LoadingOverlay("show");
		
    	$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/Folder/Secured/Create/New/Folder",
            data: JSON.stringify(createFolderDto),
            dataType: 'json',
            cache: false,
            timeout: 600000,
			success : function(result) {
				$.LoadingOverlay("hide");
				alert(result.message);
				if(result.status == 'OK'){
					var subFolder = '<button id="Folder_'+ result.obj.folderId +'" class="TreeFolder FolderIcon col-sm-2 Folder-context-menu" value="'+ result.obj.folderId +'"><i class="fa fa-folder faIconsStyle"></i> '+ result.obj.folderName +'</button>';
					$(FileSystemId).append(subFolder);
					
				}
			},
			error : function(e) {
				$.LoadingOverlay("hide");
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});
    	resetCFForm();
    }
    
    function resetCFForm(){
    	document.getElementById("createFolderForm").reset();
    }
    
    

    $("#uploadMultipleFileForm").submit(function(event) {
		event.preventDefault();
		ajaxUploadMultiFiles();
	});
    
    function ajaxUploadMultiFiles(){
    	
    	var form = $('#uploadMultipleFileForm')[0];
    	console.log(form);
        var multiFilesForm = new FormData(form);
        console.log($('#FileSystemDirectoryAddress').val());
        multiFilesForm.append("folderAddress", $('#FileSystemDirectoryAddress').val());
        multiFilesForm.append("parentFolderId", $('#FileSysParentFolderId').val());
        $.LoadingOverlay("show");
        
        alert('Total Files Uploading: ' + document.getElementById('uploadMultpleFileInput').files.length);
        
    	$.ajax({
            
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/Folder/Secured/Upload/New/Multi/Files",
            data: multiFilesForm,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
			success : function(response) {
				console.log(response);
				alert('Files Uploaded');
				$.LoadingOverlay("hide");
				ajaxGetFolders($('#FileSysParentFolderId').val());
			},
			error : function(e) {
				alert("Error!");
				$.LoadingOverlay("hide");
			}
		});
    	resetUploadMultiFilesForm();
    }
    function resetUploadMultiFilesForm() {
    	document.getElementById("uploadMultipleFileForm").reset();
    }
    
    /*
    $.LoadingOverlaySetup({
        background      : "rgba(0, 0, 0, 0.5)",
        image           : "img/custom.svg",
        imageAnimation  : "1.5s fadein",
        imageColor      : "#ffcc00"
        
    });*/

    
    
    
    $("#UploadAFolderForm").submit(function(event) {
		event.preventDefault();
		ajaxUploadAFolder();
	});
    
    function ajaxUploadAFolder(){
    	
    	var folderUploadForm = new FormData($("#UploadAFolderForm")[0]);
    	
    	folderUploadForm = getFolderRec('UploadAFolderInputId', folderUploadForm);
        folderUploadForm.append("folderAddress", $('#FileSystemDirectoryAddress').val());
        folderUploadForm.append("parentFolderId", $('#FileSysParentFolderId').val());
        $.LoadingOverlay("show");
        
    	
    	$.ajax({
            
            type: "POST",
            enctype:'multipart/form-data',
            url: "/Folder/Secured/Upload/New/Folder",
            data: folderUploadForm,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
			success : function(result) {
				alert('Folder Uploaded');
				$.LoadingOverlay("hide");
				ajaxGetFolders($('#FileSysParentFolderId').val());
			},
			error : function(e) {
				alert("Error!");
				$.LoadingOverlay("hide");
			}
		});
    	resetUploadMultiFilesForm();
    }
    function resetUploadMultiFilesForm() {
    	document.getElementById("UploadAFolderForm").reset();
    }
    
    function getFolderRec(uploadFolderInputId, folderUploadForm) {
    	
    	var uploadFolderInput = document.getElementById(uploadFolderInputId);
    	var files = uploadFolderInput.files;

    	var filesObj = [];
    	
    	console.log(folderUploadForm);
    	for (var i=0; i<files.length; i++) {
    		
    		folderUploadForm.append('filesData['+i+'].filePath', files[i].webkitRelativePath);
    		folderUploadForm.append('filesData['+i+'].fileObj', files[i]);
    	}
    	return folderUploadForm;
    }
    
})