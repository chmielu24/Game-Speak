package SerializableClass;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *stores information about data of clients on channel
 */
public class ClientsOnChannelData implements Serializable {
	private static final long serialVersionUID = 5169167470640422568L;
	public LinkedList<ClientData> clientsOnChannel;
	
	public ClientsOnChannelData()
	{
		clientsOnChannel = new LinkedList<>();
	}

}
