package game.items;

import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

<<<<<<< HEAD
public class Safe extends Container implements Serializable{
=======
/**
 * A container that may or may not be locked that contains a large sum of money
 * Unlocked with a Safe combination which is currently just a key
 * Does not yet have unlocking minigame, so no safe specific combinations
 * @author Lachlan Lee ID# 300281826
 *
 */
public class Safe extends Container{
>>>>>>> bed7984ce3299a7ee074f7ac9b32e0ce43d8ade9
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] combination;
	private boolean locked;
	String filename = "_obj_floorSafe.png";
	String direction = "N";
	double[] size = {1.3, 2};
	
	public Safe(Point position, int money, boolean locked){
		super(position, null, money);
		this.locked = locked;
		combination = new int[4];
		//generateCombination();
	}

//	/**
//	 * Generates 4 random integers between 0 and 9, and adds them to the combination array
//	 * Forming the combination for the safe
//	 */
//	private void generateCombination() {
//		Random rand = new Random();
//		for(int i = 0; i < combination.length; i++){
//			combination[i] = rand.nextInt(10);
//		}
//	}
//	
//	public boolean unlockSafe(int[] comboAttempt){
//		for(int i = 0; i < combination.length; i++){
//			if(comboAttempt[i] != combination[i]){ return false; } //A wrong number was entered
//		}
//		locked = false; //The attempted combo must be correct, so unlocks the safe
//		return true;
//	}
	
	public void unlock() {
		locked = false;
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
