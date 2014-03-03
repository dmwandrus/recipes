/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.appframework.repoaccessor.impl;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a helper class to the repository accessor. This class will provide
 * CRUD operations to the file system for application repository. If it is
 * unable to complete an action, then it will rollback whatever changes that is
 * has made.
 *
 * All actions are at a directory level.
 * 
 * This should probably handle to 95% solution.  I am not guaranteeing 
 * 100% perfect, atomic, but, the majority of the function of the overall
 * system is not file interactions.  
 *
 * @author dandrus
 */
public class TransactedFileAccessor
{

    private static final Logger LOGGER = Logger.getLogger(TransactedFileAccessor.class.getName());
    private FileAccessor fileAccessor = new FileAccessor();

    protected void setFileAccessor(FileAccessor fileAccessor)
    {
        this.fileAccessor = fileAccessor;
    }

    /**
     * Delete the directory.
     *
     * @param destDirectory
     * @throws FileAccessException
     */
    public void deleteDirectory(File destDirectory) throws FileAccessException
    {

        // Pre-delete:  get the files & save to temp location
        // Overhead of transactional support...
        String tempDir = "/tmp/fileAccessor/" + UUID.randomUUID().toString();
        Collection<File> files = fileAccessor.getFiles(destDirectory);

        try
        {
            fileAccessor.createDirectory(tempDir);
            fileAccessor.saveFiles(files, tempDir);
            files = fileAccessor.getFiles(tempDir);
        } catch (FileAccessException ex)
        {
            LOGGER.log(Level.WARNING, "Unable to backup directory contents", ex);
        }

        try
        {
            LOGGER.log(Level.FINE, "Deleting directory: {0}", destDirectory.getAbsolutePath());

            fileAccessor.deleteDirectory(destDirectory);

        } catch (FileAccessException ex)
        {
            // loop through copied directory & re-copy them into the directory.
            fileAccessor.createDirectory(destDirectory);
            fileAccessor.saveFiles(files, destDirectory.getAbsolutePath());

            throw new FileAccessException("Unable to delete directory: " + destDirectory.getAbsolutePath(), ex);
        }

    }

    /**
     * Save all of the files to the destination directory.
     *
     * @param files collection of files to save
     * @param directoryLocation destination directory
     * @throws FileAccessException if anything goes wrong
     */
    public void saveFiles(Collection<File> files, String directoryLocation) throws FileAccessException
    {
        File destDirectory = new File(directoryLocation);
        fileAccessor.createDirectory(destDirectory);

        // Save files
        try

        {
            fileAccessor.saveFiles(files, directoryLocation);
        } catch (FileAccessException fae)
        {
            // Rollback
            StringBuilder b = new StringBuilder();
            b.append("Unable to write files to app repository. ");
            b.append("Rolling back...");
            deleteDirectory(destDirectory);
            b.append("Deleted Directory ").append(destDirectory);
            throw new FileAccessException(b.toString(), fae);
        }
    }

    /**
     * Get the file at the given location
     *
     * @param fileLocation location of file to retrieve
     * @return requested File
     * @throws FileAccessException if anything goes wrong
     */
    public File getFile(String fileLocation) throws FileAccessException
    {
        File file = fileAccessor.getFile(fileLocation);
        return file;
    }

    /**
     * Get all files in the given directory location
     *
     * @param directoryLocation location of a directory with files
     * @return Collection of Files
     * @throws FileAccessException if anything goes wrong
     */
    public Collection<File> getFiles(String directoryLocation) throws FileAccessException
    {
        Collection<File> allFiles = fileAccessor.getFiles(directoryLocation);
        return allFiles;
    }
}
