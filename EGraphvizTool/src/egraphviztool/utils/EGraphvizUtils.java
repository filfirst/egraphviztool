package egraphviztool.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EGraphvizUtils {

	public static final String COLOR = "color";
	public static final String NAME = "name";

	public static Map<String, RGB> parseColors(InputStream stream) throws
			ParserConfigurationException, IOException, SAXException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(stream);
		doc.getDocumentElement().normalize();

		NodeList colorList = doc.getElementsByTagName(COLOR);
		Map<String, RGB> map = new HashMap<String, RGB>();

		for (int i = 0; i < colorList.getLength(); i++) {
			Node colorNode = colorList.item(i);

			if (colorNode.getNodeType() == Node.ELEMENT_NODE) {
				int color = Integer.parseInt(colorNode.getTextContent(), 16);
				int red = (color & 0xff0000) >> 16;
				int green = (color & 0x00ff00) >> 8;
				int blue = (color & 0x0000ff);

				NamedNodeMap nodeMap = colorNode.getAttributes();
				Node nameNode = nodeMap.getNamedItem(NAME);
				map.put(nameNode.getTextContent(), new RGB(red, green, blue));
			}
		}

		return map;
	}

}
