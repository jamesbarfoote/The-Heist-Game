package graphics;

public class TimerThread extends Thread{
	private GameCanvas display;
	private volatile boolean running = false;
	
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
				Thread.sleep(5);
				display.decrementTimer();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
}
