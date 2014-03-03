package mil.navair.iframework.common.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA. User: mweigel Date: 5/31/12 Time: 9:09 AM To
 * change this template use File | Settings | File Templates.
 */
public class FileUtil {

    private static final Logger logger = Logger.getLogger(FileUtil.class.getName());

    public static File[] getFileArray(String dirName, String... exts) throws FileNotFoundException, IOException {
        ArrayList<File> fileArrayList = new ArrayList<File>();

        if (dirName != null) {
            File directory = new File(dirName);
            if (directory.exists()) {
                if (directory.canRead()) {
                    for (String ext : exts) {
                        FilenameFilter only = new OnlyExt(ext);
                        File[] fileList = directory.listFiles(only);
                        for (File file : fileList) {
                            fileArrayList.add(file);
                        }
                    }
                } else {
                    logger.log(Level.SEVERE, "Directory: {0} can not be read!", dirName);
                    throw new IOException("FileUtil: getFileArray() Directory can not be read!");
                }
            } else {
                logger.log(Level.SEVERE, "Directory: {0} does not exist!", dirName);
                throw new FileNotFoundException("FileUtil: getFileArray() Directory does not exist!");
            }
        } else {
            logger.log(Level.SEVERE, "dirName is null", dirName);
            throw new IOException("FileUtil: getFileArray() dirName is null");
        }

        return (fileArrayList.toArray(new File[fileArrayList.size()]));
    }

    public static void deleteFiles(String dirName, String... exts) throws FileNotFoundException, IOException {

        if (dirName != null) {
            File directory = new File(dirName);
            if (directory.exists()) {
                if (directory.canRead()) {
                    for (String ext : exts) {
                        FilenameFilter only = new OnlyExt(ext);
                        File[] fileList = directory.listFiles(only);
                        for (File file : fileList) {
                            if (file.canWrite() && !file.getAbsoluteFile().delete()) {
                                logger.log(Level.SEVERE, "File: {0} could not be deleted!", file.getName());
                                throw new IOException("FileUtil: deleteFiles() File could not be deleted!");
                            }
                        }
                    }
                } else {
                    logger.log(Level.SEVERE, "Directory: {0} can not be read!", dirName);
                    throw new IOException("FileUtil: deleteFiles() Directory can not be read!");
                }
            } else {
                logger.log(Level.SEVERE, "Directory: {0} does not exist!", dirName);
                throw new FileNotFoundException("FileUtil: deleteFiles() Directory does not exist!");
            }
        } else {
            logger.log(Level.SEVERE, "dirName is null", dirName);
            throw new IOException("FileUtil: deleteFiles() dirName is null");
        }
    }

    public static void deleteFile(File file) throws IOException {
        if (file.canWrite() && !file.getAbsoluteFile().delete()) {
            logger.log(Level.SEVERE, "File: {0} could not be deleted!", file.getName());
            throw new IOException("FileUtil: deleteFiles() File could not be deleted!");
        }
    }

