package data;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import game.*;
import game.Player.Type;
import game.items.Desk;
import game.items.InteractableItem;
import game.items.Item;
import game.items.Safe;
import game.items.Weapon;

/**
 * @author boticanich
 *
 */
public class Save {

	public static final String GAME = "game";
	public static final String ROOM = "room";
	public static final String ITEM = "item";
	public static final String PLAYER = "player";
	public static final String ID = "id";
	public static final String AMOUNT = "amount";
	public static final String TYPE = "type";
	public static final String DESK = "desk";
	public static final String MONEY = "money";
	public static final String POS = "position";
	public static final String NAME = "name";
	public static final String DIR = "direction";
	public static final String INVENTORY = "inventory";
	public static final String MAP = "map";

	// http://crunchify.com/java-simple-way-to-write-xml-dom-file-in-java/

	/**
	 * Saves the entire game state to an xml file.
	 *
	 * @param xml
	 *            - Filename to save as.
	 *
	 * @param rooms
	 *            - The list of rooms in the current game.
	 */
	public static void saveToXML(ArrayList<Room> rooms) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(GAME);
			doc.appendChild(rootElement);

			// append rooms to root element
			for (Room room : rooms) {
				rootElement.appendChild(addRoom(doc, room));
			}

			// output XML to file
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// DOMSource source = new DOMSource(doc);
			// StreamResult console = new StreamResult(System.out);
			// transformer.transform(source, console);

			// Calendar date = Calendar.getInstance();
			// String filename = date.get(Calendar.YEAR) + "-" +
			// date.get(Calendar.MONTH + "-" + date.get(Calendar.DATE) + "-" +
			// date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE)
			// + ":" + date.get(Calendar.SECOND))))));
			Result output = new StreamResult(new File("game_save_001.xml"));
			Source input = new DOMSource(doc);

			transformer.transform(input, output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Template for xml
	//
	// <room name="hall">
	//
	// <item type="ImmovableItem">
	// <name>Desk</name>
	// <pos>x,y</pos>
	// </item>
	//
	// <item type="MovableItem">
	// <name>chest</name>
	// <pos>x,y</pos>
	// </item>
	//
	// <item type="Money">
	// <amount>1200</amount>
	// <pickedUp>false</pickedUp>
	// </money>
	//
	//
	// </room>

	private static Node addRoom(Document doc, Room r) {
		// <room name="hall>
		Element room = doc.createElement(ROOM);
		room.setAttribute(NAME, r.getRoomName());

		for (Item i : r.getItems())
			room.appendChild(addItems(doc, i));

		for (Player p : r.getPlayers())
			room.appendChild(addPlayers(doc, p, r));

		return room;
	}

	private static Node addItems(Document doc, Item i) {
		// <item type=MoveableItem>
		Element itemNode = doc.createElement(ITEM);
		String itemType = i.getClass().getSimpleName().toLowerCase();
		itemNode.setAttribute(TYPE, itemType);

		// add item position
		itemNode.appendChild(node(doc, POS, pointToString(i.getPosition())));

		// if a container, add items inside
		if (i instanceof game.items.Container) {
			for (InteractableItem item : ((game.items.Container) i).getItems()) {
				itemNode.appendChild(addItems(doc, item));
			}
		}

		// if money, add value
		if (i instanceof game.Money) {
			int amount = ((game.Money) i).getAmount();
			String strAmount = Integer.toString(amount);
			itemNode.appendChild(node(doc, AMOUNT, strAmount));
		}

		return itemNode;
	}

	private static Node addPlayers(Document doc, Player player, Room room) {
		// create player node
		Element playerNode = doc.createElement(PLAYER);
		// set id
		String strID = Integer.toString(player.getID());
		playerNode.setAttribute(ID, strID);

		Point point = player.getLocation();
		playerNode.appendChild(node(doc, POS, pointToString(point)));

		String type = player.getPlayerType().toString();
		playerNode.appendChild(node(doc, TYPE, type));

		String dir = player.getDirection();
		playerNode.appendChild(node(doc, DIR, dir));

		// add inventory if not empty
		if (player.getInventory() != null) {
			Map<String, Integer> inventory = player.getInventory();
			Node inventoryNode = doc.createTextNode(INVENTORY);

			for (Entry<String, Integer> entry : inventory.entrySet()) {
				String key = entry.getKey();
				String value = Integer.toString(entry.getValue());
				inventoryNode.appendChild(node(doc, MAP, key + "," + value));
			}
			playerNode.appendChild(inventoryNode);
		}
		return playerNode;
	}

	/**
	 * Helper method, point to string
	 *
	 * @param point
	 * @return - String in the form x,y
	 */
	private static String pointToString(Point point) {
		return point.getX() + "," + point.getY();
	}

	/**
	 * Helper method, create a new node in form <name>value</name>
	 *
	 * @param doc
	 *            - Document
	 * @param name
	 *            - Name of node
	 * @param value
	 *            - Value of node
	 * @return Child node to be appended
	 */
	private static Node node(Document doc, String name, String value) {
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(value));
		return node;
	}

	/**
	 * Test for xml
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Room currentRoom;
		
		Money money = new Money(1000000, new Point(2, 4));
		ArrayList<InteractableItem> deskItems = new ArrayList<InteractableItem>();
		// deskItems.add(money);
		// deskItems.add(money);
		
		ArrayList<InteractableItem> safeItems = new ArrayList<InteractableItem>();
		safeItems.add(money);
		safeItems.add(money);
		
		Safe safe = new Safe(new Point(4, 7), safeItems, 0);

		Player currentPlayer = new Player(new Weapon("Badass", true), 1,
				new Point(1, 0), game.Player.Type.robber);
		Player player2 = new Player(new Weapon("Badass", true), 1, new Point(6,
				2), game.Player.Type.robber);
		ArrayList<Player> players = new ArrayList<Player>();		
		
		currentPlayer.lootContainer(safe);
		players.add(currentPlayer);
		players.add(player2);

		currentRoom = new Room("testRoom", 0, 0, players);


		currentRoom.addItem(money);
		currentRoom
				.addItem(safe);
		currentRoom
				.addItem(new Desk(currentRoom, new Point(8, 8), deskItems, 0));

		ArrayList<Room> rooms = new ArrayList<>();
		rooms.add(currentRoom);

		saveToXML(rooms);

	}

}
