package mil.navair.iframework.appframework.common;

import org.osgi.framework.Bundle;

/**
 * Defined states of a Bundle.  
 * The OSGi spec defines each of these states as an int.
 * This enum exists for the ease of translating from an int
 * to a human-readable string.  
 */
public enum BundleState
{
    ACTIVE      (Bundle.ACTIVE), 
    INSTALLED   (Bundle.UNINSTALLED),
    RESOLVED    (Bundle.RESOLVED),
    STARTING    (Bundle.STARTING),
    STOPPING    (Bundle.STOPPING),
    UNINSTALLED (Bundle.UNINSTALLED)
    ;
    
    private final int bundleState;
    
    BundleState(int state)
    {
        this.bundleState = state;
    }
    
    public static BundleState getState(int state)
    {
        switch (state)
        {
            case Bundle.ACTIVE:
                return BundleState.ACTIVE;
            case Bundle.INSTALLED:
                return BundleState.INSTALLED;
            case Bundle.RESOLVED:
                return BundleState.RESOLVED;
            case Bundle.STARTING:
                return BundleState.STARTING;
            case Bundle.STOPPING:
                return BundleState.STOPPING;
            case Bundle.UNINSTALLED:
                return BundleState.UNINSTALLED;
            default:
                return null;
        }
    }
    

}
