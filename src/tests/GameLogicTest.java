package tests;

import java.awt.Point;

import game.Door;
import game.Player;
import game.Player.Type;
import game.items.Desk;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Tests for the Game Logic
 * @author Lachlan Lee ID# 300281826
 *
 */
public class GameLogicTest extends TestCase{

	Player p = new Player("Bob", new Point(4,4), Type.robber);
	
	public @Test void test_unlock_door_true(){
		Door d = new Door(true, new Point(4,5));
		p.getInventory().put("Key", 1);
		assertTrue(p.unlockDoor(d));
	}
	
	public @Test void test_unlock_door_false(){
		Door d = new Door(true, new Point(4,5));
		assertTrue(!p.unlockDoor(d));
	}
	
	public @Test void test_desk_position_north(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("N");
		assertTrue(d.getPositions().contains(new Point(4,3)));
	}
	
	public @Test void test_desk_position_east(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("E");
		assertTrue(d.getPositions().contains(new Point(3,4)));
	}
	
	public @Test void test_desk_position_south(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("S");
		assertTrue(d.getPositions().contains(new Point(4,5)));
	}
	
	public @Test void test_desk_position_west(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("W");
		assertTrue(d.getPositions().contains(new Point(5,4)));
	}
}
