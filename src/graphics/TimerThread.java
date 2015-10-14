package graphics;

public class TimerThread extends Thread{
	private GameCanvas display;
	private volatile boolean running = false;
	
	/**
	 * Takes the game canvas and prints out the time remaining on the screen
	 * @param cv 
	 */
	public TimerThread(GameCanvas cv){
		display = cv;
	}
	
	public void terminate(){
		running = false;
	}
	
	public void run(){
		running = true;
		while(running){
			try{
				Thread.sleep(1000);
				display.decrementTimer();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
}
