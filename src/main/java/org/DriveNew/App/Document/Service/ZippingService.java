package org.DriveNew.App.Document.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.DriveNew.App.Document.FileSystem.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZippingService {

	private final String baseDir = "/Home/Talib/CloudStore";
	
	@Autowired
	private FolderService folderService;
	
	public ByteArrayInputStream zipDirectory(String folderId) throws Exception {
	    
		Folder folder = folderService.getFolder(folderId);
		File dirObj = new File(baseDir + folder.getFolderPath() + "/" + folder.getFolderName());
		return new ByteArrayInputStream( zip(dirObj).toByteArray() );
	}

	public ByteArrayOutputStream zip(File directory) throws IOException {
	    URI base = directory.toURI();
	    Deque<File> queue = new LinkedList<File>();
	    queue.push(directory);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    
	    Closeable res = out;
	    try {
	      ZipOutputStream zout = new ZipOutputStream(out);
	      res = zout;
	      while (!queue.isEmpty()) {
	        directory = queue.pop();
	        for (File kid : directory.listFiles()) {
	          String name = base.relativize(kid.toURI()).getPath();
	          if (kid.isDirectory()) {
	            queue.push(kid);
	            
	            name = name.endsWith("/") ? name : name + "/";
	            zout.putNextEntry(new ZipEntry(name));
	          } else {
	            zout.putNextEntry(new ZipEntry(name));
	            FileInputStream in = new FileInputStream(kid);
		        zout.write(Files.readAllBytes(Paths.get(kid.getAbsolutePath())));
	            zout.closeEntry();
	            
	            in.close();
	          }
	        }
	      }
	    } finally {
	      res.close();
	    }
	    return out;
	  }
	
}
