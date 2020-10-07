package org.DriveNew.App.Document.Service;

import org.DriveNew.App.Document.FileSystem.ShareLink;
import org.DriveNew.App.Document.Repository.ShareLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareLinkService {

	@Autowired
	private ShareLinkRepository shareLinkRepository;
	
	public ShareLink saveLink(ShareLink shareLink) {
		return shareLinkRepository.save(shareLink);
	}

	public ShareLink findByLinkAndLinkTypeAndDocType(String link, String linkType, String docType) {
		return shareLinkRepository.findByLinkAndLinkTypeAndDocType(link, linkType, docType);
	}

	public boolean existsByDocIdAndDocType(String docId, String docType) {
		return shareLinkRepository.existsByDocIdAndDocType(docId, docType);
	}

	public ShareLink findByDocIdAndDocType(String docId, String docType) {
		return shareLinkRepository.findByDocIdAndDocType(docId, docType);
	}

	public boolean existsByDocIdAndDocTypeAndLinkType(String docId, String docType, String linkType) {
		return shareLinkRepository.existsByDocIdAndDocTypeAndLinkType(docId, docType, linkType);
	}
	
	public ShareLink findByDocIdAndDocTypeAndLinkType(String docId, String docType, String linkType) {
		return shareLinkRepository.findByDocIdAndDocTypeAndLinkType(docId, docType, linkType);
	}
}
