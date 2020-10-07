package org.DriveNew.App.Document.Service;

import java.io.File;
import java.util.List;

import org.DriveNew.App.Document.FileSystem.DocumentFile;
import org.DriveNew.App.Document.FileSystem.Folder;
import org.DriveNew.App.Document.FileSystem.SubDocumentFile;
import org.DriveNew.App.Document.FileSystem.SubFolder;
import org.DriveNew.App.Document.Repository.DocumentFileRepository;
import org.DriveNew.App.Document.Repository.FolderRepository;
import org.DriveNew.App.Dto.CreateFolderDto;
import org.DriveNew.App.Dto.FolderRecDto;
import org.DriveNew.App.Dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {
	
	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private DocumentFileRepository documentFileRepository;
	
	@Autowired
	private DirectoryService dirService;
	
	@Autowired
	private DocumentFileService documentFileService;
	
	final String baseDir = "/home/Talib/DriveDir";
	
	
	public Folder createRootDirectory() {

		Folder folder = new Folder("root","");
		folder.setParentFolderId("ROOT36122FOLDERBASE22163");
		dirService.createRootFolder();
		return folderRepository.save(folder);
	}
	
	public Folder getRootFolder() {
		return folderRepository.findByParentFolderId("ROOT36122FOLDERBASE22163");
	}

	public Folder getFolder(String folderId) {
		return folderRepository.findById(folderId).get();
	}

	public Folder createFolder(Folder folder) {
		new File(baseDir+folder.getFolderPath() +"/" + folder.getFolderName()).mkdirs();
		return folderRepository.save(folder);
	}

	public boolean isFolderNameExists(CreateFolderDto createFolderDto) {
		
		Folder parentFolder = getFolder(createFolderDto.getParentFolderId());
		
		for(SubFolder subFolder : parentFolder.getSubFolder()) {
			
			if(subFolder.getFolderName().equalsIgnoreCase(createFolderDto.getFolderName())) {
				return true;
			}
		}
		return false;
	}

	public Folder updateParentFolder(Folder parentFolder) {
		return folderRepository.save(parentFolder);
	}

	public boolean isFileNameExists(String parentFolderId, String fileName) {
		Folder parentFolder = getFolder(parentFolderId);
		
		for(SubDocumentFile subFile : parentFolder.getSubDocumentFile()) {
			
			if(subFile.getDocumentName().equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}
	

	public Folder createUploadFolderParent(FolderRecDto folderRecDto) {
		
		Folder parentFolder = getFolder(folderRecDto.getParentFolderId());
		if(folderRepository.existsByFolderPathAndFolderName(parentFolder.getFolderPath() +"/" + parentFolder.getFolderName() , folderRecDto.getFolderName()))
			return null;
		
		Folder newCreatedParent =  new Folder(parentFolder.getFolderId(),folderRecDto.getFolderName(), parentFolder.getFolderPath()+ "/" + parentFolder.getFolderName());
		newCreatedParent = createFolder(newCreatedParent);
		
		parentFolder.getSubFolder().add(new SubFolder(newCreatedParent.getFolderId(),newCreatedParent.getFolderName()));
		updateParentFolder(parentFolder);
		return newCreatedParent;
	}
	
	public boolean existsByFolderPathAndFolderName(String folderPath, String folderName) {
		return folderRepository.existsByFolderPathAndFolderName(folderPath, folderName);
	}
	
	public Folder findByFolderPathAndFolderName(String folderPath, String folderName) {
		return folderRepository.findByFolderPathAndFolderName(folderPath, folderName);
	}

	public Folder updateFolder(Folder folder) {
		return folderRepository.save(folder);
	}

	public List<Folder> findByFolderPathLikeAndFolderNameLike(String folderPath, String searchString) {
		return folderRepository.findByFolderPathLikeAndFolderNameLike(folderPath, searchString);
	}

	public void deleteFolderRecursively(Folder folder) {
		folderRepository.delete(folder);
		folderRepository.deleteAllByFolderPathLike(folder.getFolderPath() +"/"+ folder.getFolderName());
		documentFileService.deleteAllByFolderPathLike(folder.getFolderPath() +"/"+ folder.getFolderName());
	}
	
	public List<Folder> getTrashedFolders() {
		return folderRepository.findAllByIsTrashed("true");
	}
	
	public List<Folder> getUnTrashedFolders() {
		return folderRepository.findAllByIsTrashed("false");
	}

	public boolean existsByFolderId(String folderId) {
		return folderRepository.existsById(folderId);
	}

	public Response lockFolder(Folder folder, String folderId) {
		// TODO Auto-generated method stub
		 folderRepository.save(folder);
		 return new Response("LOCKED", "OK", folder);
	}

	public Response unlockFolder(Folder folder, String folderId) {
		// TODO Auto-generated method stub
		folderRepository.save(folder);
		 return new Response("UNLOCKED", "OK", folder);
	}

	public Response lockFile(DocumentFile file, String fileId) {
		// TODO Auto-generated method stub
		documentFileRepository.save(file);
		return new Response("LOCKED", "OK", file);
	}
	
	public Response unlockFile(DocumentFile file, String fileId) {
		// TODO Auto-generated method stub
		documentFileRepository.save(file);
		 return new Response("UNLOCKED", "OK", file);
	}
}
