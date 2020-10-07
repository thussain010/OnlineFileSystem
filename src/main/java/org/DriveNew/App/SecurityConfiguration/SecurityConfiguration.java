package org.DriveNew.App.SecurityConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration { 

	@Configuration
    @Order(1)
    public static class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		private PasswordEncoder passwordEncoder;
		
		@Autowired
		private UserSecurityService userSecurityService;
		
		
        public UserSecurityConfiguration() {
            super();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            
        	
         	http.antMatcher("/**/Secured/**").authorizeRequests()
         	
         	.antMatchers("/Login/Secured/Redirect").access("hasAnyRole('USER') or hasAnyRole('SUPER_ADMIN')")
         	.antMatchers("/Update/Secured/**").access("hasAnyRole('USER') or hasAnyRole('SUPER_ADMIN')")
         	.antMatchers("/Folder/Secured/**").access("hasAnyRole('USER') or hasAnyRole('SUPER_ADMIN')")
         	.antMatchers("/Repo/Secured/**").access("hasAnyRole('USER') or hasAnyRole('SUPER_ADMIN')")
         	.antMatchers("/SuperAdmin/Secured/**").access("hasAnyRole('SUPER_ADMIN')")
         	.antMatchers("/User/Secured/**").access("hasAnyRole('USER')")
         	
         	// log in
            .and().formLogin().loginPage("/login").loginProcessingUrl("/Login/Secured/Redirect_login").failureUrl("/login?error=loginError").defaultSuccessUrl("/Login/Secured/Redirect")
            // logout
            .and().logout().logoutUrl("/user_logout").logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID").and().exceptionHandling().accessDeniedPage("/401").and().csrf().disable();
        }
        
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        	auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
        }
    }
}