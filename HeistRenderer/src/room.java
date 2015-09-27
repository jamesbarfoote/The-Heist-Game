import java.util.ArrayList;

public class room {
	
	ArrayList<player> players = new ArrayList<player>();
	
	public room(ArrayList<player> players) {
		super();
		this.players = players;
	}

	/**
	 * @return the players
	 */
	public ArrayList<player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(ArrayList<player> players) {
		this.players = players;
	}
}