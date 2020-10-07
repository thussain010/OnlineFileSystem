package org.DriveNew.App.Document.FileSystem;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.context.SecurityContextHolder;

@Document
public class Log {
	
	@Id
	private String logId;
	private String username;
	private String date = new Date().toString();
	private String remarks = "";
	
	public Log() {
		
	}
	
	private String getEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public Log(String remarks) {
		this.username = getEmail();
		this.remarks = remarks;
	}
	
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
