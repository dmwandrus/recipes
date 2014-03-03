package mil.navair.iframework.appframework.repoaccessor.api;

import java.util.Collection;
import mil.navair.iframework.appframework.common.AppMetaData;
import mil.navair.iframework.appframework.repoaccessor.exceptions.RepoAccessorException;

/**
 * The Repository Access Service is intended to be the interface to the 
 * Application Repository.  The Application Repository consists of a file 
 * store and a database.  
 *  
 * Implementation Notes: 
 * The same code here is intended to be used to access either the Aircraft
 * Application Repository OR the Central Application Repository.  Therefore, 
 * this needs to be flexible enough to connect to either.  This is a lower
 * level OSGi service and NOT a web service.  
 */
public interface RepoAccessorService
{
    
    /**
     * Set the path of the Application Repository file system.
     * @param repositoryPath app repo file path
     */
    public void setRepositoryPath(String repositoryPath);
    
    /**
     * AppMetaData should contain a list of files that should be persisted as well. 
     * @param app
     * @return
     * @throws RepoAccessorException 
     */
    Long persistApp(AppMetaData app) throws RepoAccessorException;
    
    /**
     * Delete the app and all related files
     * @param appId
     * @throws RepoAccessorException 
     */
    void deleteApp(Long appId) throws RepoAccessorException;
    
    /**
     * retrieve a specific app, with or with out files attached
     * @param appId
     * @param withFiles
     * @return
     * @throws RepoAccessorException 
     */
    AppMetaData retrieveAppById(Long appId, boolean withFiles) throws RepoAccessorException;
    
    /**
     * retrieve all apps, none with files attached
     * @return
     * @throws RepoAccessorException 
     */
    Collection<AppMetaData> retrieveAllApps() throws RepoAccessorException;
    
    /**
     * retrieve just the list of file locations - not the actual files.
     * @param appId
     * @return
     * @throws RepoAccessorException 
     */
    Collection<String> retrieveFileLocations(Long appId) throws RepoAccessorException;

    
    // need a collection of search methods now....by Name, Organization, etc
}
