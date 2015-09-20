package game;

/**
 * The start and finish point of the game is the Van. Keeps track of whether or not the Heist gang is allowed to finish the
 * game.
 * @author Lachlan
 *
 */
public class Van {

	private boolean canFinish;
	
	public Van(){
		canFinish = false;
	}
	
	public boolean getCanFinish(){
		return canFinish;
	}
	
	public void setCanFinish(boolean objectiveStatus){
		canFinish = objectiveStatus;
	}
	
	public void finish(){
		if(canFinish == false){  }
		else{
			
		}
	}
}
