import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

import SerializableClass.*;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_CN;

/**
 * class of client on server
 */

public class Client extends Thread 
{
	private static int sID=0;
	
	/**
	 * Channel ID
	 */
	private int ID;
	
	/**
     * listener is responsible for stores information about data of Clients on server 
     */
	private ClientData clientData = new ClientData();
	
	/**
	 * channel is responsible for handle Channel
	 */
	private Channel channel;

	/**
	 * create new socket of client
	 */
	private Socket socket;
	
	/**
	 * Input stream from socket
	 */
	private ObjectInputStream in;
	/**
	 * Output stream from socket
	 */
	private ObjectOutputStream out;
	
	/**
	 * is responsible for checking authorization of client
	 */
	private boolean IsAutorized = false;
	
	/**
	 * initialize client
	 * @param socket Endpoint for communication between two machines
	 */
	public Client(Socket socket) throws IOException
	{
		setID(sID++);
		
		this.socket = socket;
		
        this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}
	
	/**
	 *  receive messages form server
	 */
	public void run()
	{
		try {	
			while(isConnected() && !isClosed())
			{
				Message input = null;
					
				input = (Message) in.readObject();
			
				if (input == null) {
		        	Disconnect();
		            return;
		        }
				
				HandleMessage(input);
			}
	        
        }catch (EOFException | SocketException e){
        	//Disconnect();
        }
		catch (ClassNotFoundException | IOException e) {
			System.err.println("Client input exception");
			e.printStackTrace();
		}
		finally
		{
			Disconnect();
		}
	}
	
	/**
	 * Disconnecting client:
	 * close socket and quit channel
	 */
	public void Disconnect()
	{
		System.out.println("Client Disconnect " + getClientName());

		
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Client closing socket exception");
			e.printStackTrace();
		}
		finally
		{
			if(channel != null)
				channel.QuitChannel(this);
		}
	}
		
	/**
	 * send message to client
	 * @param m our message
	 * @param type message type
	 */
	public void Message(Object m, MessageType type)
	{
		if(isConnected() && !isClosed())
		{
			try {
				Message msg = new Message();
				msg.messageType = type;
				msg.message = m;
				
				out.writeObject(msg);
				out.flush();
				
			} catch (IOException e) {
				System.err.println("Message to client error");
				e.printStackTrace();
			}
		}
	}
	

	 /**
     * Prompt for and return the desired client name.
     */
	public String getClientName() {
		return getClientData().ClientName;
	}
	
	/**
	 * create new client or set name of existing client
	 * @param name
	 */
	public void setClientName(String name) {
		
		if(IsAutorized)
		{
			channel.LogChat(getClientData().ClientName + " change name to " + name);
			clientData = new ClientData();
			clientData.ClientName = name;
			clientData.ID = getID();
			channel.SendChannelInfo();
		}
		else
		{
			clientData = new ClientData();
			clientData.ClientName = name;
			clientData.ID = getID();		
		}
	}
	
	/**
	 * @return client ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * set client ID
	 */
	public void setID(int iD) {
		ID = iD;
	}
	
	/**
	 * @return socket of client
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * @return state of connection in socket
	 */
	public boolean isConnected() {
		
		return socket.isConnected();
	}
	
	/**
	 * @return return state of close of connection in socket
	 */
	public boolean isClosed() {
		
		return socket.isClosed();
	}
	
	/**
	 * disconnected client
	 */
	protected void finalize()
    {
    	Disconnect();
    }

	/**
	 * @return channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @return clientData
	 */
	public ClientData getClientData()
	{
		return clientData;
	}

	/**
	 * @param channel - channel which set
	 */
	public void setChannel(Channel channel) {	
		
		if(channel == null)
			this.channel = null;
		else
		{
			if(this.channel != null)
			{
				this.channel.QuitChannel(this);
			}
			
			this.channel = channel;
		}
	}
	
	/**
	 * client switch channel (join to the another channel)
	 */
	public void SwitchChannel(ChannelData newChannel)
	{
		if(newChannel.ID == channel.getID())
			return;
		
		for (Channel el : Server.ChannelList) {
			if(el.getID() == newChannel.ID)
			{
				el.JoinChannel(this);
				return;
			}
		}
		
		Message("This channel not exist", MessageType.message);

	}
	
	/**
	 * Handle message form client, in start client must set his nickname
	 * @param m message from client 
	 */
	protected void HandleMessage(Message m)
	{
		try
		{
			if(!IsAutorized)
			{
				if(m.messageType == MessageType.clientData)
				{
					setClientName((String)m.message);	
									
					Message(Server.serverData, MessageType.serverData);
							
			        Server.ChannelList.get(0).JoinChannel(this);
	
					IsAutorized = true;
				}
				
				return;
			}
			
			switch(m.messageType)
			{
			case clientData: setClientName((String)m.message);
				break;
			case serverData: Message(Server.serverData, MessageType.serverData);
				break;
			case disconnect: Disconnect(); 
				break;
			case message: channel.SendTextMessage((String)m.message, this);
				break;
			case changeChannel: SwitchChannel((ChannelData)m.message);
				break;
			case channelInfo: Message(channel.GetClientsOnChannel(), MessageType.channelInfo);
				break;
			case sendMessageToUser: MessageToUser((MessageToUser) m.message);
				break;
			default:
				break;
				
			}
		}
		catch(ClassCastException e)
		{
			System.err.println("Bad message type cannont cast class");
		}
	}
	
	/**
	 * Send private message specific user
	 * @param m message
	 */
	private void MessageToUser(MessageToUser m)
	{
		//System.out.println(m.To);
		//System.out.println(m.Message);

		
		for (Channel channel : Server.ChannelList) {
			for (Client client : channel.Clients) {
				//System.out.println(client.getName());
				if(client.getClientName().equals(m.To))
				{
					m.from = client.getClientName();
					m.Time = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute();

					client.Message(m,  MessageType.sendMessageToUser);
					Message("Send to " + m.To, MessageType.log);
					return;
				}
			}
		}
		
		Message("User " + m.To + " not exist on server", MessageType.log);

	}
	
}
