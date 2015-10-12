package game.items;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import game.Room;

public class Safe extends Container{
	
	private int[] combination;
	private boolean locked;
	String filename = "_obj_floorSafe.png";
	String direction = "N";
	double[] size = {1.3, 2};
	
	public Safe(Room room, Point position, ArrayList<InteractableItem> items, int money){
		super(room, position, items, money);
		locked = true;
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
	
	public boolean unlockSafe(int[] comboAttempt){
		for(int i = 0; i < combination.length; i++){
			if(comboAttempt[i] != combination[i]){ return false; } //A wrong number was entered
		}
		locked = false; //The attempted combo must be correct, so unlocks the safe
		return true;
	}
	
	public int[] getCombination(){
		return combination;
	}

	public boolean isLocked(){
		return locked;
	}
	
	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}
	
	@Override
	public double[] getSize(){
		return size;
	}
}
