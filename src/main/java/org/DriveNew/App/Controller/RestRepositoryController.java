package org.DriveNew.App.Controller;

import java.util.HashMap;
import java.util.List;

import org.DriveNew.App.Controller.Service.UserControllerService;
import org.DriveNew.App.Document.User.Privileges;
import org.DriveNew.App.Document.User.Role;
import org.DriveNew.App.Dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

@RestController
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
@RequestMapping("/Repo/Secured/")
public class RestRepositoryController {


	@Autowired
	private UserControllerService userControllerService;
	
	
	@GetMapping("/get/Privileges")
	@JsonIgnore
	public Response getPrivileges() {
		List<Privileges> privileges = userControllerService.getAllPrivileges();
		return new Response("Success", "Done", privileges);
	}

	
	
	@GetMapping("/get/Roles")
	@JsonIgnore
	public Response getRoles() {
		List<Role> roles = userControllerService.getAllRoles();
		return new Response("Success", "Done", roles);
	}
	
	@GetMapping("/get/Update/Roles")
	@JsonIgnore
	public Response getUpdateRoles() {
		
		HashMap<String, Object> map = userControllerService.getRolesPrivilegeMap();
		return new Response("Success", "Done", map);
	}
	
	
}
