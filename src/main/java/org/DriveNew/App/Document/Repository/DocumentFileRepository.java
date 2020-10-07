package org.DriveNew.App.Document.Repository;

import java.util.List;

import org.DriveNew.App.Document.FileSystem.DocumentFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentFileRepository extends MongoRepository<DocumentFile, String>{

	List<DocumentFile> findByFolderPathLikeAndFileNameLike(String folderPath, String searchString);

	void deleteAllByFolderPathLike(String folderPath);

	List<DocumentFile> findAllByIsTrashed(String string);

}
