package org.DriveNew.App;

import java.io.File;
import java.util.Set;
import org.DriveNew.App.Document.Service.FolderService;
import org.DriveNew.App.Document.Service.PrivilegeService;
import org.DriveNew.App.Document.Service.RoleService;
import org.DriveNew.App.Document.Service.UserDetailsService;
import org.DriveNew.App.Document.Service.UserService;
import org.DriveNew.App.Document.User.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitiateDatabase implements ApplicationRunner{

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	@Autowired
	private FolderService folderService;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		
		if(!userService.existsByEmail("thussain010@gmail.com")) {
			
			new File("/Home/Talib/CloudStore").mkdirs();
			Set<String> privileges = privilegeService.initiatePrivileges();
			Role role = roleService.createNewRole("ROLE_SUPER_ADMIN", privileges);
			userService.createSuperUser("thussain010@gmail.com","thussain010",role);
			userDetailsService.addSuperUserDetails("thussain010@gmail.com");
			folderService.createRootDirectory();
			Logger logger=LoggerFactory.getLogger(this.getClass());
			logger.info("Database Initiated");
		}
	}
}
