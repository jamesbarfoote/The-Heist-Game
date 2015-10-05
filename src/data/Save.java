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
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder icBuilder;
		try {
			icBuilder = icFactory.newDocumentBuilder();
			Document doc = icBuilder.newDocument();
			Element mainRootElement = doc.createElementNS(null, "Room");
			doc.appendChild(mainRootElement);

			// append child elements to root element
			for (Room r : rooms) {
				mainRootElement.appendChild(getRoom(doc, r));
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
//	<room name="hall">
//
//		<item type="ImmovableItem">
//			<name>Desk</name>
//			<pos>x,y</pos>
//		</item>
//
//		<item type="MovableItem">
//		<name>chest</name>
//		<pos>x,y</pos>
//		</item>
//
//		<money>
//			<amount>1200</amount>
//			<pickedUp>false</pickedUp>
//			TODO: need a position of money
//		</money>
//
//		<door>
//			<room1>roomName</room1>
//			<room2>roomName</room2>
//			<room1Entry>4,5</room1Entry>
//			<room2Entry>4,5</room2Entry>
//		</door>
//
//		<door>
//			<room1>roomName</room1>
//			<room2>roomName</room2>
//			<room1Entry>4,5</room1Entry>
//			<room2Entry>4,5</room2Entry>
//		</door>
//
//	</room>

	private static Node getRoom(Document doc, Room r) {
		Element room = doc.createElement("room");
		room.setAttribute("name", r.getRoomName());
		for(Item i : r.getItems())
			room.appendChild(getRoomItems(doc, name, value));
		for(Money m : r.getMoney())
			room.appendChild(getRoomMoney(doc, r));
		for(Door d : r.getDoors())
			room.appendChild(getRoomDoors(doc, r));
		return room;
	}

	private static Node getRoomItems(Document doc, String name, String value) {
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(value));
		return node;
	}

	private static Node getRoomMoney(Document doc, String name, String value) {
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(value));
		return node;
	}

	private static Node getRoomDoor(Document doc, String name, String value) {
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(value));
		return node;
	}

}
