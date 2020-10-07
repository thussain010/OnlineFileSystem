package org.DriveNew.App.Document.User;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Privileges {

	@Id
	private String id;
	
	private String privilegeName;
	
	public Privileges() {
	}
	
	public Privileges(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}
}