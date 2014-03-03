/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.common;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * This test will ensure that the annotations mapping the AppMetaData (and
 * associated data) is correct. This will access the database.
 *
 * OpenJPA requires compile-time optimization of the Entities, based off of the
 * persistence.xml file. This makes it difficult to Unit Test the entity
 * annotations outside of this bundle.
 *
 * @author dandrus
 */
public class AppMetaDataTest
{

    private static final Logger logger = Logger.getLogger(AppMetaDataTest.class.getName());
    private static final String puName = "test_app_meta_data_pu";
    private static EntityManagerFactory emf;
//    private static EntityManager em;

    public AppMetaDataTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        emf = Persistence.createEntityManagerFactory(puName);
//        em = emf.createEntityManager();

        logger.setLevel(Level.FINE);
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
//        em.close();
        emf.close();
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAuthor method, of class AppMetaData.
     */
    @Test
    public void testEntityManager()
    {
        logger.info("Testing that entity manager factory exists");
        assertNotNull(emf);
//        assertNotNull(em);
    }

    @Test
    public void testPersist()
    {

        AppMetaData app = TestUtils.createAppMetaData();
        logger.info("Pre-persist: " + app);

        try
        {
            Long id = persist(app);
            AppMetaData retrievedApp = retrieve(id);

            if (app == retrievedApp)
            {
                logger.severe("comparing same object instance");
                fail("Comparisons are on same object instance");
            }


            assertEquals("App Authors are not equal", app.getAuthor(), retrievedApp.getAuthor());
            assertEquals("App Descriptions are not equal", app.getDescription(), retrievedApp.getDescription());
            assertEquals("App ids are not equal", app.getId(), retrievedApp.getId());
            assertEquals("App names are not equal", app.getName(), retrievedApp.getName());
            assertEquals("App organizations are not equal", app.getOrganization(), retrievedApp.getOrganization());
            assertEquals("App versions are not equal", app.getVersion(), retrievedApp.getVersion());
            assertEquals("number of App tags are not equal", app.getTags().size(), retrievedApp.getTags().size());
            assertEquals("App tags are not equal", app.getTags(), retrievedApp.getTags());
            assertEquals("App sec. levels are not equal", app.getSecurityLevel(), retrievedApp.getSecurityLevel());
            assertEquals("App mission areas are not equal", app.getMissionArea(), retrievedApp.getMissionArea());
            assertEquals("App functional areas are not equal", app.getSecurityLevel(), retrievedApp.getSecurityLevel());

            Collection<BundleMetaData> origBundles = app.getBundles();
            Collection<BundleMetaData> retBundles = retrievedApp.getBundles();

            assertEquals("Num bundles are not the same", origBundles.size(), retBundles.size());
            for(BundleMetaData origBundle:origBundles)
            {
                boolean found = false;
                for(BundleMetaData retBundle:retBundles)
                {
                    if(origBundle.getFileName().equals(retBundle.getFileName()))
                    {
                        found = true;
                        assertEquals(origBundle.getExpectedCheckSum(), retBundle.getExpectedCheckSum());
                    }
                }
                assertTrue(found);
            }


            assertEquals("Apps are not equal", app, retrievedApp);


            delete(id);

            AppMetaData retrievedApp2 = null;
            try
            {
                retrievedApp2 = retrieve(id);
                fail("expected exception to be thrown.  App should not exist");
            } catch (Exception ex)
            {
                // expected exception - no app.
                
            }

            assertNull(retrievedApp2);

        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to persist", ex);
            fail("Unable to persist");

        }


    }

    public AppMetaData retrieve(Long id) throws Exception
    {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try
        {
            Query retrieveQuery = em.createNamedQuery("AppMetaData.findById");
            retrieveQuery.setParameter("id", id);
            AppMetaData retrievedApp = (AppMetaData) retrieveQuery.getSingleResult();
            logger.log(Level.FINE, "retrieved app: " + retrievedApp);
            return retrievedApp;
        } catch (Exception ex)
        {
            throw ex;
        } finally
        {
            em.getTransaction().commit();
            em.close();
        }

    }

    public void delete(Long id) throws Exception
    {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try
        {
            
            Query retrieveQuery = em.createNamedQuery("AppMetaData.findById");
            retrieveQuery.setParameter("id", id);
            AppMetaData retrievedApp = (AppMetaData) retrieveQuery.getSingleResult();
            em.remove(retrievedApp);
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Exception while deleting", ex);
            throw ex;
        } finally
        {
            em.getTransaction().commit();
            em.close();
        }
    }

    public Long persist(AppMetaData app) throws Exception
    {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try
        {
            em.persist(app);
            em.flush();
            logger.log(Level.FINE, "post-persist app: " + app);

            Long id = app.getId();
            return id;
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Exception while persisting", ex);
            throw ex;
        } finally
        {
            em.getTransaction().commit();
            em.close();
        }

    }

    
}
