package mil.navair.iframework.appframework.bundlemgmt.impl;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.navair.iframework.appframework.bundlemgmt.api.BundleMgmtResponse;
import mil.navair.iframework.appframework.bundlemgmt.api.BundleMgmtService;
import mil.navair.iframework.appframework.bundlemgmt.exceptions.BundleMgmtException;
import mil.navair.iframework.appframework.common.BundleState;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.service.blueprint.container.BlueprintContainer;

/**
 * This service is part of the suite of services pertaining to application
 * management. This particular service connects to the OSGi container and will
 * install, uninstall, start & stop bundles.
 *
 * The {@link BundleMgmtResponse} is intended to hold the success or failure of
 * the requested action, as well as details about the bundle that the action was
 * performed upon. If the action failed for a known reason, the error will be in
 * the BundleMgmtResponse. If an unknown error occurred, the BundleMgmtException
 * will be thrown.
 *
 * Future Enhancements Validate that expected services also started. Retrieve
 * service-level information for bundles
 *
 *
 */
public class DefaultBundleMgmtService implements BundleMgmtService
{

    private static final Logger LOGGER = Logger.getLogger(DefaultBundleMgmtService.class.getName());
    private BundleContext bundleContext;

    /**
     * Get the bundle context.
     * @return {@link BundleContext}
     */
    public BundleContext getBundleContext()
    {
        return bundleContext;
    }

    /**
     * Set the bundle context.  This is intended to be injected
     * @param bundleContext {@link BundleContext}
     */
    public void setBundleContext(BundleContext bundleContext)
    {
        this.bundleContext = bundleContext;
    }

    /**
     * debug method
     * @param method method name
     */
    private void enter(String method)
    {
        LOGGER.log(Level.FINE, "Entering {0}.{1}", new Object[]
                {
                    this.getClass().getName(), method
                });
    }

    /**
     * debug method
     * @param method method name
     */
    private void exit(String method)
    {
        LOGGER.log(Level.FINE, "Exiting {0}.{1}", new Object[]
                {
                    this.getClass().getName(), method
                });



    }

