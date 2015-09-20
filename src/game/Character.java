package game;

/**
 * Abstract class containing common methods for Robbers and Guards
 * @author Lachlan
 *
 */
public abstract class Character {

	private Weapon currentWeapon;
	
	public Character(Weapon currentWeapon){
		this.currentWeapon = currentWeapon;
	}
	
	public Weapon getCurrentWeapon(){
		return currentWeapon;
	}
}
