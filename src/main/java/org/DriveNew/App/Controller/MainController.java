package org.DriveNew.App.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	
	@GetMapping("/")
	public String getHome() {
		return "login";
	}
	
    @PreAuthorize("hasAnyRole('USER') or hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/Login/Secured/Redirect")
    public String postLoginPage(HttpServletRequest request) {
    	
    	String authiorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    	if(authiorities.equalsIgnoreCase("[ROLE_SUPER_ADMIN]")) {
    		return "redirect:/SuperAdmin/Secured/SuperAdminPanel";
    	}
    	else if (authiorities.equalsIgnoreCase("[ROLE_USER]")) {
    		return "redirect:/User/Secured/UserPanel";
    	}
    	else {
    		new SecurityContextLogoutHandler().logout(request, null, null);
        	return "Failure";
    	}
    }
    
    @GetMapping("/401")
    public String get401() {
    	return "401";
    }
    
    @GetMapping("/login")
    public String loginPage() {
    	return "login";
    }
    @GetMapping("/logout") 
	public String logout(HttpServletRequest request) {
    	new SecurityContextLogoutHandler().logout(request, null, null);
    	return "redirect:/login";
    }
    
    @GetMapping("/user_logout") 
	public String logoutUser(HttpServletRequest request) {
    
    	new SecurityContextLogoutHandler().logout(request, null, null);
    	return "redirect:/login?logout";
    }
	
	
	
}
