package game;

import game.items.Weapon;

import java.awt.Point;
import java.io.Serializable;
import java.util.Map;

public class Player implements Serializable{
	private Room room;
	private Weapon weapon;
	private int score;
	private Type t;
	private int ID;
	private Point currentPosition, oldPosition; //Location(coords of the square with current room)
	private Map<String, Integer> inventory;	//contains player items
	
	//Robber or guard(can't pick up money, can open all doors) enum
	public enum Type{
		robber, guard
	}
	
	public Player(Weapon w, int PlayerNum, Point p, Type t)
	{
		this.weapon = w;
		this.currentPosition = p;
		this.oldPosition  = p;
		this.t = t;
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
	
	public void setgetPlayerType(Type t)
	{
		this.t = t;
	}
	
	/**
	 * @return the location
	 */
	public Point getLocation() {
		return currentPosition;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.currentPosition = location;
	}
	
	/**
	 * @return the oldLocation
	 */
	public Point getOldLocation() {
		return oldPosition;
	}

	/**
	 * @param oldLocation the oldLocation to set
	 */
	public void setOldLocation(Point location) {
		this.oldPosition = location;
	}
	
	@Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
