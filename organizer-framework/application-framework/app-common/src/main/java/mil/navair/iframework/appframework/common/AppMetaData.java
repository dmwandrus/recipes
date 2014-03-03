package mil.navair.iframework.appframework.common;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;



/**
 * This class defines the Application that can be deployed and managed in the 
 * application repository.  
 * This class has all of the JPA Annotations required to persist the 
 * relevant fields to the database.  
 * 
 * @author dandrus
 */
@Entity
@Table(name = "app_meta_data", schema = "p8a_if_aar")
@NamedQueries(
{
    @NamedQuery(name = "AppMetaData.findAll", query = "select app from AppMetaData app"),
    @NamedQuery(name = "AppMetaData.findById", query = "select app from AppMetaData app where app.id = :id")   
})
@XStreamAlias("AppMetaData")
public class AppMetaData //implements Serializable
{

    private static final Logger LOGGER = Logger.getLogger(AppMetaData.class.getName());

    @Id
    @GeneratedValue
    @Column(name = "pk_id")
    private long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "version")
    private String version;
    
    
    @Column(name = "long_name")
    private String longName;
    @Column(name = "organization")
    private String organization;
    @Column(name = "description")
    private String description;
    @Column(name = "author")
    private String author;
    @Column(name = "security_level")
    private String securityLevel;
    @Column(name = "mission_area")
    private String missionArea;
    @Column(name = "functional_area")
    private String functionalArea;
    
    @Column(name = "UUID")
    private UUID uuid;
    
    @ElementCollection
    @CollectionTable(name = "app_tags", 
                     joinColumns = @JoinColumn(name = "pk_id"))
    @Column(name = "tag")
    @XStreamImplicit(itemFieldName = "tag")
    private Set<String> tags = new HashSet<String>();
    
    @ElementCollection
    @CollectionTable(name = "bundle_meta_data",
                     joinColumns = @JoinColumn(name = "app_id"))
    @XStreamImplicit(itemFieldName = "bundle")
    private Set<BundleMetaData> bundles = new HashSet<BundleMetaData>();

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getSecurityLevel()
    {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel)
    {
        this.securityLevel = securityLevel;
    }

    public String getFunctionalArea()
    {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea)
    {
        this.functionalArea = functionalArea;
    }

    public String getMissionArea()
    {
        return missionArea;
    }

    public void setMissionArea(String missionArea)
    {
        this.missionArea = missionArea;
    }

    public void addBundle(File bundle, String checksum, String filename)
    {
        BundleMetaData bmd = new BundleMetaData();
        bmd.setBundleFile(bundle);
        bmd.setExpectedCheckSum(checksum);
        bmd.setFileName(filename);
        bundles.add(bmd);
    }

    public void addBundle(BundleMetaData bmd)
    {
        bundles.add(bmd);
    }

    public Set<BundleMetaData> getBundles()
    {
        return bundles;
    }

    public void setBundles(Set<BundleMetaData> bundles)
    {
        this.bundles = bundles;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOrganization()
    {
        return organization;
    }

    public void setOrganization(String organization)
    {
        this.organization = organization;
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }

    public void addTag(String tag)
    {
        tags.add(tag);
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName(String longName)
    {
        this.longName = longName;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
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
        final AppMetaData other = (AppMetaData) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
        {
            return false;
        }
        if ((this.organization == null) ? (other.organization != null) : !this.organization.equals(other.organization))
        {
            return false;
        }
        if ((this.version == null) ? (other.version != null) : !this.version.equals(other.version))
        {
            return false;
        }
        if ((this.author == null) ? (other.author != null) : !this.author.equals(other.author))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + (this.organization != null ? this.organization.hashCode() : 0);
        hash = 79 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 79 * hash + (this.author != null ? this.author.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();

        if (LOGGER.isLoggable(Level.FINE))
        {
            b.append("\nAppMetaData");
            b.append("\n\tid: ").append(id);
            b.append("\n\tname: ").append(name);
            b.append("\n\torganization: ").append(organization);
            b.append("\n\tversion: ").append(version);
            b.append("\n\tdescription: ").append(description);
            b.append("\n\tauthor: ").append(author);
            b.append("\n\ttags: ").append(tags);
            b.append("\n\tbundles to checksums: ").append(bundles);
            b.append("\n\tlong name: ").append(this.longName);
            b.append("\n\tfunctional area: ").append(this.functionalArea);
            b.append("\n\tsecurity level: ").append(this.securityLevel);
            b.append("\n\tmission area: ").append(this.missionArea);
            b.append("\n\tUUID: ").append(this.uuid);

        } else
        {
            b.append("AppMetaData");
            b.append(": [id -> ").append(id);
            b.append("; name -> ").append(name);
            b.append("; organization -> ").append(organization);
            b.append("; version -> ").append(version);
            b.append("]");
        }


        return b.toString();
    }
    
    public static AppMetaData fromXml(String xml)
    {
        AppMetaData appMetaData = new AppMetaData();
        if(xml != null)
        {
            XStream xStream = new XStream(new DomDriver());
            
            xStream.processAnnotations(AppMetaData.class);
            xStream.processAnnotations(BundleMetaData.class);
            appMetaData = (AppMetaData) xStream.fromXML(xml);
        }
        return appMetaData;
    }
    
    public String toXml()
    {
        XStream xStream = new XStream();
        xStream.processAnnotations(AppMetaData.class);
        xStream.processAnnotations(BundleMetaData.class);
        String toXML = xStream.toXML(this);
        LOGGER.info("XML: \n"+toXML);
        return toXML;
    }
}
