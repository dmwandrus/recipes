/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.common;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author dandrus
 */
public class AppConversionTest
{
    
    private static final Logger logger = Logger.getLogger(AppConversionTest.class.getName());
    
    @Test
    public void testToXml() throws UnsupportedEncodingException
    {
        XStream xStream = new XStream();
        AppMetaData app = TestUtils.createAppMetaData();
        xStream.processAnnotations(AppMetaData.class);
        xStream.processAnnotations(BundleMetaData.class);
        
        String toXML = xStream.toXML(app);
        logger.info("XML: \n"+toXML);
    }
    

    @Test
    public void testBackAndForth() 
    {
        AppMetaData app = TestUtils.createAppMetaData();
        logger.info("Original App: "+app);
        String xmlApp = app.toXml();
        logger.info("Original App to XML: "+xmlApp);
        AppMetaData appFromXml = AppMetaData.fromXml(xmlApp);
        logger.info("App FROM XML: "+appFromXml);
        
        assertEquals(app, appFromXml);
        //String xml = getXMLTestApp();
    }
    
    
    
    @Test
    public void testFromXml()
    {
        try
        {
            String xml = getXMLTestApp();
            logger.info("XML: "+xml);
            AppMetaData appFromXml = AppMetaData.fromXml(xml);
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(AppMetaDataTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(AppMetaDataTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getXMLTestApp() throws FileNotFoundException, IOException
    {
        InputStream testFile = this.getClass().getResourceAsStream("/xml/test-app.xml");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(testFile));
            StringBuilder xmlString = new StringBuilder();
            String line = in.readLine();
            while(line != null)
            {
                xmlString.append(line);
                line = in.readLine();
            }
            return xmlString.toString();
    }
}

