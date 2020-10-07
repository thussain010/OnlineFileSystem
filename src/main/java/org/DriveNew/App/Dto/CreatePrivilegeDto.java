package org.DriveNew.App.Dto;

import java.util.List;

public class CreatePrivilegeDto {

	private String privilegeName;
	
	private List<String> rolesList;

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public List<String> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<String> rolesList) {
		this.rolesList = rolesList;
	}
}
