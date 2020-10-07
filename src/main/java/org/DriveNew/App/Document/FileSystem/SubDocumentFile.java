package org.DriveNew.App.Document.FileSystem;

public class SubDocumentFile {

	private String documentId;
	private String documentName;
	private String isTrashed = "false";
	private String isLocked = "false";
	
	
	public SubDocumentFile() {
	}
	
	public SubDocumentFile(DocumentFile docFile) {
		this.documentId = docFile.getDocumentId();
		this.documentName = docFile.getFileName();
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

	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
}
