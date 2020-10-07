package org.DriveNew.App.Controller;

import java.util.Collections;
import java.util.List;

import org.DriveNew.App.Controller.Service.UserControllerService;
import org.DriveNew.App.Document.FileSystem.Log;
import org.DriveNew.App.Document.Service.LogService;
import org.DriveNew.App.Dto.Response;
import org.DriveNew.App.Dto.RoleDto;
import org.DriveNew.App.Dto.UpdateRoleDto;
import org.DriveNew.App.Dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
@RequestMapping("/SuperAdmin/Secured")
public class SuperAdminController {
	int login=-1;
	
	@Autowired
	private UserControllerService userControllerService;
	
	@Autowired
	private LogService log;
	
	@GetMapping("/SuperAdminPanel")
	public ModelAndView getSuperAdminPanel() {
		
		log.save("User Logged In");
		return new ModelAndView("User/SuperAdminPanel", "userType", "SuperAdmin");
	}
	
	@GetMapping("/SuperAdminPanel/Details")
	@ResponseBody
	public Response getSuperAdminPanelData() {

		return new Response("User Details", "OK", userControllerService.getUserDetails());
	}
	
	@GetMapping("/SuperAdminPanel/AllUserDetails")
	@ResponseBody
	public Response getAllUserDetails() {
		log.save("Viewed all user details");
		return new Response("All Users List", "OK", userControllerService.getAllUsers());
	}
	@PostMapping("/Create/New/User")
	@ResponseBody
	public Response createNewUser(@ModelAttribute UserDetailsDto userDetailsDto) {
		
		if(userControllerService.existsUser(userDetailsDto.getEmail()) && userControllerService.existsUserDetails(userDetailsDto.getEmail()))
			return new Response("User with Email: "+ userDetailsDto.getEmail() +" Already Exists", "Error", userDetailsDto);
		
		String msg = userControllerService.createUser(userDetailsDto);
		if(msg.equals("User Created"))
			log.save("Created New User : " + userDetailsDto.getName() + " with Role " + userDetailsDto.getRoleName());
		return new Response(msg, "Done", userDetailsDto);
	}
	
	
	@PostMapping("/Create/New/Role")
	@ResponseBody
	public Response createNewRole(@ModelAttribute RoleDto roleDto) {
		
		String roleName = "ROLE_" +roleDto.getRoleName().replaceAll(" ", "_").toUpperCase();
		roleDto.setRoleName(roleName);
		
		if(!userControllerService.existsRole(roleName)) {
			userControllerService.createRole(roleDto);
			log.save("Created New Role : " + roleDto.getRoleName() + " with Privileges:  " + roleDto.getPrivilegesList());
			return new Response("Role Created", "Done", roleDto); 
		}else {
			return new Response("Role Already Exists", "Duplicate", roleDto); 
		}
		
	}
	
	
	
	@PostMapping("/Validate/Email")
	@ResponseBody
	public Response validateEmail(@RequestBody String email) {
		
		email = email.replaceAll("\"", "");
		if(userControllerService.existsUser(email))
			return new Response("Email Already Exists", "Done", email);
		return new Response("Email not Exists", "OK", email);
	}

	@PostMapping("/Update/Role")
	@ResponseBody
	public Response updateRole(@RequestBody UpdateRoleDto updateRoleDto) {
		
		String remark = userControllerService.updateRole(updateRoleDto);
		log.save("Updated Roles " + remark);
		return new Response("Ok","200",null);
	}
	
	
	@GetMapping("/Log/Reports")
	@ResponseBody
	public Response getLogs() {
		login++;
		if (login>0) {
			log.save("Viewed Logs");
		}
		List<Log> logs = log.getLogs();
		Collections.reverse(logs);
		return new Response("Logs","200",logs);
		//return new ModelAndView("User/LogReport", "Reports", logs);
	}
	
}