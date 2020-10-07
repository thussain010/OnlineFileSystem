package org.DriveNew.App.Document.Repository;

import java.util.Set;

import org.DriveNew.App.Document.User.Privileges;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrivilegeRepository extends MongoRepository<Privileges, String> {

	Privileges findByPrivilegeName(String priv);

	Set<Privileges> findByPrivilegeNameIn(Set<String> privilegesList);
}
