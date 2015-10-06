package data;

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

			//TODO: remove debugging printlns
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

	private static void itemNode(Node itemNode) {

		// get attributes names and values
		NamedNodeMap nodeMap = itemNode.getAttributes();

		for (int i = 0; i < nodeMap.getLength(); i++) {

			Node node = nodeMap.item(i);

			System.out.println("attr name : " + node.getNodeName());
			System.out.println("attr value : " + node.getNodeValue());

		}
	}
}
