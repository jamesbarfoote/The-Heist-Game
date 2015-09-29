package game;

import java.awt.Point;

/**
 * NPC Guards that inhibit the robbers from progressing, either by blocking their path, or attacking them wit their weapon
 * @author Lachlan
 *
 */
public class Guard extends Character{

	public String type; //The type of guard this guard is
	
	public Guard(String type,/* Weapon currentWeapon, Room currentRoom,*/ Point currentPosition){
		super(/*currentWeapon, currentRoom,*/ currentPosition);
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
}