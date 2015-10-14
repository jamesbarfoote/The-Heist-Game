package game.items;

import java.awt.Point;
import java.io.Serializable;

import game.Room;

<<<<<<< HEAD
public class Note extends InteractableItem implements Serializable{
=======
/**
 * A note that contains a safe combination for unlocking a safe
 * Not currently in use as there is no unlocking minigame and therefore an equivalent for keys is
 * being used currently
 * @author Lachlan Lee ID# 300281826
 *
 */
public class Note extends InteractableItem{
>>>>>>> bed7984ce3299a7ee074f7ac9b32e0ce43d8ade9

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name; //Name of the note in inventory and game dialog
	private String text; //The message written on the note
	private Safe safeForCombo; //The safe the combination refers to, if applicable
	private Container containedIn; //Container this object is in/on
	
	/**
	 * Constructor for notes that contain messages not related to a safe combination
	 * @param name
	 * @param text
	 * @param room
	 * @param containedIn
	 */
	public Note(String name, String text, Room room, Container containedIn, Point position){
		super(position);
		this.name = name;
		this.text = text;
		this.containedIn = containedIn;
	}
	
	/**
	 * Constructor for notes containing a safe combination
	 * @param name
	 * @param safeForCombo
	 * @param room
	 * @param containedIn
	 */
	public Note(String name, Safe safeForCombo, Room room, Container containedIn, Point position){
		super(position);
		this.name = name;
		this.text = "";
		this.safeForCombo = safeForCombo;
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
	
	public String getName(){
		return name;
	}
	
	public String getText(){
		return text;
	}
	
	public Safe getSafeForCombo(){
		return safeForCombo;
	}
	
	public Container getContainedIn(){
		return containedIn;
	}

	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOldPosition(Point oldLocation) {
		
	}
	
	@Override
	public void setPosition(Point newLocation){
		
	}
}
