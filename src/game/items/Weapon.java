package game.items;

import game.Player;

import java.io.Serializable;

/**
 * Various weapons can be wielded and used by both the robbers and guards. Most of these will be projectile weapons,
 * but some will have to be used at melee range.
 * Not yet in use.
 * @author Lachlan Lee ID# 300281826
 *
 */
public class Weapon implements Serializable {

	private static final long serialVersionUID = 2168163321139030636L;
	private String weaponType;
	private Player carrier; //The wielder of the weapon
	private boolean isGun; //If it is a projectile weapon or not
	
	public Weapon(String weaponType, boolean isGun){
		this.weaponType = weaponType;
		this.isGun = isGun;
	}
	
	public void setCarrier(Player p)
	{
		this.carrier = p;
	}
	
	public String getWeaponType(){
		return weaponType;
	}
	
	public Player getCarrier(){
		return carrier;
	}
	
	public boolean getIsGun(){
		return isGun;
	}
}
