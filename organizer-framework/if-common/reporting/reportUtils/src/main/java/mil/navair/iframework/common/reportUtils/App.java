/*
 * * To change this template, choose Tools | Templates
 * * and open the template in the editor.
 * */
package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Framework definition of an App.
 */
public class App implements Serializable {

    /**
     * Framework Generated unique id.
     */
    private Integer appId;
    /**
     * App defined name, must be unique within the framework.
     */
    private String name;
    /**
     * status of app. 
     */
    private ApplicationStatus status;
    /**
     * App defined version.
     */
    private String version;
    /**
     * App defined description.
     */
    private String description;
    /**
     * Framework data field, when this application was installed *
     */
    private Date deploymentDate;
    /**
     * Host name that this app is running on *
     */
    private String appHostName;
    /**
     * Owner/developer of application *
     */
    private String appOwner;
    /**
     * osgi bundles required by the App.
     */
    private Set<AppArchive> archives = new HashSet<AppArchive>();
    /**
     * Allowable permissions to access this App. TODO: Not currently
     * used/implemented/defined.
     */
    private Set<String> permissions = new HashSet<String>();
    /**
     * Targets that the App is registered for. Targets include database targets,
     * messaging targets, webservice targets, etc.
     */
    private Set<MessageTarget> targets = new HashSet<MessageTarget>();
    /**
     * Types of messages that this App produces.
     */
    private Set<MessageType> messageTypes = new HashSet<MessageType>();
    private Set<ReportType> reportTypes = new HashSet<ReportType>();

    /**
     * Get the framework generated application id.
     *
     * @return application id
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * Get the name of the application.
     *
     * @return application name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the status of this application. 
     *
     * @return status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Get the description of this application.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * App defined version of software.
     *
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get the set of artifacts necessary for this application. TODO - not used
     * yet. How would this be used? - provisioning is not to the point where it
     * starts a new karaf instance on a new vm where it can then send the set of
     * bundles over to the new instance. - while some of these archives may be
     * bundles, what if some of them are properties files? features files? Need
     * to figure out how to place items where they belong
     *
     * @return set of artifacts
     */
    public Set<AppArchive> getAppArchives() {
        return archives;
    }

    /**
     * Get the set of allowable actions for this application. TODO - not used
     * yet.
     *
     * @return
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    /**
     * Get the set of targets for this application.
     *
     * @return application targets
     */
    public Set<MessageTarget> getTargets() {
        return targets;
    }

    /**
     * Set the id of this application. WARNING: This is set by the framework and
     * should not be changed by the application.
     *
     * @param id new application id
     */
    public void setAppId(Integer id) {
        this.appId = id;
    }

    /**
     * Set the name of this application. This name is bounded to be only
     * alphanumeric characters and between 10 & 24 characters. This name must
     * also be unique from all the applications currently loaded into the
     * framework.
     *
     * @param name new application name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the description of the application. This is a free text field to
     * allow the application writer to add in any relevant information.
     *
     * @param description description of application
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the version of the application. This is a string field in order to
     * allow any variant desired.
     *
     * Examples: 1.0.12-GA 5 8.4.12 7.2.35-a
     *
     * @param version new version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * This is intended to be used by the framework to adjust the status of an
     * application.
     *
     * @param status new status
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
        
    }

    /**
     * Add an additional application archive to the current collection of
     * archives.
     *
     * @param archive archive to add
     */
    public void addAppArchive(AppArchive archive) {
        archives.add(archive);
    }

    /**
     * Remove an existing application archives from the current collection of
     * archives.
     *
     * @param archive archive to delete
     */
    public void removeAppArchive(AppArchive archive) {
        archives.remove(archive);
    }

    /**
     * Add an additional Message Target to the current collection of targets.
     *
     * @param target target to add
     */
    public void addTarget(MessageTarget target) {
        targets.add(target);
    }

    /**
     * Remove an existing Message Target from the current collection of targets.
     *
     * @param target target to delete
     */
    public void removeTarget(MessageTarget target) {
        targets.remove(target);
    }

