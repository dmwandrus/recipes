/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import org.osgi.framework.Bundle;

/**
 * This class defines the information relevant to bundles that are part of
 * an application.  
 * 
 * @author dandrus
 */
@Embeddable
@XStreamAlias("BundleMetaData")
public class BundleMetaData //implements Serializable
{

    private static final Logger LOGGER = Logger.getLogger(BundleMetaData.class.getName());
    
    @XStreamOmitField
    @Transient
    private File bundleFile;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "checksum")
    private String expectedCheckSum;
    @Column(name = "bundle_id")
    private long bundleId;
    
    @Transient
    private BundleState state;
    
    @XStreamOmitField
    @Transient
    private Bundle osgiBundle;

    // future implementations can have a list of exported packages & services
    // as well as a list of required imports.
    //  i.e.:    
    // private Collection<String> exportedServices;
    // private Collection<String> exportedPackages;
    // private Collection<String> requiredServices;
    // private Collection<String> requiredPackages;
    

    public File getBundleFile()
    {
        return bundleFile;
    }

    public void setBundleFile(File bundle)
    {
        this.bundleFile = bundle;
    }

    public String getExpectedCheckSum()
    {
        return expectedCheckSum;
    }

    public void setExpectedCheckSum(String expectedCheckSum)
    {
        this.expectedCheckSum = expectedCheckSum;
    }
    
    public boolean validateChecksum()
    {
        if(bundleFile != null)
        {
            String actual = BundleUtils.getMd5Sum(fileName);
            if(actual.contentEquals(expectedCheckSum))
            {
                return true;
            }
        }else{
            LOGGER.warning("Please attach file before attempting to validate the checksum.");
        }
        
        return false;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public long getBundleId()
    {
        return bundleId;
    }

    public void setBundleId(long bundleId)
    {
        this.bundleId = bundleId;
    }

    public Bundle getOsgiBundle()
    {
        return osgiBundle;
    }

    public void setOsgiBundle(Bundle osgiBundle)
    {
        this.osgiBundle = osgiBundle;
    }

    public BundleState getState()
    {
        return state;
    }

    public void setState(BundleState state)
    {
        this.state = state;
    }
    
    
    
    

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final BundleMetaData other = (BundleMetaData) obj;
        if ((this.fileName == null) ? (other.fileName != null) : !this.fileName.equals(other.fileName))
        {
            return false;
        }
        if ((this.expectedCheckSum == null) ? (other.expectedCheckSum != null) : !this.expectedCheckSum.equals(other.expectedCheckSum))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
        hash = 53 * hash + (this.expectedCheckSum != null ? this.expectedCheckSum.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();

        if (LOGGER.isLoggable(Level.FINE))
        {
            b.append("\n\tBundleMetaData");
            b.append("\n\t\tfileName: ").append(fileName);
            b.append("\n\t\tchecksum: ").append(expectedCheckSum);

        } else
        {
            b.append("BundleMetaData");
            b.append(": [fileName -> ").append(fileName);
            b.append("; expectedCheckSum -> ").append(expectedCheckSum);
            b.append("]");
        }
        return b.toString();
    }
    
    
}
