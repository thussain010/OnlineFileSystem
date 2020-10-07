package org.DriveNew.App.Document.Repository;

import org.DriveNew.App.Document.FileSystem.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String>{

}