    /**
     * Add an additional permission/role to the set of permissions.
     *
     * @param permission permission to add
     */
    public void addPermission(String permission) {
        permissions.add(permission);
    }

    /**
     * Remove a permission/role from the set of permissions.
     *
     * @param permission permission to remove
     */
    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    /**
     * Replace the set of Message Targets with the new Set of targets.
     *
     * @param targets new target set
     */
    public void setTargets(Set<MessageTarget> targets) {
//        this.targets.clear();
//        this.targets.addAll(targets);
        this.targets = targets;
    }

    /**
     * Replace the set of permissions with the new set of permissions
     *
     * @param permissions new permissions/roles
     */
    public void setPermissions(Set<String> permissions) {
//        this.permissions.clear();
//        this.permissions.addAll(permissions);
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Application ID: ").append(appId);
        b.append("\nApplication name: ").append(name);
        b.append("\nApp status: ").append(status);
        b.append("\nApp version: ").append(version);
        b.append("\nApp desc: ").append(description);

        for (String permission : permissions) {
            b.append("\nPermission: ").append(permission);
        }

        for (MessageTarget t : targets) {
            b.append("\nApp Target: ").append(t);
        }

        for (MessageType mt : messageTypes) {
            b.append("\nMessageType: ").append(mt);
        }

        for (ReportType rt : reportTypes) {
            b.append("\nReportType: ").append(rt);
        }


        return b.toString();
    }

    /**
     * Get the types of messages that this application is registered to receive.
     *
     * @return registered message types.
     */
    public Set<MessageType> getMessageTypes() {
        // TODO - determine whether or not to do defensive copies of 
        //        collections.  Also, recall that this class is persisted 
        //        by hibernate and hibernate is 'so smart' and doesn't always
        //        allow you to follow good programming practice.  
//        return Collections.unmodifiableSet(messageTypes);
        return messageTypes;
    }

    /**
     * Set the types of messages that this application should receive.
     *
     * @param messageTypes new message types
     */
    public void setMessageTypes(Set<MessageType> messageTypes) {
//		this.messageTypes.clear();
//        this.messageTypes.addAll(messageTypes);
        this.messageTypes = messageTypes;
    }

    /**
     * Get the host name that this application is deployed on
     *
     * @return application host name
     */
    public String getAppHostName() {
        return appHostName;
    }

    /**
     * Set the host name that this application is deployed on. This should be
     * set during the deployment of the application during the installation
     * process.
     *
     * @param appHostName host name where application lives
     */
    public void setAppHostName(String appHostName) {
        this.appHostName = appHostName;
    }

    /**
     * Get the owner of the application. This could be a third party company or
     * the framework. This is here to identify the party responsible for the
     * maintenance of this application.
     *
     * @return application owner.
     */
    public String getAppOwner() {
        return appOwner;
    }

    /**
     * Set the owner of the application.
     *
     * @param appOwner
     */
    public void setAppOwner(String appOwner) {
        this.appOwner = appOwner;
    }

    /**
     * Get the collection of application artifacts
     *
     * @return application archives
     */
    public Set<AppArchive> getArchives() {
        return archives;
    }

    /**
     * Set the collection of application artifacts.
     *
     * @param archives application archives
     */
    public void setArchives(Set<AppArchive> archives) {
        this.archives = archives;
    }

    /**
     * Get the date that this application was deployed.
     *
     * @return deployment date
     */
    public Date getDeploymentDate() {
        return deploymentDate;
    }

    /**
     * Set the date that this application was deployed. This is intended for the
     * framework to set upon application provisioning.
     *
     * @param deploymentDate new deployment date.
     */
    public void setDeploymentDate(Date deploymentDate) {
        this.deploymentDate = deploymentDate;
    }

    public Set<ReportType> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(Set<ReportType> reportTypes) {
        this.reportTypes = reportTypes;
    }
}
