/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.da.organizer.recipes.itest;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ops4j.pax.exam.Option;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.*;
import org.apache.karaf.tooling.exam.options.LogLevelOption;
import static org.ops4j.pax.exam.CoreOptions.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleRevision;

/**
 *
 * @author dandrus
 */
public class TestUtils
{
    
    static final Logger logger = Logger.getLogger(TestUtils.class.getName());
    
    public static final String GROUP_ID = "com.da.karaf";
    public static final String CUSTOM_VERSION = "1.0-SNAPSHOT";
    public static final String KARAF_VERSION = "2.3.0";
    public static final String RECIPE_TIER = "recipes-tier";

    
    public static void sleep(int seconds, String message)
    {
        try
        {
            logger.info("========================================");
            logger.info("Sleeping:  "+message);
            Thread.sleep(1000*seconds);
            logger.info("Waking: "+message);
        } catch (InterruptedException ex)
        {
            logger.log(Level.SEVERE, "couldn't sleep", ex);
        }
    }
    
    private static Option[] createKarafDistConfig(String artifactId)
    {
        return new Option[]{
            karafDistributionConfiguration()
                .frameworkUrl(maven()
                    .groupId(GROUP_ID)
                    .artifactId(artifactId)
                    .type("tar.gz")
                    .version(CUSTOM_VERSION))
                .karafVersion(KARAF_VERSION)
                .name(artifactId)
                .unpackDirectory(new File("target/paxexam/unpack/"+artifactId+"/")),
            keepRuntimeFolder(),
            logLevel(LogLevelOption.LogLevel.INFO),
//            bundle("wrap:mvn:com.da.organizer.recipes/recipes-common/1.0-SNAPSHOT/jar/tests"),
            debugConfiguration("5555", true),
            cleanCaches(true)
        };
                    
    }
    
    public static Option[] getRecipesKaraf()
    {
        return createKarafDistConfig(RECIPE_TIER);
    }
    
    
    public static boolean bundlesStarted(BundleContext bc)
    {
        boolean bundlesStarted = true;
        Bundle[] bundles = bc.getBundles();
        for (Bundle bundle : bundles)
        {
            StringBuilder b = new StringBuilder();

            String bundleState = getBundleState(bundle.getState());
            b.append("\n\nBundle ").append(bundle.getBundleId());
            b.append("[").append(bundleState).append("]");
            b.append("\n\tName -> ").append(bundle.getSymbolicName());
            b.append("\n\tRegistered Services: ").append(bundle.getRegisteredServices());
            
            if( !bundleState.equals("ACTIVE"))
            {
                if(bundleState.equals("RESOLVED"))
                {
                    // check to see if it is a fragment....
//                    if( !((bundle.adapt(BundleRevision.class).getTypes() & BundleRevision.TYPE_FRAGMENT) != 0) )
//                    {
//                        // this is not a fragment
//                        b.append("\n\tBUNDLE IS NOT STARTED PROPERLY");
//                        bundlesStarted = false;
//                    }
                }
                else{
                    b.append("\n\tBUNDLE IS NOT STARTED PROPERLY");
                    bundlesStarted = false;
                }
            }
            logger.fine(b.toString());
        }
        return bundlesStarted;
    }
    
    public static String getBundleState(int state)
    {

        switch (state)
        {
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.STOPPING:
                return "STOPPING";
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
            default:
                return "UNKNOWN";
        }
    }
}
