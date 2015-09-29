package graphics;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import control.GraphicUpdateThread;
import data.fileReader;
import control.moveAction;
import control.gameAction;
import game.Player;
import game.Room;
import game.Weapon;

/**
 * Creates the game application window which displays the gui
 * @author godfreya
 */
public class GameFrame extends JFrame{
	private GraphicUpdateThread ck; //graphical update thread
	private GameCanvas canvas;  //graphics canvas
	static JLabel movements = new JLabel();
	static JLabel functionality = new JLabel();
	
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private static final String MOVE_UP = "move up";
	private static final String MOVE_RIGHT = "move right";
	private static final String MOVE_DOWN = "move down";
	private static final String MOVE_LEFT = "move left";
	private static final String ZOOM_IN = "zoom in";
	private static final String ZOOM_OUT = "zoom out";
	
	Room currentRoom;
	Player player;

	public GameFrame(){
		super("The Heist");
		fileReader data = new fileReader();
		player = new Player(new Weapon("Badass", true), 1, new Point(0,0), game.Player.Type.robber);
		Player player2 = new Player(new Weapon("Badass", true), 1, new Point(8,1), game.Player.Type.robber);
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(player);
		players.add(player2);
		currentRoom = new Room("testRoom", data.getWidth(), data.getHeight(), players);
		canvas = new GameCanvas(data.getTiles(), currentRoom);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//go full screen
		setUndecorated(true); 
		setIconImage(GameCanvas.loadImage("money_bag_icon.png"));
		pack();
		
		//go full screen
		setExtendedState(MAXIMIZED_BOTH); 
		//start graphics thread running
		ck = new GraphicUpdateThread(this);
		ck.start();
		
		//make us visible
		setVisible(true);
		this.canvas.setDimension(getWidth(), getHeight());
		canvas.requestFocus();
		keyBindings();
	}

	 /**
		 * Binds the given keyboard inputs to actions so pressing the key calls
		 * a new move action.
		 */
		private void keyBindings() {
			/*---------------------Movements------------------------------*/
			movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), MOVE_UP);
			movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), MOVE_RIGHT);
			movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), MOVE_DOWN);
			movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), MOVE_LEFT);
			
			movements.getActionMap().put(MOVE_UP, new moveAction("Up", player, canvas));
			movements.getActionMap().put(MOVE_RIGHT, new moveAction("Right", player, canvas));
			movements.getActionMap().put(MOVE_DOWN, new moveAction("Down", player, canvas));
			movements.getActionMap().put(MOVE_LEFT, new moveAction("Left", player, canvas));
			
			add(movements);
			/*-------------------Functionality----------------------------*/
			functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("EQUALS"), ZOOM_IN);
			functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("MINUS"), ZOOM_OUT);
			
			functionality.getActionMap().put(ZOOM_IN, new gameAction("=", player, canvas));
			functionality.getActionMap().put(ZOOM_OUT, new gameAction("Minus", player, canvas));
			
			add(functionality);
		}
	
	public void repaint(){
		canvas.repaint();
	}
	
	public GameCanvas getCanvas(){
		return canvas;
	}
}
