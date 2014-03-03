package mil.navair.iframework.appframework.repoaccessor.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import mil.navair.iframework.appframework.common.AppMetaData;
import mil.navair.iframework.appframework.common.BundleMetaData;
import mil.navair.iframework.appframework.repoaccessor.api.RepoAccessorService;
import mil.navair.iframework.appframework.repoaccessor.exceptions.RepoAccessorException;

/**
 * The Repository Access Service manages the installing of new applications, the
 * deleting of applications, and the retrieval of applications from the
 * application repository.
 *
 * The Application Repository consists of a database and a file system. The
 * database holds the meta data about the application while the file system
 * holds the actual java bundles that ARE the application.
 *
 * Because of the nature of the 2 part repository, this accessor has to
 * partially manage transactions. The database transaction is managed by the
 * container and will be rolled back upon an exception. The file system
 * transaction is managed by a custom Transacted File Accessor, which will throw
 * an exception if there is an error and it will roll back all changes that it
 * has made.
 *
 * Exceptions from the File Accessor and the Entity Manager are wrapped into a
 * RepoAccessorException and rethrown.
 *
 * TODO: argument validation. at a minimum, no arguments should be null.
 */
public class DefaultRepoAccessorService implements RepoAccessorService
{

    private static final Logger LOGGER = Logger.getLogger(DefaultRepoAccessorService.class.getName());
    private String repositoryPath;
    private EntityManager entityManager;
    private TransactedFileAccessor fileAccessor;

    /**
     * Default Constructor
     */
    public DefaultRepoAccessorService()
    {
        LOGGER.log(Level.FINE, "Created {0}", this.getClass());
    }

    /**
     * Get the path of the application repository.
     *
     * @return path to top level of application repository
     */
    public String getRepositoryPath()
    {
        return repositoryPath;
    }

    /**
     * The absolute path of the repository is injected into this class.
     *
     * @param repositoryPath Where the application repository lives
     */
    @Override
    public void setRepositoryPath(String repositoryPath)
    {
        this.repositoryPath = repositoryPath;
    }

    /**
     * Get the entity manager used for entity CRUD operations
     *
     * @return Entity Manager
     */
    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    /**
     * The Entity Manager is injected into this class.
     *
     * @param entityManager EntityManager
     */
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public TransactedFileAccessor getFileAccessor()
    {
        return fileAccessor;
    }

    public void setFileAccessor(TransactedFileAccessor fileAccessor)
    {
        this.fileAccessor = fileAccessor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long persistApp(AppMetaData app) throws RepoAccessorException
    {
        LOGGER.log(Level.FINE, "persisting application: {0}", app);

        String directoryPath = getDirectoryPath(app);
        Collection<File> files = new ArrayList<File>();

        for (BundleMetaData bundle : app.getBundles())
        {
            files.add(bundle.getBundleFile());
        }

        try
        {
            fileAccessor.saveFiles(files, directoryPath);
        } catch (FileAccessException fae)
        {
            // If I throw an exception here, then the container managed 
            // transaction will also roll back the database write
            // and I'm depending on the "TransactedFileAccessor" to take care
            // of rolling back its own changes.  
            throw new RepoAccessorException("Unable to persist application to file system", fae);
        }

        try
        {
            entityManager.persist(app);
//            entityManager.flush();
        } catch (PersistenceException pe)
        {
            throw new RepoAccessorException("Unable to persist application to database repository", pe);
        }

        return app.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteApp(Long appId) throws RepoAccessorException
    {
        AppMetaData app = retrieveAppById(appId, false);



        // delete files from files system
        String directoryPath = getDirectoryPath(app);
        try
        {
            fileAccessor.deleteDirectory(new File(directoryPath));
        } catch (FileAccessException ex)
        {
            throw new RepoAccessorException("Unable to delete files from file repository", ex);
        }

        try
        {
            // delete app in database
            entityManager.remove(app);
        } catch (PersistenceException pe)
        {
            throw new RepoAccessorException("Unable to delete application from database repository", pe);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppMetaData retrieveAppById(Long appId, boolean withFiles) throws RepoAccessorException
    {

        try
        {
            Query retrieveQuery = entityManager.createNamedQuery("AppMetaData.findById");
            retrieveQuery.setParameter("id", appId);
            AppMetaData app = (AppMetaData) retrieveQuery.getSingleResult();
            if (withFiles)
            {
                // attach files
                for (BundleMetaData bundle : app.getBundles())
                {
                    String filePath = getFilePath(app, bundle);
                    try
                    {
                        File file = fileAccessor.getFile(filePath);
                        bundle.setBundleFile(file);
                    } catch (FileAccessException ex)
                    {
                        throw new RepoAccessorException("Unable to attach bundle file:" + filePath);
                    }
                }
            }
            return app;
        } catch (javax.persistence.NoResultException ex)
        {
            throw new RepoAccessorException("No App with id: " + appId + " exists", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<AppMetaData> retrieveAllApps() throws RepoAccessorException
    {
        Query query = entityManager.createNamedQuery("AppMetaData.findAll");
        List<AppMetaData> allApps = query.getResultList();
        return allApps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> retrieveFileLocations(Long appId) throws RepoAccessorException
    {
        Collection<String> bundlePaths = new ArrayList<String>();
        Query query = entityManager.createNamedQuery("AppMetaData.findById");
        AppMetaData app = (AppMetaData) query.getSingleResult();
        for (BundleMetaData bundle : app.getBundles())
        {
            // get file location
            bundlePaths.add(getFilePath(app, bundle));
        }
        return bundlePaths;
    }

    /**
     * Utility method to consistently get the correct file path for a bundle
     * location.
     *
     * @param app owning application
     * @param bundle bundle in question
     * @return absolute file location of the bundle.
     */
    private String getFilePath(AppMetaData app, BundleMetaData bundle)
    {
        String bundlePath = getDirectoryPath(app) + bundle.getFileName();
        return bundlePath;
    }

    /**
     * Utility method to consistently get the correct file path for the
     * directory that holds all files for the application.
     *
     * @param app owning application
     * @return absolute location of application directory
     */
    private String getDirectoryPath(AppMetaData app)
    {
        String bundlePath = repositoryPath + "/" + app.getOrganization() + "/" + app.getVersion() + "/";
        return bundlePath;
    }
}
