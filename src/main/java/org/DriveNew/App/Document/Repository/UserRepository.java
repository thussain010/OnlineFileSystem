package org.DriveNew.App.Document.Repository;

import org.DriveNew.App.Document.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{

	boolean existsByEmail(String username);

	User findByEmail(String username);

	boolean existsByPassword(String password);
}
