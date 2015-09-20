package game;

/**
 * Various weapons can be wielded and used by both the robbers and guards. Most of these will be projectile weapons,
 * but some will have to be used at melee range.
 * @author Lachlan
 *
 */
public class Weapon {

	private String weaponType;
	private Character carrier; //The wielder of the weapon
	private boolean isGun; //If it is a projectile weapon or not
	
	public Weapon(String weaponType, Character carrier, boolean isGun){
		this.weaponType = weaponType;
		this.carrier = carrier;
		this.isGun = isGun;
	}
	
	public String getWeaponType(){
		return weaponType;
	}
	
	public Character getCarrier(){
		return carrier;
	}
	
	public boolean getIsGun(){
		return isGun;
	}
}
