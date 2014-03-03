package mil.navair.iframework.appframework.centralrepomgmt.impl;

import org.junit.*;
import static org.junit.Assert.*;
import mil.navair.iframework.appframework.centralrepomgmt.api.CentralRepoMgmtService;
import mil.navair.iframework.appframework.centralrepomgmt.exceptions.CentralRepoMgmtException;
import java.util.logging.Logger;

/**
 *
 */
public class DefaultCentralRepoMgmtServiceTest
{
    private static final Logger logger = Logger.getLogger(DefaultCentralRepoMgmtServiceTest.class.getName());
    
    public DefaultCentralRepoMgmtServiceTest()
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
    }

    /**
     * Test of testMethod method, of class DefaultCentralRepoMgmtService.
     */
    @Test
    public void testTestMethod() throws Exception
    {
        System.out.println("testMethod");
        String test = "myTestString";
        CentralRepoMgmtService instance = new DefaultCentralRepoMgmtService();
        String expResult = test;
        String result = instance.testMethod(test);
        assertEquals(expResult, result);
        
    }
}

