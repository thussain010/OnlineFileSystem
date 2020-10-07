package org.DriveNew.App.Document.Repository;

import java.util.List;

import org.DriveNew.App.Document.FileSystem.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FolderRepository extends MongoRepository<Folder, String>{

	Folder findByParentFolderId(String string);

	Folder findByFolderPath(String string);

	Folder findByFolderPathAndFolderName(String folderPath, String folderName);

	boolean existsByFolderPathAndFolderName(String folderPath, String folderName);

	List<Folder> findByFolderPathLikeAndFolderNameLike(String folderPath, String searchString);


	void deleteAllByFolderPathLike(String folderPath);

	List<Folder> findAllByIsTrashed(String string);

}
