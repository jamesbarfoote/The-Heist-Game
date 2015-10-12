package graphics;

import game.Money;
import game.Player;
import game.Room;
import game.control.gameAction;
import game.control.moveAction;
import game.items.Desk;
import game.items.InteractableItem;
import game.items.Item;
import game.items.Safe;
import game.items.Weapon;
//import networking.Client;
//import networking.Server;
import networking.Client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import data.fileReader;

/**
 * Is the main class in the game. Acts as the controller, taking input, consulting the model
 * and updating the view accordingly. Holds the canvas which constructs the GUI.
 * @author Godfreya, CombuskenKid
 */
public class Main extends JFrame implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	private GraphicsUpdater graphicsUpdater;	//graphical update thread
	private GameCanvas canvas;  	//graphics canvas
	static JLabel movements = new JLabel();
	static JLabel functionality = new JLabel();

	private static final long serialVersionUID = 1l;

	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private static final String MOVE_UP = "move up";
	private static final String MOVE_RIGHT = "move right";
	private static final String MOVE_DOWN = "move down";
	private static final String MOVE_LEFT = "move left";
	private static final String ZOOM_IN = "zoom in";
	private static final String ZOOM_OUT = "zoom out";
	private static final String ROTATE_CLOCKWISE = "rotate clockwise";
	private static final String ROTATE_ANTICLOCKWISE = "rotate anticlockwise";
	private static final String INTERACT_WITH_OBJECT = "interact with object";
	private static final String DROP_MONEY = "drop money";
<<<<<<< HEAD
	private Client cM;

=======
	
>>>>>>> refs/remotes/origin/master
	//private Client cM;
	Room currentRoom;
	Player player;

	public Main(){
		super("The Heist");
		fileReader data = new fileReader("6");

		//Create player
		Player currentPlayer = new Player(new Weapon("Badass", true), 1, new Point(1,1), game.Player.Type.robber);
		//Player player2 = new Player(new Weapon("Badass", true), 1, new Point(6,2), game.Player.Type.robber);
		List<Player> players = new CopyOnWriteArrayList<Player>();
		players.add(currentPlayer);
		//players.add(player2);
		this.player = currentPlayer;

		try {
			cM = new Client(43200, "localhost", currentPlayer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Connect to the server. Change localhost to the actual host computer

		players = cM.getPlayers();
		for(Player p: players)
		{
			if(p.getID() == cM.getID())
			{
				player = p;
			}
		}

		System.out.println("Number of players = " + players.size());
		this.currentRoom = new Room("testRoom", data.getWidth(), data.getHeight(), players);

		Money money = new Money(1000000, currentRoom, new Point(2, 4));
		Money money2 = new Money(1000000, currentRoom, new Point(20, 5));
		Money money3 = new Money(1000000, currentRoom, new Point(23, 6));
		Money money4 = new Money(1000000, currentRoom, new Point(19, 3));
		ArrayList<InteractableItem> deskItems = new ArrayList<InteractableItem>();
		deskItems.add(money);
		currentRoom.addItem(money);
		currentRoom.addItem(money2);
		currentRoom.addItem(money3);
		currentRoom.addItem(money4);
		currentRoom.addItem(new Safe(currentRoom, new Point(4, 7), deskItems));
		currentRoom.addItem(new Desk(currentRoom, new Point(12, 10), deskItems));
		currentRoom.addItem(new Desk(currentRoom, new Point(22, 22), deskItems));

		//Create canvas
		setSize(getToolkit().getScreenSize());
		canvas = new GameCanvas(getSize(), data.getTiles(), currentRoom, cM);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		add(canvas);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(GameCanvas.loadImage("money_bag_icon.png"));

		//go full screen
		setUndecorated(true); 
		pack();
		setVisible(true);
		this.canvas.setDimension(getWidth(), getHeight());
		canvas.requestFocus();

		//start graphics thread running
		graphicsUpdater = new GraphicsUpdater(canvas);
		graphicsUpdater.start();
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

		movements.getActionMap().put(MOVE_UP, new moveAction("Up", this.player, canvas));
		movements.getActionMap().put(MOVE_RIGHT, new moveAction("Right", this.player, canvas));
		movements.getActionMap().put(MOVE_DOWN, new moveAction("Down", this.player, canvas));
		movements.getActionMap().put(MOVE_LEFT, new moveAction("Left", this.player, canvas));

		add(movements);
		/*-------------------Functionality----------------------------*/
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("EQUALS"), ZOOM_IN);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("MINUS"), ZOOM_OUT);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("E"), ROTATE_CLOCKWISE);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("Q"), ROTATE_ANTICLOCKWISE);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("P"), INTERACT_WITH_OBJECT);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("B"), DROP_MONEY);
<<<<<<< HEAD

=======
			
>>>>>>> refs/remotes/origin/master
		functionality.getActionMap().put(ZOOM_IN, new gameAction("=", this.player, canvas));
		functionality.getActionMap().put(ZOOM_OUT, new gameAction("Minus", this.player, canvas));
		functionality.getActionMap().put(ROTATE_CLOCKWISE, new gameAction("E", this.player, canvas));
		functionality.getActionMap().put(ROTATE_ANTICLOCKWISE, new gameAction("Q", this.player, canvas));
<<<<<<< HEAD

		functionality.getActionMap().put(INTERACT_WITH_OBJECT, new gameAction("P", this.player, canvas));
		functionality.getActionMap().put(DROP_MONEY, new gameAction("B", this.player, canvas));

=======
		
		functionality.getActionMap().put(INTERACT_WITH_OBJECT, new gameAction("P", this.player, canvas));
		functionality.getActionMap().put(DROP_MONEY, new gameAction("B", this.player, canvas));
		
>>>>>>> refs/remotes/origin/master
		add(functionality);
	}

	/**
	 * Method for handling keyboard input
	 */
	public void keyPressed(KeyEvent e) {
		canvas.keyPressed(e);
	}

	//------below are various methods for handling mouse input-------

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void mousePressed(MouseEvent e) {}

	public void mouseWheelMoved(MouseWheelEvent e){
		canvas.mouseWheelMoved(e);
	}

	public void mouseReleased(MouseEvent e) {
		canvas.mouseReleased(e);
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public void mouseMoved(MouseEvent e){
		canvas.mouseMoved(new Point(e.getX(), e.getY()));
	}

	public void mouseDragged(MouseEvent e){}

	public GameCanvas getCanvas(){
		return canvas;
	}

	//main method for starting program
	public static void main(String[] args){
		new Main();
	}
}
