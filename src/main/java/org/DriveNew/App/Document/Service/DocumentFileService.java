package org.DriveNew.App.Document.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.DriveNew.App.Document.FileSystem.DocumentFile;
import org.DriveNew.App.Document.Repository.DocumentFileRepository;
import org.DriveNew.App.Dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentFileService {

	@Autowired
	private DocumentFileRepository documentFileRepository;

	
	final String baseDir = "/Home/Talib/CloudStore";
	
	public DocumentFile saveNewDocument(DocumentFile docFile, MultipartFile file) throws IllegalStateException, IOException {
		try {
			long size=file.getSize();
			String filePath=docFile.getFilePath();
			String folderPath=docFile.getFolderPath();
			Logger logger=LoggerFactory.getLogger(this.getClass());
			logger.warn("FILEPATH::"+filePath+"::FOLDER::"+folderPath+"::SIZE::"+size+"ORIGINALFILENAME::"+file.getOriginalFilename());
			File myFile=new File(baseDir + folderPath);
			try {
				if(!myFile.exists()) {
					myFile.mkdirs();
				}
			} catch (SecurityException e) {
				// TODO: handle exception
				e.printStackTrace();
				new Response("Directory not available on server", "SecurityError", myFile);
			}
			String[] filename;
			int length=0;
			logger.warn("INDEXOF SLASH::"+file.getOriginalFilename().indexOf("/"));
			if (file.getOriginalFilename().indexOf("/")!=-1) {
				filename=file.getOriginalFilename().split("/");
				length=filename.length;
				logger.warn("LENGTH OF FILENAME ARRAY::"+length);
				if (length>0) {
					logger.warn(myFile.getAbsolutePath()+"/"+filename[length-1]+"MY ABSOLUTE PATH CREATED:::if");
					file.transferTo(new File(myFile.getAbsolutePath() +"/"+filename[length-1]));
				}
			}else {
				logger.warn(myFile.getAbsolutePath()+"/"+file.getOriginalFilename()+"MY ABSOLUTE PATH CREATED:::else");
				file.transferTo(new File(myFile.getAbsolutePath() +"/"+file.getOriginalFilename()));
			}
			//logger.warn(myFile.getAbsolutePath()+"/"+filename[length-1]+"MY ABSOLUTE PATH CREATED:::");
			//file.transferTo(new File(myFile.getAbsolutePath() +"/"+file.getOriginalFilename()));
		}
		catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			new Response("File path not available", "FileNotFound", e.getMessage());
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			new Response("File or Folder not uploaded properly", "Error", file);
		}
		
		return documentFileRepository.save(docFile);
	}

	public DocumentFile getFile(String fileId) {
		return documentFileRepository.findById(fileId).get();
	}

	public List<DocumentFile> findByFolderPathLikeAndFileNameLike(String folderPath, String searchString) {
		return documentFileRepository.findByFolderPathLikeAndFileNameLike(folderPath, searchString);
	}

	public DocumentFile UpdateFile(DocumentFile file) {
		return documentFileRepository.save(file);
	}

	public void deleteFilePermanant(String fileId) {
		documentFileRepository.deleteById(fileId);
	}

	public void deleteAllByFolderPathLike(String folderPath) {
		documentFileRepository.deleteAllByFolderPathLike(folderPath);
	}

	public List<DocumentFile> getTrashedFiles() {
		return documentFileRepository.findAllByIsTrashed("true");
	}

	public boolean existsFile(String fileId) {
		return documentFileRepository.existsById(fileId);
	}
}
