
package mil.navair.iframework.appframework.aircraftrepomgmt.exceptions;

/**
 * This exception is is used by the AircraftRepoMgmt Service to 
 * identify the errors when they are thrown from this service.  
 */
public class AircraftRepoMgmtException extends Exception
{

    public AircraftRepoMgmtException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public AircraftRepoMgmtException(String message)
    {
        super(message);
    }

}
