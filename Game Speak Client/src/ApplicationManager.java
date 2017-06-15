/**
 * Main application class
 */
public class ApplicationManager {
	
	/**
	 * Application Manager is singleton, you can get reference with Instance()
	 */
	private static ApplicationManager singleton;	
	/**
	 * textChat is responsible for communication with user Input and Output
	 */
	public static TextChat textChat;
	/**
	 * channelsList is responsible for drawing channels data from server and switching channel event when user click on channel
	 */
	public static ChannelsList channelsList;
	/**
	 * informationsList is responsible for drawing additional informations to user like a other user on channel
	 */
	public static InformationsList informationsList;
	/**
	 * currentConnection is responsible for start a connection and handling messages
	 */
	public Connection currentConnection = null;

	
	/**
	 * returns reference to ApplicationManager instance
	 */
	public static ApplicationManager Instance()
	{
		return singleton;
	}
	
	
	/**
	 * window is responsible for drawing all content on start
	 */
	private Window window;		
	/**
	 * User nickname
	 */
	private String MyName = "user";
	
	/**
	 * Initialize window, textChat, channelsList and informationsList on start and set default nickname for user
	 */
	public ApplicationManager() {
		
		if(singleton == null)
			singleton = this;
		else
			return;
		
		
		window = new Window();
		textChat = new TextChat(window.getchatOut(), window.getChatIn());
		channelsList = new ChannelsList(window.getChannelsPanel());
		informationsList = new InformationsList(window.getInformationPanel());
				
		SetName("User");
	}
	
	
	/**
	 * Quit from application
	 */
	public void Exit()
	{
		Disconnect();
		
		System.exit(0);
	}
	
	/**
	 * Return user nickname
	 */
	public String GetName()
	{
		return MyName;
	}
	
	/**
	 * Set user nickname and notify server
	 * @param newName New nickname
	 */
	public void SetName(String newName)
	{
		MyName = newName;
		
		if(currentConnection != null)
			currentConnection.ChangeName(GetName());
	}
	
	/**
	 * Open connection
	 * @param c Connection data
	 */
	public void Connect(ConnectionInfo c)
	{
		Disconnect();	
		currentConnection = new Connection(c);
		
		textChat.Clear();
		currentConnection.start();
	}
	
	/**
	 * Disconnect form server
	 */
	public void Disconnect()
	{
		if(currentConnection != null)
		{
			currentConnection.Disconnect();
			currentConnection = null;

	    	ApplicationManager.textChat.writeGreen("Disconnected");
	    	ApplicationManager.channelsList.ClearChannels();
	    	ApplicationManager.informationsList.Clear();
		}
	}
	
}
