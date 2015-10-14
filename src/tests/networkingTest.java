package tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.IOException;

import org.junit.Test;

import game.Player;
import game.items.Weapon;
import networking.Client;
import networking.Server;

public class networkingTest {
	Thread myThread;
	Server s;


	@Test
	public void canConnect() throws IOException, InterruptedException
	{
		Player player = new Player("Bob", new Weapon("Badass", true), new Point(1,1), game.Player.Type.robber);
		startServer();
		Client c = new Client(43200, "localhost", player);
		s.stopServer();
		myThread.interrupt();
	}


	
	@Test
	public void returnedListCorrect()
	{
		Player player = new Player("Bob", new Weapon("Badass", true), new Point(1,1), game.Player.Type.robber);
		startServer();
		Client c = null;
		try {
			c = new Client(43200, "localhost", player);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(c.getPlayers().size() == 1);
		s.stopServer();
		myThread.interrupt();
	}
	
	//@Test
//	public void setsID()
//	{
//		s = new Server();
//		s.run();
//		Player player = new Player("Bob", new Weapon("Badass", true), new Point(1,1), game.Player.Type.robber);
//		startServer();
//		Client c = null;
//		try {
//			c = new Client(43200, "localhost", player);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		player = c.getPlayer();
//		assertTrue(c.getID() == 0);
//		s.stopServer();
//		//myThread.interrupt();
//	}

	public void startServer()
	{
		s = new Server(); //Start the server in a new thread
		myThread = new Thread(s);
		myThread.setDaemon(true); 		
		myThread.start(); 
		
	}



}
