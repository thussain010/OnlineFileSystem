package org.DriveNew.App.Document.FileSystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Folder {

	@Id
	private String folderId;
	
	private String folderName;
	
	private String createdDate = new Date().toString();
	
	private String folderPath;
	
	private String absolutePath;
	
	private List<SubFolder> subFolder =new ArrayList<>();
	
	private String parentFolderId;
	
	private List<SubDocumentFile> subDocumentFile = new ArrayList<>();
	
	private String isTrashed = "false";
	
	private String isLocked = "false";
	
	private String dateCreated = new Date().toString();
	private String key;

	public Folder(String folderName, String folderPath) {
		this.folderName = folderName;
		this.folderPath = folderPath;
	}
	
	public Folder() {
	}

	public Folder(String parentFolderId, String folderName, String folderPath) {
		this.parentFolderId = parentFolderId;
		this.folderName = folderName;
		this.folderPath = folderPath;
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

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public String getFolderId() {
		return folderId;
	}

	
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	public List<SubFolder> getSubFolder() {
		return subFolder;
	}

	public void setSubFolder(List<SubFolder> subFolder) {
		this.subFolder = subFolder;
	}

	public List<SubDocumentFile> getSubDocumentFile() {
		return subDocumentFile;
	}

	public void setSubDocumentFile(List<SubDocumentFile> subDocumentFile) {
		this.subDocumentFile = subDocumentFile;
	}
}
