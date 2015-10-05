package game.items;

import java.awt.Point;
import java.util.Random;

import game.Room;

public class Safe extends InteractableItem{
	
	private int[] combination;
	public Room room;
	public Point position; //Position in the room
	
	public Safe(Room room, Point position){
		this.room = room;
		this.position = position;
		combination = new int[4];
		generateCombination();
	}

	/**
	 * Generates 4 random integers between 0 and 9, and adds them to the combination array
	 * Forming the combination for the safe
	 */
	private void generateCombination() {
		Random rand = new Random();
		for(int i = 0; i < combination.length; i++){
			combination[i] = rand.nextInt(10);
		}
	}
}
