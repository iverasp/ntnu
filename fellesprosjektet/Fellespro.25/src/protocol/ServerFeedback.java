package protocol;

import java.io.Serializable;

public class ServerFeedback implements Serializable {

	public static final int OK = 1;
	public static final int ERROR = 2;
	
	private int type;
	private String event;
	private String message;
	
	public ServerFeedback(int type, String event, String message) {
		this.type = type;
		this.event = event;
		this.message = message;
	}
	
	public int getType() {
		return type;
	}
	
	public String getEvent() {
		return event;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return event + " : " + type + " : " + " : " + message;
	}
	
}
