import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.EOFException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.AdjustmentListener;
import javax.swing.*;

class ChatGUI extends JFrame {

	// Size of the window
	final int FRAME_WIDTH = 600, FRAME_HEIGHT = 400;

	// Holds the current user name and preferences
	User currentUser = new User();

	// Initializing components
	JTextField messageInputField = new JTextField();
	JLabel chatLabel = new JLabel("<html>");
	JLabel userLabel = new JLabel(currentUser.getName());
	JButton sendButton = new JButton("Send");
	JButton settingsButton = new JButton("Settings");
	JPanel userPanel = new JPanel();
	JPanel chatPanel = new JPanel();
	JPanel inputPanel = new JPanel();
	JPanel mainPanel = new JPanel();

	// Scroll for the chat panel on overflow
	JScrollPane chatScrollPane = new JScrollPane(chatPanel);
	JScrollBar chatScrollBar = chatScrollPane.getVerticalScrollBar();
	
	// Main layout in the frame
	BoxLayout mainPanelBoxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	// Initializing action and key listeners
	ListenForButton buttonClicked = new ListenForButton();
	ListenForKey keyPressed = new ListenForKey();

	DataOutputStream dout;

	ObjectOutputStream out;
	ObjectInputStream in;

	public ChatGUI(DataOutputStream dout) {
		// Initializes the output stream for outgoing messages
		this.dout = dout;

		// Initial configurations of frame and components
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Chat");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		userLabel.setPreferredSize(new Dimension(FRAME_WIDTH - 130, 30));
		settingsButton.setPreferredSize(new Dimension(110, 30));
		messageInputField.setPreferredSize(new Dimension(FRAME_WIDTH - 90, 30));
		sendButton.setPreferredSize(new Dimension(70, 30));
		chatScrollPane.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

		chatPanel.setAutoscrolls(true);
		chatPanel.setBackground(new Color(200, 200, 200));

		mainPanel.setLayout(mainPanelBoxlayout);

		settingsButton.addActionListener(buttonClicked);
		sendButton.addActionListener(buttonClicked);

		userPanel.add(userLabel);
		userPanel.add(settingsButton);
		inputPanel.add(messageInputField);
		inputPanel.add(sendButton);
		chatPanel.add(chatLabel);
		mainPanel.add(userPanel);
		mainPanel.add(chatScrollPane);
		mainPanel.add(inputPanel);
		messageInputField.addKeyListener(keyPressed);
		this.add(mainPanel);

		this.addWindowListener(new WindowAdapter() {
			// Listens for window events

			@Override
	        public void windowClosing(WindowEvent winEvt) {
				try {
					// Serialzes user object when window is closed
					out.writeObject(currentUser);
					out.flush();
				} catch(Exception e) {
					System.out.println(e);
				}
	        }

	    });

		chatScrollBar.addAdjustmentListener(new AdjustmentListener() {
			// Listens for adjustments made to the scroll bar

			public void adjustmentValueChanged(AdjustmentEvent e) {
				// Scrolls to the bottom when new message are added
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});

		this.setVisible(true);
		
		try {
			// Deserializes the user object and sets it to currentUser
			in = new ObjectInputStream(new FileInputStream("user.ser"));
			currentUser = (User) in.readObject();
			in.close();

			// Sets name and color preferences to from the current user object
			userLabel.setText(currentUser.getName());
			userPanel.setBackground(currentUser.getUserPanelColor());
			chatPanel.setBackground(currentUser.getChatPanelColor());
			inputPanel.setBackground(currentUser.getInputPanelColor());
		} catch(FileNotFoundException e) {
			// Catches exception and does nothing if the serialization file is not found
		} catch(EOFException e) {
			// Catches exception and does nothing if the serialization file could not be read
		} catch(Exception e) {
			System.out.println(e);
		}

		try {
			// Initializes the object output stream to serialize the user
			out = new ObjectOutputStream(new FileOutputStream("user.ser"));
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public void setUser(User currentUser) {
		// Sets username and color preferrences
		this.currentUser = currentUser;
		userLabel.setText(currentUser.getName());
		userPanel.setBackground(currentUser.getUserPanelColor());
		chatPanel.setBackground(currentUser.getChatPanelColor());
		inputPanel.setBackground(currentUser.getInputPanelColor());
	}

	// Getter for user current user
	public User getUser() {
		return currentUser;
	}

	// Appends incoming messages to the chat
	public void newMessage(String message) {
		chatLabel.setText(chatLabel.getText() + "<div align=left width=" + (FRAME_WIDTH - 30) + ">" + message + "</div><br>");
	}

	class ListenForButton implements ActionListener {
		// Listens for clicks

		@Override
		public void actionPerformed(ActionEvent e) {
			String message;

			if(e.getSource() == sendButton) {
				message = messageInputField.getText();
				if(!message.equals(""))
					try {
						// Sends the outgoig message
						dout.writeUTF("<u>" + currentUser.getName() + "</u>" + "<br>" + message);
						// Appends outgoing messages to the chat
						chatLabel.setText(chatLabel.getText() + "<div align=right width=" + (FRAME_WIDTH - 30) + ">" + message + "</div><br>");
					} catch(Exception ex) {
						// Alerts for broken pipe
						new AlertGUI("Connection lost");
					}
				// Resets the message input field
				messageInputField.setText("");
			}

			if(e.getSource() == settingsButton)
				// Opens the frame to set user preferences
				new SetUserGUI(ChatGUI.this);
		}

	}

	public class ListenForKey implements KeyListener {
		// Listens for keys

		@Override
		public void keyTyped(KeyEvent e) {
			// Does nothing when a key is typed
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				// Clicks on the send button when the enter key is pressed
				sendButton.doClick();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// Does nothing when a key is key is released
		}
	}

};