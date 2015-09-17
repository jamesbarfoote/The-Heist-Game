package networking;

import graphics.GameFrame;

/**
 * Thread which continually updates the graphics display
 * @author godfreya
 *
 */

public class ClockThread extends Thread{
	private GameFrame display;
	private final int DELAY = 33; //delay between updates, set to 33 to give roughly 30 frames per second
	
	public ClockThread(GameFrame gf){
		display = gf;
	}
	
	public void run() {
		while(true) {		
			try {
				Thread.sleep(DELAY);
				display.repaint();
			} catch(InterruptedException e) {

			}			
		}
	}
	
}
