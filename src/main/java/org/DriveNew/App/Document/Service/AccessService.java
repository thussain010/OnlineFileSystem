package org.DriveNew.App.Document.Service;

import java.util.ArrayList;
import java.util.List;

import org.DriveNew.App.Document.User.Role;
import org.DriveNew.App.Document.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	public boolean isUser() {
		String authiorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
		if(authiorities.contains("[ROLE_SUPER_ADMIN]") || authiorities.contains("[ROLE_DRIVER]"))
			return false;
		else return true;
	}
	
	public String getEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public User getUser() {
		return userService.getUser(getEmail());
	}
	
	public List<String> getPrivileges() {
		Role role = roleService.getRole(getUser().getRoleIds().get(0));
		List<String> privs = new ArrayList<>();
		for(String privIds : role.getPrivilegesId()) {
			
			privs.add(privilegeService.getPrivilegeById(privIds).getPrivilegeName());
		}
		return privs;
	}
	
	
	public boolean canUpload() {
		
		for(String priv: getPrivileges()) {
			if(priv.equals("Upload"))
				return true;
		}
		return false;
	}
	
	public boolean canShare() {
		for(String priv: getPrivileges()) {
			if(priv.equals("Share"))
				return true;
		}
		return false;
	}
	
	public boolean canDownload() {
		for(String priv: getPrivileges()) {
			if(priv.equals("Download"))
				return true;
		}
		return false;
	}
	
	
}
