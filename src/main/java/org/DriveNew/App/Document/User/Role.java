package org.DriveNew.App.Document.User;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Role {

	@Id
	private String roleId;

	private String role;
	
	private Set<String> privilegesId;

	public Role() {

	}

	public Role(String role) {
		this.role = role;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<String> getPrivilegesId() {
		return privilegesId;
	}

	public void setPrivilegesId(Set<String> privilegesId) {
		this.privilegesId = privilegesId;
	}
}