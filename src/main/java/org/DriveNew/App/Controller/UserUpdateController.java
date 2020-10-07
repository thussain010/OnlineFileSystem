package org.DriveNew.App.Controller;

import org.DriveNew.App.Document.Service.LogService;
import org.DriveNew.App.Document.Service.UserDetailsService;
import org.DriveNew.App.Document.Service.UserService;
import org.DriveNew.App.Dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('USER') or hasAnyRole('ROLE_SUPER_ADMIN')")
@RequestMapping("/Update/Secured")
public class UserUpdateController {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LogService log;
	
	
	@PutMapping("/Profile")
	public Response updateProfile(@RequestParam("name") String name,@RequestParam("mobileNumber") String mobileNumber) {
		log.save("Profile Updated  with Name: " + name + " , Mobile Number: " + mobileNumber);
		return new Response("Profile Updated", "OK", userDetailsService.updateProfile(name, mobileNumber));
	}
	
	@PostMapping("/Password")
	public Response updatePassword(@RequestParam("confirmPassword") String confirmPassword) {
		
		userService.updatePassword(confirmPassword);
		log.save("Password Updated");
		return new Response("Password Updated", "OK", confirmPassword);
	}
}
