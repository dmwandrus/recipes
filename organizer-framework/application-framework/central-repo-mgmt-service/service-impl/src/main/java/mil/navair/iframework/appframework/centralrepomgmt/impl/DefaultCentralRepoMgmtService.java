package mil.navair.iframework.appframework.centralrepomgmt.impl;


import mil.navair.iframework.appframework.centralrepomgmt.api.CentralRepoMgmtService;
import mil.navair.iframework.appframework.centralrepomgmt.exceptions.CentralRepoMgmtException;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;

/**
 * TODO: Add overview of service implementation
 * 
 */
public class DefaultCentralRepoMgmtService implements CentralRepoMgmtService
{

    private static final Logger LOGGER = Logger.getLogger(DefaultCentralRepoMgmtService.class.getName());
    private BundleContext bundleContext;
    

    @Override
    public String testMethod(String test) throws CentralRepoMgmtException
    {
        // TODO implement methods here
        LOGGER.info("Hello from DefaultCentralRepoMgmtService to "+test);
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
