package graphics;

public class TimerThread extends Thread{
	private GameCanvas display;
	
	public TimerThread(GameCanvas cv){
		display = cv;
	}
	
	public void run(){
		while(true){
			try{
				Thread.sleep(1000);
				display.decrementTimer();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
}
