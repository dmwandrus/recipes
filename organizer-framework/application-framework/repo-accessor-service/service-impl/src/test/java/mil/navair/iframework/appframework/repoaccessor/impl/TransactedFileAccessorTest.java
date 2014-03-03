/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.repoaccessor.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * This test is going to use both a mock and a "spy" (real) FileAccessor,
 * in order to test the exception-handling and transaction/rollback capabilities
 * 
 * This test is aimed more at testing the rollback capability of the 
 * Transacted File Accessor.  Makes sure that exceptions are thrown when
 * file access fails and rollbacks are executed successfully. 
 *
 * Mockito notes: 
 * Using Mockito mock methods do nothing by default. 
 * each time you set do*, it is only for the very next invocation.
 *
 * @author dandrus
 */
public class TransactedFileAccessorTest
{

    private static final Logger logger = Logger.getLogger(TransactedFileAccessorTest.class.getName());
    
    TransactedFileAccessor tfa = new TransactedFileAccessor();
    protected String dirLocation = "/tmp/tx-unittest/";
    protected String originDirLocation = dirLocation+"origin/";
    protected String destDirLocation  = dirLocation+"dest/";

    public TransactedFileAccessorTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        try
        {
            FileUtils.deleteDirectory(new File(dirLocation));
        } catch (IOException ex)
        {
            Logger.getLogger(TransactedFileAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of saveFiles method, of class TransactedFileAccessor. ensuring that
     * exceptions are thrown as required.
     */
    @Test
    public void testSaveFilesExceptions()
    {
        File destDir = new File(destDirLocation);
        FileAccessor mockAccessor = mock(FileAccessor.class);
        tfa.setFileAccessor(mockAccessor);
        Collection<File> files = new ArrayList<File>();

        // test saving success
        try
        {
            tfa.saveFiles(files, destDirLocation);
        } catch (FileAccessException ex)
        {
            logger.log(Level.SEVERE, "Unexpected Exception", ex);
            fail("Unexpected exception");
        }

        // test saving, but unable to save
        try
        {
            doThrow(FileAccessException.class).when(mockAccessor).saveFiles(files, destDirLocation);
            tfa.saveFiles(files, destDirLocation);
            fail("Did not throw exception when unable to save files");
        } catch (FileAccessException ex)
        {
            logger.log(Level.INFO, "Expected Exception: "+ex.getMessage());
        }

        // test saving, but unable to create directory
        try
        {
            doThrow(FileAccessException.class).when(mockAccessor).createDirectory(destDir);
            tfa.saveFiles(files, destDirLocation);
            fail("Did not throw exception when unable to create directory");
        } catch (FileAccessException ex)
        {
            logger.log(Level.INFO, "Expected Exception: "+ex.getMessage());
        }
    }

    /**
     * Test: Ensure 1) directory does not exist before saving files
     *
     * 2) success case: directory exists after saving files, with files. use
     * "real functionality"
     *
     * 3) fail case: directory does not exist after attempting to save files may
     * be tricky to set up...Need the "real functionality" to create directory,
     * but, set it up to throw an exception on the file save.
     *
     */
    @Test
    public void testSucessSaveFiles()
    {
        File destDir = new File(destDirLocation);
        FileAccessor realAccessor = new FileAccessor();

        tfa.setFileAccessor(realAccessor);
        Collection<File> files = new ArrayList<File>();

        // 1)  directory shouldn't exist before we start
        assertFalse(destDir.exists());
        try
        {
            tfa.saveFiles(files, destDirLocation);
        } catch (FileAccessException ex)
        {
            logger.log(Level.SEVERE, "Unexpected Exception", ex);
            fail("unexpected exception");
        }
        assertTrue(destDir.exists());
        try
        {
            tfa.deleteDirectory(destDir);
        } catch (FileAccessException ex)
        {
            logger.log(Level.SEVERE, "Unexpected Exception", ex);
            fail("unexpected exception");
        }
        assertFalse(destDir.exists());
    }

    @Test
    public void testFailSaveFiles()
    {

        File destDir = new File(destDirLocation);
        FileAccessor mockAccessor = mock(FileAccessor.class);

        tfa.setFileAccessor(mockAccessor);
        Collection<File> files = new ArrayList<File>();

        // 1)  directory shouldn't exist before we start
        assertFalse(destDir.exists());

        // now, schedule mockAccessor to really create the directory, but 
        // then throw an exception while saving.  
        // then ensure that the dest dir still doesn't exist.
        try
        {
            // set it up....
            doCallRealMethod().when(mockAccessor).createDirectory(destDir);
            doThrow(FileAccessException.class).when(mockAccessor).saveFiles(files, destDirLocation);
            doCallRealMethod().when(mockAccessor).deleteDirectory(destDir);
            
            // execute test...
            tfa.saveFiles(files, destDirLocation);
            
        } catch (FileAccessException ex)
        {
            logger.log(Level.INFO, "Expected Exception: "+ex.getMessage());
        }

        // ensuring that the directory creation was rolled back.
        assertFalse(destDir.exists());

    }
    
    @Test
    public void testSaveAndDeleteFail()
    {
        // this is going to be tricky.  
        FileAccessorTest testHelper = new FileAccessorTest();
        File destDir = new File(destDirLocation);
        FileAccessor spyAccessor = spy(new FileAccessor());

        tfa.setFileAccessor(spyAccessor);
        // creates 3 files in given directory. 
        Collection<File> files = testHelper.createTestFiles(originDirLocation);
        
        // 1)  directory shouldn't exist before we start
        assertFalse(destDir.exists());
        
        // 2)  SETUP:  successfully save set of files
        try
        {
            tfa.saveFiles(files, destDirLocation);
        } catch (FileAccessException ex)
        {
            logger.log(Level.SEVERE, "Unexpected Exception", ex);
            fail("unexpected exception");
        }
        
        assertTrue(destDir.exists());
        assertTrue(destDir.listFiles().length == 3);
        
        // At this point in time, I should have 3 files successfully saved to 
        // my desired destination directory.
        // Now, I would like to test the rollback capability of the delete. 
        // To do this, I need to use the real, or "spy" mock.
        // It will use the REAL implementation except where I tell it to
        // throw the exceptino.
        try
        {
            doAnswer(new Answer() {

                public Object answer(InvocationOnMock invocation) throws Throwable
                {
                    invocation.callRealMethod(); 
                    throw new FileAccessException("Mocked failure - hopefully after successful deletion");
                }
            }).when(spyAccessor).deleteDirectory(destDir);

            tfa.deleteDirectory(destDir);
        } catch (FileAccessException ex)
        {
            logger.log(Level.INFO, "Expected Exception: "+ex.getMessage());
        }
        
        assertTrue(destDir.exists());
        assertTrue(destDir.listFiles().length == 3);
        
    }
}
