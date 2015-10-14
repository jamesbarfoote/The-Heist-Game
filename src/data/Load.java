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

			//get timer
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
			Node playerNode = roomsNodeList.item(i);
			players.add(addPlayers(playerNode));
		}

		room.setPlayers(players);

		return room;
	}

	// <item = type"money">
	private static void getRoomItems(Element roomNode, Room room) {
		// get all the items in the room
		NodeList roomItemsNodeList = roomNode.getChildNodes();

		System.out.println("Length of roomItemsNodeList: " + roomItemsNodeList.getLength());

		for (int count = 0; count < roomItemsNodeList.getLength(); count++) {
			// if the node is an element
			System.out.println("count: " + count);
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

		Map<String,Integer> items = new HashMap<>();

		System.out.println("Safe\nPosition: " + point + ", money: " + money);

		Safe safe = new Safe(stringToPoint(point), Integer.parseInt(money), locked);

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

		Map<String,Integer> items = new HashMap<>();

		System.out.println("Desk\nPosition: " + point + ", money: " + money);

		Desk desk = new Desk(stringToPoint(point), items);

		return desk;
	}

	private static Player addPlayers(Node playerNode) {

		NodeList playerNodeList = playerNode.getChildNodes();

		// name
		Node node = playerNodeList.item(1);
		Element element = (Element) node;
		String name = element.getTextContent();

		// weapon

		// point
		node = playerNodeList.item(3);
		element = (Element) node;
		String point = element.getTextContent();

		// type
		node = playerNodeList.item(5);
		element = (Element) node;
		String type = element.getTextContent();

		System.out.println("Player\nName: " + point+", Point: " + point + ", Type: "+ type);

		Player player = new Player(name, stringToPoint(point),
				stringToType(type));

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
