package org.DriveNew.App.Document.FileSystem;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Lock {
	private String key1="";
	private String key2="";
	private String itemId="";
	
	public Lock() {}
	public Lock(String key1, String key2, String itemId) {
		super();
		this.key1 = key1;
		this.key2 = key2;
		this.itemId = itemId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getKey1() {
		return key1;
	}
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	public String getKey2() {
		return key2;
	}
	public void setKey2(String key2) {
		this.key2 = key2;
	}
	
}
