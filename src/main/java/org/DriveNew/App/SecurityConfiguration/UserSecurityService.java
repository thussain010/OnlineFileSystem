package org.DriveNew.App.SecurityConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.DriveNew.App.Document.Service.RoleService;
import org.DriveNew.App.Document.Service.UserService;
import org.DriveNew.App.Document.User.Role;
import org.DriveNew.App.Document.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (!userService.existsByEmail(username))
			throw new UsernameNotFoundException("Email Id Not exists");

		User user = userService.getUser(username);
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAdminAuthorities(roleService.getUserRole(user)));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAdminAuthorities(List<Role> roles){
        
		Collection<GrantedAuthority> authorities = null;
		authorities = new ArrayList<>();
		
		for(Role role: roles) {
			if(role.getRole().equalsIgnoreCase("ROLE_SUPER_ADMIN")) {
				authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
				return authorities;
			}
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
		
    }

}
