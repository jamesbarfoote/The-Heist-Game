package game.items;

import game.Room;

import java.awt.Point;
import java.util.ArrayList;

public class Desk extends Container {
	
	String filename = "_obj_desk.png";
	String direction = "N";
	double[] size = {0.6, 0.8};
	private ArrayList<Point> positions = new ArrayList<Point>();
	

	public Desk(Room room, Point position, ArrayList<InteractableItem> items, int money) {
		super(room, position, items, money);
		this.positions.add(new Point((int) position.getX(), (int) position.getY() - 1));
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}
	
	@Override
	public double[] getSize(){
		return this.size;
	}
	
	
	public void setPositions(String direction){
		this.positions.clear();
		this.direction = direction;
		
		Point p = this.getPosition();
		this.positions.add(p);
		if(direction.equals("N")){
			this.positions.add(new Point((int) p.getX(), (int) p.getY() - 1));
		}
		else if(direction.equals("E")){
			this.positions.add(new Point((int) p.getX() - 1, (int) p.getY()));
		}
		else if(direction.equals("S")){
			this.positions.add(new Point((int) p.getX(), (int) p.getY() + 1));
		}
		else if(direction.equals("W")){
			this.positions.add(new Point((int) p.getX() + 1, (int) p.getY()));
		}
	}
	
	
	public ArrayList<Point> getPositions(){
		return this.positions;
	}
}