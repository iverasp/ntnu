package protocol;

import java.io.Serializable;

public class ClientRequest implements Serializable {
	
	private String request;
	private Serializable data;
	
	public ClientRequest(String request, Serializable data) {
		this.request = request;
		this.data = data;
	}
	
	public ClientRequest(String request) {
		this.request = request;
	}
	
	public String getRequest() {
		return request;
	}
	
	public Serializable getData() {
		return data;
	}
	
	public String toString() {
		return request + " : " + data;
	}
}
