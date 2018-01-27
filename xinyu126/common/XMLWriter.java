/**
 * 
 */
package xinyu126.common;

import java.io.File;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * @author xinyu126
 *
 */
public class XMLWriter {
	public Document getRootElement() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document doc = db.newDocument();
		return doc;
	}
	public void save2File(Document doc, File file) {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer tf = null;
		try {
			tf = tfactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties properties = tf.getOutputProperties();
		properties.setProperty(OutputKeys.ENCODING, "GB2312");
		properties.setProperty(OutputKeys.METHOD, "xml");
		tf.setOutputProperties(properties);
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);
		try {
			tf.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
