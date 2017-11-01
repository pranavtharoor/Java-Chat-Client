import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

class App {

	public static void main(String args[]) {
		try {
			// Socket connection to host
			Socket s = new Socket("pranavtharoor.me", 5003);

			// Input and output streams for socket
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			DataInputStream din = new DataInputStream(s.getInputStream());

			// Initializing the GUI for the application
			ChatGUI chat = new ChatGUI(dout);

			// Starts Input Stream in a seperate thread so that it doesn't pause the execution of the program
			new Thread(new InputStream(din, chat)).start();
		} catch(UnknownHostException e) {
			// If the host in not found
			new AlertGUI("Check your internet connection");
		} catch(Exception e) {
			System.out.println(e);
		}
	}

}