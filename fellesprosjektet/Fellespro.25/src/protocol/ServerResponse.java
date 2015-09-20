package protocol;

import java.io.Serializable;

public class ServerResponse implements Serializable {

	private String response;
	private Serializable data;
	
	public ServerResponse(String response, Serializable data) {
		this.response = response;
		this.data = data;
	}
	
	public ServerResponse(String response) {
		this.response = response;
	}
	
	public String getResponse() {
		return response;
	}
	
	public Serializable getData() {
		return data;
	}
	
	public String toString() {
		return response + " : " + data;
	}
}
