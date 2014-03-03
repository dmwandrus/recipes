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
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This test exists to ensure that the File Accessor is performing as it should, 
 * saving, deleting, creating files.  
 * 
 * @author dandrus
 */
public class FileAccessorTest
{

    private static final Logger LOGGER = Logger.getLogger(FileAccessorTest.class.getName());
    
    protected String defaultPath = System.getProperty("user.home") + "/app-repo/test/";
    protected String newDirPath = defaultPath+"testDirectory/";
    protected String originDir = "/tmp/unittest/";
    private FileAccessor accessor = new FileAccessor();
    
    public FileAccessorTest()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
        // always delete the test repo after each test
        File dir = new File(defaultPath);
        try
        {
            accessor.deleteDirectory(dir);
        } catch (FileAccessException ex)
        {
            Logger.getLogger(FileAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testCreateAndDeleteDirectory()
    {
        
        File dir = new File(newDirPath);
        assertFalse(dir.exists());
        
        createTestRepoDirectory();

        deleteTestRepoDirectory();
        
        
    }
    
    protected void createTestRepoDirectory()
    {
        File dir = new File(newDirPath);
        try
        {
            accessor.createDirectory(dir);
        } catch (FileAccessException ex)
        {
            Logger.getLogger(FileAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("exception");
        }
        assertTrue(dir.exists());
    }
    
    protected void deleteTestRepoDirectory()
    {
        File dir = new File(newDirPath);
        try
        {
            accessor.deleteDirectory(dir);
        } catch (FileAccessException ex)
        {
            Logger.getLogger(FileAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("exception");
        }
        assertFalse(dir.exists());
    }
    
    protected Collection<File> createTestFiles(String originDirectory)
    {
        Collection<File> fileList = new ArrayList<File>();
        try
        {
            // create original test files
            
            File file1 = new File(originDirectory+"file1.txt");
            File file2 = new File(originDirectory+"file2.txt");
            File file3 = new File(originDirectory+"file3.txt");
            
            String file1Contents = RandomStringUtils.randomAlphanumeric(5000);
            String file2Contents = RandomStringUtils.randomAlphanumeric(15000);
            String file3Contents = RandomStringUtils.randomAlphanumeric(10000);
            
            FileUtils.writeStringToFile(file1, file1Contents);
            FileUtils.writeStringToFile(file2, file2Contents);
            FileUtils.writeStringToFile(file3, file3Contents);
            
            fileList.add(file1);
            fileList.add(file2);
            fileList.add(file3);
            
        } catch (IOException ex)
        {
            Logger.getLogger(FileAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Unable to create files");
        }
        return fileList;
    }
    
    @Test
    public void testSaveAndRetrieveFiles()
    {
        String noSlashDirPath = defaultPath+"testDirectory";
        String slashDirPath = defaultPath+"testDirectory/";
        // Setup Test...
        Collection<File> fileList = createTestFiles(originDir);
        
        // Now, copy files to test repo
        createTestRepoDirectory();
        
        try
        {
            accessor.saveFiles(fileList, noSlashDirPath);
        } catch (FileAccessException ex)
        {
            Logger.getLogger(FileAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Unable to save Files");
        }
        try
        {
            // Now, retrieve files
            Collection<File> allFileSlash = accessor.getFiles(slashDirPath);
            Collection<File> allFileNoSlash = accessor.getFiles(noSlashDirPath);
            
            assertNotNull(allFileSlash);
            assertTrue(allFileSlash.size() == 3);
            
            assertNotNull(allFileNoSlash);
            assertTrue(allFileNoSlash.size() == 3);
        } catch (FileAccessException ex)
        {
            Logger.getLogger(FileAccessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // finally, delete directory
        deleteTestRepoDirectory();
        
    }
    
    
    
    
    
}
