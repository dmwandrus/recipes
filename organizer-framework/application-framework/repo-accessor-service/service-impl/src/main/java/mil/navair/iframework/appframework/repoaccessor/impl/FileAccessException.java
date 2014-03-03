/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.repoaccessor.impl;

/**
 *
 * @author dandrus
 */
public class FileAccessException extends Exception
{
    public FileAccessException(String message)
    {
        super(message);
    }
    
    public FileAccessException(String message, Exception ex)
    {
        super(message, ex);
    }
}
