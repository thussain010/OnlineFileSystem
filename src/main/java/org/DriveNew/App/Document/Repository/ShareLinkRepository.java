package org.DriveNew.App.Document.Repository;

import org.DriveNew.App.Document.FileSystem.ShareLink;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShareLinkRepository extends MongoRepository<ShareLink, String>{

	ShareLink findByLinkAndLinkTypeAndDocType(String link, String linkType, String docType);

	boolean existsByDocIdAndDocType(String fileId, String docType);

	ShareLink findByDocIdAndDocType(String docId, String docType);

	boolean existsByDocIdAndDocTypeAndLinkType(String docId, String docType, String linkType);

	ShareLink findByDocIdAndDocTypeAndLinkType(String docId, String docType, String linkType);

}
