package org.DriveNew.App.Dto;

import java.util.Set;

public class RoleDto {

	private String roleName;
	private Set<String> privilegesList;
	
	public RoleDto() {
		
	}
	
	public RoleDto(String roleName, Set<String> privilegesList) {
		this.roleName = roleName;
		this.privilegesList = privilegesList;
	}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Set<String> getPrivilegesList() {
		return privilegesList;
	}
	public void setPrivilegesList(Set<String> privilegesList) {
		this.privilegesList = privilegesList;
	}
}
