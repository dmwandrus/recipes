/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.repoaccessor.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import mil.navair.iframework.appframework.repoaccessor.api.RepoAccessorService;
import mil.navair.iframework.appframework.repoaccessor.exceptions.RepoAccessorException;

/**
 *
 * @author dandrus
 */
public class RepoAccessorTester
{
    
    private RepoAccessorService service;

    public void init()
    {
        System.out.println("Initialization Method");
        try
        {
            service.retrieveAllApps();
        } catch (RepoAccessorException ex)
        {
            Logger.getLogger(RepoAccessorTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public RepoAccessorService getService()
    {
        return service;
    }

    public void setService(RepoAccessorService service)
    {
        this.service = service;
    }
    
    
    
}
