
package mil.navair.iframework.appframework.bundlemgmt.exceptions;

/**
 * This exception is is used by the BundleMgmt Service to 
 * identify the errors when they are thrown from this service.  
 */
public class BundleMgmtException extends Exception
{

    /**
     * Create a new Bundle Management Exception with an
     * embedded exception.
     * 
     * @param message  Specific error message
     * @param cause  parent exception
     */
    public BundleMgmtException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Create a new Bundle Management Exception
     * @param message  specific error message
     */
    public BundleMgmtException(String message)
    {
        super(message);
    }

}
