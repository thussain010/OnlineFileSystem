package org.DriveNew.App.Document.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.DriveNew.App.Document.Repository.DataRepository;
import org.DriveNew.App.Document.Repository.PrivilegeRepository;
import org.DriveNew.App.Document.User.Privileges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {

	@Autowired
	private PrivilegeRepository privilegeRepository;
	
	@Autowired
	private DataRepository dataRepository;
	
	
	
	public Privileges savePrivilege(Privileges privilege) {
		return privilegeRepository.save(privilege);
	}
	
	public Set<String> initiatePrivileges() {
		
		Map<String,String> map = dataRepository.getPrivileges();
		Set<String> privIds= new HashSet<>();
		for(Map.Entry<String, String> m:map.entrySet()) {
			
			Privileges privilege = new Privileges((String) m.getValue());
			privilege = savePrivilege(privilege);
			privIds.add(privilege.getId());
		}
		return privIds;
	}

	public List<Privileges> getAllPriviliges() {
		return privilegeRepository.findAll();
	}

	public boolean exists(String priv) {
		return privilegeRepository.findByPrivilegeName(priv) == null ? false:true;
	}

	public Privileges getPrivilege(String priv) {
		return privilegeRepository.findByPrivilegeName(priv);
	}
	
	public Privileges getPrivilegeById(String id) {
		return privilegeRepository.findById(id).get();
	}

	public Set<String> getPrivilegeIds(Set<String> privilegesList) {
		
		Set<Privileges> privs = privilegeRepository.findByPrivilegeNameIn(privilegesList);
		Set<String> privIds = new HashSet<>();
		
		for(Privileges p : privs) {
			privIds.add(p.getId());
		}
		return privIds;
	}

}
