import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import SerializableClass.*;

/**
 * class of channel	
 */
public class Channel 
{
	private static int sID = 0;
	
	/**
	 * channelData is responsible for stores of channel's data
	 */
	private ChannelData channelData;
	
	
	public static ArrayList<ChannelData> AllChannelsData = new ArrayList<>();
	
	
    public List<Client> Clients = new LinkedList<Client>();

    /**
     * initialize channel: 
     * set Id, set name, add channel to List of all channel
     */
	public Channel(String ChannelName)
	{
		this.channelData = new ChannelData();
		this.setID(sID++);
		this.setChannelName(ChannelName);
		
		System.out.println("Channel " + ChannelName + " is on");
		AllChannelsData.add(channelData);
	}
	

	/**
	 * add client to the channel
	 * send message to server
	 * @param c client who join channel
	 */
	public synchronized void JoinChannel(Client c)
	{		
			if(c.getChannel() != null)
				c.getChannel().QuitChannel(c);
				
			
			Clients.add(c);
			
			c.setChannel(this);
			c.Message(this.channelData, MessageType.changeChannel);
			
			System.out.println("JoinChannel: " +c.getClientName() + " connect " + getChannelName());
			
			SendMessage(c.getClientData(), MessageType.UserJoinChannel);
			
	}
	
	/**
	 * remove client from channel
	 * @param c client who is going to remove from channel
	 */
	public synchronized void QuitChannel(Client c)
	{		
			System.out.println("QuitChannel: " + c.getClientName() + " disconnect " + getChannelName());
			SendMessage(c.getClientData(), MessageType.UserDisconnectChannel);
			
			Clients.remove(c);
			c.setChannel(null);
	}
	
	/**
	 * Send message to the all clients of client's list in this channel
	 * @param message message
	 * @param type type of message
	 */
	private synchronized void SendMessage(Object message, MessageType type)
	{
		for (Client o : Clients)
        {
        	o.Message(message, type);
        }
	}
	
	
	/**
	 * send message with information about time and sender
	 * @param client client who send message
	 * @param msg out message
	 */
	public synchronized void SendTextMessage(String msg, Client client)
	{
		MessageWithTime message = new MessageWithTime();
		message.message = msg;
		message.time = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute();
		message.sender = client.getClientName();
		
		SendMessage(message, MessageType.message);
	}
	
	/**
	 * send log Chat message to all clients
	 */
	public synchronized void LogChat(String Log)
	{
		//System.out.println("LogChat:" + Log);	
		SendMessage(Log, MessageType.log);
	}
	
	/**
	 * send message with information about channel
	 */
	public synchronized void SendChannelInfo() {
		
		SendMessage(GetClientsOnChannel(), MessageType.channelInfo);
	}
	
	/**
	 * @return data of clients in channel
	 */
	public synchronized ClientsOnChannelData GetClientsOnChannel()
	{
		ClientsOnChannelData data = new ClientsOnChannelData();
		
		for (Client cl : Clients) {
			data.clientsOnChannel.add(cl.getClientData());
		}
		
		return data;
	}

	/**
	 * stop the work of channel:
	 * disconnected all clients in channel
	 */
	public synchronized void Stop() {
		
    	for (Client c : Clients) 
    	{
    		if (c != null)
			c.Disconnect();
		}
	}
	
	/**
	 * @return channel ID
	 */
	public int getID() {
		return channelData.ID;
	}
	
	/**
	 * @param iD new channel ID
	 */
	private void setID(int iD) {
		channelData.ID = iD;
	}
	
	/**
	 * @return Channel name
	 */
	public String getChannelName() {
		return channelData.ChannelName;
	}

	/**
	 * @param channelName is new channel name
	 */
	public void setChannelName(String channelName) {
		channelData.ChannelName = channelName;
	}
	
	
}
