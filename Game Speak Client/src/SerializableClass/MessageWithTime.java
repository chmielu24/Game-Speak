package SerializableClass;

import java.io.Serializable;

/**
 *stores information about message with time and sender
 */

public class MessageWithTime implements Serializable {

	private static final long serialVersionUID = -6658251995299946392L;
	public String message;
	public String time;
	public String sender;


}
