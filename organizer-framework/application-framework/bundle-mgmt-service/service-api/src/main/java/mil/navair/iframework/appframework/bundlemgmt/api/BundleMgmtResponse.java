package mil.navair.iframework.appframework.bundlemgmt.api;


import mil.navair.iframework.appframework.common.BundleState;


/**
 * This is a response object for the Bundle Management Service.
 * This holds a variety of information from the success/failure
 * of the requested action to the bundle id and new state.  
 * If there is a failure, it will also hold an error, stating why
 * the action failed. 
 */
public class BundleMgmtResponse
{
    private long bundleId;
    private boolean success;
    private BundleState bundleStatus;
    private Exception error;

    /**
     * @return OSGi Bundle ID of the bundle that was touched
     */
    public long getBundleId()
    {
        return bundleId;
    }

    /**
     * 
     * @param bundleId OSGi Bundle ID of the bundle that was touched
     */
    public void setBundleId(long bundleId)
    {
        this.bundleId = bundleId;
    }

    /**
     * @return state of the Bundle after the action is completed
     */
    public BundleState getBundleStatus()
    {
        return bundleStatus;
    }

    /**
     * 
     * @param bundleStatus state of the Bundle after the action is completed
     */
    public void setBundleStatus(BundleState bundleStatus)
    {
        this.bundleStatus = bundleStatus;
    }

    /**
     * @return whether or not the requested action was successful
     */
    public boolean isSuccess()
    {
        return success;
    }

    /**
     * @param success whether or not the requested action was successful
     */
    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    /**
     * @return get the error that caused the action to fail.  Will return null
     *         if action was successful.
     */
    public Exception getError()
    {
        return error;
    }

    /**
     * @param error the exception that caused the action to fail.  
     */
    public void setError(Exception error)
    {
        this.error = error;
    }
    
    

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append("BundleMgmtResponse: [");
        b.append("bundleId => ").append(bundleId);
        b.append(", success => ").append(success);
        b.append(", bundleStatus => ").append(bundleStatus);
        b.append("]");
        return b.toString();
    }
    
    
    
}
