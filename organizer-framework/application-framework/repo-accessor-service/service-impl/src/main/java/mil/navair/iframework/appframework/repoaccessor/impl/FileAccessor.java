package mil.navair.iframework.appframework.repoaccessor.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

/**
 * Wrapper around Apache Commons IO for easy file access.
 *
 * This makes heavy use of the Apache Commons IO library to ease annoyances of
 * actual file access and byte transferring, etc.
 *
 * All methods wrap the underlying exceptions with a FileAccessException.
 *
 * @author dandrus
 */
public class FileAccessor
{

    private static final Logger LOGGER = Logger.getLogger(FileAccessor.class.getName());

    public void createDirectory(String directory) throws FileAccessException
    {
        createDirectory(new File(directory));
    }
    
    /**
     * Creates the Directory, if it doesn't already exist.
     *
     * @param destDirectory Directory to create
     * @throws FileAccessException if anything fails
     */
    public void createDirectory(File destDirectory) throws FileAccessException
    {
        if (!destDirectory.exists())
        {
            try
            {
                LOGGER.log(Level.FINE, "Creating Directory: {0}", destDirectory);
                FileUtils.forceMkdir(destDirectory);
            } catch (IOException ex)
            {
                LOGGER.log(Level.FINE, "Unable to create directroy: " + destDirectory.getAbsolutePath(), ex);
                throw new FileAccessException("unable to create directory", ex);
            }
        }
    }
    
    public void deleteDirectory(String directory) throws FileAccessException
    {
        deleteDirectory(new File(directory));
    }

    /**
     * Delete the directory.
     *
     * @param destDirectory
     * @throws FileAccessException
     */
    public void deleteDirectory(File destDirectory) throws FileAccessException
    {
        try
        {
            LOGGER.log(Level.FINE, "Deleting Directory: {0}", destDirectory);
            FileUtils.deleteDirectory(destDirectory);
        } catch (IOException ex)
        {
            LOGGER.log(Level.FINE, "Unable to delete directroy: " + destDirectory.getAbsolutePath(), ex);
            throw new FileAccessException("unable to delete directory", ex);
        }
    }

    /**
     * Save all of the files to the destination directory. Assumes the directory
     * exists.
     *
     * @param files collection of files to save
     * @param directoryLocation destination directory
     * @throws FileAccessException if anything goes wrong, or if destination
     * directory does not exist.
     */
    public void saveFiles(Collection<File> files, String directoryLocation) throws FileAccessException
    {
        File destDirectory = new File(directoryLocation);
        if (!destDirectory.exists())
        {
            throw new FileAccessException("Directory " + directoryLocation + " does not exist");
        }
        if (!destDirectory.isDirectory())
        {
            throw new FileAccessException("Location: '" + directoryLocation + "' is not a directory");
        }

        // Ensuring that the save will work properly, even if the string for the
        // directory location is missing the trailing slash.
        if (!directoryLocation.endsWith("/"))
        {
            directoryLocation = directoryLocation + "/";
        }

        // Save files
        for (File file : files)
        {
            try
            {

                LOGGER.log(Level.FINE, "Saving File: {0} to {1}", new Object[]
                        {
                            file.getName(), directoryLocation
                        });
                File destFile = new File(directoryLocation + file.getName());
                FileUtils.copyFile(file, destFile);
            } catch (IOException ex)
            {
                LOGGER.log(Level.FINE, "Unable to write file: " + file.getName(), ex);
                throw new FileAccessException("Unable to write files to app repository.", ex);
            }
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
        LOGGER.log(Level.FINE, "Retrieving File at: {0}", fileLocation);
        File file = FileUtils.getFile(fileLocation);
        if (file == null)
        {
            LOGGER.log(Level.FINE, "No file exists at: {0}", fileLocation);
            throw new FileAccessException("No file exists at: " + fileLocation);
        }
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
        File destDirectory = new File(directoryLocation);
        return getFiles(destDirectory);
    }

    /**
     * Get all files in the given directory location
     *
     * @param directoryLocation location of a directory with files
     * @return Collection of Files
     * @throws FileAccessException if anything goes wrong
     */
    public Collection<File> getFiles(File destDirectory) throws FileAccessException
    {

        if (!destDirectory.exists())
        {
            LOGGER.log(Level.FINE, "Unable to retrieve files in directory: {0}, as it doesn''t exist", destDirectory.getAbsoluteFile());
            throw new FileAccessException("Unable to retrieve files in directory: " + destDirectory.getAbsolutePath() + ", as it doesn't exist");
        }
        LOGGER.log(Level.FINE, "Retrieving Files from: {0}", destDirectory);
        Collection<File> allFiles = FileUtils.listFiles(destDirectory, FileFileFilter.FILE, null);
        return allFiles;
    }
}
