package org.DriveNew.App.Controller;

import org.DriveNew.App.Document.Service.LogService;
import org.DriveNew.App.Document.Service.UserDetailsService;
import org.DriveNew.App.Dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasAnyRole('USER')")
@RequestMapping("/User/Secured")
public class UserController {
	
	@Autowired
	private LogService log;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@GetMapping("/UserPanel")
	public ModelAndView getSuperAdminPanel() {
		
		log.save("User Logged In");
		return new ModelAndView("User/UserPanel", "userType", "User");
	}
	
	@GetMapping("/UserPanel/Details")
	@ResponseBody
	public Response getSuperAdminPanelData() {

		return new Response("User Details", "OK", userDetailsService.getUserDetails());
	}

}
