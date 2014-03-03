/*
 *  * To change this template, choose Tools | Templates
 *   * and open the template in the editor.
 *    */
package mil.navair.iframework.common.reportUtils;

/**
 * Status enum for Applications. 
 * @author dandrus
 **/
public enum ApplicationStatus
{

    /**
     * No Meta data exists for this app
     */
    NotInstalled, 
    /**
     * Meta data exists & targets are provisioned
     */
    Installed, 
    /**
     * karaf instance is running & all bundles are deployed successfully
     */
    Running, 
    /**
     * karaf instance is running & not all bundles are deployed successfully
     */
    Degraded, 
    /**
     * karaf instance is running & all bundles are stopped
     */
    Stopped, 
    /**
     * command has been pushed out to start running the app on a given instance
     */
    Starting,
    /**
     * command has been pushed out to stop running the app on a given instance
     */
    Stopping;
}

