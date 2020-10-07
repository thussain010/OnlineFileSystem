package org.DriveNew.App.Document.Service;

import java.util.List;

import org.DriveNew.App.Document.FileSystem.Log;
import org.DriveNew.App.Document.Repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	@Autowired
	private LogRepository logRepository;
	
	
	public List<Log> getLogs() {
		return logRepository.findAll();
	}
	
	public Log save(String remarks) {
		return logRepository.save(new Log(remarks));
	}
}
