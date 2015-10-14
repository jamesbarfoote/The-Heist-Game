package data;

import game.*;
import game.Player.Type;
import game.items.Desk;
import game.items.InteractableItem;
import game.items.Safe;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Nick Botica - 300298159
 *
 */
public class Load {

	public static Room loadFromXML(String fileName) {

		Room roomToReturn = new Room("currentRoom", 0, 0, null);
		Long timer;

		try {
			File file = new File(fileName);

			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = docBuilder.parse(file);

			Element gameNode = doc.getDocumentElement();

			// get timer
			NodeList timerNodeList = gameNode.getElementsByTagName(Save.TIMER);
			Element timerNode = (Element) timerNodeList.item(0);
			String strTimer = timerNode.getTextContent();
			timer = Long.parseLong(strTimer);

			System.out.println(Save.TIMER + ": " + timer);

			getRooms(gameNode, roomToReturn);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return roomToReturn;
	}

	// <room name="hall">
	private static Room getRooms(Element gameNode, Room room) {
		// get all the rooms
		NodeList roomsNodeList = gameNode.getElementsByTagName(Save.ROOM);

		// design change, only one 'room' per map
		Element roomNode = (Element) roomsNodeList.item(0);

		// pass room to get the items in the room
		getRoomItems(roomNode, room);

		// get all the players
		NodeList playersNodeList = gameNode.getElementsByTagName(Save.PLAYER);

		ArrayList<Player> players = new ArrayList<>();

		for (int i = 0; i < playersNodeList.getLength(); i++) {
			Element playerNode = (Element) playersNodeList.item(i);
			players.add(addPlayer(playerNode));
		}

		room.setPlayers(players);

		return room;
	}

	// <item = type"money">
	private static void getRoomItems(Element roomNode, Room room) {
		// get all the items in the room
		NodeList roomItemsNodeList = roomNode.getChildNodes();

		for (int count = 0; count < roomItemsNodeList.getLength(); count++) {
			// if the node is an element
			// System.out.println("count: " + count);
			if (roomItemsNodeList.item(count) instanceof Element) {
				Element itemNode = (Element) roomItemsNodeList.item(count);

				String type = itemNode.getAttribute(Save.TYPE);
				switch (type) {
				case (Save.MONEY):
					room.addItem(addMoney(itemNode));
					break;
				case (Save.SAFE):
					room.addItem(addSafe(itemNode));
					break;
				case (Save.DESK):
					room.addItem(addDesk(itemNode));
					break;
				}
			}
		}
	}

	private static Money addMoney(Element moneyNode) {

		NodeList moneyNodeList = moneyNode.getChildNodes();

		// position
		Node node = moneyNodeList.item(1);
		Element element = (Element) node;
		String point = element.getTextContent();

		// amount
		node = moneyNodeList.item(3);
		element = (Element) node;
		String amount = element.getTextContent();

		System.out.println("Money\nAmount: " + amount + ", Point: " + point);

		Money money = new Money(Integer.parseInt(amount), stringToPoint(point));

		return money;
	}

	private static Safe addSafe(Element safeNode) {

		NodeList safeNodeList = safeNode.getChildNodes();

		// position
		Node node = safeNodeList.item(1);
		Element element = (Element) node;
		String point = element.getTextContent();

		// money
		node = safeNodeList.item(3);
		element = (Element) node;
		String money = element.getTextContent();

		// locked
		node = safeNodeList.item(5);
		element = (Element) node;
		boolean locked = Boolean.parseBoolean(element.getTextContent());

		Map<String, Integer> items = new HashMap<>();

		System.out.println("Safe\nPosition: " + point + ", money: " + money
				+ ", Locked: " + locked + "\n");

		Safe safe = new Safe(stringToPoint(point), Integer.parseInt(money),
				locked);

		return safe;
	}

	private static Desk addDesk(Element deskNode) {

		NodeList deskNodeList = deskNode.getChildNodes();

		// position
		Node node = deskNodeList.item(1);
		Element element = (Element) node;
		String point = element.getTextContent();

		// money
		node = deskNodeList.item(3);
		element = (Element) node;
		String money = element.getTextContent();

		Map<String, Integer> items = new HashMap<>();

		System.out.println("Desk\nPosition: " + point + ", money: " + money
				+ "\n");

		Desk desk = new Desk(stringToPoint(point), items);

		return desk;
	}

	private static Player addPlayer(Element playerNode) {

		NodeList playerNodeList = playerNode.getChildNodes();

		// id
		String strID = playerNode.getAttribute(Save.ID);
		int id = Integer.parseInt(strID);

		// name
		Node node = playerNodeList.item(1);
		Element element = (Element) node;
		String name = element.getTextContent();

		// location
		node = playerNodeList.item(3);
		element = (Element) node;
		String point = element.getTextContent();

		// type
		node = playerNodeList.item(5);
		element = (Element) node;
		String type = element.getTextContent();

		// direction
		node = playerNodeList.item(7);
		element = (Element) node;
		String dir = element.getTextContent();

		// money
		node = playerNodeList.item(9);
		element = (Element) node;
		int money = Integer.parseInt(element.getTextContent());

		Map<String, Integer> inventory = new HashMap<>();

		NodeList playerInventoryList = playerNode
				.getElementsByTagName(Save.INVENTORY);

		// read inventory items and add to inventory map
		for (int i = 0; i < playerInventoryList.getLength(); i++) {
			String item = playerInventoryList.item(i).getTextContent();
			String key = item.split(",")[0];
			int value = Integer.parseInt(item.split(",")[1]);
			inventory.put(key, value);
		}

		// TODO: debug
		System.out.println("Player\nID: " + id + ", Name: " + name
				+ ", Point: " + point + ", Type: " + type + ", Direction: "
				+ dir + ", Inventory: " + inventory.toString() + ", Money: "
				+ money + "\n");

		// make new player to return
		Player player = new Player(name, stringToPoint(point),
				stringToType(type));
		// set id
		player.setID(id);

		// set direction from string to int N,E,S,W
		if (dir == "N")
			player.setDirection(0);
		else if (dir == "E")
			player.setDirection(1);
		else if (dir == "S")
			player.setDirection(2);
		else if (dir == "W")
			player.setDirection(3);
		// set money
		player.setMoney(money);

		return player;
	}

	/**
	 * Helper method, parse string to a player type
	 *
	 * @param pos
	 *            - In the form string
	 * @return - Type
	 */
	private static Type stringToType(String type) {
		if (type.equals("robber"))
			return Player.Type.robber;
		else if (type.equals("guard"))
			return Player.Type.guard;
		return null;
	}

	/**
	 * Helper method, parse string to a point
	 *
	 * @param pos
	 *            - In the form x,y
	 * @return - Point
	 */
	private static Point stringToPoint(String pos) {

		int x = Integer.parseInt(pos.split(",")[0]);
		int y = Integer.parseInt(pos.split(",")[1]);

		Point point = new Point(x, y);

		return point;
	}

	public static void main(String[] args) {
		loadFromXML("game_save_001.xml");
	}
}
