import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import SerializableClass.*;

/**
 * Connection is responsible for open socket, set input and output stream and communicate with server
 */
public class Connection extends Thread {
	
	/**
	 * socket is responsible for endpoint for communication between two machines
	 */
	private Socket socket = null;
	
	/**
	 * connectionInfo is responsible for serialize connection data
	 */
	private ConnectionInfo connectionInfo;
	
	/**
	 * Input stream from socket
	 */
	private ObjectInputStream in;
	
	/**
	* Output stream from socket
	*/
	private ObjectOutputStream out;
    
	/**
	 * IsConnected is responsible for checking state of connection
	 */
	private boolean IsConnected = false;

	private ServerData data;
	private ClientsOnChannelData clientsData;
	private ChannelData currentChannel;
	
	/**
	 * Initialize connection
	 * @param c information about connection
	 */
	public Connection(ConnectionInfo c)
	{			
		connectionInfo = c;
		if(connectionInfo.ServerAdress == null)
			connectionInfo.ServerAdress = "127.0.0.1";
		
		try {
			socket = new Socket(connectionInfo.ServerAdress, connectionInfo.port); 
			this.in = new ObjectInputStream(socket.getInputStream());
	        this.out = new ObjectOutputStream(socket.getOutputStream()); 
			IsConnected = true;
		}
			catch(UnknownHostException e)
	    	{
				ApplicationManager.textChat.write("Unknown Host\n");
	    	}
	    	catch(ConnectException e)
	    	{
	    		ApplicationManager.textChat.write("Cannot connect\n");
	    	}
	    	catch(IOException e)
	    	{
	    		e.printStackTrace();
	    	}
		       
	}
	/**
	 * Close connection
	 */
	public void Disconnect()
	{
		if(IsConnected)
		{
	    	IsConnected = false;

			try {
				if(socket != null)
				socket.close();
			} catch (IOException e) {
				System.err.println("Closing connection errror");
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 *  receive messages form server
	 */
	@Override
	public void run()
	{
		if(socket != null && !socket.isClosed() && socket.isConnected())
		{
			SendMessage(ApplicationManager.Instance().GetName(), MessageType.clientData);
			ApplicationManager.textChat.writeGreen("Connected to " + connectionInfo.ServerAdress);
			
			try 
			{
				while (IsConnected) 
		        {
		          Message message = (Message) in.readObject();
					
		            if(!IsConnected)
		            	break;
		            	
		            if(message == null)
		            {
		            	IsConnected = false;
		            	break;
		            }
		            
		            HandleMessage(message);                     
		        }
			 
			} 
			catch (SocketException | EOFException e) {
				if(IsConnected)
				ApplicationManager.textChat.writeGreen("Server go down!");
			}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally
			{
				ApplicationManager.Instance().Disconnect();
			}
		}
		else
		{
			ApplicationManager.textChat.writeGreen("Cannot connect!");
			ApplicationManager.Instance().Disconnect();

		}
	}
	
	/**
	 * Handle message form server
	 * @param message message from server 
	 */
	private void HandleMessage(Message message)
	{
		switch (message.messageType) {
		case disconnect: ApplicationManager.Instance().Disconnect();
			break;
		case log: PrintLogMessageOnTextChat((String)message.message);
			break;
		case message: PrintMessageOnTextChat((MessageWithTime)message.message);
			break;
		case UserJoinChannel: UserJoinChannel((ClientData)message.message);
			break;
		case UserDisconnectChannel: UserDisconnectChannel((ClientData)message.message);
			break;
		case changeChannel: OnChannelChanged((ChannelData)message.message);
			break;
		case serverData: data = (ServerData) message.message; 
						 ApplicationManager.channelsList.DrawChannels(data);
				break;
		case channelInfo: clientsData = (ClientsOnChannelData)message.message;
						  ApplicationManager.informationsList.ShowInfo(clientsData);
		 		break;
		case sendMessageToUser: ApplicationManager.textChat.write((MessageToUser)message.message);
			break;
		default:
			break;
		}
	}
	
	/**
	 * send message with string
	 * @param text message
	 */
	public void SendTextMessage(String text)
	{
		if(text.contains("/to"))
		{
			MessageToUser m = new MessageToUser();
			m.Message = text.substring(text.lastIndexOf("\"")+1);
			m.To = text.substring(text.indexOf("\"") +1, text.lastIndexOf("\""));
			
			//ApplicationManager.textChat.write(m.To);
			///ApplicationManager.textChat.write(m.Message);
			
			SendMessage(m, MessageType.sendMessageToUser);
		}
		else
		SendMessage(text, MessageType.message);
	}
	
	/**
	 * Send new nickname to server
	 * @param newName new nickname
	 */
	public void ChangeName(String newName)
	{
		SendMessage(newName, MessageType.clientData);
	}
	
	/**
	 * change channel on 
	 * @param newChannel new channel data
	 */
	public void changeChannel(ChannelData newChannel)
	{
		SendMessage((Object)newChannel, MessageType.changeChannel);
	}
	
	/**
	 * send message about join user to channel
	 * @param d Client Data
	 */
	private void UserJoinChannel(ClientData d)
	{
		ApplicationManager.textChat.writeLog(d.ClientName + " Join to " + currentChannel.ChannelName);
		SendMessage(null, MessageType.channelInfo);

	}
	
	/**
	 * send message about disconnected user from channel
	 * @param d Client Data
	 */
	private void UserDisconnectChannel(ClientData d)
	{
		ApplicationManager.textChat.writeLog(d.ClientName + " Quit " + currentChannel.ChannelName);
		SendMessage(null, MessageType.channelInfo);

	}
	
	/**
	 * Write message on textChat
	 */
	private void PrintMessageOnTextChat(MessageWithTime message)
	{
		ApplicationManager.textChat.write(message);

	}
	
	/**
	 * write Log on textChat
	 */
	private void PrintLogMessageOnTextChat(String message)
	{
		ApplicationManager.textChat.writeLog(message);

	}
	
	/**
	 * change channel
	 * @param currentChannel
	 */
	private void OnChannelChanged(ChannelData currentChannel)
	{
		if(this.currentChannel != null)
		ApplicationManager.textChat.Clear();	
		
		ApplicationManager.channelsList.SerCurrentChannel(currentChannel.ID);
		
		this.currentChannel = currentChannel;
	}
	
	/**
	 * send message to server
	 * @param m our message
	 * @param type message type
	 */
	private void SendMessage(Object m, MessageType type)
	{
		Message message = new Message();
		message.messageType = type;
		message.message = m;
		
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			ApplicationManager.textChat.write("Cant send message");
			e.printStackTrace();
		}
	}
}
