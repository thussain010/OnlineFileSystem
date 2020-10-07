package org.DriveNew.App.Document.Service;

import java.util.ArrayList;
import java.util.List;

import org.DriveNew.App.Document.Repository.UserRepository;
import org.DriveNew.App.Document.User.Role;
import org.DriveNew.App.Document.User.User;
import org.DriveNew.App.Dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private String getEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public boolean existsByEmail(String username) {
		return userRepository.existsByEmail(username);
	}


	public User getUser(String username) {
		return userRepository.findByEmail(username);
	}


	public void createSuperUser(String username, String password, Role role) {
		if(userRepository.existsByEmail(username))
			return;
		
		User user = new User();
		user.setEmail(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setRoleIds(getRoleIds(role));
		userRepository.save(user);
	}


	public void createUser(User user) {
		userRepository.save(user);
	}


	public void createUser(UserDetailsDto userDetailsDto) {
		
		User user = new User(userDetailsDto);
		user.setPassword(passwordEncoder.encode(userDetailsDto.getPassword()));
		user.setRoleIds(getRoleIds( roleService.getRoleByRoleName(userDetailsDto.getRoleName())));
		createUser(user);
	}


	public List<String> getUserAuthority(String email) {
		User user = userRepository.findByEmail(email);
		return roleService.getRoles(user);
	}
	
	private List<String> getRoleIds(Role role) {
		
		List<String> roleIds = new ArrayList<>();
		roleIds.add(role.getRoleId());
		return roleIds;
	}


	public void updatePassword(String confirmPassword) {
		User user = userRepository.findByEmail(getEmail());
		user.setPassword(passwordEncoder.encode(confirmPassword));
		userRepository.save(user);
	}

	public boolean existsCurrentPassword(String currentPassword) {
		return userRepository.existsByPassword(passwordEncoder.encode(currentPassword));
	}
}
