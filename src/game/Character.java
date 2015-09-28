package game;

/**
 * Abstract class containing common methods for Robbers and Guards
 * @author Lachlan
 *
 */
public abstract class Character {

	private Weapon currentWeapon;
	private Room currentRoom;
	private RoomPosition currentPosition;
	
	public Character(Weapon currentWeapon, Room currentRoom, RoomPosition currentPosition){
		this.currentWeapon = currentWeapon;
		this.currentRoom = currentRoom;
		this.currentPosition = currentPosition;
	}
	
	/**
	 * Moves a player 1 tile in a given direction.
	 * Possible directions are left, right, up and down
	 * Ensures you cannot pass through a wall
	 * @param direction
	 */
	public void move(String direction){
		RoomPosition temp = currentPosition; //Temporary object to pass to the move method in Room
		if(direction == "left" && currentPosition.getXPos() > 0){
			currentPosition.decX();
			currentRoom.moveCharacter(currentPosition, temp); //Updates the rooms records with the new position
		}
		else if(direction == "right" && currentPosition.getXPos() < currentRoom.getWidth()-1){
			currentPosition.incX();
			currentRoom.moveCharacter(currentPosition, temp); //Updates the rooms records with the new position
		}
		else if(direction == "up" && currentPosition.getYPos() > 0){
			currentPosition.decY();
			currentRoom.moveCharacter(currentPosition, temp); //Updates the rooms records with the new position
		}
		else if(direction == "down" && currentPosition.getYPos() < currentRoom.getHeight()-1){
			currentPosition.incY();
			currentRoom.moveCharacter(currentPosition, temp); //Updates the rooms records with the new position
		}
	}
	
	/**
	 * Moves the character into the room on the other side of the door they are entering.
	 * Updates the currentRoom and currentPosition fields to reflect this change of position
	 * @param d - the door the character is passing through
	 */
	public void moveToNextRoom(Door d){
		if(d.getRoom1().equals(currentRoom)){
			currentRoom = d.getRoom2();
			currentPosition = d.getRoom2Entry();
			d.getRoom1().removeCharacter(d.getRoom1Entry()); //Removes the character from the old rooms records
			d.getRoom2().addCharacter(d.getRoom2Entry()); //Adds the character to the new rooms records
		}
		else{
			currentRoom = d.getRoom1();
			currentPosition = d.getRoom1Entry();
			d.getRoom2().removeCharacter(d.getRoom2Entry()); //Removes the character from the old rooms records
			d.getRoom1().addCharacter(d.getRoom1Entry()); //Adds the character to the new rooms records
		}
	}
	
	public Weapon getCurrentWeapon(){
		return currentWeapon;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public RoomPosition getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(RoomPosition currentPosition) {
		this.currentPosition = currentPosition;
	}
}
