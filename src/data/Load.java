package data;

import game.*;

import java.awt.Point;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Load {

	private static final String ITEM = "item";
	private static final String TYPE = "type";
	private static final String ROOM = "room";
	private static final String DESK = "desk";
	private static final String MONEY = "money";

	public static Room loadFromXML(String fileName, Room room) {

		try {
			File file = new File(fileName);

			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = docBuilder.parse(file);

			Element gameNode = doc.getDocumentElement();

			getRooms(gameNode, room);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return room;
	}

	// <room name="hall">
	private static void getRooms(Element gameNode, Room room) {
		// get all the rooms
		NodeList roomsNodeList = gameNode.getElementsByTagName(ROOM);

		// design change, only one 'room' per map
		Element roomNode = (Element) roomsNodeList.item(0);

		// pass room to get the items in the room
		getRoomItems(roomNode, room);
	}

	// <item = type"money">
	private static void getRoomItems(Element roomNode, Room room) {
		// get all the items in the room
		NodeList roomItemsNodeList = roomNode.getChildNodes();

		for (int count = 0; count < roomItemsNodeList.getLength(); count++) {
			// if the node is an element
			if (roomItemsNodeList.item(count) instanceof Element) {
				Element itemNode = (Element) roomItemsNodeList.item(count);

				String type = itemNode.getAttribute(TYPE);
				switch (type) {
				case (MONEY):
					room.addItem(addMoney(itemNode));
					break;
				case (DESK):
					room.addItem(addDesk(itemNode));
					break;
				}
			}

		}
	}

	private static Money addMoney(Element moneyNode) {

		NodeList moneyNodeList = moneyNode.getChildNodes();

		// pos
		Node node = moneyNodeList.item(1);
		Element element = (Element) node;
		String pos = element.getTextContent();

		// amount
		node = moneyNodeList.item(3);
		element = (Element) node;
		String amount = element.getTextContent();

		Money money = new Money(Integer.parseInt(amount), null,
				stringToPoint(pos));

		return money;
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
