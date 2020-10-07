package org.DriveNew.App.Dto;

import java.util.ArrayList;
import java.util.List;


public class FolderRecDto {

	private String folderName;
	
	private List<FilesData> filesData = new ArrayList<>();
	
	private String folderAddress;
	
	private String parentFolderId;

	public List<FilesData> getFilesData() {
		return filesData;
	}

	public void setFilesData(List<FilesData> filesData) {
		this.filesData = filesData;
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

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
}
