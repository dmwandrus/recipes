package mil.navair.iframework.common.utilities;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Created with IntelliJ IDEA. User: mweigel Date: 5/31/12 Time: 9:09 AM To
 * change this template use File | Settings | File Templates.
 */
public class XMLUtil {

    private static Logger logger = Logger.getLogger("gov.navair.proto.im.mole.common.utilities.XMLUtil");

    /**
     * Covert XML file to a String removing all white spaces.
     *
     * @param filea The inputed xmlfile
     * @return String This xml file as a String
     */
    public static String xmlFileToString(File file) {
        StringBuilder strBuilder = new StringBuilder();

        try {
            FileInputStream infile = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(infile);
            BufferedReader in = new BufferedReader(new InputStreamReader(bis));
            String line = null;

            while ((line = in.readLine()) != null) {
                strBuilder.append(line.trim());
            }

            infile.close();
            bis.close();
            in.close();
        } catch (java.io.IOException evt) {
            evt.getMessage();
        }

        return (strBuilder.toString());
    }

    /**
     * Reformat an String xml file with spaces and indents.
     *
     * @param input The xml string
     * @return String The formated xml file as a String
     */
    public static String format(String input) {
        return format(input, 2);
    }

    /**
     * Reformat an String xml file with spaces and indents.
     *
     * @param input The xml string
     * @param indent The number of spaces to indent
     * @return String The formated xml file as a String
     */
    public static String format(String input, int indent) {
        String formatedXMLString = null;

        try {
            if (input != null) {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setAttribute("indent-number", indent);
                Transformer transformer = transformerFactory.newTransformer();
                if (transformer != null) {
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.transform(xmlInput, xmlOutput);
                    formatedXMLString = xmlOutput.getWriter().toString();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }

        return (formatedXMLString);
    }

    public static Document xmlToDocument(File file) {
        Document doc = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
        } catch (SAXException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (doc);
    }

    public static Element xmlToElement(File file) throws IOException {
        Document doc = xmlToDocument(file);
        Node root = doc.getDocumentElement();
        Element rootelement = (Element) root;

        return (rootelement);
    }

    public static Unmarshaller getJAXBUnmarshaller(Class theClass) {
        Unmarshaller unmarshaller = null;
        try {
            JAXBContext jaxBContext = JAXBContext.newInstance(theClass);
            unmarshaller = jaxBContext.createUnmarshaller();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }

        return (unmarshaller);
    }

    public static Marshaller getJAXBMarshaller(Class theClass) {
        Marshaller marshaller = null;
        try {
            JAXBContext jaxBContext = JAXBContext.newInstance(theClass);
            marshaller = jaxBContext.createMarshaller();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }

        return (marshaller);
    }

    public static <T> JAXBElement<T> getJAXBElement(Unmarshaller unmarshaller, TextMessage m) throws Exception {
        JAXBElement<T> element = (JAXBElement<T>) unmarshaller.unmarshal(new StreamSource(new StringReader(m.getText())));

        return element;
    }

    /**
     * This method will return an object generated by the JavaTM Architecture
     * for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 See
     * <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
     */
    public static <T> T getResourceType(Unmarshaller unmarshaller, TextMessage m) throws Exception {
        JAXBElement<T> element = getJAXBElement(unmarshaller, m);
        T rt = element.getValue();

        return rt;
    }
}