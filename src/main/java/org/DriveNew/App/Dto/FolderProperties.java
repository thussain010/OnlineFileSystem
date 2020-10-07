package org.DriveNew.App.Dto;

import org.DriveNew.App.Document.FileSystem.Folder;

public class FolderProperties {
	
	private String folderName;
	private String folderId;
	
	private String folderPath;
	
	private String folderSize;
	
	private String subFolderNum;
	
	private String subFileNum;
	
	private String createdDate;

	public FolderProperties() {
		
	}
	
	public FolderProperties(Folder folder) {
		
		this.folderId = folder.getFolderId();
		this.folderName = folder.getFolderName();
		this.folderPath = folder.getFolderPath();
		this.createdDate = folder.getCreatedDate();
	}
	
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getFolderSize() {
		return folderSize;
	}

	public void setFolderSize(String folderSize) {
		this.folderSize = folderSize;
	}

	public String getSubFolderNum() {
		return subFolderNum;
	}

	public void setSubFolderNum(String subFolderNum) {
		this.subFolderNum = subFolderNum;
	}

	public String getSubFileNum() {
		return subFileNum;
	}

	public void setSubFileNum(String subFileNum) {
		this.subFileNum = subFileNum;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	
}
