import java.io.*;
import java.net.*;
import java.util.Scanner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
			while(true) {
				chat.newMessage(din.readUTF());
			}
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
			Thread is = new Thread(new InputStream(din, chat));
			is.start();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

class ChatGUI extends JFrame {

	final int FRAME_WIDTH = 400, FRAME_HEIGHT = 300;

	JTextField messageInputField = new JTextField();
	JLabel chatLabel = new JLabel("<html>");
	JButton sendButton = new JButton("Send");
	JPanel chatPanel = new JPanel();
	JPanel inputPanel = new JPanel();
	JPanel mainPanel = new JPanel();
	JScrollPane chatScrollFrame = new JScrollPane(chatPanel);
	BoxLayout mainPanelBoxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	ListenForButton buttonClicked = new ListenForButton();
	ListenForKey keyPressed = new ListenForKey();

	DataOutputStream dout;

	public ChatGUI(DataOutputStream dout) {

		this.dout = dout;

		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Chat");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		messageInputField.setPreferredSize(new Dimension(FRAME_WIDTH - 90, 30));
		sendButton.setPreferredSize(new Dimension(70, 30));

		chatPanel.setAutoscrolls(true);
		chatScrollFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - 40));

		chatPanel.setBackground(new Color(200, 200, 200));

		mainPanel.setLayout(mainPanelBoxlayout);

		sendButton.addActionListener(buttonClicked);

		inputPanel.add(messageInputField);
		inputPanel.add(sendButton);

		chatPanel.add(chatLabel);

		mainPanel.add(chatScrollFrame);
		mainPanel.add(inputPanel);

		messageInputField.addKeyListener(keyPressed);
		this.add(mainPanel);
		this.setVisible(true);
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
						dout.writeUTF(message);
					} catch(Exception ex) {
						System.out.println(ex);
					}
				messageInputField.setText("");
			}
		}

	}

	public class ListenForKey implements KeyListener {
		public void keyTyped(KeyEvent e) {}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				sendButton.doClick();
			}
		}

		public void keyReleased(KeyEvent e) {}
	}

};