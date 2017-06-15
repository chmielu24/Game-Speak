package SerializableClass;

import java.io.Serializable;

/**
 * Stores message and type on this message
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 7938637163285683712L;
	public MessageType messageType;
	public Object message;
}
