import java.io.DataInputStream;

class InputStream implements Runnable {
	// Runnable used to run the Input Stream on a seperate thread
	
	DataInputStream din;
	ChatGUI chat;

	public InputStream(DataInputStream din, ChatGUI chat) {
		// Input stream for new messages
		this.din = din;
		this.chat = chat;
	}

	@Override
	public void run() {
		try {
			while(true)
				// Passes new messages to the chat frame
				chat.newMessage(din.readUTF());
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}