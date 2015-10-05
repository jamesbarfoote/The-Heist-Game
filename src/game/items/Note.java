package game.items;

import game.Room;

public class Note extends InteractableItem{

	private String name; //Name of the note in inventory and game dialog
	private String text; //The message written on the note
	private Safe safeForCombo; //The safe the combination refers to, if applicable
	private Room room;
	private Container containedIn; //Container this object is in/on
	
	/**
	 * Constructor for notes that contain messages not related to a safe combination
	 * @param name
	 * @param text
	 * @param room
	 * @param containedIn
	 */
	public Note(String name, String text, Room room, Container containedIn){
		this.name = name;
		this.text = text;
		this.room = room;
		this.containedIn = containedIn;
	}
	
	/**
	 * Constructor for notes containing a safe combination
	 * @param name
	 * @param safeForCombo
	 * @param room
	 * @param containedIn
	 */
	public Note(String name, Safe safeForCombo, Room room, Container containedIn){
		this.name = name;
		this.text = "";
		this.safeForCombo = safeForCombo;
		this.room = room;
		this.containedIn = containedIn;
		setTextAsSafeCombination();
	}

	/**
	 * Sets the notes text to the given safes combination
	 */
	private void setTextAsSafeCombination() {
		text += " " + safeForCombo.getCombination()[0];
		text += " " + safeForCombo.getCombination()[1];
		text += " " + safeForCombo.getCombination()[2];
		text += " " + safeForCombo.getCombination()[3];
	}
}
