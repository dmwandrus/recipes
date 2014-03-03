package mil.navair.iframework.appframework.bundlemgmt.api;

import mil.navair.iframework.appframework.bundlemgmt.exceptions.BundleMgmtException;
import mil.navair.iframework.appframework.common.BundleState;


/**
 * This service is part of the suite of services pertaining to 
 * application management.  
 * This particular service connects to the OSGi container and 
 * will install, uninstall, start & stop bundles.  
 * 
 * The {@link BundleMgmtResponse} is intended to hold the success 
 * or failure of the requested action, as well as details about the 
 * bundle that the action was performed upon.  If the action failed for 
 * a known reason, the error will be in the BundleMgmtResponse.  
 * If an unknown error occurred, the BundleMgmtException will be thrown. 
 * 
 */
public interface BundleMgmtService
{
    
    /**
     * Install the bundle from the given location.  The option to 
     * start on install is provided.  
     * 
     * Options on how to define the bundle location:
     * mvn:groupid/artifactid/version
     * file:path/to/actual/bundle
     * 
     * If the bundle has already been installed, the BundleMgmtResponse
     * should still return successful, with data about the previously 
     * installed bundle.
     * 
     * If the installation is successful, but the start is not, then
     * the installation will not be rolled back.  The error will be 
     * communicated via the BundleMgmtResponse and addressed by the caller.  
     * 
     * @param bundleLocation where the bundle resides. 
     * @param start whether or not to start the bundle immediately
     * @return {@link  BundleMgmtResponse}
     * @throws BundleMgmtException unhandled exception
     */
    BundleMgmtResponse installBundle(String bundleLocation, boolean start) throws BundleMgmtException;
    
    
    /**
     * Start the bundle with the given id.
     * If the bundle doesn't exist, the BundleMgmtResponse should return failure.
     * If the bundle is already started, the BundleMgmtResponse should return success.
     * 
     * @param bundleId  id of bundle to start
     * @return {@link  BundleMgmtResponse}
     * @throws BundleMgmtException unhandled exception
     */
    BundleMgmtResponse startBundle(long bundleId) throws BundleMgmtException;
    
    /**
     * Stop the bundle with the given id.
     * If the bundle doesn't exist, the BundleMgmtResponse should return failure.
     * If the bundle is already stopped, the BundleMgmtResponse should return success.
     * 
     * @param bundleId  id of bundle to pause
     * @return {@link  BundleMgmtResponse}
     * @throws BundleMgmtException unhandled exception
     */
    BundleMgmtResponse pauseBundle(long bundleId) throws BundleMgmtException;
    
    
    /**
     * 
     * If the bundle doesn't exist (is already uninstalled or was never installed)
     * the BundleMgmtResponse should return success.
     * 
     * @param bundleId  id of bundle to uninstall
     * @return {@link  BundleMgmtResponse}
     * @throws BundleMgmtException unhandled exception
     */
    BundleMgmtResponse uninstallBundle(long bundleId) throws BundleMgmtException;
    
    
    /**
     * Query the bundle with the given id to determine its status.  
     * 
     * @param bundleId  id of bundle to query
     * @return {@link  BundleMgmtResponse}
     * @throws BundleMgmtException unhandled exception
     */
    BundleState getBundleStatus(long bundleId) throws BundleMgmtException;
    
    
}
