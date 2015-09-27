import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class fileReader {
	
	private String[][] tiles = new String[10][10];	//Fix this needs to update size of array if data is too large.
	private int width, height = 0;

	public fileReader() {
		readFile();
	}

	/**
	 * read the board layout file for drawing the board.
	 */
	@SuppressWarnings("resource")
	private void readFile(){
		File roomFile = new File("TestRoom.txt");
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
						tiles[lineNum][rowNum] = "floor";
					}
					else if(value.equals("1")){
						tiles[lineNum][rowNum] = "wall";
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
