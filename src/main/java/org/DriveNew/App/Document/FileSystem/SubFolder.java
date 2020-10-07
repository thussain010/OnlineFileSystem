package org.DriveNew.App.Document.FileSystem;

public class SubFolder {

	private String folderId;
	private String folderName;
	private String isTrashed = "false";
	private String isLocked = "false";
	
	
	public SubFolder() {
		
	}
	
	public SubFolder(String folderId, String folderName) {
		this.folderId = folderId;
		this.folderName = folderName;
	}
	
	public SubFolder(Folder folder) {
		this.folderId = folder.getFolderId();
		this.folderName = folder.getFolderName();
	}

	
	
	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public String getIsTrashed() {
		return isTrashed;
	}

	public void setIsTrashed(String isTrashed) {
		this.isTrashed = isTrashed;
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
}
