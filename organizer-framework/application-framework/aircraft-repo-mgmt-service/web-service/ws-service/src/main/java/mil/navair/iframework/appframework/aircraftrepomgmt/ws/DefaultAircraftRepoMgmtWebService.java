package mil.navair.iframework.appframework.aircraftrepomgmt.ws;

import mil.navair.iframework.appframework.aircraftrepomgmt.api.AircraftRepoMgmtService;
import mil.navair.iframework.appframework.aircraftrepomgmt.exceptions.AircraftRepoMgmtException;
import mil.navair.iframework.appframework.aircraftrepomgmt.ws.HelloPortType;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.osgi.framework.BundleContext;


public class DefaultAircraftRepoMgmtWebService implements HelloPortType
{
    private static final Logger LOGGER = Logger.getLogger(DefaultAircraftRepoMgmtWebService.class.getName());
    private AircraftRepoMgmtService service;
    private BundleContext bc;


    public void setService(AircraftRepoMgmtService service)
    {
        this.service = service;
    }

    public AircraftRepoMgmtService getService()
    {
        return service;
    }
    
    public BundleContext getBc() {
        return bc;
    }

    public void setBc(BundleContext bc) {
        this.bc = bc;
    }

    

    @Override
    public String sayHello(String name)
    {
        LOGGER.info("Web Service Called with "+name);
        LOGGER.info("Web Service forwarding to local service");
        String returning = "noValue";
        try{
            returning = this.service.testMethod(name);
        }catch(AircraftRepoMgmtException ex)
        {
            LOGGER.log(Level.WARNING, "Exception while calling service", ex);
        }
        LOGGER.info("Web Service returning with "+returning);
        return name; 
    }
}
