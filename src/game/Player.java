package game;

import java.awt.Point;

public class Player {
	private Room room;
	private Weapon weapon;
	private int score;
	private Point location;//Location(coords of the square with current room)
	private Type t;
	
	//Robber or guard(can't pick up money, can open all doors) enum
	public enum Type{
		robber, guard
	}
	
	public Player(Room r, Weapon w, Point p, Type t)
	{
		this.room = r;
		this.weapon = w;
		this.location = p;
		this.t = t;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public Room getRoom()
	{
		return room;
	}
	
	public Weapon getWeapon()
	{
		return weapon;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public Type getPlayerType()
	{
		return t;
	}
	
	public void updateScore(int s)
	{
		score = s;
	}
	
	public void updateRoom(Room r)
	{
		room = r;
	}
	
	public void changeWeapon(Weapon w)
	{
		weapon = w;
	}
	
	public void updateLocation(Point p)
	{
		location = p;
	}
	
	public void setgetPlayerType(Type t)
	{
		this.t = t;
	}
	
	

}
