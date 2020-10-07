package org.DriveNew.App.Dto;

public class Response {

	private String message;
	private String status;
	private Object obj;
	
	public Response() {
		
	}
	
	public Response(String message, String status, Object obj) {
		this.message = message;
		this.status = status;
		this.obj = obj;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
}
