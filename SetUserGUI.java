import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.*;

class SetUserGUI extends JFrame {
	
	// Size of the window
	final int FRAME_WIDTH = 300, FRAME_HEIGHT = 210;

	// Initializing components
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

	// Initializing action and key listeners
	ListenForButton buttonClicked = new ListenForButton();
	ListenForKey keyPressed = new ListenForKey();

	// Main layout in the frame
	BoxLayout mainPanelBoxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	ChatGUI chatGUI;
	User newUser;

	public SetUserGUI(ChatGUI chatGUI) {

		// Initializes objects for yhe current user and chat
		this.chatGUI = chatGUI;
		newUser = chatGUI.getUser();

		// Initial configurations of frame and components
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
		
		// Seting text of username input with current username
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
		// Listens for clicks

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == saveUserButton) {

				// Edits the name of the user
				String username = usernameInput.getText();
				if(username.equals("")) username = "Anonymous";
				newUser.setName(username);

				// Passes the edited user object to the chat frame
				chatGUI.setUser(newUser);

				// Closes window after setting the user
				SetUserGUI.this.dispatchEvent(new WindowEvent(SetUserGUI.this, WindowEvent.WINDOW_CLOSING));
			}

			// Opens a color picker to pick background colors

			if(e.getSource() == userColorButton)
				newUser.setUserPanelColor(JColorChooser.showDialog(SetUserGUI.this,"Pick a color", new Color(100, 100, 200)));

			if(e.getSource() == chatColorButton)
				newUser.setChatPanelColor(JColorChooser.showDialog(SetUserGUI.this,"Pick a color", new Color(100, 100, 200)));

			if(e.getSource() == inputColorButton)
				newUser.setInputPanelColor(JColorChooser.showDialog(SetUserGUI.this,"Pick a color", new Color(100, 100, 200)));
		}		

	}

	public class ListenForKey implements KeyListener {
		// Listens for keys

		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				// Clicks on the save button when enter key is pressed
				saveUserButton.doClick();
		}

		@Override
		public void keyReleased(KeyEvent e) {}
	}

}