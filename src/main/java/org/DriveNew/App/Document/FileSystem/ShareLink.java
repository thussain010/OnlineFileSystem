package org.DriveNew.App.Document.FileSystem;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ShareLink {

	@Id
	private String id;
	
	@Indexed(unique = true)
	private String link = RandomStringUtils.random(10, true, true);
	
	private String linkType;
	
	
	private String docType;
	
	
	private String docId;
	
	private String docParentId;
	
	private String dateCreated = new Date().toString();
	
	public ShareLink() {
		
	}

	public ShareLink(Folder folder) {
		this.docType = "Folder";
		this.docId = folder.getFolderId();
		this.docParentId = folder.getParentFolderId();
	}

	public ShareLink(DocumentFile file) {
		this.docType = "File";
		this.docId = file.getDocumentId();
		this.docParentId = file.getParentFolderId();
	}

	
	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getDocParentId() {
		return docParentId;
	}

	public void setDocParentId(String docParentId) {
		this.docParentId = docParentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}
}
