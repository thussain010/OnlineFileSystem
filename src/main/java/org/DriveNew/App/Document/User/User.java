package org.DriveNew.App.Document.User;

import java.util.List;

import org.DriveNew.App.Dto.UserDetailsDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id
	private String id;
	
	@Indexed(unique = true)
	private String email;

	private String password;

	private List<String> roleIds;
	
	
	
	public User() {
	}

	public User(User user) {

		this.email = user.getEmail();
		this.password = user.getPassword();
		this.roleIds = user.getRoleIds();
	}

	public User(UserDetailsDto userDetailsDto) {
		this.email = userDetailsDto.getEmail();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}
}
