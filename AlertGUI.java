import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.*;

class AlertGUI extends JFrame {

	// Size of the window
	final int FRAME_WIDTH = 300, FRAME_HEIGHT = 170;

	// Initializing components
	JLabel alertLabel = new JLabel("Set username");
	JButton okButton = new JButton("OK");
	JPanel mainPanel = new JPanel();
	JPanel okPanel = new JPanel();
	JPanel alertPanel = new JPanel();

	// Initializing action listener
	ListenForButton buttonClicked = new ListenForButton();

	// Main layout in the frame
	BoxLayout mainPanelBoxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

	public AlertGUI(String alert) {
		// Initial configurations of frame and components
		this.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Alert");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		alertLabel.setText(alert);
		alertPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
		okButton.addActionListener(buttonClicked);
		alertPanel.add(alertLabel);
		okPanel.add(okButton);
		mainPanel.setLayout(mainPanelBoxlayout);
		mainPanel.add(alertPanel);
		mainPanel.add(okPanel);

		this.add(mainPanel);
		this.setVisible(true);
	}

	public class ListenForButton implements ActionListener {
		// Listens for clicks

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == okButton) {
				// Closes frame when ok button is clicked
				AlertGUI.this.dispatchEvent(new WindowEvent(AlertGUI.this, WindowEvent.WINDOW_CLOSING));

				// Ends the program
				System.exit(0);
			}
		}		

	}

}