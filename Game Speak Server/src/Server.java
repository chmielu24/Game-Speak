import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import SerializableClass.ServerData;

/**
 *main application class is responsible for action of server
 */
public class Server 
{
	/**
	 * Port to use for create socket
	 */
    private static final int PORT = 9001;

    /**
     * list of channel
     */
    public static ArrayList<Channel> ChannelList = new ArrayList<Channel>();
    
    /**
     * listener is responsible for waits for requests to come in over the network
     * prefer low latency and high bandwidth over short connection time
     */
	public static ServerSocket listener = null;
	
	/**
	 * serverData is responsible for send information about server in network
	 */
	public static ServerData serverData = new ServerData();
	
	/**
	 * initialize server
	 * Creates a server socket, bound to the specified port
	 */
	public Server() throws IOException
	{		
		serverData.channels = Channel.AllChannelsData;
		
        listener = new ServerSocket(PORT);
        listener.setPerformancePreferences(0, 2, 2);
        
    }
	
	/**
	 * add channel to the ChannelList
	 * accept the connection to be made to this socket
	 */
    public void Run() throws IOException
    {
    	try
    	{
			ChannelList.add(new Channel("Default Channel"));	
			ChannelList.add(new Channel("channel 1"));
			ChannelList.add(new Channel("channel 2"));
			ChannelList.add(new Channel("channel 3"));
			ChannelList.add(new Channel("channel 4"));
			ChannelList.add(new Channel("channel 5"));

        	
        	while (true) 
        	{
        		Socket newClientSocket = listener.accept();
        		OnClientConnect(newClientSocket);
            }

    	}
    	finally
    	{
    		Stop();
    	}
    }
    
    /**
     * create client connect - run new thread for client
     * @param s client socket
     */
    public void OnClientConnect(Socket s)
    {
    	System.out.println("Client Connect");
		
		try {        
			Client c = new Client(s);
	        c.start();

		} catch (IOException e) {
			
			e.printStackTrace();
			try {
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
    }
    
    /**
     * stop the work of server:
     * close server socket, stop work of channel
     */
    public void Stop()
    {
    	if(listener != null && !listener.isClosed())
    	{
        	System.out.println("Closing Server Socket...");

			try {
				listener.close();
			} catch (IOException e1) {
				System.err.println("Server socket closing error");
				e1.printStackTrace();
			}
			
        	System.out.println("Disconnecting all Clients");

			for (Channel channel : ChannelList) 
	    	{	
	    		channel.Stop();
	    		
	    	}
			
        	System.out.println("All Clients disconnected");	
        	
        	System.out.println("Server Socket is close");
    	}
    	
    }
    
    /**
     * stop work of server in method finalize
     */
    protected void finalize()
    {
    	System.out.println("finalize Stop Server");
    	Stop();
    	
    }

	
}
