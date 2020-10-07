package org.DriveNew.App.Dto;

public class CreateFolderDto {

	private String folderName;
	private String folderAddress;
	private String parentFolderId;
	
	public String getParentFolderId() {
		return parentFolderId;
	}
	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFolderAddress() {
		return folderAddress;
	}
	public void setFolderAddress(String folderAddress) {
		this.folderAddress = folderAddress;
	}
}
