package mil.navair.iframework.common.reportUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Michael Weigel
 *
 * This class will return a HashMap of the content payload of a MOLE XML data
 * file.
 */
public class ParseXMLTarget {

    static final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA =
            "http://www.w3.org/2001/XMLSchema";
    static final boolean SHOW_KEYS = false;
    private static final Logger logger = Logger.getLogger(ParseXMLTarget.class.getName());
    private SAXParserFactory factory;
    private SAXParser saxParser;
    private String targetName;

    public ParseXMLTarget(String targetName) {

        this.targetName = targetName;

        try {
            factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
            saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }
    }

    public Map<String, ArrayList<String>> parse(File file) {
        MyHandler handler = new MyHandler();
        try {
            saxParser.parse(file, handler);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }

        return (handler.getMap());
    }

    public Map<String, ArrayList<String>> parse(InputStream in) {
        MyHandler handler = new MyHandler();
        try {
            saxParser.parse(in, handler);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }

        return (handler.getMap());
    }

    private class MyHandler extends DefaultHandler {

        boolean targetFound;
        String startElement;
        String endElement;
        String hashKey;
        Attributes attributes;
        HashMap<String, ArrayList<String>> hashMap;

        public MyHandler() {
            targetFound = false;
            hashMap = new HashMap<String, ArrayList<String>>();
            hashKey = "";
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {

            if (qName.equalsIgnoreCase(targetName)) {
                targetFound = true;
            }

            if (targetFound) {
                startElement = qName.trim();

                if (hashKey.length() == 0) {
                    hashKey = startElement;
                } else {
                    int index = hashKey.lastIndexOf('.') + 1;
                    String subString = hashKey.substring(index, hashKey.length());

                    if (!hashKey.contains(startElement) || !startElement.equals(subString)) {
                        hashKey = hashKey + "." + startElement;
                    }
                }

                this.attributes = attributes;

                //logger.info("startElement = " + startElement);
            }
        }

        @Override
        public void endElement(String uri, String localName,
                String qName) throws SAXException {

            if (qName.equalsIgnoreCase("content")) {
                targetFound = false;
            }

            endElement = qName;

            if (targetFound) {
                int index = hashKey.lastIndexOf('.');

                if (index > 0) {
                    hashKey = hashKey.substring(0, index);
                }

                //logger.info("endElement = " + endElement);
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {

            if (targetFound) {
                String str = new String(ch, start, length).trim();

                // Collect any attributes and map them also
                if (str != null && str.length() > 0) {
                    String attributeName = null;
                    for (int i = 0; i < attributes.getLength(); i++) {
                        StringBuilder strBuilder = new StringBuilder(hashKey);
                        strBuilder.append(".");
                        strBuilder.append(attributes.getQName(i).trim());
                        String key = strBuilder.toString();
                        if (hashMap.containsKey(key)) {
                            hashMap.get(key).add(attributes.getValue(i));
                        } else {
                            ArrayList<String> list = new ArrayList<String>();
                            list.add(attributes.getValue(i));
                            hashMap.put(key, list);
                        }
                    }

                    // There can be arrays of a given element data
                    if (hashMap.containsKey(hashKey)) {
                        hashMap.get(hashKey).add(str);
                    } else {
                        ArrayList<String> list = new ArrayList<String>();
                        list.add(str);
                        hashMap.put(hashKey, list);
                    }
                }
            }
        }

        public Map<String, ArrayList<String>> getMap() {
            if (SHOW_KEYS) {
                // Lets look at the keys
                Iterator<String> iterator = hashMap.keySet().iterator();
                while (iterator.hasNext()) {
                    logger.log(Level.INFO, "key = {0}", iterator.next());
                }
            }

            return (hashMap);
        }
    }
}
