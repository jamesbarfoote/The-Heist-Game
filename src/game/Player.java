package game;

import java.awt.Point;
import java.util.Date;

public class Player implements Cloneable{
	private Room room;
	private Weapon weapon;
	private int score;
	private Point location;//Location(coords of the square with current room)
	private Type t;
	private int ID;
	
	//Robber or guard(can't pick up money, can open all doors) enum
	public enum Type{
		robber, guard
	}
	
	public Player(Room r, Weapon w, Point p, Type t, int id)
	{
		this.room = r;
		this.weapon = w;
		this.location = p;
		this.t = t;
		this.ID = id;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public void setID(int i)
	{
		this.ID = i;
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
	
	@Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
