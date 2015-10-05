package game.items;

import java.awt.Point;
import java.util.Random;

import game.Money;
import game.Room;

public class Safe extends Container{
	
	private int[] combination;
	private Money cashInSafe;
	
	public Safe(Room room, Point position, Money cashInSafe){
		super(room, position);
		this.cashInSafe = cashInSafe;
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
	
	public int[] getCombination(){
		return combination;
	}
	
	/**
	 * Returns the integer value of the Money object contained in the safe
	 * @return
	 */
	public int getMoney(){
		return cashInSafe.getAmount();
	}
}
