package org.DriveNew.App.Document.Service;

import java.util.ArrayList;
import java.util.List;

import org.DriveNew.App.Document.Repository.UserDetailsRepository;
import org.DriveNew.App.Document.User.UserDetails;
import org.DriveNew.App.Dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	private String getEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public boolean exists(String userId) {
		return userDetailsRepository.existsById(userId);
	}
	
	public UserDetails getUserDetails(String userId) {
		return userDetailsRepository.findById(userId).get();
	}
	
	public UserDetails getUserDetails() {
		return getUserDetailsByEmail(getEmail());
	}

	public UserDetails getUserDetailsByEmail(String email) {
		return userDetailsRepository.findByEmail(email);
	}

	public void addSuperUserDetails(String email) {
		UserDetails userDetails = new UserDetails();
		userDetails.setName("Nitin Kumar");
		userDetails.setMobileNumber("7988165546");
		userDetails.setEmail(email);
		userDetailsRepository.save(userDetails);
		/*UserDetails driverDetails = new UserDetails();
		driverDetails.setName("DRIVER");
		driverDetails.setMobileNumber("7073311897");
		driverDetails.setEmail(email);
		userDetailsRepository.save(userDetails);*/
	}

	public void saveUserDetails(UserDetailsDto userDetailsDto) {
		
		UserDetails userDetails = new UserDetails();
		userDetails.setEmail(userDetailsDto.getEmail());
		userDetails.setName(userDetailsDto.getName());
		userDetails.setMobileNumber(userDetailsDto.getMobileNumber());
		userDetailsRepository.save(userDetails);
	}

	public boolean existsByEmail(String email) {
		return userDetailsRepository.existsByEmail(email);
	}

	public UserDetails updateProfile(String name, String mobileNumber) {
		UserDetails userDetails = getUserDetailsByEmail(getEmail());
		userDetails.setName(name);
		userDetails.setMobileNumber(mobileNumber);
		return userDetailsRepository.save(userDetails);
	}

	public List<UserDetails> getAllUsers() {
		// TODO Auto-generated method stub
		List<UserDetails> users =new ArrayList<UserDetails>();
		 users=userDetailsRepository.findAll();
		 return users;
	}
}