    /**
     * {@inheritDoc }
     */
    @Override
    public BundleMgmtResponse installBundle(String bundleLocation, boolean start) throws BundleMgmtException
    {
        enter("installBundle");
        BundleMgmtResponse response = new BundleMgmtResponse();
        Bundle installedBundle = null;
        
        try
        {
            installedBundle = bundleContext.installBundle(bundleLocation);
        } catch (BundleException ex)
        {
            response.setError(new BundleMgmtException("Unable to install bundle: " + bundleLocation, ex));
            response.setSuccess(false);
            response.setBundleId(-1l);
            response.setBundleStatus(BundleState.UNINSTALLED);
            return response;
        }

        long bid = installedBundle.getBundleId();
        response.setBundleId(bid);
        // so far, so good, now, if I'm supposed to start....
        if (start)
        {
            // Return the startBundle response as is.  If it is successful,
            // then the entire action is successfull.  If it is not, 
            // then its error is the one that matters right now.
            BundleMgmtResponse startResponse = startBundle(bid);
            return startResponse;
        } else
        {
            // else, don't start the bundle, just return the successful
            // installation of this bundle. 
            response.setBundleStatus(getBundleStatus(bid));
            response.setSuccess(true);
            return response;
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public BundleMgmtResponse startBundle(long bundleId) throws BundleMgmtException
    {
        enter("startBundle");
        BundleMgmtResponse response = new BundleMgmtResponse();
        try
        {
            bundleContext.getBundle(bundleId).start();
            int state = bundleContext.getBundle(bundleId).getState();
            response.setBundleId(bundleId);
            response.setBundleStatus(BundleState.getState(state));
            response.setSuccess(true);

            logServices(bundleId);
            return response;
        } catch (BundleException ex)
        {
            int state = bundleContext.getBundle(bundleId).getState();
            response.setBundleId(bundleId);
            response.setBundleStatus(BundleState.getState(state));
            response.setSuccess(false);
            response.setError(new BundleMgmtException("Unable to start bundle: " + bundleId, ex));
            return response;
        } finally
        {
            exit("startBundle");
        }
    }

    /**
     * debug method to see what service references are associated with
     * a given bundle
     * @param bundleId 
     */
    private void logServices(long bundleId)
    {
        Bundle b = bundleContext.getBundle(bundleId);
        ServiceReference<?>[] registeredServices = b.getRegisteredServices();
        StringBuilder log = new StringBuilder();
        log.append("Bundle: " + b + "[" + BundleState.getState(b.getState()) + "]");
        if (registeredServices != null)
        {
            log.append(" {" + registeredServices.length + " services }");
            for (ServiceReference<?> ref : registeredServices)
            {
                log.append(": Ref: " + ref);
                
            }
        }
        LOGGER.fine(log.toString());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public BundleMgmtResponse pauseBundle(long bundleId) throws BundleMgmtException
    {
        enter("pauseBundle");
        BundleMgmtResponse response = new BundleMgmtResponse();
        try
        {
            bundleContext.getBundle(bundleId).stop();
            int state = bundleContext.getBundle(bundleId).getState();
            response.setBundleId(bundleId);
            response.setBundleStatus(BundleState.getState(state));
            response.setSuccess(true);
            return response;
        } catch (BundleException ex)
        {
            int state = bundleContext.getBundle(bundleId).getState();
            response.setBundleId(bundleId);
            response.setBundleStatus(BundleState.getState(state));
            response.setSuccess(false);
            response.setError(new BundleMgmtException("Unable to pause bundle: " + bundleId, ex));
            return response;
        } finally
        {
            exit("pauseBundle");
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public BundleMgmtResponse uninstallBundle(long bundleId) throws BundleMgmtException
    {
        enter("uninstallBundle");
        BundleMgmtResponse response = new BundleMgmtResponse();
        try
        {
            Bundle b = bundleContext.getBundle(bundleId);
            if (b != null)
            {
                b.uninstall();
                int state = b.getState();
                response.setBundleStatus(BundleState.getState(state));

                /**
                 * According to the Javadoc for Bundles:
                 *
                 * "The UNINSTALLED state is only visible after a bundle is
                 * uninstalled; the bundle is in an unusable state but
                 * references to the Bundle object may still be available and
                 * used for introspection."
                 *
                 * So, in order to completely remove this bundle and make it's
                 * reference unavailable from this point on, the following
                 * method call is necessary.  
                 * 
                 * The other way to completely remove the bundle would be
                 * to restart the container.  
                 *
                 */
                getFrameworkWiring().refreshBundles(Collections.singletonList(b));

            }

            response.setBundleId(bundleId);
            response.setBundleStatus(BundleState.UNINSTALLED);
            response.setSuccess(true);
            return response;
        } catch (BundleException ex)
        {
            int state = bundleContext.getBundle(bundleId).getState();
            response.setBundleId(bundleId);
            response.setBundleStatus(BundleState.getState(state));
            response.setSuccess(false);
            response.setError(new BundleMgmtException("Unable to uninstall bundle: " + bundleId, ex));
            return response;

        } finally
        {
            exit("uninstallBundle");
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public BundleState getBundleStatus(long bundleId) throws BundleMgmtException
    {
        enter("getBundleStatus");
        int state = bundleContext.getBundle(bundleId).getState();
        BundleState convertState = BundleState.getState(state);
        exit("getBundleStatus");
        return convertState;
    }


    /**
     * Get the OSGi System Bundle & cast to Framework Wiring.  
     * @return {@link FrameworkWiring}
     */
    private FrameworkWiring getFrameworkWiring()
    {
        Bundle systemBundle = bundleContext.getBundle(0);
        FrameworkWiring fw = systemBundle.adapt(FrameworkWiring.class);
        return fw;
    }
}
