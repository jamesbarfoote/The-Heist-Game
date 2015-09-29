package graphics;

import game.Player;
import game.Room;
import game.control.gameAction;
import game.control.moveAction;
import game.items.Weapon;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

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
public class Main extends JFrame implements KeyListener, MouseListener, MouseMotionListener{
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
	
	Room currentRoom;
	Player player;

	public Main(){
		super("The Heist");
		fileReader data = new fileReader();
		
		//Create player
		player = new Player(new Weapon("Badass", true), 1, new Point(0,0), game.Player.Type.robber);
		Player player2 = new Player(new Weapon("Badass", true), 1, new Point(8,1), game.Player.Type.robber);
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(player);
		players.add(player2);
		currentRoom = new Room("testRoom", data.getWidth(), data.getHeight(), players);
		
		//Create canvas
		canvas = new GameCanvas(data.getTiles(), currentRoom);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(GameCanvas.loadImage("money_bag_icon.png"));
		
		//go full screen
		setUndecorated(true); 
		pack();
		setExtendedState(MAXIMIZED_BOTH); 
		
		//start graphics thread running
		graphicsUpdater = new GraphicsUpdater(this);
		graphicsUpdater.start();
		
		//make us visible
		setVisible(true);
		this.canvas.setDimension(getWidth(), getHeight());
		keyBindings();
		canvas.requestFocus();
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
		
	/**
	 * Method for handling keyboard input
	 */
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_ESCAPE) {			
			canvas.gameMenuSelect();
		}
	}
		
	//------below are various methods for handling mouse input-------
		
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void mousePressed(MouseEvent e) {}

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
	
	public void repaint(){
		canvas.repaint();
	}
	
	public GameCanvas getCanvas(){
		return canvas;
	}
	
	//main method for starting program
	public static void main(String[] args){
		new Main();
	}
}
