package org.DriveNew.App.Dto;

import org.springframework.web.multipart.MultipartFile;

public class FilesData {
	
	private String filePath;
	
	private MultipartFile fileObj;

	public FilesData() {
		
	}
	public FilesData(FilesData fileData) {
		this.filePath = fileData.getFilePath();
		this.fileObj = fileData.getFileObj();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public MultipartFile getFileObj() {
		return fileObj;
	}

	public void setFileObj(MultipartFile fileObj) {
		this.fileObj = fileObj;
	}
}
