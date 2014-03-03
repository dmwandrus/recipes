package mil.navair.iframework.common.messaging;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;

public class MessagingFactory {
    
    private static final Logger logger = Logger.getLogger(MessagingFactory.class.getName());
    public static enum DEST_TYPE {QUEUE,TOPIC};
    public static enum CONNECTION_TYPE {P8A_BROKER,TOMS_BROKER};
    // TODO: move this url to a properties file and reference a proper ip address
    private static final String AMQP_CONN = "tcp://localhost:61616";
    private static final String TOMS_CONN = "tcp://172.17.4.2:61616";
    //private static final String AMQP_USER = "system";
    //private static final String AMQP_PASS = "manager";

    /**
     * @return returns a JMS Connection object
     * @throws MessagingFactoryException
     */
    public static Connection getConnection(CONNECTION_TYPE connType) throws MessagingFactoryException {
        Connection conn = null;
        try {
            String connectionURI = (connType.equals(CONNECTION_TYPE.P8A_BROKER)) ? AMQP_CONN : TOMS_CONN;
            conn = ActiveMQConnection.makeConnection(connectionURI);
            //return connectionFactory.createConnection(AMQP_USER,AMQP_PASS);
            return conn;            
        } catch (URISyntaxException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new MessagingFactoryException("Exception ocurred while getting broker connection",ex);
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new MessagingFactoryException("Exception ocurred while getting broker connection",ex);
        }
    }
    
    /** 
     * @param destName The name of the destination you wish to create (i.e. "FOO.BAR")
     * @param destType The type of destination you wish to create (i.e. a queue or topic)
     * @return returns an JMS Destination object
     * @throws MessagingFactoryException 
     * This method returns a JMS Destination of type Topic or Queue. If the destination does
     * not exist, it will be created as a temporary destination that goes away when the broker
     * is shut down.
     */
    public static Destination createDestination(String destName, DEST_TYPE destType) throws MessagingFactoryException {
        Connection conn = null;
        Destination dest;
        try {
            conn = getConnection(CONNECTION_TYPE.P8A_BROKER);
            conn.start();
            byte amdDestType = (destType==DEST_TYPE.TOPIC) ? ActiveMQDestination.TOPIC_TYPE : ActiveMQDestination.QUEUE_TYPE;
            dest = (Destination) ActiveMQDestination.createDestination(destName,amdDestType);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new MessagingFactoryException("Exception encountered creating destination: "+destName,ex);
        } finally {
            if (conn!=null) {
                try {
                    conn.close();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        return dest;
    }
    
    /** 
     * @param destName The name of the destination you wish to create (i.e. "FOO.BAR")
     * @param destType The type of destination you wish to create (i.e. a queue or topic)
     * @return returns an JMS Destination object
     * @throws MessagingFactoryException 
     * This method returns a JMS Destination of type Topic or Queue. If the destination does
     * not exist, it will return null.
     */    
    public static Destination getDestination(String destName, DEST_TYPE destType) throws MessagingFactoryException {
        logger.info("BEGIN MessagingFactory getDestination");
        Destination dest = null;
        try {
            if (destType.equals(DEST_TYPE.QUEUE)) {
                logger.info("Searching queues");
                dest = searchQueues(destName);
            } else {
                logger.info("Searching topics");
                dest = searchTopics(destName);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new MessagingFactoryException("Exception encountered searching for destination: "+destName,ex);
        }
        logger.info("END MessagingFactory getDestination - dest: "+dest);
        return dest;
    }    
    
    /**
     * 
     * @param queueName Case-sensitive name of the queue to delete (i.e "FOO.BAR")
     * @return Returns true if the operation was a success and false otherwise
     */
    public static boolean deleteQueue(String queueName) {
        return deleteDestination(queueName,DEST_TYPE.QUEUE);
    }
    
    /**
     * 
     * @param topicName Case-sensitive name of the topic to delete (i.e "FOO.BAR")
     * @return Returns true if the operation was a success and false otherwise
     */
    public static boolean deleteTopic(String topicName) {
        return deleteDestination(topicName,DEST_TYPE.TOPIC);       
    }
    
   /**
     * @param destination The name of the particular destination to remove
     * @param destType The type of destination to delete
     * @return Returns true if the operation was a success and false otherwise
     */
    private static boolean deleteDestination(String destinationName, DEST_TYPE destType) {
        ActiveMQConnection conn = null;
        boolean success = false;
        ActiveMQDestination dest = (destType==DEST_TYPE.QUEUE) ? searchQueues(destinationName) : searchTopics(destinationName);
        try {
            if (dest!=null) {
                conn = (ActiveMQConnection) getConnection(CONNECTION_TYPE.P8A_BROKER);
                conn.destroyDestination(dest);
                success = true;
            }
        } catch (MessagingFactoryException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (conn!=null) {
                try {
                    conn.stop();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        return success;        
    }
    
    private static ActiveMQDestination searchQueues(String destName) {
        ActiveMQDestination dest = null;
        ActiveMQConnection connAMQ = null;       
        try {
            connAMQ = (ActiveMQConnection) getConnection(CONNECTION_TYPE.P8A_BROKER);
            connAMQ.start();
            for (ActiveMQQueue queue : connAMQ.getDestinationSource().getQueues()) {
                if (queue.getQueueName().equals(destName)) {
                    dest = queue;
                }
            }
            if (dest==null) {// If still null then search temporary queues
                Iterator<ActiveMQTempQueue> itr = connAMQ.getDestinationSource().getTemporaryQueues().iterator();
                while (itr.hasNext()) {
                    ActiveMQTempQueue queue = itr.next();
                    if (queue.getQueueName().equals(destName)) {
                        dest = queue;
                    }
                }
            }
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (MessagingFactoryException ex) {        
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (connAMQ!=null) {
                try {
                    connAMQ.close();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        return dest;
    }
    
    private static ActiveMQDestination searchTopics(String destName) {
        logger.info("BEGIN MessagingFactory searchTopics");
        ActiveMQDestination dest = null;
        ActiveMQConnection connAMQ = null;
        try {
            connAMQ = (ActiveMQConnection) getConnection(CONNECTION_TYPE.P8A_BROKER);
            connAMQ.start();
            Set<ActiveMQTopic> brokerTopics = connAMQ.getDestinationSource().getTopics();
            logger.info("Number of topics found: "+brokerTopics.size());
            
            for (ActiveMQTopic topic : brokerTopics) {
                logger.info("Topic name: "+topic.getTopicName() + " Name to search for: "+destName);
                if (topic.getTopicName().equals(destName)) {
                    logger.info("Match: "+topic);
                    dest = topic;
                    break;
                }
            }
            if (dest==null) {// If still null then search temporary topics
                Iterator<ActiveMQTempTopic> itr = connAMQ.getDestinationSource().getTemporaryTopics().iterator();
                while (itr.hasNext()) {
                    ActiveMQTempTopic topic = itr.next();
                    if (topic.getTopicName().equals(destName)) {
                        dest = topic;
                    }
                }
            }            
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (MessagingFactoryException ex) {        
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (connAMQ!=null) {
                try {
                    connAMQ.close();
                } catch (JMSException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        logger.info("END MessagingFactory searchTopics");
        return dest;
    }    
}
