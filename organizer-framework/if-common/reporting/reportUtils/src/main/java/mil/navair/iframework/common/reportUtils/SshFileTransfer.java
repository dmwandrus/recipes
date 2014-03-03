package mil.navair.iframework.common.reportUtils;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.navair.iframework.common.utilities.EncriptAndDecript;

/**
 *
 * @author mweigel
 */
public class SshFileTransfer {

    private static final int PORT = 22;
    private static final boolean PRINT_TIME_STAMP = true;
    private static final String MESSAGE_HEADER = "SshFileTransfer: ";
    private static final Logger logger = Logger.getLogger(SshFileTransfer.class.getName());

    public static synchronized void writeFile(FileTransferRequest fileTransferRequest) {
        FileInputStream fis = null;
        try {
            String lfile = fileTransferRequest.getFileName();
            String rfile = fileTransferRequest.getDir() + "/" + lfile;
            String host = fileTransferRequest.getServer();
            String user = fileTransferRequest.getUserName();
            String password = EncriptAndDecript.decrypt(fileTransferRequest.getPassword());

            JSch jsch = new JSch();

            Session session = jsch.getSession(user, host, PORT);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.setPassword(password);
            session.connect();

            // exec 'scp -t rfile' remotely
            String command = "scp " + (PRINT_TIME_STAMP ? "-p" : "") + " -t " + rfile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if (checkForSuccess(in)) {
                if (PRINT_TIME_STAMP) {
                    long seconds = System.currentTimeMillis() / 1000;
                    command = "T " + seconds + " 0";
                    // The access time should be sent here,
                    // but it is not accessible with JavaAPI ;-<
                    command += (" " + seconds + " 0\n");
                    out.write(command.getBytes());
                    out.flush();
                }

                if (checkForSuccess(in)) {
                    byte[] bytes = fileTransferRequest.getFileData();

                    // send "C0644 filesize filename", where filename should not include '/'
                    long filesize = bytes.length;
                    command = "C0644 " + filesize + " ";
                    command += lfile;
                    command += "\n";
                    out.write(command.getBytes());
                    out.flush();
                    if (checkForSuccess(in)) {
                        out.write(bytes);
                        out.flush();
                        out.close();
                    }
                }
            }
         
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            System.out.println(e);
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ee) {
            }
        }
    }

    static boolean checkForSuccess(InputStream in) throws IOException {

        int b = in.read();
        // b may be 0 for success, 1 for error, 2 for fatal error, or -1
        if (b == 1 || b == 2) {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');

            if (b == 1 || b == 2) { // error
                logger.log(Level.SEVERE, MESSAGE_HEADER + "{0}", sb.toString());
            }
        }

        return b == 0;
    }
}