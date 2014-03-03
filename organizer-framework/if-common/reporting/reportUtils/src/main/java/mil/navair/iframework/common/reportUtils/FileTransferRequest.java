/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.reportUtils;


import java.io.Serializable;

/**
 *
 * @author mweigel
 */
public class FileTransferRequest implements Serializable {
    private String fileName;
    private byte[] fileData;
    private FileDestinationType fileDestinationType;

    public void setFileDestinationType(FileDestinationType fileDestinationType) {
        this.fileDestinationType = fileDestinationType;
    }

    public String getDir() {
        return fileDestinationType.getDir();
    }

    public String getPassword() {
        return fileDestinationType.getPassword();
    }

    public String getServer() {
        return fileDestinationType.getServer();
    }

    public String getUserName() {
        return fileDestinationType.getUserName();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
