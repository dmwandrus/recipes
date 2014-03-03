package mil.navair.iframework.appframework.aircraftrepomgmt.impl;


import mil.navair.iframework.appframework.aircraftrepomgmt.api.AircraftRepoMgmtService;
import mil.navair.iframework.appframework.aircraftrepomgmt.exceptions.AircraftRepoMgmtException;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;

/**
 * TODO: Add overview of service implementation
 * 
 */
public class DefaultAircraftRepoMgmtService implements AircraftRepoMgmtService
{

    private static final Logger LOGGER = Logger.getLogger(DefaultAircraftRepoMgmtService.class.getName());
    private BundleContext bundleContext;
    

    @Override
    public String testMethod(String test) throws AircraftRepoMgmtException
    {
        // TODO implement methods here
        LOGGER.info("Hello from DefaultAircraftRepoMgmtService to "+test);
        return test;
    }
    
    public BundleContext getBundleContext()
    {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext)
    {
        this.bundleContext = bundleContext;
    }
}
