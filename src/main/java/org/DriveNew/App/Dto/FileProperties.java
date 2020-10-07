package org.DriveNew.App.Dto;

import org.DriveNew.App.Document.FileSystem.DocumentFile;

public class FileProperties {

	private String fileName;
	private String fileId;
	
	private String filePath;
	
	private String fileSize;
	
	private String createdDate;
	
	public FileProperties() {
	}
	
	public FileProperties(DocumentFile file) {
		this.fileName = file.getFileName();
		this.fileId = file.getDocumentId();
		this.filePath = file.getFilePath();
		this.createdDate = file.getDateCreated();
		this.fileSize = Double.toString(file.getFileSize() / (1024*1024.0D)) + " MB";
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
