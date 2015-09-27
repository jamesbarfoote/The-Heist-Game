import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class roomCanvas extends Canvas{

	private static final long serialVersionUID = 1L;
	private static final String IMG_PATH = "";
	private AffineTransform at;
	String[][] tiles;
	player player;
	int width, height, rows, columns;
	
	double translateX, translateY;
	double zoom;
	int zooming = 0;	//0 = Not zooming, 1 = zooming in, 2 = zooming out

	public roomCanvas(String[][] tiles, int rows, int columns, Dimension screenSize, player player){
		this.tiles = tiles;
		this.player = player;
		this.width = screenSize.width;
		this.height = screenSize.height;
		this.rows = rows;
		this.columns = columns;
		System.out.println(rows + " " + columns);
		this.zoom = 70;
		this.translateX = this.width/2;
		this.translateY = this.height/2 + (((columns-1)/2.0)*this.zoom/2);
		setBackground(Color.black);
		setSize(this.width, this.height);
		
		Point location  = player.getLocation();
		this.translateX = this.translateX - (location.getX() + location.getY()) * zoom/2;
		this.translateY = this.translateY - (((columns-location.getX()-1) + location.getY()) * zoom/4);
	}	
	
	/**
	 * Paints the board on the canvas.
	 */
	public void paint(Graphics g) {
		Graphics2D g2;
		g2 = (Graphics2D) g;
		setSize(width, height);
		translateRoom();
		try {
			drawRoom(g2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void translateRoom(){
		//Game is being zoomed in.
		Point location = player.getLocation();
		if(this.zooming == 1){
			this.translateX = this.translateX - ((location.getX() + location.getY()) * 5);	//5 because it's currently set to zoom/2 and the change is 10
			if(location.getY() > location.getX()){
				this.translateY = this.translateY - ((location.getY() - location.getX()) * 2.5);
			}
			else if(location.getY() < location.getX()){
				this.translateY = this.translateY + ((location.getX() - location.getY()) * 2.5);
			}
			this.zooming = 0;
			return;
		}
		//Game is being zoomed out.
		else if(this.zooming == 2){
			this.translateX = this.translateX + ((location.getX() + location.getY()) * 5);	//5 because it's currently set to zoom/2 and the change is 10
			if(location.getY() > location.getX()){
				this.translateY = this.translateY + ((location.getY() - location.getX()) * 2.5);
			}
			else if(location.getY() < location.getX()){
				this.translateY = this.translateY - ((location.getX() - location.getY()) * 2.5);
			}
			this.zooming = 0;
			return;
		}
		
		//Player is moving
		Point oldLocation = player.getOldLocation();
		//If moving north
		if(oldLocation.getX() < location.getX() && oldLocation.getY() == location.getY()){
			this.translateX = this.translateX - zoom/2;
			this.translateY = this.translateY + zoom/4;
		}
		//If moving south
		else if(oldLocation.getX() > location.getX() && oldLocation.getY() == location.getY()){
			this.translateX = this.translateX + zoom/2;
			this.translateY = this.translateY - zoom/4;
		}
		//If moving east
		else if(oldLocation.getY() < location.getY() && oldLocation.getX() == location.getX()){
			this.translateX = this.translateX - zoom/2;
			this.translateY = this.translateY - zoom/4;
		}
		//If moving west
		else if(oldLocation.getY() > location.getY() && oldLocation.getX() == location.getX()){
			this.translateX = this.translateX + zoom/2;
			this.translateY = this.translateY + zoom/4;
		}
	}
	
	private void drawRoom(Graphics2D g) throws InterruptedException{		
		for (int i = tiles.length-1; i >= 0; i--){
		    for (int j = 0; j < tiles[i].length; j++){
		    	Point point = new Point(i, j);
		    	Point p = twoDToIso(point);
		    	if(tiles[i][j] == "floor"){
		    		//Thread.sleep(300);
		    		drawTile(g, p);
		    		//Thread.sleep(800);
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "wall"){
		    		drawTile(g, p);
		    		drawIcons(g, point);
		    	}
		    }
		}
	}
	
	private void drawTile(Graphics2D g, Point p){
		try {
			BufferedImage myPicture = ImageIO.read(new File(IMG_PATH + "floor.jpg"));
			double width = zoom/2;
			double height = zoom/2;
			BufferedImage scaled = getScaledImage(myPicture, (int) width, (int) height);
			this.at = new AffineTransform();
			this.at.translate(p.x + this.translateX, p.y + this.translateY);
			g.drawImage(scaled, this.at, getParent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawIcons(Graphics2D g, Point point){		
//		Draw the player(s)		
		Point location = player.getLocation();
		if(location.equals(point)){
			try {
				BufferedImage myPicture = ImageIO.read(new File(IMG_PATH + "link.jpg"));
				double width = zoom/2;
				double height = zoom/2;
				BufferedImage scaled = getScaledImage(myPicture, (int) width, (int) height);
				AffineTransform at = new AffineTransform();
				at.translate(this.width/2, this.height/2);
				g.drawImage(scaled, at, getParent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private BufferedImage getScaledImage(Image img, int w, int h){
	    BufferedImage resized = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resized.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(img, 0, 0, w, h, null);
	    g2.dispose();
	    return resized;
	}
	
	private Point twoDToIso(Point point){
		Point tempPt = new Point(0,0);
		tempPt.x = (point.x * (int) zoom / 2) + (point.y * (int) zoom / 2);
		tempPt.y = (point.y * (int) zoom / 4) - (point.x * (int) zoom / 4);
		return tempPt;
	}
	
	public double getZoom(){
		return this.zoom;
	}
	
	public void setZoom(double zoom, int direction){
		this.zoom = zoom;
		this.zooming = direction;
	}
}