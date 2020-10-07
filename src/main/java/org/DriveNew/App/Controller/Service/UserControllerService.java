package org.DriveNew.App.Controller.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.DriveNew.App.Document.Service.PrivilegeService;
import org.DriveNew.App.Document.Service.RoleService;
import org.DriveNew.App.Document.Service.UserDetailsService;
import org.DriveNew.App.Document.Service.UserService;
import org.DriveNew.App.Document.User.Privileges;
import org.DriveNew.App.Document.User.Role;
import org.DriveNew.App.Document.User.UserDetails;
import org.DriveNew.App.Dto.RoleDto;
import org.DriveNew.App.Dto.UpdateRoleDto;
import org.DriveNew.App.Dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserControllerService {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	
	public String getEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public UserDetails getUserDetails() {
		return userDetailsService.getUserDetailsByEmail(getEmail());
	}

	public String createUser(UserDetailsDto userDetailsDto) {
		try {
			userService.createUser(userDetailsDto);
			userDetailsService.saveUserDetails(userDetailsDto);
		} catch(Exception e) {
			return "Internal Server Error!!! User Can't be Created";
		}
		return "User Created";
	}

	public void createRole(RoleDto roleDto) {
		
		Set<String> privilegeIds = privilegeService.getPrivilegeIds(roleDto.getPrivilegesList());
		Role role = new Role(roleDto.getRoleName());
		role.setPrivilegesId(privilegeIds);
		roleService.saveRole(role);
	}
	
	public List<Role> getAllRoles() {
		return roleService.getAllUserRoles();
	}

	public List<Privileges> getAllPrivileges() {
		return privilegeService.getAllPriviliges();
	}

	public HashMap<String, Object> getRolesPrivilegeMap() {
		
		HashMap<String, Object> map = new HashMap<>();
		List<Role> roles = getAllRoles();
		List<Privileges> privileges = getAllPrivileges();
		map.put("roles", roles);
		map.put("privileges", privileges);
		return map;
	}
	
	public String updateRole(UpdateRoleDto updateRoleDto) {

		String remarks = "";
		List<Role> roles = new ArrayList<>();		
		for(RoleDto roleDto: updateRoleDto.getRoleDto()) {

			Role role = roleService.getRoleByRoleName(roleDto.getRoleName());
			remarks += "Role: " + role.getRole() + " privileges: ";
			
			Set<String> rolePrivilegesId = new HashSet<>();			
			for(String privilegeName : roleDto.getPrivilegesList()) {
				
				Privileges priv = privilegeService.getPrivilege(privilegeName);
				rolePrivilegesId.add(priv.getId());
			}
			remarks+= roleDto.getPrivilegesList() + "\n";
			role.setPrivilegesId(rolePrivilegesId);
			roles.add(role);
		}

		roleService.updateRolesList(roles);
		return remarks;
	}

	public boolean existsRole(String roleName) {
		return roleService.existsRole(roleName);
	}
	
	public boolean existsUser(String email) {
		return userService.existsByEmail(email);
	}

	public boolean existsUserDetails(String email) {
		return userDetailsService.existsByEmail(email);
	}

	public List<UserDetails> getAllUsers() {
		// TODO Auto-generated method stub
		return userDetailsService.getAllUsers();
	}
}
