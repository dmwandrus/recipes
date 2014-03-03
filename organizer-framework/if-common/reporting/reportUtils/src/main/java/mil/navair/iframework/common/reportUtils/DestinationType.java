/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mweigel
 */
public class DestinationType implements Serializable {
    private Set<String> email;
    private Set<FileDestinationType> fileDestination;

    public DestinationType() {
        email = new HashSet<String>();
        fileDestination = new HashSet<FileDestinationType>();
    }

    public Set<String> getEmail() {
        return email;
    }

    public void setEmail(Set<String> email) {
        this.email = email;
    }

    public Set<FileDestinationType> getFileDestination() {
        return fileDestination;
    }

    public void setFileDestination(Set<FileDestinationType> fileDestination) {
        this.fileDestination = fileDestination;
    }

    @Override
    public String toString() {
        return "MessageTarget ["
                + "\n\temail => " + email
                + "\n\tfileDestinationSet => " + fileDestination + ']';
    }
}
