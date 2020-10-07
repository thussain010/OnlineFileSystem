package org.DriveNew.App.Dto;

public class FileObject {

	private Object fileObj;
	private String fileExtension;
	
	
	
	public FileObject(Object fileObj, String fileExtension) {
		this.fileObj = fileObj;
		this.fileExtension = fileExtension;
	}
	public Object getFileObj() {
		return fileObj;
	}
	public void setFileObj(Object fileObj) {
		this.fileObj = fileObj;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
}
