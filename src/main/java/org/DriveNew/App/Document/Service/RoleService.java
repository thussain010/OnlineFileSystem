package org.DriveNew.App.Document.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.DriveNew.App.Document.Repository.RoleRepository;
import org.DriveNew.App.Document.User.Role;
import org.DriveNew.App.Document.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	public Role getRole(String roleId) {
		return roleRepository.findById(roleId).get();
	}
	
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	public Role createNewRole(String roleName, Set<String> privilegesIds) {
		Role role = new Role(roleName);
		role.setPrivilegesId(privilegesIds);
		return saveRole(role);
	}

	public Role getRoleByRoleName(String roleName) {
		return roleRepository.findByRole(roleName);
	}

	public List<Role> getAllUserRoles() {
		return roleRepository.findAllByRoleNot("ROLE_SUPER_ADMIN");
	}

	public void updateRolesList(List<Role> roles) {
		roleRepository.saveAll(roles);
	}

	public boolean existsRole(String roleName) {
		
		return getRoleByRoleName(roleName) != null ? true : false;
	}

	public List<String> getRoles(User user) {
		
		List<String> roles = new ArrayList<>();
		
		for(String roleId : user.getRoleIds()) {
			roles.add(getRole(roleId).getRole());
		}
		return roles;
	}

	public List<Role> getUserRole(User user) {
		
		List<Role> roles = new ArrayList<>();
		
		for(String roleId: user.getRoleIds()) {
			roles.add(getRole(roleId));
		}
		return roles;
	}
}
