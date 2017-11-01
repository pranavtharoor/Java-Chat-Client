import java.awt.Color;
import java.io.Serializable;

class User implements Serializable {
	// The user object gets saved to a file when the program is closed

	String name;
	Color chatPanelColor;
	Color inputPanelColor;
	Color userPanelColor;

	// Sets the name as Anonymous if the name is not specified 
	
	public User() {
		name = "Anonymous";
	}

	public User(String name) {
		this.name = name;
	}

	// Getters and setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
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

}