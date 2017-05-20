import java.io.IOException;

public class Main {


    public static void main(String[] args) {
    	
        System.out.println("The chat server is running.");
        
        Server s;
		try {
			s = new Server();
			s.Run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
    }

}
