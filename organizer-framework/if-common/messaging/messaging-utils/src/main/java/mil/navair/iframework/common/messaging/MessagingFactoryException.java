/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.messaging;

/**
 *
 * @author andrew.burks
 */
public class MessagingFactoryException extends Exception{
    public MessagingFactoryException(String msg) {
            super(msg);
    }    
    public MessagingFactoryException(String msg, Exception e) {
            super(msg,e);
    }
}