    public static void writeFile(String dirName, String fileName, byte[] bytes) throws FileNotFoundException, IOException {
        if (dirName != null) {
            File directory = new File(dirName);
            if (directory.exists()) {
                if (directory.canRead() && directory.canWrite()) {
                    FileOutputStream fileOutputStream = null;
                    BufferedOutputStream bufferedOutputStream = null;
                    try {
                        StringBuilder filePathName = new StringBuilder(dirName);
                        filePathName.append("/");
                        filePathName.append(fileName);
                        File file = new File(filePathName.toString());

                        fileOutputStream = new FileOutputStream(file);
                        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                        bufferedOutputStream.write(bytes, 0, bytes.length);
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                        fileOutputStream.close();
                    } catch (FileNotFoundException ex) {
                        logger.log(Level.SEVERE, null, ex);
                        throw new FileNotFoundException("FileUtil: writeFile()");
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                        throw new IOException("FileUtil: writeFile()");
                    } finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }

                            if (bufferedOutputStream != null) {
                                bufferedOutputStream.close();
                            }
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, null, ex);
                            throw new IOException("FileUtil: writeFile()");
                        }
                    }
                } else {
                    logger.log(Level.SEVERE, "Directory: {0} can not be read!", dirName);
                    throw new IOException("FileUtil: writeFile() Directory can not be read!");
                }
            } else {
                logger.log(Level.SEVERE, "Directory: {0} does not exist!", dirName);
                throw new FileNotFoundException("FileUtil: writeFile() Directory does not exist!");
            }
        } else {
            logger.log(Level.SEVERE, "dirName is null", dirName);
            throw new IOException("FileUtil: writeFile() dirName is null");
        }
    }

    public static void writeFile(FileOutputStream fileOutputStream, byte[] bytes) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(bytes, 0, bytes.length);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        fileOutputStream.close();
    }

    public static void writeFile(String dirName, String fileName, Object obj) throws FileNotFoundException, IOException {
        if (dirName != null) {
            File directory = new File(dirName);
            if (directory.exists()) {
                if (directory.canRead() && directory.canWrite()) {
                    FileOutputStream fileOutputStream = null;
                    ObjectOutputStream objectOutputStream = null;
                    try {
                        StringBuilder filePathName = new StringBuilder(dirName);
                        filePathName.append("/");
                        filePathName.append(fileName);
                        File file = new File(filePathName.toString());

                        fileOutputStream = new FileOutputStream(file);
                        objectOutputStream = new ObjectOutputStream(fileOutputStream);

                        objectOutputStream.writeObject(obj);
                        objectOutputStream.flush();
                        objectOutputStream.close();
                        fileOutputStream.close();


                    } catch (FileNotFoundException ex) {
                        logger.log(Level.SEVERE, null, ex);
                        throw new FileNotFoundException("FileUtil: writeFile()");
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                        throw new IOException("FileUtil: writeFile()");
                    } finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }

                            if (objectOutputStream != null) {
                                objectOutputStream.close();
                            }
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, null, ex);
                            throw new IOException("FileUtil: writeFile()");
                        }
                    }
                } else {
                    logger.log(Level.SEVERE, "Directory: {0} can not be read!", dirName);
                    throw new IOException("FileUtil: writeFile() Directory can not be read!");
                }
            } else {
                logger.log(Level.SEVERE, "Directory: {0} does not exist!", dirName);
                throw new FileNotFoundException("FileUtil: writeFile() Directory does not exist!");
            }
        } else {
            logger.log(Level.SEVERE, "dirName is null", dirName);
            throw new IOException("FileUtil: writeFile() dirName is null");
        }
    }

    public static File readFile(String dirName, String fileName) throws FileNotFoundException, IOException {
        File file = null;

        if (dirName != null) {
            File directory = new File(dirName);
            if (directory.exists()) {
                if (directory.canRead()) {
                    StringBuilder filePathName = new StringBuilder(dirName);
                    filePathName.append("/");
                    filePathName.append(fileName);
                    file = new File(filePathName.toString());
                } else {
                    logger.log(Level.SEVERE, "Directory: {0} can not be read!", dirName);
                    throw new IOException("FileUtil: readFile() Directory can not be read!");
                }
            } else {
                logger.log(Level.SEVERE, "Directory: {0} does not exist!", dirName);
                throw new FileNotFoundException("FileUtil: readFile() Directory does not exist!");
            }
        } else {
            logger.log(Level.SEVERE, "dirName is null", dirName);
            throw new IOException("FileUtil: readFile() dirName is null");
        }

        return file;
    }

    public static byte[] readFile(FileInputStream fileInputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (fileInputStream != null) {
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) != -1) {
                byteArrayOutputStream.write(buffer);
            }
        } else {
            logger.log(Level.SEVERE, "fileInputStream is null");
            throw new IOException("FileUtil: readFile() fileInputStream is null");
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] fileToBytes(File file) throws FileNotFoundException, IOException {
        byte[] bytes = null;

        if (file.exists() && file.canRead()) {
            FileInputStream infile = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(infile);

            int len = (int) file.length();
            bytes = new byte[len];
            bis.read(bytes, 0, len);
            bis.close();
            infile.close();
        }

        return (bytes);
    }

    public static Object fileToObject(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        Object obj = null;

        if (file.exists() && file.canRead()) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            obj = ois.readObject();
            ois.close();
            fis.close();
        }

        return (obj);
    }

    @SuppressWarnings("NestedAssignment")
    public static String fileToString(File file) throws IOException {
        StringBuilder strBuilder = new StringBuilder();

        if (file.exists() && file.canRead()) {
            FileInputStream infile = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(infile);
            BufferedReader in = new BufferedReader(new InputStreamReader(bis));

            @SuppressWarnings("UnusedAssignment")
            String line = null;
            while ((line = in.readLine()) != null) {
                strBuilder.append(line.trim());
            }

            in.close();
            bis.close();
            infile.close();
        }

        return (strBuilder.toString());
    }

    public static List<String> fileToStringList(File file) throws FileNotFoundException, IOException {
        ArrayList<String> arrayList = null;

        if (file.exists() && file.canRead()) {
            FileInputStream infile = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(infile);
            BufferedReader in = new BufferedReader(new InputStreamReader(bis));

            arrayList = new ArrayList<String>();

            @SuppressWarnings("UnusedAssignment")
            String line = null;
            while ((line = in.readLine()) != null) {
                arrayList.add(line);
            }

            in.close();
            bis.close();
            infile.close();
        }

        return (arrayList);
    }

    public static void stringListToFile(File file, List<String> list) throws FileNotFoundException, IOException {

        if (file != null && list != null) {
            FileOutputStream outfile = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(outfile);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(bos));

            for (String line : list) {
                bufferedWriter.write(line, 0, line.length());
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            bufferedWriter.close();
            bos.close();
            outfile.close();
        } else {
            throw new IOException("FileUtil: stringListToFile() file OR list is null!");
        }
    }

    public static Properties getProperties(URL url) throws IOException {
        InputStream in = null;
        Properties configProps = null;

        try {
            logger.log(Level.INFO, "PATH: resolved name = {0}", url);
            in = url.openStream();
            configProps = new Properties();
            configProps.load(in);
            in.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new IOException("FileUtil: getProperties()");
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                throw new IOException("FileUtil: getProperties() could not close InputStream");
            }
        }

        return (configProps);


    }

    private static class OnlyExt implements FilenameFilter {

        String ext;

        public OnlyExt(String ext) {
            this.ext = "." + ext;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(ext);
        }
    }
}
