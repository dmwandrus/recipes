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
public class FileDestinationType implements Serializable {

    private String userName;
    private String password;
    private String server;
    private String dir;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "\nFile Destination ["
                + "\n\tuserName => " + userName
                + "\n\tpassword => " + password
                + "\n\tserver => " + server
                + "\n\tdir => " + dir + ']';
    }
}
