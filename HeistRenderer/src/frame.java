import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class frame  implements KeyListener{	
	
	static JFrame frame;
	static JLabel movements = new JLabel();
	static JLabel functionality = new JLabel();
	static Dimension screenSize = new Dimension(1280, 720);
	
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private static final String MOVE_UP = "move up";
	private static final String MOVE_RIGHT = "move right";
	private static final String MOVE_DOWN = "move down";
	private static final String MOVE_LEFT = "move left";
	private static final String ZOOM_IN = "zoom in";
	private static final String ZOOM_OUT = "zoom out";
	
	player player;
	roomCanvas roomCanvas;
	
	public frame(){
		createGUI();
		fileReader data = new fileReader();
		player = new player(new Point(8,1));
		roomCanvas = new roomCanvas(data.getTiles(), data.getWidth(), data.getHeight(), screenSize, player);
		frame.add(roomCanvas);
		frame.addKeyListener(this);
		keyBindings();
	}
	
	 private static void createGUI() {
	        //Create and set up the window.
	        frame = new JFrame("The Heist");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        JLabel emptyLabel = new JLabel("");
	        emptyLabel.setPreferredSize(screenSize);
	        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

	        //Display the window.
	        //frame.setUndecorated(true); 
	        frame.pack();
	        frame.setVisible(true);
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
		
		movements.getActionMap().put(MOVE_UP, new MoveAction("Up", player, roomCanvas));
		movements.getActionMap().put(MOVE_RIGHT, new MoveAction("Right", player, roomCanvas));
		movements.getActionMap().put(MOVE_DOWN, new MoveAction("Down", player, roomCanvas));
		movements.getActionMap().put(MOVE_LEFT, new MoveAction("Left", player, roomCanvas));
		
		frame.add(movements);
		/*-------------------Functionality----------------------------*/
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("EQUALS"), ZOOM_IN);
		functionality.getInputMap(IFW).put(KeyStroke.getKeyStroke("MINUS"), ZOOM_OUT);
		
		functionality.getActionMap().put(ZOOM_IN, new gameAction("=", player, roomCanvas));
		functionality.getActionMap().put(ZOOM_OUT, new gameAction("Minus", player, roomCanvas));
		
		frame.add(functionality);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
