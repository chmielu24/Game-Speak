import java.io.IOException;

/**
 * Create Server
 */
public class Main {

	/**
	 * start function
	 */
    public static void main(String[] args) {
    	
        System.out.println("The chat server is running.");
        
		try {
			new Server().Run();
		} catch (IOException e) {
			System.err.println("Server run error");
			e.printStackTrace();
		}
        
       
    }

}
