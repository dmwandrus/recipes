/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.util.messaging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import junit.framework.TestCase;
import mil.navair.iframework.common.messaging.MessagingFactory;
import mil.navair.iframework.common.messaging.MessagingFactoryException;

/**
 *
 * @author andrew.burks
 */
//@Ignore("Assumes ActiveMQ is up and running")
public class MessagingFactoryUnitTest extends TestCase {
    private static final Logger logger = Logger.getLogger(MessagingFactoryUnitTest.class.getName());
    private final String TEST_QUEUE = "messagingfactory.unittest.queue";
    private final String TEST_TOPIC = "messagingfactory.unittest.topic";
    
    public void testGetConnection() {
        logger.info("BEGIN Testing getConnection");
        try {
            Connection conn = MessagingFactory.getConnection(MessagingFactory.CONNECTION_TYPE.P8A_BROKER);
            assertNotNull(conn);
        } catch (MessagingFactoryException ex) {
            logger.log(Level.SEVERE, null, ex);
            fail("Error connecting to message broker");
        }
        logger.info("END Testing getConnection");
    }
    
    public void testCreateDestination() {
        logger.info("BEGIN Testing createDestination");
        Connection conn = null;
        Session sess = null;
        MessageProducer queuePublisher = null;
        MessageProducer topicPublisher = null;
        try {            
            // Create test queue
            logger.info("Creating queue...");
            Destination destQueue = MessagingFactory.createDestination(TEST_QUEUE, MessagingFactory.DEST_TYPE.QUEUE);
            assertNotNull(destQueue);
            logger.info("Queue created...");
            
            // Create test topic
            logger.info("Creating topic...");
            Destination destTopic = MessagingFactory.createDestination(TEST_TOPIC, MessagingFactory.DEST_TYPE.TOPIC);
            assertNotNull(destTopic);
            logger.info("Topic created...");
            
            // Throw a message on both destinations so that they stick around for deletion in later tests
            conn = MessagingFactory.getConnection(MessagingFactory.CONNECTION_TYPE.P8A_BROKER);
            try {
                conn.start();
                sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
                queuePublisher = sess.createProducer(destQueue);
                topicPublisher = sess.createProducer(destTopic);
                Message m = sess.createTextMessage("Testing...testing 1,2,3");
                queuePublisher.send(m);
                topicPublisher.send(m);
            } catch (JMSException ex) {
                logger.log(Level.SEVERE, null, ex);
                fail("Error creating connection to send meassge to test queue");
            }
        } catch (MessagingFactoryException ex) {
            logger.log(Level.SEVERE, null, ex);
            fail("Error creating destination");
        } finally {
            if (queuePublisher!=null) {
                try {
                    queuePublisher.close();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            if (topicPublisher!=null) {
                try {
                    topicPublisher.close();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }            
            if (sess!=null) {
                try {
                    sess.close();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            if (conn!=null) {
                try {
                    conn.close();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        logger.info("END Testing createDestination");
    }
    
    public void testGetDestination() {
        logger.info("BEGIN Testing testGetDestination");
        try {
            assertNotNull(MessagingFactory.getDestination(TEST_QUEUE, MessagingFactory.DEST_TYPE.QUEUE));
        } catch (MessagingFactoryException ex) {
            logger.log(Level.SEVERE, null, ex);
            fail("Error testing getDestination on test queue");
        }
        
        try {
            assertNotNull(MessagingFactory.getDestination(TEST_TOPIC, MessagingFactory.DEST_TYPE.TOPIC));
        } catch (MessagingFactoryException ex) {
            logger.log(Level.SEVERE, null, ex);
            fail("Error testing getDestination on test topic");
        }
        logger.info("END Testing testGetDestination");       
    }
    
    public void testDeleteQueue() {
        logger.info("BEGIN Testing deleteQueue");
        assertTrue(MessagingFactory.deleteQueue(TEST_QUEUE));
        logger.info("END Testing deleteQueue");
    }
    
    public void testDeleteTopic() {
        logger.info("BEGIN Testing deleteTopic");
        assertTrue(MessagingFactory.deleteTopic(TEST_TOPIC));
        logger.info("END Testing deleteTopic");
    }    
}
