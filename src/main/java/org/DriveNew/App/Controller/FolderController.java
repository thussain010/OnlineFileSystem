package org.DriveNew.App.Controller;


import java.util.Map;


import org.DriveNew.App.Controller.Service.FolderControllerService;
import org.DriveNew.App.Document.FileSystem.DocumentFile;
import org.DriveNew.App.Document.FileSystem.Folder;
import org.DriveNew.App.Document.FileSystem.Lock;
import org.DriveNew.App.Document.FileSystem.ShareLink;
import org.DriveNew.App.Document.Service.AccessService;
import org.DriveNew.App.Document.Service.FolderService;
import org.DriveNew.App.Dto.CreateFolderDto;
import org.DriveNew.App.Dto.FileProperties;
import org.DriveNew.App.Dto.FolderProperties;
import org.DriveNew.App.Dto.FolderRecDto;
import org.DriveNew.App.Dto.Response;
import org.DriveNew.App.Dto.UploadFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/Folder/Secured")
@PreAuthorize("hasAnyRole('ROLE_USER') or hasAnyRole('ROLE_SUPER_ADMIN')")
public class FolderController {

	@Autowired
	private FolderControllerService folderControllerService;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private AccessService access;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setAutoGrowCollectionLimit(50000);
    }
	
	@PostMapping("/Create/New/Folder")
	public Response createFolder(@RequestBody CreateFolderDto createFolderDto) {
		
		if(access.isUser() && !(access.canUpload()))
			return new Response("Access is Denied", "Invalid", "Denied");
		return folderControllerService.createFolder(createFolderDto);
	}
	
	@PutMapping("/Unlock/Folder/{folderId}")
	public Response unlockFolder(@PathVariable("folderId") String folderId, @RequestBody Folder folder) {
		folder.setIsLocked("false");
		folder.setKey("");
		return folderControllerService.unlockFolder(folder, folderId);
	}
	
	@PutMapping("/Unlock/Folder/File/{fileId}")
	public Response unlockFile(@PathVariable("fileId") String fileId, @RequestBody DocumentFile file) {
		file.setIsLocked("false");
		file.setKey("");
		return folderControllerService.unlockFile(file, fileId);
	}
	
	@PutMapping("/Lock/Folder")
	public Response lockFolder(@RequestBody Lock lock) {
		String key1, key2;
		Folder folder=folderService.getFolder(lock.getItemId());
		try {
			key1=lock.getKey1();
			key2=lock.getKey2();
			if (key1.equals(key2)) {
				folder.setKey(key1);
				folder.setIsLocked("true");
			}else {
				return new Response("Key Entry Mismatch", "Invalid", "Denied");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(access.isUser() && !(access.canUpload()))
			return new Response("Access is Denied", "Invalid", "Denied");
		return folderControllerService.lockFolder(folder, folder.getFolderId());
	}
	
	@PutMapping("/Lock/File")
	public Response lockFile(@RequestBody Lock lock) {
		String key1, key2;
		DocumentFile file=folderControllerService.findDocumentFileById(lock.getItemId());
		try {
			key1=lock.getKey1();
			key2=lock.getKey2();
			if (key1.equals(key2)) {
				file.setKey(key1);
				file.setIsLocked("true");
			}else {
				return new Response("Key Entry Mismatch", "Invalid", "Denied");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(access.isUser() && !(access.canUpload()))
			return new Response("Access is Denied", "Invalid", "Denied");
		return folderControllerService.lockFile(file, file.getDocumentId());
	}
	
	@PostMapping("/Upload/New/Multi/Files")
	public Response uploadNewFile(@RequestParam("uploadFile") MultipartFile[] uploadFile, @RequestParam("") String folderAddress, @RequestParam("") String parentFolderId) {
		UploadFileDto uploadFileDto=new UploadFileDto();
		uploadFileDto.setFolderAddress(folderAddress);
		uploadFileDto.setParentFolderId(parentFolderId);
		uploadFileDto.setUploadFile(uploadFile);
		if(access.isUser() && !(access.canUpload()))
			return new Response("Access is Denied", "Invalid", "Denied");
		if(uploadFileDto.getUploadFile().length>0) {
			return folderControllerService.uploadFile(uploadFileDto);
		}	
		return new Response("File Not Uploaded", "Bad Request", "Improper Data");
	}
	
	@PostMapping("/Upload/New/Folder")
	public Response uploadNewFolder(@ModelAttribute FolderRecDto folderRecDto) {
		
		if(access.isUser() && !(access.canUpload()))
			return new Response("Access is Denied", "Invalid", "Denied");
		
		folderControllerService.UploadFolder(folderRecDto);
		return new Response();
	}
	
	@GetMapping("/Tree/Folder/{folderId}")
	public Response getFolder(@PathVariable("folderId") String folderId) {
		return new Response("Folder Found", "OK", folderControllerService.getFolderMap(folderId));
	}
	
	@GetMapping("/Tree/Folder/File/Current/{fileId}")
	public Response getCurrentFile(@PathVariable("fileId") String fileId) {
		return new Response("Folder Found", "OK", folderControllerService.findDocumentFileById(fileId));
	}
	
	@GetMapping("/Tree/Folder/Current/{folderId}")
	public Response getCurrentFolder(@PathVariable("folderId") String folderId) {
		return new Response("Folder Found", "OK", folderService.getFolder(folderId));
	}
	
	@RequestMapping(value="/Tree/File/{fileId}" ,produces = MediaType.ALL_VALUE, consumes = MediaType.ALL_VALUE)
	public Object getFile(@PathVariable("fileId") String fileId) {
		return folderControllerService.getFile(fileId);
	}

	@GetMapping("/get/Base/FileSystem")
	public Response getBaseFileSystem() {
		return new Response("Root Folder","OK",folderControllerService.getRootFolder());
	}
	
	
	@GetMapping("/get/Properties/Folder/{folderId}")
	public Response getFolderProps(@PathVariable("folderId") String folderId) {
		
		FolderProperties folderProps = new FolderProperties(folderControllerService.getFolder(folderId));
		folderProps = folderControllerService.configFolderProps(folderProps);
		return new Response("Folder Properties", "OK", folderProps);
	}
	
	@GetMapping("/get/Properties/File/{fileId}")
	public Response getFileProps(@PathVariable("fileId") String fileId) {
		
		FileProperties fileProps = new FileProperties(folderControllerService.findDocumentFileById(fileId));
		return new Response("File Properties", "OK", fileProps);
	}
	
	
	/*					Search			*/
	
	@GetMapping("/Folder/{folderId}/Search/{searchString}")
	public Response getSearchResults(@PathVariable("folderId") String folderId, @PathVariable("searchString") String searchString) {
		
		Map<String, Object> map = folderControllerService.getSearchResults(folderId, searchString);
		return new Response("Data Searched", "ok", map);
	}
	
	
	/*					Trash			*/
	
	@GetMapping("/get/Trash")
	public Response getTrashMap() {
		return new Response("Trash Opened", "ok", folderControllerService.getTrashMap());
	}
	
	
	@GetMapping("/Move/Trash/File/{fileId}")
	public Object MoveFileTrash(@PathVariable("fileId") String fileId) {
		
		if(isUser()) 
			return new Response("You do not have Privilege To Delete File", "Invalid", fileId);
		folderControllerService.FileTrashOperation(fileId, "true");
		return new Response("File Moved To Trash", "OK", fileId);
	}
	
	@GetMapping("/Move/Trash/Folder/{folderId}")
	public Object MoveFolderTrash(@PathVariable("folderId") String folderId) {
		
		if(isUser()) 
			return new Response("You do not have Privilege To Delete Folder", "Invalid", folderId);
		folderControllerService.FolderTrashOperation(folderId, "true");
		return new Response("Folder Moved To Trash", "OK", folderId);
	}
	
	@GetMapping("/Restore/File/{fileId}")
	public Object RestoreFile(@PathVariable("fileId") String fileId) {
		
		folderControllerService.FileTrashOperation(fileId, "false");
		return new Response("File Restored To System", "OK", fileId);
	}
	
	@GetMapping("/Restore/Folder/{folderId}")
	public Object RestoreFolder(@PathVariable("folderId") String folderId) {
		
		folderControllerService.FolderTrashOperation(folderId, "false");
		return new Response("Folder Restored To System", "OK", folderId);
	}
	
	@GetMapping("/Delete/Permanent/File/{fileId}")
	public Object DeletePermanentFile(@PathVariable("fileId") String fileId) {
		
		folderControllerService.DeleteFilePermanent(fileId);
		return new Response("File Permanently Deleted from System", "OK", fileId);
	}
	
	@GetMapping("/Delete/Permanent/Folder/{folderId}")
	public Object DeletePermanentFolder(@PathVariable("folderId") String folderId) {
		
		folderControllerService.DeleteFolderPermanent(folderId);
		return new Response("File Permanently Deleted from System", "OK", folderId);
	}
	
	@GetMapping("/Delete/Permanent/All")
	public Object DeletePermanentAll() {
		
		folderControllerService.DeletePermanentAll();
		return new Response("All Files & Folders Restored to File System", "OK", "Done");
	}
	
	@GetMapping("/Restore/All")
	public Object RestoreAll() {
		
		folderControllerService.RestoreAll();
		return new Response("All Files & Folders Restored to File System", "OK", "Done");
	}
	
	
	/*				Download				*/
	
	@GetMapping("/Download/File/{fileId}")
	public Object downloadFile(@PathVariable("fileId") String fileId) {
		return folderControllerService.downloadTheFile(folderControllerService.findDocumentFileById(fileId));
	}
	
	@GetMapping("/Download/Folder/{folderId}")
	public Object downloadFolder(@PathVariable("folderId") String folderId) throws Exception {
		return folderControllerService.downloadFolder(folderId);
	}
	
	/*				Sharable Link				*/
	
	@GetMapping("/Sharable/File/{fileId}")
	public Response getShareableFileLink(@PathVariable("fileId") String fileId) {
		
		if(folderControllerService.existsShareFileLink(fileId))
			return new Response("File Access Link Generated", "OK", folderControllerService.getShareFileLink(fileId));
		
		if(access.isUser() && !(access.canShare()))
			return new Response("Access is Denied", "Invalid", "Denied");
		
		ShareLink shareLink = folderControllerService.generateFileShareableLink(fileId);
		return new Response("File Access Link Generated", "OK", shareLink);
	}
	
	@GetMapping("/Sharable/Folder/{folderId}/Access/Link")
	public Response getShareableFolderLink(@PathVariable("folderId") String folderId) {
		
		if(folderControllerService.existsShareFolderLink(folderId))
			return new Response("Folder Access Link Generated", "OK", folderControllerService.getShareFolderLink(folderId));
		
		if(access.isUser() && !(access.canShare()))
			return new Response("Access is Denied", "Invalid", "Denied");
		
		
		ShareLink shareLink = folderControllerService.generateFolderShareableLink(folderId);
		return new Response("Folder Access Link Generated", "OK", shareLink);
	}
	
	@GetMapping("/Sharable/Folder/Download/Link/{folderId}")
	public Response getShareableDownloadFolderLink(@PathVariable("folderId") String folderId) {
		
		if(folderControllerService.existsDownloadFolderLink(folderId))
			return new Response("Folder Download Link Generated", "OK", folderControllerService.getDownloadFolderLink(folderId));
		
		if(access.isUser() && !(access.canShare()))
			return new Response("Access is Denied", "Invalid", "Denied");
		
		ShareLink shareLink = folderControllerService.generateDownloadFolderShareableLink(folderId);
		return new Response("Folder Download Link Generated", "OK", shareLink);
	}
	
	@GetMapping("/get/FolderId/link/{link}")
	public Response getFolderId(@PathVariable("link") String link) {
		return new Response("Folder Id", "OK", folderControllerService.accessSharedFolderId(link));
	}

	
	
	/*				Shared Link				*/
	
	@GetMapping("/Shared/File/{link}")
	public Object accessSharedFile(@PathVariable("link") String link) {
		
		String fileId = folderControllerService.downloadSharedFileId(link);

		if(access.isUser() && !(access.canDownload()))
			return "Access is Denied";

		
		if(!folderControllerService.existsFile(fileId) || folderControllerService.findDocumentFileById(fileId).getIsTrashed().equals("true"))
			return "Sorry You can't use this Shared Link. The Link is Removed or File Deleted.";
		
		return downloadFile(fileId);
	}
	
	@GetMapping("/Shared/Folder/{link}")
	public Object accessSharedFolder(@PathVariable("link") String link) {
		
		
		
		String folderId = folderControllerService.accessSharedFolderId(link);
		if(!folderControllerService.existsFolder(folderId) || folderControllerService.getFolder(folderId).getIsTrashed().equals("true"))
			return "Sorry You can't use this Shared Link. The Link is Removed or Folder Deleted.";
		ModelAndView mv = null;
		if(isUser()) {
			mv = new ModelAndView("User/UserPanel");
			mv.addObject("userType", "User");
		}
		else {
			mv = new ModelAndView("User/SuperAdminPanel");
			mv.addObject("userType", "SuperAdmin");
		}
		mv.addObject("isAccess","true");
		mv.addObject("folderId", folderId);
		return mv;
	}
	
	private boolean isUser() {
		String authiorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
		if(authiorities.equalsIgnoreCase("[ROLE_SUPER_ADMIN]") || authiorities.equalsIgnoreCase("[ROLE_DRIVER]"))
			return false;
		else return true;
	}
	
	@GetMapping("/Download/Shared/Folder/{link}")
	public Object downloadSharedFolder(@PathVariable("link") String link) throws Exception {
		
		if(access.isUser() && !(access.canDownload()))
			return "Access is Denied";

		
		String folderId = folderControllerService.downloadSharedFolderId(link);
		if(!folderControllerService.existsFolder(folderId)  || folderControllerService.getFolder(folderId).getIsTrashed().equals("true"))
			return "Sorry You can't use this Shared Link. The Link is Removed or Folder Deleted.";
		return downloadFolder(folderId);
	}
}
