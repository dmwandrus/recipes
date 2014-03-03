package mil.navair.iframework.appframework.aircraftrepomgmt.impl;

import org.junit.*;
import static org.junit.Assert.*;
import mil.navair.iframework.appframework.aircraftrepomgmt.api.AircraftRepoMgmtService;
import mil.navair.iframework.appframework.aircraftrepomgmt.exceptions.AircraftRepoMgmtException;
import java.util.logging.Logger;

/**
 *
 */
public class DefaultAircraftRepoMgmtServiceTest
{
    private static final Logger logger = Logger.getLogger(DefaultAircraftRepoMgmtServiceTest.class.getName());
    
    public DefaultAircraftRepoMgmtServiceTest()
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
     * Test of testMethod method, of class DefaultAircraftRepoMgmtService.
     */
    @Test
    public void testTestMethod() throws Exception
    {
        System.out.println("testMethod");
        String test = "myTestString";
        AircraftRepoMgmtService instance = new DefaultAircraftRepoMgmtService();
        String expResult = test;
        String result = instance.testMethod(test);
        assertEquals(expResult, result);
        
    }
}

