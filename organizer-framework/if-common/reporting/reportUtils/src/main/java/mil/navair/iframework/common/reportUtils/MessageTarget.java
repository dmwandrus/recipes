/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Generic Definition of a Messaging Endpoint. 
 * Currently have 6 types of endpoints:  
 *   1) queue    : jms-type of messaging queue
 *   2) soap     : wsdl defined endpoint
 *   3) database : app-specific database location
 *   4) soap-private     : wsdl defined endpoint provided by third-party app
 *   5) file     : location in file system to store files
 *   6) rmi      : definition of an rmi endpoint
 * 
 * 
 */
public class MessageTarget implements Serializable
{
    /**
     * Framework generated unique ID.
     */
    private Integer targetId;
    /**
     * App-defined Target Name.
     */
    private String targetName;
    
    /**
     * location, hostname & port of target
     * means slightly different things for each type of target
     */
    private String location;
    private String hostname;
    private Integer port;
    
    private boolean guaranteeDelivery;
    
    /**
     * Status of this target - running or stopped
     */
    private TargetStatus status;
    /**
     * Type of target.
     */
    private TargetType targetType;
  
    /**
     * a basic username credential to associate with this target
     */
    private String username;
    /**
     * a basic password credential to associate with this target
     */
    private String password;
    
    /**
     * Types of messages that this target is interested
     * in receiving. 
     */
    public Set<MessageTypeReference> messageTypeReferences = new HashSet<MessageTypeReference>();

    /**
     * Default Constructor
     */
    public MessageTarget()
    {
        super();
    }

    /**
     * Application Defined Name of the target. 
     * @return target name
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * Application Username, defined by app or assigned by framework
     * @return 
     */
    public String getUsername() {
    	return username;
    }
    
    /**
     * Application Password, defined by app or assigned by framework
     * @return 
     */
    public String getPassword() {
    	return password;
    }


        /**
     * Type of Target.  
     * i.e. what type of protocol is this target type expecting?
     * currently supporting:  queue, soap, database
     * @return target type
     */

    public TargetType getTargetType() {
        return targetType;
    }

    
    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public void setTargetName(String name) {
        this.targetName = name;

    }

    public void setUsername(String x) {
    	username = x;
    }
    
    public void setPassword(String x) {
    	password = x;
    }
    /**
     * Framework Generated Unique Key.
     * @return unique Identifier for this Target 
     */
    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer id) {
        this.targetId = id;
    }
	/**
     * Types of messages that this target is interested
     * in receiving. 
     * @return set of message types
     */
    public Set<MessageTypeReference> getMessageTypeReferences() {
    	return messageTypeReferences;
    }

    public void setMessageTypeReferences(Set<MessageTypeReference> refs)
    {
        this.messageTypeReferences = refs;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public boolean isGuaranteeDelivery() {
        return guaranteeDelivery;
    }

    public void setGuaranteeDelivery(boolean guaranteeDelivery) {
        this.guaranteeDelivery = guaranteeDelivery;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public TargetStatus getStatus()
    {
        return status;
    }

    public void setStatus(TargetStatus status)
    {
        this.status = status;
    }
    
    

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final MessageTarget other = (MessageTarget) obj;
        if ((this.targetName == null) ? (other.targetName != null) : !this.targetName.equals(other.targetName))
        {
            return false;
        }
        if ((this.location == null) ? (other.location != null) : !this.location.equals(other.location))
        {
            return false;
        }
        if ((this.hostname == null) ? (other.hostname != null) : !this.hostname.equals(other.hostname))
        {
            return false;
        }
        if ((this.port == null) ? (other.port != null) : !this.port.equals(other.port))
        {
            return false;
        }
        if (this.targetType != other.targetType && (this.targetType == null || !this.targetType.equals(other.targetType)))
        {
            return false;
        }
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username))
        {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + (this.targetName != null ? this.targetName.hashCode() : 0);
        hash = 67 * hash + (this.location != null ? this.location.hashCode() : 0);
        hash = 67 * hash + (this.hostname != null ? this.hostname.hashCode() : 0);
        hash = 67 * hash + (this.port != null ? this.port.hashCode() : 0);
        hash = 67 * hash + (this.targetType != null ? this.targetType.hashCode() : 0);
        hash = 67 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 67 * hash + (this.password != null ? this.password.hashCode() : 0);
        return hash;
    }

    
    



    @Override
    public String toString() {
        return "MessageTarget [" +
                "\n\ttargetId => " + targetId +
                "\n\tname     => " + targetName +
                "\n\tstatus   => " + status +
                "\n\tusername => " + username +
                "\n\tpassword => " + password +
                "\n\tlocation => " + location +
                "\n\thostname => " + hostname +
                "\n\tportNum  => " + port +
                "\n\ttargetType => " + targetType +
                "\n\tmessageTypeReferences => " + messageTypeReferences + ']';
    }
}
