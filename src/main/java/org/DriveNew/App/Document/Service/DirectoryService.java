package org.DriveNew.App.Document.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.DriveNew.App.Document.FileSystem.DocumentFile;
import org.DriveNew.App.Document.FileSystem.Folder;
import org.DriveNew.App.Document.FileSystem.SubDocumentFile;
import org.DriveNew.App.Document.FileSystem.SubFolder;
import org.DriveNew.App.Dto.FilesData;
import org.DriveNew.App.Dto.FolderRecDto;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectoryService {

	@Autowired
	private FolderService folderService;
	
	@Autowired
	private DocumentFileService documentFileService;
	
	@Autowired
	private LogService log;
	
	private final String baseDir = "/Home/Talib/CloudStore";
	
	public void createRootFolder() {
		new File(baseDir).mkdirs();
	}

	public void createDirectoryRecursively(Folder parentFolder, FolderRecDto folderRecDto) {
		
		
		Folder tempParentFolder = parentFolder;
		
		Folder folder = null;
		
		for(FilesData fileData : folderRecDto.getFilesData()) {
			
			String path = fileData.getFilePath();
			String splittedPath[] = path.split("/");
			
			tempParentFolder = folderService.getFolder(parentFolder.getFolderId());
			
			for(int i=0;i< splittedPath.length-1 ;i++) {
				
				if(folderService.existsByFolderPathAndFolderName(tempParentFolder.getFolderPath() + "/" +tempParentFolder.getFolderName(), splittedPath[i])) {
					tempParentFolder = folderService.findByFolderPathAndFolderName(tempParentFolder.getFolderPath() + "/" +tempParentFolder.getFolderName(), splittedPath[i]);
					continue;
				}
				
				
				folder = new Folder(tempParentFolder.getFolderId(), splittedPath[i], tempParentFolder.getFolderPath() + "/" + tempParentFolder.getFolderName());
				folder = folderService.createFolder(folder);
				tempParentFolder.getSubFolder().add(new SubFolder(folder));
				folderService.updateParentFolder(tempParentFolder);
				
				
				tempParentFolder = folder;
			}
			addFileToParentFolder(tempParentFolder,fileData);
		}
	}

	private Folder addFileToParentFolder(Folder tempParentFolder, FilesData fileData) {
		
		DocumentFile docFile = new DocumentFile();
		String strings[] = fileData.getFileObj().getOriginalFilename().split("/");
		String fileName = strings[strings.length-1];
		docFile.setFileName(fileName);
		docFile.setFilePath(tempParentFolder.getFolderPath() + "/" + tempParentFolder.getFolderName() +"/" + fileName);
		docFile.setFileSize(fileData.getFileObj().getSize());
		docFile.setFolderPath(tempParentFolder.getFolderPath() + "/" + tempParentFolder.getFolderName());
		docFile.setParentFolderId(tempParentFolder.getFolderId());
		
		try {
			docFile = documentFileService.saveNewDocument(docFile, fileData.getFileObj());
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
			
		tempParentFolder.getSubDocumentFile().add(new SubDocumentFile(docFile));
		return folderService.updateParentFolder(tempParentFolder);
	}

	public Map<String, String> calculateParentMap(Folder folder) {
		
		Map<String, String> map = new LinkedHashMap<>();
		map.put(folder.getFolderName(), folder.getFolderId());
		Folder parent = folder;
		while(true) {
			
			if(parent.getParentFolderId().contains("ROOT36122FOLDERBASE22163")) 
				break;
			parent = folderService.getFolder(parent.getParentFolderId());
			
			map.put(parent.getFolderName(), parent.getFolderId());
			
		}
		Map<String, String> revMap = new LinkedHashMap<>();
		
		List<String> reverseOrderedKeys = new ArrayList<>(map.keySet());
		Collections.reverse(reverseOrderedKeys);
		reverseOrderedKeys.forEach((key)->revMap.put(key,map.get(key)));
		
		return revMap;
	}

	public DocumentFile deleteFile(String fileId) {
		DocumentFile file = documentFileService.getFile(fileId);
		Folder parentFolder = folderService.getFolder(file.getParentFolderId());
		
		documentFileService.deleteFilePermanant(fileId);
		for(int i=0; i<parentFolder.getSubDocumentFile().size(); i++) {
			if(parentFolder.getSubDocumentFile().get(i).getDocumentId().equals(fileId)) {
				parentFolder.getSubDocumentFile().remove(i);
			}
		}
		
		deleteFileFromDirectory(file);
		folderService.updateParentFolder(parentFolder);
		log.save("File " + file.getFileName() + " permanently Deleted");
		return file;
	}

	private void deleteFileFromDirectory(DocumentFile file) {
		new File(baseDir + file.getFilePath()).deleteOnExit();
	}

	public void deleteDirectoryRecursively(String folderId) {
		
		Folder folder = folderService.getFolder(folderId);
		Folder parentFolder = folderService.getFolder(folder.getParentFolderId());
		folderService.deleteFolderRecursively(folder);
		for(int i=0; i<parentFolder.getSubFolder().size(); i++) {
			if(parentFolder.getSubFolder().get(i).getFolderId().equals(folderId)) {
				parentFolder.getSubFolder().remove(i);
			}
		}
		deleteFolderFromDirectory(folder);
		folderService.updateParentFolder(parentFolder);
		log.save("Folder "+ folder.getFolderName() + " Permanently Deleted");
	}

	private void deleteFolderFromDirectory(Folder folder) {
		try {
			FileUtils.deleteDirectory(new File(baseDir + folder.getFolderPath() + "/" + folder.getFolderName()));
		} catch (IOException e) {
		}
	}
}
