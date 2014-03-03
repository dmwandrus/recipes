/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.common;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;

/**
 *
 * @author dandrus
 */
public class BundleStateTest
{
    
    public BundleStateTest()
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

    @Test
    public void testGetState()
    {
        
        assertEquals(BundleState.ACTIVE, BundleState.getState(Bundle.ACTIVE));
        assertEquals(BundleState.INSTALLED, BundleState.getState(Bundle.INSTALLED));
        assertEquals(BundleState.RESOLVED, BundleState.getState(Bundle.RESOLVED));
        assertEquals(BundleState.STARTING, BundleState.getState(Bundle.STARTING));
        assertEquals(BundleState.STOPPING, BundleState.getState(Bundle.STOPPING));
        assertEquals(BundleState.UNINSTALLED, BundleState.getState(Bundle.UNINSTALLED));
        
    }
}

