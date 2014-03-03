
package mil.navair.iframework.appframework.centralrepomgmt.exceptions;

/**
 * This exception is is used by the CentralRepoMgmt Service to 
 * identify the errors when they are thrown from this service.  
 */
public class CentralRepoMgmtException extends Exception
{

    public CentralRepoMgmtException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CentralRepoMgmtException(String message)
    {
        super(message);
    }

}
