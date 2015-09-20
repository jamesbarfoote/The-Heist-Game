package game;

/**
 * NPC Guards that inhibit the robbers from progressing, either by blocking their path, or attacking them wit their weapon
 * @author Lachlan
 *
 */
public class Guard extends Character{

	public String type; //The type of guard this guard is
	
	public Guard(String type, Weapon currentWeapon){
		super(currentWeapon);
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
}
