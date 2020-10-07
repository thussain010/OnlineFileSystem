package org.DriveNew.App.Document.Repository;

import org.DriveNew.App.Document.User.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailsRepository extends MongoRepository<UserDetails, String>{
	
	UserDetails findByEmail(String email);

	boolean existsByEmail(String email);

	//UserDetails findOne(String userId);
}
