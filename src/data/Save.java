package data;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

import game.Door;
import game.Money;
import game.Player;
import game.Room;
import game.items.Container;
import game.items.Desk;
import game.items.Item;
import game.items.Safe;

/**
 * @author Nick Botica - 300298159
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
	public static final String SAFE = "safe";
	public static final String POS = "position";
	public static final String NAME = "name";
	public static final String DIR = "direction";
	public static final String INVENTORY = "inventory";
	public static final String MAP = "map";
	public static final String TIMER = "timer";
	public static final String LOCKED = "locked";
	public static final String DOOR = "door";

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
	public static void saveToXML(Room room) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(GAME);
			doc.appendChild(rootElement);

			// add timer
			rootElement.appendChild(node(doc, TIMER, "1000"));

			// append rooms to root element
			rootElement.appendChild(addRoom(doc, room));

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
			room.appendChild(addItem(doc, i));

		for (Player p : r.getPlayers())
			room.appendChild(addPlayer(doc, p));

		for (Door d : r.getDoors())
			room.appendChild(addDoor(doc, d));

		return room;
	}

	private static Node addItem(Document doc, Item item) {
		// <item type=MoveableItem>
		Element itemNode = doc.createElement(ITEM);
		String itemType = item.getClass().getSimpleName().toLowerCase();
		itemNode.setAttribute(TYPE, itemType);

		// add item position
		itemNode.appendChild(node(doc, POS, pointToString(item.getPosition())));

		// if a container
		if (item instanceof game.items.Container) {
			// add money int
			int money = ((Container) item).getMoney();
			itemNode.appendChild(node(doc, MONEY, Integer.toString(money)));

			// if a safe
			if (item instanceof game.items.Safe) {
				boolean locked = ((game.items.Safe) item).isLocked();
				itemNode.appendChild(node(doc, LOCKED, Boolean.toString(locked)));
			}

			addContents(doc, (Container) item, itemNode);
		}

		// if money, add value
		if (item instanceof game.Money) {
			int amount = ((game.Money) item).getAmount();
			String strAmount = Integer.toString(amount);
			itemNode.appendChild(node(doc, AMOUNT, strAmount));
		}

		return itemNode;
	}


	/**
	 * Add a player and return a node
	 *
	 * @param doc
	 * @param player
	 * @return
	 */
	private static Node addPlayer(Document doc, Player player) {
		// create player node
		Element playerNode = doc.createElement(PLAYER);

		// id
		String strID = Integer.toString(player.getID());
		playerNode.setAttribute(ID, strID);

		// name
		String name = player.getName();
		playerNode.appendChild(node(doc, NAME, name));

		// location
		Point point = player.getLocation();
		playerNode.appendChild(node(doc, POS, pointToString(point)));

		// type
		String type = player.getPlayerType().toString();
		playerNode.appendChild(node(doc, TYPE, type));

		// direction
		String dir = player.getDirection();
		playerNode.appendChild(node(doc, DIR, dir));

		// moneyHeld
		int money = player.getMoneyHeld();
		playerNode.appendChild(node(doc, MONEY, Integer.toString(money)));

		// inventory
		addInventory(doc, player, playerNode);

		return playerNode;
	}

	private static Node addDoor(Document doc, Door door){
		// create door node
				Element doorNode = doc.createElement(DOOR);

				// location
				Point point = door.getPosition();
				doorNode.appendChild(node(doc, POS, pointToString(point)));
				// locked
				boolean locked = door.isLocked();
				doorNode.appendChild(node(doc, LOCKED, Boolean.toString(locked)));

				return doorNode;
	}


	private static void addContents(Document doc, Container item,
			Element itemNode) {
		// add map of Strings and Integers, if not null
		if (((game.items.Container) item).getItems() != null) {
			Map<String, Integer> items = ((game.items.Container) item)
					.getItems();

			for (Entry<String, Integer> entry : items.entrySet()) {
				String key = entry.getKey();
				String value = Integer.toString(entry.getValue());
				itemNode.appendChild(node(doc, ITEM, key + "," + value));
			}
		}
	}

	/**
	 * Add the players inventory to the player node
	 *
	 * @param doc
	 *            - The document
	 * @param player
	 *            - Player to add
	 * @param playerNode
	 *            - Player node to add inventory to
	 */
	private static void addInventory(Document doc, Player player,
			Element playerNode) {
		// add map of Strings and Integers, if not null
		if (player.getInventory() != null) {
			Map<String, Integer> inventory = player.getInventory();

			for (Entry<String, Integer> entry : inventory.entrySet()) {
				String key = entry.getKey();
				String value = Integer.toString(entry.getValue());
				playerNode.appendChild(node(doc, INVENTORY, key + "," + value));
			}
		}
	}

	/**
	 * Helper method, point to string
	 *
	 * @param point
	 * @return - String in the form x,y
	 */
	private static String pointToString(Point point) {
		int x = (int) point.getX();
		int y = (int) point.getY();

		return x + "," + y;

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
	 * Test for save xml
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		Room currentRoom;

		Money money = new Money(1000000, new Point(2, 4));

		Map<String, Integer> deskItems = new HashMap<String, Integer>();
		deskItems.put("Poos", 1);
		deskItems.put("Wees", 54);

		Safe safe = new Safe(new Point(4, 7), 0, true);
		Desk desk = new Desk(new Point(8, 8), deskItems);
		ArrayList<Player> players = new ArrayList<Player>();
		currentRoom = new Room("testRoom", 0, 0, players);

		//Door door = new Door();

		currentRoom.addItem(money);
		currentRoom.addItem(safe);
		currentRoom.addItem(desk);

		Player currentPlayer = new Player("player 1", new Point(1, 1),
				game.Player.Type.robber);
		Player player2 = new Player("player 2", new Point(6, 2),
				game.Player.Type.robber);

		currentPlayer.setInventory(deskItems);
		players.add(currentPlayer);
		players.add(player2);

		saveToXML(currentRoom);

	}

}
