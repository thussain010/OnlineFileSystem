package org.DriveNew.App.Document.Repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DataRepository {

	public Map<String,String> getPrivileges() {
		
		Map<String,String> privateMap = new HashMap<String,String>();
		privateMap.put("Upload", "Upload");
		privateMap.put("Share", "Share");
		privateMap.put("Download", "Download");
		privateMap.put("FolderLock", "Folder Lock");
		return privateMap;
	}
}
