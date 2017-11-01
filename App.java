import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.Border;

class InputStream implements Runnable {
	
	DataInputStream din;
	ChatGUI chat;

	public InputStream(DataInputStream din, ChatGUI chat) {
		this.chat = chat;
		this.din = din;
	}

	@Override
	public void run() {
		try {
			while(true)
				chat.newMessage(din.readUTF());
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

class App {
	public static void main(String args[]) {
		try {
			Socket s = new Socket("pranavtharoor.me", 5003);
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			DataInputStream din = new DataInputStream(s.getInputStream());
			ChatGUI chat = new ChatGUI(dout);
			new Thread(new InputStream(din, chat)).start();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

interface Theme {
	public Color getChatPanelColor();
	public Color getUserPanelColor();
	public Color getInputPanelColor();
	public void setChatPanelColor(Color chatPanelColor);
	public void setUserPanelColor(Color userPanelColor);
	public void setInputPanelColor(Color inputPanelColor);
}

class User implements Serializable, Theme {
	String name;
	Color chatPanelColor;
	Color inputPanelColor;
	Color userPanelColor;
	
	public Color getChatPanelColor() {
		return chatPanelColor;
	}

	public void setChatPanelColor(Color chatPanelColor) {
		this.chatPanelColor = chatPanelColor;
	}
	
	public Color getUserPanelColor() {
		return userPanelColor;
	}

	public void setUserPanelColor(Color userPanelColor) {
		this.userPanelColor = userPanelColor;
	}

	public Color getInputPanelColor() {
		return inputPanelColor;
	}

	public void setInputPanelColor(Color inputPanelColor) {
		this.inputPanelColor = inputPanelColor;
	}
	public User() {
		name = "Anonymous";
	}

	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class SetUserGUI extends JFrame {

	final int FRAME_WIDTH = 300, FRAME_HEIGHT = 210;

	JTextField usernameInput = new JTextField();
	JLabel usernameLabel = new JLabel("Set username");
	JButton userColorButton = new JButton("Change user panel color");
	JButton chatColorButton = new JButton("Change chat panel color");
	JButton inputColorButton = new JButton("Change input panel color");
	JButton saveUserButton = new JButton("Save");
	JPanel mainPanel = new JPanel();
	JPanel usernamePanel = new JPanel();
	JPanel userColorPanel = new JPanel();
	JPanel chatColorPanel = new JPanel();
	JPanel inputColorPanel = new JPanel();
	JPanel savePanel = new JPanel();

	ListenForButton buttonClicked = new ListenForButton();
	ListenForKey keyPressed = new ListenForKey();

	BoxLayout mainPanelBoxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	ChatGUI chatGUI;
	User newUser;

	public SetUserGUI(ChatGUI chatGUI) {

		this.chatGUI = chatGUI;
		newUser = chatGUI.getUser();

		this.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("User");

		saveUserButton.addActionListener(buttonClicked);
		userColorButton.addActionListener(buttonClicked);
		chatColorButton.addActionListener(buttonClicked);
		inputColorButton.addActionListener(buttonClicked);
		usernameInput.addKeyListener(keyPressed);

		usernameInput.setPreferredSize(new Dimension(100, 30));
		usernamePanel.setPreferredSize(new Dimension(FRAME_WIDTH, 35));
		userColorButton.setPreferredSize(new Dimension(FRAME_WIDTH - 30, 25));
		chatColorButton.setPreferredSize(new Dimension(FRAME_WIDTH - 30, 25));
		inputColorButton.setPreferredSize(new Dimension(FRAME_WIDTH - 30, 25));
		
		usernameInput.setText(newUser.getName());

		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameInput);
		userColorPanel.add(userColorButton);
		chatColorPanel.add(chatColorButton);
		inputColorPanel.add(inputColorButton);
		savePanel.add(saveUserButton);
		mainPanel.add(usernamePanel);
		mainPanel.add(userColorPanel);
		mainPanel.add(chatColorPanel);
		mainPanel.add(inputColorPanel);
		mainPanel.add(savePanel);

		this.add(mainPanel);
		this.setVisible(true);
	}

	public class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == saveUserButton) {
				String username = usernameInput.getText();
				if(username.equals("")) username = "Anonymous";
				newUser.setName(username);
				chatGUI.setUser(newUser);
				SetUserGUI.this.dispatchEvent(new WindowEvent(SetUserGUI.this, WindowEvent.WINDOW_CLOSING));
			}

			if(e.getSource() == userColorButton)
				newUser.setUserPanelColor(JColorChooser.showDialog(SetUserGUI.this,"Pick a color", new Color(100, 100, 200)));

			if(e.getSource() == chatColorButton)
				newUser.setChatPanelColor(JColorChooser.showDialog(SetUserGUI.this,"Pick a color", new Color(100, 100, 200)));
			
			if(e.getSource() == inputColorButton)
				newUser.setInputPanelColor(JColorChooser.showDialog(SetUserGUI.this,"Pick a color", new Color(100, 100, 200)));
		}		

	}

	public class ListenForKey implements KeyListener {
		public void keyTyped(KeyEvent e) {}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				saveUserButton.doClick();
		}

		public void keyReleased(KeyEvent e) {}
	}

}

class ChatGUI extends JFrame {

	final int FRAME_WIDTH = 600, FRAME_HEIGHT = 400;

	User currentUser = new User();

	JTextField messageInputField = new JTextField();
	JLabel chatLabel = new JLabel("<html>");
	JLabel userLabel = new JLabel(currentUser.getName());
	JButton sendButton = new JButton("Send");
	JButton userButton = new JButton("User");
	JPanel userPanel = new JPanel();
	JPanel chatPanel = new JPanel();
	JPanel inputPanel = new JPanel();
	JPanel mainPanel = new JPanel();
	JScrollPane chatScrollFrame = new JScrollPane(chatPanel);
	BoxLayout mainPanelBoxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	ListenForButton buttonClicked = new ListenForButton();
	ListenForKey keyPressed = new ListenForKey();

	DataOutputStream dout;

	ObjectOutputStream out;
	ObjectInputStream in;

	public ChatGUI(DataOutputStream dout) {

		this.dout = dout;

		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Chat");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		userLabel.setPreferredSize(new Dimension(FRAME_WIDTH - 90, 30));
		userButton.setPreferredSize(new Dimension(70, 30));
		messageInputField.setPreferredSize(new Dimension(FRAME_WIDTH - 90, 30));
		sendButton.setPreferredSize(new Dimension(70, 30));
		chatScrollFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - 40));

		chatPanel.setAutoscrolls(true);
		chatPanel.setBackground(new Color(200, 200, 200));

		mainPanel.setLayout(mainPanelBoxlayout);

		userButton.addActionListener(buttonClicked);
		sendButton.addActionListener(buttonClicked);

		userPanel.add(userLabel);
		userPanel.add(userButton);
		inputPanel.add(messageInputField);
		inputPanel.add(sendButton);
		chatPanel.add(chatLabel);
		mainPanel.add(userPanel);
		mainPanel.add(chatScrollFrame);
		mainPanel.add(inputPanel);

		messageInputField.addKeyListener(keyPressed);
		this.add(mainPanel);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
				try {
					out.writeObject(currentUser);
					out.flush();
				} catch(Exception e) {
					System.out.println(e);
				}
	        }
	    });

		this.setVisible(true);
		
		try {
			in = new ObjectInputStream(new FileInputStream("user.ser"));
			currentUser = (User) in.readObject();
			in.close();
			userLabel.setText(currentUser.getName());
			userPanel.setBackground(currentUser.getUserPanelColor());
			chatPanel.setBackground(currentUser.getChatPanelColor());
			inputPanel.setBackground(currentUser.getInputPanelColor());
		} catch(Exception e) {
			System.out.println(e);
		}

		try {
			out = new ObjectOutputStream(new FileOutputStream("user.ser"));
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public void setUser(User currentUser) {
		this.currentUser = currentUser;
		userLabel.setText(currentUser.getName());
		userPanel.setBackground(currentUser.getUserPanelColor());
		chatPanel.setBackground(currentUser.getChatPanelColor());
		inputPanel.setBackground(currentUser.getInputPanelColor());
	}

	public User getUser() {
		return currentUser;
	}

	public void newMessage(String message) {
		chatLabel.setText(chatLabel.getText() + "<div align=left width=" + (FRAME_WIDTH - 30) + ">" + message + "</div><br>");
	}

	class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String message;

			if(e.getSource() == sendButton) {
				message = messageInputField.getText();
				if(!message.equals(""))
					try {
						chatLabel.setText(chatLabel.getText() + "<div align=right width=" + (FRAME_WIDTH - 30) + ">" + message + "</div><br>");
						dout.writeUTF("<u>" + currentUser.getName() + "</u>" + "<br>" + message);
					} catch(Exception ex) {
						System.out.println(ex);
					}
				messageInputField.setText("");
			}

			if(e.getSource() == userButton)
				new SetUserGUI(ChatGUI.this);

		}

	}

	public class ListenForKey implements KeyListener {
		public void keyTyped(KeyEvent e) {}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				sendButton.doClick();
		}

		public void keyReleased(KeyEvent e) {}
	}

};