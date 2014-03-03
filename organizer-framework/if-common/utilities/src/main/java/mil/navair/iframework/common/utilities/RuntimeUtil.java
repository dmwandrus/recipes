/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mweigel
 */
public class RuntimeUtil {

    @SuppressWarnings("NestedAssignment")
    public static boolean isProcess(String name) {
        boolean status = false;
        @SuppressWarnings("UnusedAssignment")
        String processString = null;

        try {
            // run the Unix "ps -ef" command
            Process p = Runtime.getRuntime().exec("ps -ef");

            p.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // read the output from the command
            while ((processString = stdInput.readLine()) != null) {
                if (status = processString.contains(name)) {
                    break;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RuntimeUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RuntimeUtil.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        return status;
    }

    public static Process startProcess(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);

            // We DO NOT WAIT FOR
        } catch (IOException ex) {
            Logger.getLogger(RuntimeUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (process);
    }
}
