
package mil.navair.iframework.appframework.repoaccessor.exceptions;

/**
 * This exception is is used by the RepoAccessor Service to 
 * identify the errors when they are thrown from this service.  
 */
public class RepoAccessorException extends Throwable
{

    public RepoAccessorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RepoAccessorException(String message)
    {
        super(message);
    }

}
