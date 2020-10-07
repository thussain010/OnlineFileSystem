package org.DriveNew.App.Document.Repository;

import java.util.List;

import org.DriveNew.App.Document.User.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
	Role findByRole(String role);

	List<Role> findAllByRoleNot(String role);
}
