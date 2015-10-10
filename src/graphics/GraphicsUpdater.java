package graphics;


/**
 * Thread for continually updating the graphics display
 * @author godfreya
 */
public class GraphicsUpdater extends Thread{
	private GameCanvas display;
	private final int DELAY = 2; //delay between updates, set to 33 to give roughly 30 frames per second
	
	public GraphicsUpdater(GameCanvas cv){
		display = cv;
	}
	
	//run thread to update graphics forever
	public void run() {
		while(true) {		
			try {
				Thread.sleep(DELAY);
				display.repaint();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
}