package graphics;

public abstract class Menu extends Dialogue{
	protected Action action;
	
	public enum Action{QUIT}
	
	/**
	 * for confirmation dialogues to approve the proposed action
	 */
	public abstract void accept();
	
	/**
	 * for confirmation dialogues to decline the proposed action
	 */
	public abstract void decline();

}
