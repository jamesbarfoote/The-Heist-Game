package data;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import game.*;
import game.items.Item;

/**
 * @author boticanich
 *
 */
public class Save {

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
			Element rootElement = doc.createElement("game");
			doc.appendChild(rootElement);

			// append rooms to root element
			for (Room r : rooms) {
				rootElement.appendChild(getRoom(doc, r));
			}

			// output DOM XML to console
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(System.out);
			transformer.transform(source, console);

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
	// <money>
	// <amount>1200</amount>
	// <pickedUp>false</pickedUp>
	// </money>
	//
	// <door>
	// <room1>roomName</room1>
	// <room2>roomName</room2>
	// <room1Entry>4,5</room1Entry>
	// <room2Entry>4,5</room2Entry>
	// </door>
	//
	// <door>
	// <room1>roomName</room1>
	// <room2>roomName</room2>
	// <room1Entry>4,5</room1Entry>
	// <room2Entry>4,5</room2Entry>
	// </door>
	//
	// </room>

	private static Node getRoom(Document doc, Room r) {
		// <room name="hall>
		Element room = doc.createElement("room");
		room.setAttribute("name", r.getRoomName());

		for (Item i : r.getItems())
			room.appendChild(addItems(doc, i));

		for (Money m : r.getMoney())
			room.appendChild(addMoney(doc, m));

		for (Door d : r.getDoors())
			room.appendChild(addDoors(doc, d));

		return room;
	}

	private static Node addItems(Document doc, Item i) {
		// <item type=MoveableItem>
		Element itemNode = doc.createElement("item");
		itemNode.setAttribute("type", i.getClass().getName());

		// add item name?
		itemNode.appendChild(node(doc, "name", i.toString()));
		// add item position
		itemNode.appendChild(node(doc, "pos", i.getPosition().toString()));

		return itemNode;
	}

	private static Node addMoney(Document doc, Money m) {
		// <money>
		Element moneyNode = doc.createElement("money");

		moneyNode.appendChild(node(doc, "amount",
				Integer.toString(m.getAmount())));
		moneyNode.appendChild(node(doc, "pickedUp",
				Boolean.toString(m.getPickedUp())));
		moneyNode.appendChild(node(doc, "pos", m.getPosition().toString()));
		
		return moneyNode;
	}

	private static Node addDoors(Document doc, Door d) {
		// <door>
		Element doorNode = doc.createElement("door");

		doorNode.appendChild(node(doc, "roor1", d.getRoom1().toString()));
		doorNode.appendChild(node(doc, "roor2", d.getRoom2().toString()));
		doorNode.appendChild(node(doc, "room1Entry", d.getRoom1Entry()
				.toString()));
		doorNode.appendChild(node(doc, "room2Entry", d.getRoom2Entry()
				.toString()));

		return doorNode;
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
		saveToXML(null);

	}
}