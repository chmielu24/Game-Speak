import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Channel extends Thread 
{
	private static int sID = 0;
	
	private int ID;
	private String ChannelName;
	
    
    public List<Client> Clients = new LinkedList<Client>();
    private Queue<Client> JoinClientsQueue = new LinkedList<Client>();

    
	public Channel(String ChannelName)
	{
		ID = sID++;
		this.setChannelName(ChannelName);
	}
	
	public String getChannelName() {
		return ChannelName;
	}

	public void setChannelName(String channelName) {
		ChannelName = channelName;
	}
	
	
	public void JoinChannel(Client c)
	{		
		synchronized(JoinClientsQueue)
		{
			JoinClientsQueue.add(c);
		}
	}
	

	private void ChannelManager()
	{
			try {
				TextChat();
				
				synchronized(JoinClientsQueue)
				{
					while(!JoinClientsQueue.isEmpty())
					{
						Client c = JoinClientsQueue.poll();
						c.WriteMessage("You are talking in " + ChannelName);
						Clients.add(c);
						System.out.println(c.getName() + " join to " + ChannelName);
						System.out.println(c.isConnected() + " " + Clients.size());
					}
				}
				
				for (Client client : Clients) {
					if(client.isConnected() == false)
						Clients.remove(client);
					}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	private void TextChat() throws IOException
	{
		for (Client client : Clients) 
		{
				if(!client.isConnected())
				{
					Clients.remove(client);
					continue;
				}
				
				if(client.getIn().ready())
				{
					String input = client.getIn().readLine();
					
	                if (input == null) {
	                    return;
	                }     
	                
					System.out.println(client.getName() + ": " + input);
	
				
		            for (Client o : Clients)
		            {
		            		o.WriteMessage(client.getName() + ": " + input);
		            }
				}
            
		}
	}
	
	public void run()
	{

		System.out.println("Channel " + ChannelName + " is on");

		
		while(true)
		ChannelManager();		

		//System.out.println("Channel " + ChannelName + " is off");


	}
}
