package SerializableClass;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * stores information about data of server
 */
public class ServerData implements Serializable{
	private static final long serialVersionUID = -4567537673436985635L;
	public ArrayList<ChannelData> channels;
	
}
