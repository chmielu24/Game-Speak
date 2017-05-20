import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client 
{
	private static int sID=0;
	
	private int ID;
	private String Name;
	
	private Socket socket;
	
	private BufferedReader in;
	private PrintWriter out;
	
	private boolean isConnected = false;
	
	public Client()
	{
		setID(sID);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getID() {
		return ID;
	}

	private void setID(int iD) {
		ID = iD;
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}
	 
	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        
        Connect();
	}
	
	public void Connect() throws IOException
	{
		out.println("SUBMITNAME");
		
		Name = in.readLine();
        if (Name == null) {
        	Disconnect();
        	return;
        }
        
        out.println("NAMEACCEPTED");
        setConnected(true);
        
	}
	
	public void Disconnect()
	{
		WriteMessage("Disconnect");
		
		setConnected(false);
		
		System.out.println("Client " + Name + " disconnect");
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void WriteMessage(String m)
	{
		if(isConnected())
		getOut().println("MESSAGE" + m);
	}

	public boolean isConnected() {
		return isConnected;
	}
	
	private void setConnected(boolean b) {
		this.isConnected = b;
	}

	
}
