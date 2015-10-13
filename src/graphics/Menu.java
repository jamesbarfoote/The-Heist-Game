package graphics;

import networking.Client;

public abstract class Menu extends Dialogue{
	protected Action action;
	
	//possible actions to be taken that must be confirmed
	public enum Action{QUIT, TEXT, IP}
	
	/**
	 * for confirmation dialogues to approve the proposed action
	 */
	public abstract void accept(String data);
	
	/**
	 * for confirmation dialogues to decline the proposed action
	 */
	public abstract void decline();
	
	/**
	 * for setting the game button coordinates for drawing and selecting purposes
	 */
	protected abstract void setButtonCoordinates();

}
