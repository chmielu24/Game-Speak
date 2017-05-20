import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;


public class Server 
{
    private static final int PORT = 9001;


    private static ArrayList<Channel> ChannelList = new ArrayList<Channel>();
	public static ServerSocket listener = null;

	
	public Server() throws IOException
	{
        	listener = new ServerSocket(PORT);
	}
	
    public void Run() throws IOException
    {
    	try
    	{
			Channel ch1 = new Channel("Default Channel");
			ChannelList.add(ch1);
			
			Channel ch2 = new Channel("channel 1");
			ChannelList.add(ch2);
        	
        	for (Channel channel : ChannelList) {
				channel.start();
			}
        	
        	while (true) 
        	{
        		Socket newClientSocket = listener.accept();
        		
        		OnClientConnect(newClientSocket);
            }

    	}
    	finally
    	{
    		Close();
    	}
    }
    
    public void OnClientConnect(Socket s)
    {
    	System.out.println("Client Connect");
		
		try {        
			Client c = new Client();
			c.setSocket(s);
	        ChannelList.get(0).JoinChannel(c);

		} catch (IOException e) {
			
			e.printStackTrace();
			try {
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

    }
    
    public void Close()
    {
			try 
			{
		    	if(listener != null)
				listener.close();
		    	
		    	for (Channel channel : ChannelList) 
		    	{	
			    	for (Client c : channel.Clients) 
			    	{
						c.Disconnect();
					}
		    	}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

	
}
