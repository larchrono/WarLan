/**
 * ½âÎöxmlÎÄµµ¡£
 */
package xinyu126.common;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author xinyu126
 *
 */
public class XMLParser {
	private class IPXMLReader extends DefaultHandler {
		String tag;
		LinkedList<InetAddress> IPList;
		IPXMLReader(LinkedList<InetAddress> IPList) {
			this.IPList=IPList;
		}
		@Override
		public void characters(char[] ch, int start, int length) {
			String value = new String(ch, start, length);
			if (tag.equals("ip")) {
				if (value.endsWith("*")) {
					int t = value.lastIndexOf('.');
					String ipstr = value.substring(0, t + 1);
					for (int i = 1; i < 255; i++) {
						try {
							InetAddress ip = InetAddress.getByName(ipstr + i);
							IPList.add(ip);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} 
		}
		@Override
		public void endDocument() {

		}
		@Override
		public void endElement(String uri, String localName, String qName) {
			tag = "";
		}
		@Override
		public void startDocument() {
			tag = "";
		}
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			tag = qName.toLowerCase();
		}
	}
	class UserXMLReader extends DefaultHandler {
		String tag;
		@Override
		public void characters(char[] ch, int start, int length) {
			String value = new String(ch, start, length);
			if (tag.equals("user")) {
				userName = value;
			} else if (tag.equals("version")) {
				version = value;
			} else if (tag.equals("qu")) {
				qu = value;
			} else if (tag.equals("lou")) {
				lou = value;
			}
		}
		@Override
		public void endDocument() {

		}
		@Override
		public void endElement(String uri, String localName, String qName) {
			tag = "";
		}
		@Override
		public void startDocument() {
			tag = "";
		}
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			tag = qName;
		}
	}
	/*
	 * public static void main(String[] args){ new XMLParser().ParseIP(new
	 * File("xml\\IP.xml")); }
	 */
	private String userName;
	private String version;
	private String lou;
	private String qu;
	
	public String getlou() {
		if (lou!= null) {
		return lou;
		}
		return "";
	}

	public String getqu() {
		if (qu != null) {
		return qu;
		}
		return "";
	}
	public String getuserName() {
		if (userName != null) {
			return userName;
		} else {
			return "";
		}

	}

	public String getVersion() {
		if (version != null) {
		return version;
		}
		return "";
	}
	public LinkedList<InetAddress> ParseIP(File file) {
		LinkedList<InetAddress> IPList = new LinkedList<InetAddress>();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(file, new IPXMLReader(IPList));

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IPList;
	}
	public void Parser(File file) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {			
				SAXParser sp = spf.newSAXParser();
				sp.parse(file, new UserXMLReader());
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
