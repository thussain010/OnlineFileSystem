package org.DriveNew.App.Document.FileSystem;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document
public class DocumentFile {

	@Id
	private String documentId;
	
	private String fileName;
	
	private String filePath;
	
	private String folderPath;
	
	private Long fileSize;
	
	private String parentFolderId;
	
	private String isTrashed = "false";
	
	private String isLocked = "false";
	
	private String dateCreated = new Date().toString();
	
	private String key="";
	//private MultipartFile files;
	
	
	public DocumentFile(Folder parentFolder, MultipartFile files) {
		this.fileName = files.getOriginalFilename();
		this.folderPath = parentFolder.getFolderPath() + "/" + parentFolder.getFolderName();
		this.filePath = this.folderPath + "/" + this.fileName;
		this.parentFolderId = parentFolder.getFolderId();
		this.fileSize = files.getSize();
		//this.files=files;
	}
	
	

	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public String getIsLocked() {
		return isLocked;
	}



	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}



	public DocumentFile() {
		
	}
	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getIsTrashed() {
		return isTrashed;
	}

	public void setIsTrashed(String isTrashed) {
		this.isTrashed = isTrashed;
	}
	
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
}
