package mil.navair.iframework.appframework.centralrepomgmt.ws;

import mil.navair.iframework.appframework.centralrepomgmt.api.CentralRepoMgmtService;
import mil.navair.iframework.appframework.centralrepomgmt.exceptions.CentralRepoMgmtException;
import mil.navair.iframework.appframework.centralrepomgmt.ws.HelloPortType;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.osgi.framework.BundleContext;


public class DefaultCentralRepoMgmtWebService implements HelloPortType
{
    private static final Logger LOGGER = Logger.getLogger(DefaultCentralRepoMgmtWebService.class.getName());
    private CentralRepoMgmtService service;
    private BundleContext bc;


    public void setService(CentralRepoMgmtService service)
    {
        this.service = service;
    }

    public CentralRepoMgmtService getService()
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
        }catch(CentralRepoMgmtException ex)
        {
            LOGGER.log(Level.WARNING, "Exception while calling service", ex);
        }
        LOGGER.info("Web Service returning with "+returning);
        return name; 
    }
}
