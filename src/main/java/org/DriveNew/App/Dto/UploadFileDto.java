package org.DriveNew.App.Dto;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileDto {

	private MultipartFile[] uploadFile;
	private String folderAddress;
	private String parentFolderId;
	

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

	public MultipartFile[] getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile[] uploadFile) {
		this.uploadFile = uploadFile;
	}
}
