package org.DriveNew.App.Controller.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.DriveNew.App.Document.FileSystem.DocumentFile;
import org.DriveNew.App.Document.FileSystem.Folder;
import org.DriveNew.App.Document.FileSystem.ShareLink;
import org.DriveNew.App.Document.FileSystem.SubDocumentFile;
import org.DriveNew.App.Document.FileSystem.SubFolder;
import org.DriveNew.App.Document.Service.AccessService;
import org.DriveNew.App.Document.Service.DirectoryService;
import org.DriveNew.App.Document.Service.DocumentFileService;
import org.DriveNew.App.Document.Service.FolderService;
import org.DriveNew.App.Document.Service.LogService;
import org.DriveNew.App.Document.Service.ShareLinkService;
import org.DriveNew.App.Document.Service.ZippingService;
import org.DriveNew.App.Dto.CreateFolderDto;
import org.DriveNew.App.Dto.FolderProperties;
import org.DriveNew.App.Dto.FolderRecDto;
import org.DriveNew.App.Dto.Response;
import org.DriveNew.App.Dto.UploadFileDto;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FolderControllerService {
	Logger logger=LoggerFactory.getLogger(FolderControllerService.class);
	
	@Autowired
	private ZippingService zippingService;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private DocumentFileService documentFileService;
	
	@Autowired
	private DirectoryService directoryService;
	
	@Autowired
	private ShareLinkService shareLinkService;
	
	@Autowired
	private LogService log;
	
	@Autowired
	private AccessService access;
	
	
	final String baseDir = "/Home/Talib/CloudStore";
	
	public Folder getRootFolder() {
		return folderService.getRootFolder();
	}

	public Response createFolder(CreateFolderDto createFolderDto) {
		
		
		if(folderService.isFolderNameExists(createFolderDto))
			return new Response("Folder " +createFolderDto.getFolderName()+" Already Exists","Duplicate",createFolderDto);
		
		Folder parentFolder = folderService.getFolder(createFolderDto.getParentFolderId());
		Folder folder = new Folder(parentFolder.getFolderId() , createFolderDto.getFolderName(), parentFolder.getFolderPath() + "/" + parentFolder.getFolderName());
		folder = folderService.createFolder(folder);
		
		parentFolder.getSubFolder().add(new SubFolder(folder));
		folderService.updateParentFolder(parentFolder);
		log.save("Created New Folder: " + folder.getFolderName() + " at Path: " + folder.getFolderPath());
		return new Response("Folder Created","OK",folder);
	}

	public Folder getFolder(String folderId) {
		return folderService.getFolder(folderId);
	}
	
	public Map<String, Object> getFolderMap(String folderId) {
		
		Map<String, Object> map = new LinkedHashMap<>();
		Folder folder = folderService.getFolder(folderId);
		map.put("folder", folder);
		map.put("parentMap", directoryService.calculateParentMap(folder));
		return map;
	}
	
	public Response UploadFolderRec(FolderRecDto folderRecDto) {
		return new Response("Folder Uploaded", "OK", folderRecDto);
	}
	
	
	public Response uploadFile(UploadFileDto uploadFileDto) {
		
		Folder parentFolder = folderService.getFolder(uploadFileDto.getParentFolderId());
		logger.warn("PARENT FOLDER ID::"+uploadFileDto.getParentFolderId());
		String existstingFiles = "";
		
		String remark = "File: ";
		
		if(uploadFileDto.getUploadFile().length < 0)
			return new Response("At least Upload a Single File", "Null Error", uploadFileDto);
		
		try {
			
			for(MultipartFile file: uploadFileDto.getUploadFile()) {
				if(folderService.isFileNameExists(parentFolder.getFolderId(), file.getOriginalFilename())) {
					existstingFiles += file.getOriginalFilename() + ", ";
					continue;
				}
				remark += file.getName() + ", ";
				Logger logger=LoggerFactory.getLogger(this.getClass());
				logger.warn("FOLDERCONTROLLERSERVICE::FILESIZE::"+file.getSize());
				DocumentFile docFile = new DocumentFile(parentFolder, file);
				
				docFile = documentFileService.saveNewDocument(docFile, file);
					
				parentFolder.getSubDocumentFile().add(new SubDocumentFile(docFile));
				parentFolder = folderService.updateParentFolder(parentFolder);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Response("There is Some Error in Uploading Files.\nMaybe Some Files Will be Uploaded","Error",parentFolder);
		}
		
		remark += " Uploaded at " + parentFolder.getFolderPath();
		log.save(remark);
		if(existstingFiles.equals("")) {
			if(uploadFileDto.getUploadFile().length == 1)
				return new Response("File Uploaded Successfully","OK",parentFolder);
			else 
				return new Response("Files Uploaded Successfully","Multiple OK",parentFolder);
		}
		else {
			if(uploadFileDto.getUploadFile().length == 1)
				return new Response("File " + existstingFiles + " Already Exists","Duplicate",parentFolder);
			else return new Response("Some Files Skipped (maybe All).Because they Already Exists\n" + existstingFiles,"Multiple Duplicate",parentFolder);
		}
		
	}

	public DocumentFile findDocumentFileById(String fileId) {
		return documentFileService.getFile(fileId);
	}
	
	public Object getFile(String fileId) {
		DocumentFile file = documentFileService.getFile(fileId);
		FileSystemResource f = new FileSystemResource(baseDir + file.getFilePath());
		if(file.getFileSize() >= 419430400L) {
			try {
				return downloadFile(file);
			} catch(Exception e) {
				return new Response("File Can not be Downloaded", "Error", "File Not Found");
			}
		}
		try {
			switch(FilenameUtils.getExtension(file.getFileName())) {
				case "java":
				case "py":
				case "js":
				case "css":
				case "pl":
				case "c":
				case "cpp":
						
				case "txt":
					return ResponseEntity
			            .ok()
			            .contentLength(f.contentLength()).contentType(MediaType.TEXT_PLAIN)
			            .body(new InputStreamResource(f.getInputStream()));
				case "pdf":
					return ResponseEntity
				            .ok()
				            .contentLength(f.contentLength()).contentType(MediaType.APPLICATION_PDF)
				            .body(new InputStreamResource(f.getInputStream()));
				case "mp4":
					
				case "mp3":
					return ResponseEntity
				            .ok()
				            .contentLength(f.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM)
				            .body(new InputStreamResource(f.getInputStream()));
					
				case "png":

					return ResponseEntity
				            .ok()
				            .contentLength(f.contentLength()).contentType(MediaType.IMAGE_PNG)
				            .body(new InputStreamResource(f.getInputStream()));
				case "jpg":
				case "jpeg":
					return ResponseEntity
				            .ok()
				            .contentLength(f.contentLength()).contentType(MediaType.IMAGE_JPEG)
				            .body(new InputStreamResource(f.getInputStream()));
				case "gif":

					return ResponseEntity
				            .ok()
				            .contentLength(f.contentLength()).contentType(MediaType.IMAGE_GIF)
				            .body(new InputStreamResource(f.getInputStream()));
				
				case "html":
					return ResponseEntity
				            .ok()
				            .contentLength(f.contentLength()).contentType(MediaType.TEXT_HTML)
				            .body(new InputStreamResource(f.getInputStream()));
			}
		}
		catch(Exception e) {
			return new Response("File Can not be Opened", "Error", "File Not Found");
		}
		try {
			return downloadFile(file);
		} catch(Exception e) {
			return new Response("File Can not be Opened", "Error", "File Not Found");
		}
	}
	
	public Object downloadFile(DocumentFile file) throws IOException {
		
		if(access.isUser() && !(access.canDownload()))
			return "Access is Denied";
		
		HttpHeaders headers = new HttpHeaders();
		
		String filename = file.getFileName();
		headers.add("content-disposition", "inline;filename=" + filename);
	    FileInputStream fileInputStream =  new FileInputStream(new File(baseDir + file.getFilePath()));
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(new InputStreamResource(fileInputStream), headers, HttpStatus.OK);
	    return response;
	}

	public Response UploadFolder(FolderRecDto folderRecDto) {
		Folder parentFolder = folderService.createUploadFolderParent(folderRecDto);
		
		if(parentFolder == null) {
			return new Response("Folder Exists Already.Please Try with a Different Name", "Error", folderRecDto);
		}
		
		if(folderRecDto.getFilesData().size() < 1) 
			return new Response("Folder Created Successfully", "OK", parentFolder);
		
		log.save("Created New Directory: " + parentFolder.getFolderName() + " at " + parentFolder.getFolderPath());
		directoryService.createDirectoryRecursively(parentFolder, folderRecDto);
		return new Response();
	}

	public ShareLink generateFileShareableLink(String fileId) {
		
		DocumentFile file = documentFileService.getFile(fileId);
		ShareLink shareLink = new ShareLink(file);
		shareLink.setLinkType("Download");
		log.save("File: " + file.getFilePath() + " Download Link generated");
		return shareLinkService.saveLink(shareLink);
	}
	
	public ShareLink generateFolderShareableLink(String folderId) {

		Folder folder = folderService.getFolder(folderId);
		ShareLink shareLink = new ShareLink(folder);
		shareLink.setLinkType("Access");
		log.save("Folder: " + folder.getFolderName() + " Located at " + folder.getFolderPath() + " Access Link generated");
		return shareLinkService.saveLink(shareLink);
	}
	
	public ShareLink generateDownloadFolderShareableLink(String folderId) {
		
		Folder folder = folderService.getFolder(folderId);
		ShareLink shareLink = new ShareLink(folder);
		shareLink.setLinkType("Download");
		log.save("Folder: " + folder.getFolderName() + " Located at " + folder.getFolderPath() + " Download Link generated");
		return shareLinkService.saveLink(shareLink);
	}

	public String accessSharedFolderId(String link) {
		
		ShareLink sharedLink = shareLinkService.findByLinkAndLinkTypeAndDocType(link, "Access", "Folder");
		return sharedLink.getDocId();
	}

	public String accessSharedFileId(String link) {
		ShareLink sharedLink = shareLinkService.findByLinkAndLinkTypeAndDocType(link, "Access", "File");
		return sharedLink.getDocId();
	}
	
	
	public String downloadSharedFolderId(String link) {
		
		ShareLink sharedLink = shareLinkService.findByLinkAndLinkTypeAndDocType(link, "Download", "Folder");
		return sharedLink.getDocId();
	}

	public String downloadSharedFileId(String link) {
		ShareLink sharedLink = shareLinkService.findByLinkAndLinkTypeAndDocType(link, "Download", "File");
		return sharedLink.getDocId();
	}

	public boolean existsShareFileLink(String fileId) {
		return shareLinkService.existsByDocIdAndDocType(fileId, "File");
	}

	public ShareLink getShareFileLink(String fileId) {
		return shareLinkService.findByDocIdAndDocType(fileId, "File");
	}

	public boolean existsShareFolderLink(String folderId) {
		return shareLinkService.existsByDocIdAndDocTypeAndLinkType(folderId, "Folder", "Access");
	}

	public ShareLink getShareFolderLink(String folderId) {
		return shareLinkService.findByDocIdAndDocTypeAndLinkType(folderId, "Folder", "Access");
	}

	public boolean existsDownloadFolderLink(String folderId) {
		return shareLinkService.existsByDocIdAndDocTypeAndLinkType(folderId, "Folder", "Download");
	}

	public ShareLink getDownloadFolderLink(String folderId) {
		return shareLinkService.findByDocIdAndDocTypeAndLinkType(folderId, "Folder", "Download");
	}

	public Object downloadTheFile(DocumentFile file) {
		try {
			log.save("Downloaded File: " + file.getFileName() + " from Location " + file.getFolderPath());
			return downloadFile(file);
		} catch (IOException e) {
			return new Response("File has Some Error", "Error", "Null");
		}
	}

	public Object downloadFolder(String folderId) throws Exception {
		
		if(access.isUser() && !(access.canDownload()))
			return "Access is Denied";
		
		ByteArrayInputStream bis = zippingService.zipDirectory(folderId);
		
		Folder folder = folderService.getFolder(folderId);
		HttpHeaders headers = new HttpHeaders();
		
		String filename = folder.getFolderName() + ".zip";
		headers.add("content-disposition", "inline;filename=" + filename);
	    
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(new InputStreamResource(bis), headers, HttpStatus.OK);
	    log.save("Downloaded Folder Zip: " + filename + " from location " + folder.getFolderPath());
	    return response;
	}

	

	public FolderProperties configFolderProps(FolderProperties folderProps) {
		String folderDir = baseDir + folderProps.getFolderPath() + "/" + folderProps.getFolderName();
		folderProps.setSubFileNum(Long.toString(getFilesCount(new File(folderDir))));
		folderProps.setSubFolderNum(Long.toString(getFoldersCount(new File(folderDir))));
		
		long size = new File(folderDir).length();
		try {
			size = Files.walk(Paths.get(folderDir)).mapToLong( p -> p.toFile().length() ).sum();
		} catch (IOException e) {
		}
		
		folderProps.setFolderSize(Double.toString((size/(1024*1024.0D))) + " MB");
		return folderProps;
	}
	
	public long getFoldersCount(File file) {
		
		  File[] files = file.listFiles();
		  long folderCount = 0;
		  for (File f : files) {
		    if (f.isDirectory()) {
		    	folderCount += getFoldersCount(f);
		    }
		  }
		  return folderCount;
	}

	public long getFilesCount(File file) {
			
		  File[] files = file.listFiles();
		  long fileCount = 0;
		  for (File f : files) {
		    if (f.isDirectory()) {
		    	fileCount += getFilesCount(f);
		    }
		    else
		    	fileCount++;
		  }
		  return fileCount;
	}

	public Map<String, Object> getSearchResults(String folderId, String searchString) {
		
		Map<String, Object> map = new LinkedHashMap<>();
		
		Folder folder = folderService.getFolder(folderId);
		String folderPath = folder.getFolderPath() + "/" + folder.getFolderName();
		List<Folder> folders = folderService.findByFolderPathLikeAndFolderNameLike(folderPath, searchString);
		List<DocumentFile> files = documentFileService.findByFolderPathLikeAndFileNameLike(folderPath, searchString);
		
		map.put("folders", folders);
		map.put("files", files);
		return map;
	}
	
	
	public void FolderTrashOperation(String folderId, String operation) {
		Folder folder = folderService.getFolder(folderId);
		Folder parentFolder = folderService.getFolder(folder.getParentFolderId());
		folder.setIsTrashed(operation);
		for(int i=0; i<parentFolder.getSubFolder().size(); i++) {
			if(parentFolder.getSubFolder().get(i).getFolderId().equals(folderId)) {
				
				parentFolder.getSubFolder().get(i).setIsTrashed(operation);
			}
		}
		folderService.updateFolder(folder);
		folderService.updateParentFolder(parentFolder);
		
		if(operation.equals("true")) log.save("Folder " + folder.getFolderName() + " moved to Trash");
		else log.save("Folder " + folder.getFolderName() + " Restored");
	}
	
	public void FileTrashOperation(String fileId, String operation) {
		DocumentFile file = documentFileService.getFile(fileId);
		Folder parentFolder = folderService.getFolder(file.getParentFolderId());
		file.setIsTrashed(operation);
		for(int i=0; i<parentFolder.getSubDocumentFile().size(); i++) {
			if(parentFolder.getSubDocumentFile().get(i).getDocumentId().equals(fileId)) {
				parentFolder.getSubDocumentFile().get(i).setIsTrashed(operation);
			}
		}
		documentFileService.UpdateFile(file);
		folderService.updateParentFolder(parentFolder);
		if(operation.equals("true")) log.save("File " + file.getFileName() + " moved to Trash");
		else log.save("File " + file.getFileName() + " Restored to System");
	}

	public void DeleteFilePermanent(String fileId) {
		directoryService.deleteFile(fileId);
	}

	public void DeleteFolderPermanent(String folderId) {
		directoryService.deleteDirectoryRecursively(folderId);
	}

	public Map<String, Object> getTrashMap() {
		Map<String, Object> map = new LinkedHashMap<>();
		
		List<Folder> folders = folderService.getTrashedFolders();
		map.put("folders", folders);
		map.put("files", documentFileService.getTrashedFiles());
		return map;
	}

	public void DeletePermanentAll() {
		List<Folder> folders = folderService.getTrashedFolders();
		List<DocumentFile> files = documentFileService.getTrashedFiles();
		
		for(Folder folder: folders) {
			DeleteFolderPermanent(folder.getFolderId());
		}
		for(DocumentFile file: files) {
			DeleteFilePermanent(file.getDocumentId());
		}
	}
	
	public void RestoreAll() {
		List<Folder> folders = folderService.getTrashedFolders();
		List<DocumentFile> files = documentFileService.getTrashedFiles();
		
		for(Folder folder: folders) {
			FolderTrashOperation(folder.getFolderId(), "false");
		}
		for(DocumentFile file: files) {
			FileTrashOperation(file.getDocumentId(), "false");
		}
	}

	public boolean existsFolder(String folderId) {
		return folderService.existsByFolderId(folderId);
	}

	public boolean existsFile(String fileId) {
		return documentFileService.existsFile(fileId);
	}

	public Response lockFolder(Folder folder, String folderId) {
		folderService.lockFolder(folder, folderId);
		log.save(folder.getFolderName()+" Folder Locked at: "+folder.getFolderPath());
		return new Response("Folder Locked", "OK", folder);
	}
	
	public Response lockFile(DocumentFile file, String fileId) {
		folderService.lockFile(file, fileId);
		log.save("File Locked: "+file.getFileName()+" at: "+file.getFilePath());
		return new Response("File Locked", "OK", file);
	}

	public Response unlockFolder(Folder folder, String folderId) {
		folderService.unlockFolder(folder, folderId);
		log.save(folder.getFolderName()+" Folder Locked at: "+folder.getFolderPath());
		return new Response("Folder Unlocked", "OK", folder);
	}
	
	public Response unlockFile(DocumentFile file, String fileId) {
		folderService.unlockFile(file, fileId);
		log.save("File Unlocked: "+file.getFileName()+" at: "+file.getFilePath());
		return new Response("File Unlocked", "OK", file);
	}
	
}
