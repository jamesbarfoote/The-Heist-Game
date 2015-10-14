package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class fileReader {
	
	private String[][] tiles = new String[999][999];
	private static final String ASSET_PATH = "res" + File.separator +  "Rooms" + File.separator; //path for locating room files
	private int width, height = 0;
	private String room;

	public fileReader(String room) {
		this.room = room;
		readFile();
	}
	
	/*
	 * 0 = marble1
	 * 1 = wall
	 * 2 = marble2
	 * 3 = checkered
	 * 4 = door
	 * 5 = door2
	 * 6 = vault
	 * 7 = vaultDoor1
	 * 8 = vaultDoor2
	 * 9 = vaultDoor3
	 */

	/**
	 * read the board layout file for drawing the board.
	 */
	@SuppressWarnings("resource")
	private void readFile(){
		File roomFile = new File(ASSET_PATH + this.room + ".txt");
		try {
			Scanner scan = new Scanner(roomFile);
			int lineNum = 0;
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				Scanner lineSc = new Scanner(line);
				int rowNum = 0;
				
				while(lineSc.hasNext()){

					String value = lineSc.next();
					if(value.equals("0")){
						tiles[lineNum][rowNum] = "marble";
					}
					else if(value.equals("1")){
						tiles[lineNum][rowNum] = "wall";
					}
					else if(value.equals("2")){
						tiles[lineNum][rowNum] = "marble2";
					}
					else if(value.equals("3")){
						tiles[lineNum][rowNum] = "checkered";
					}
					else if(value.equals("4")){
						tiles[lineNum][rowNum] = "door";
					}
					else if(value.equals("5")){
						tiles[lineNum][rowNum] = "door2";
					}
					else if(value.equals("6")){
						tiles[lineNum][rowNum] = "vault";
					}
					else if(value.equals("7")){
						tiles[lineNum][rowNum] = "vaultWall1";
					}
					else if(value.equals("8")){
						tiles[lineNum][rowNum] = "vaultWall2";
					}
					else if(value.equals("9")){
						tiles[lineNum][rowNum] = "vaultWall3";
					}
					else{
						throw new ArrayStoreException();
					}
					rowNum++;
				}
				if(rowNum > width){
					width = rowNum;
				}
				lineNum++;
			}
			height = lineNum;
			String[][] newArray = new String[height][width];
			for(int i = 0; i < 999; i++){
				for(int j = 0; j < 999; j++){
					if(this.tiles[i][j] == null){
						break;
					}
					else{
						newArray[i][j] = this.tiles[i][j];
					}
				}
				if(this.tiles[i][0] == null){
					break;
				}
			}
			this.tiles = newArray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String[][] getTiles(){
		return tiles;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
}
