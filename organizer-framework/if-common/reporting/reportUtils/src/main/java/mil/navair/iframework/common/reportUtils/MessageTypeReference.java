package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;


/**
 * messages are referenced by applications using a naming
 * convention as follows:  <Application Name>.messageTypeName
 * e.g.  MOLE.cot, MOLE.video, MOLE.imagery, SampleApp.event34
 *
 */
public class MessageTypeReference implements Serializable
{
	private String owner; //example is MOLE
	private String name; //name used to identify a message type example is COT
	
    public MessageTypeReference() {
    }
    
    /**
     * Get the message type name
     * @return message type name
     */
	public String getName() {
		return name;
	}

    /**
     * Set the message type name
     * @param name message type name
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Get the application name that this message type comes from.
     * @return app name
     */
	public String getOwner() {
		return owner;
	}

    /**
     * Set the application name that this message type comes from.
     * @param owner application name
     */
	public void setOwner(String owner) {
		this.owner = owner;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageTypeReference other = (MessageTypeReference) obj;
        if ((this.owner == null) ? (other.owner != null) : !this.owner.equals(other.owner)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.owner != null ? this.owner.hashCode() : 0);
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "MessageTypeReference{" + "owner=" + owner + "name=" + name + '}';
    }

}
