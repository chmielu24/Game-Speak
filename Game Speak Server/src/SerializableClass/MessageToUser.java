package SerializableClass;

import java.io.Serializable;

public class MessageToUser implements Serializable {

	private static final long serialVersionUID = 2054394753227296026L;
	
	public String from;
	public String To;
	public String Message;
	public String Time;

}
