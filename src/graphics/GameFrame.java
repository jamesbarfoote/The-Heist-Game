package graphics;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import control.GraphicUpdateThread;

/**
 * Creates the game application window which displays the gui
 * @author godfreya
 */
public class GameFrame extends JFrame{
	private GraphicUpdateThread ck; //graphical update thread
	private GameCanvas canvas;  //graphics canvas

	public GameFrame(){
		super("The Heist");
		canvas = new GameCanvas();
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
		canvas.requestFocus();
	}

	public void repaint(){
		canvas.repaint();
	}
	
	public GameCanvas getCanvas(){
		return canvas;
	}
}
