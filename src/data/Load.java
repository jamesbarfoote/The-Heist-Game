package data;

import game.items.Item;

import java.awt.Point;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Load {

	public static void loadFromXML(String fileName) {

		try {

			File file = new File(fileName);

			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = docBuilder.parse(file);

			// TODO: remove debugging printlns
			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			// sanity check, then parse
			if (doc.hasChildNodes()) {
				parseFile(doc.getChildNodes());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void parseFile(NodeList nodeList) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name
				String typeNode = tempNode.getNodeName();

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						Node node = nodeMap.item(i);

						switch (typeNode) {
						case "item":
							itemNode(node);
							break;
						case "money":
							moneyNode(node);
							break;
						case "door":
							doorNode(node);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Parse item.
	 *
	 * @param itemNode
	 * @return - Item from xml
	 */
	private static Item itemNode(Node itemNode) {

		// get attributes names and values
		NamedNodeMap nodeMap = itemNode.getAttributes();
		itemNode

		// 1st child node of item
		// <name>
		Node node = nodeMap.item(0);
		String name = node.getNodeValue();
		// <pos>
		node = nodeMap.item(1);
		String pos = node.getNodeValue();
		Point point = stringToPoint(pos);

		
		
		
		

		return item;
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

		Point temp = new Point(x, y);
		return temp;
	}

}
