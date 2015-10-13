package graphics;

import game.Player;
import game.Room;
import game.control.gameAction;
import game.control.moveAction;
import game.items.Weapon;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

//import networking.Client;
//import networking.Server;
import networking.Client;

/**
 * Is the main class in the game. Acts as the controller, taking input, consulting the model
 * and updating the view accordingly. Holds the canvas which constructs the GUI.
 * @author Godfreya, CombuskenKid, james.barfoote
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
	private static final String MOVE_UP2 = "move up";
	private static final String MOVE_RIGHT2 = "move right";
	private static final String MOVE_DOWN2 = "move down";
	private static final String MOVE_LEFT2 = "move left";
	private static final String ZOOM_IN = "zoom in";
	private static final String ZOOM_OUT = "zoom out";
	private static final String ROTATE_CLOCKWISE = "rotate clockwise";
	private static final String ROTATE_ANTICLOCKWISE = "rotate anticlockwise";
	private static final String INTERACT_WITH_OBJECT = "interact with object";
	private static final String DROP_MONEY = "drop money";
	private Player player; //The current player

	public Main(){
		super("The Heist");

		//Create player
		Player currentPlayer = new Player("Bob", new Weapon("Badass", true), 1, new Point(1,1), game.Player.Type.robber);
		List<Player> players = new CopyOnWriteArrayList<Player>();
		players.add(currentPlayer);
		this.player = currentPlayer;
	
		//Create canvas
		setSize(getToolkit().getScreenSize());
		canvas = new GameCanvas(getSize(), currentPlayer, players);
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
		movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("W"), MOVE_UP2);
		movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("D"), MOVE_RIGHT2);
		movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("S"), MOVE_DOWN2);
		movements.getInputMap(IFW).put(KeyStroke.getKeyStroke("A"), MOVE_LEFT2);

		movements.getActionMap().put(MOVE_UP, new moveAction("Up", this.player, canvas));
		movements.getActionMap().put(MOVE_RIGHT, new moveAction("Right", this.player, canvas));
		movements.getActionMap().put(MOVE_DOWN, new moveAction("Down", this.player, canvas));
		movements.getActionMap().put(MOVE_LEFT, new moveAction("Left", this.player, canvas));
		movements.getActionMap().put(MOVE_UP2, new moveAction("W", this.player, canvas));
		movements.getActionMap().put(MOVE_RIGHT2, new moveAction("D", this.player, canvas));
		movements.getActionMap().put(MOVE_DOWN2, new moveAction("S", this.player, canvas));
		movements.getActionMap().put(MOVE_LEFT2, new moveAction("A", this.player, canvas));

		add(movements);
		/*-------------------Functionality----------------------------*/
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("EQUALS"), ZOOM_IN);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("MINUS"), ZOOM_OUT);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("E"), ROTATE_CLOCKWISE);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("Q"), ROTATE_ANTICLOCKWISE);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("P"), INTERACT_WITH_OBJECT);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("B"), DROP_MONEY);

		functionality.getActionMap().put(ZOOM_IN, new gameAction("=", this.player, canvas));
		functionality.getActionMap().put(ZOOM_OUT, new gameAction("Minus", this.player, canvas));
		functionality.getActionMap().put(ROTATE_CLOCKWISE, new gameAction("E", this.player, canvas));
		functionality.getActionMap().put(ROTATE_ANTICLOCKWISE, new gameAction("Q", this.player, canvas));

		functionality.getActionMap().put(INTERACT_WITH_OBJECT, new gameAction("P", this.player, canvas));
		functionality.getActionMap().put(DROP_MONEY, new gameAction("B", this.player, canvas));

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
