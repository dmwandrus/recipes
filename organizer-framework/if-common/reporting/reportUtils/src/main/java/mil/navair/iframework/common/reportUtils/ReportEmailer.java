package mil.navair.iframework.common.reportUtils;

/**
 * @author mweigel
 */
import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import mil.navair.iframework.common.utilities.EncriptAndDecript;
import mil.navair.iframework.common.utilities.FileUtil;
import org.apache.commons.lang3.StringUtils;

public class ReportEmailer {

    private static final boolean USE_MS_OUTLOOK = true; // if false, then gmail
    private static final String OUTLOOK_CONFIG_FILE = "outlookConfig.properties";
    private static final String GMAIL_CONFIG_FILE = "gmailConfig.properties";
    private static final String MESSAGE_HEADER = "ReportEmailer: ";
    private static final String SVG_FORMAT = "svg";
    private static final String GIF_FORMAT = "gif";
    private static final Logger logger = Logger.getLogger(ReportEmailer.class.getName());
    private static Properties configProps = null;
    private static Properties javaMailProperties = null;
    private String subject;
    private String reportPath;
    private String imagePath;
    private String msgText;
    private String smtpHost;
    private String port;
    private String domain;
    private String username;
    private String password;
    private String from;
    private String reportName;
    private byte[] reportBytes;
    private ReportFormatEnum renderFormat;
    private StringBuilder messageText;
    private HashSet<String> emailSet;
    private boolean authen;

    public ReportEmailer() {
        /* This exception is well documented on the www. This solution was the
         * only one that has worked: Please see: https://issues.jboss.org/browse/AS7-1375
         * and https://issues.jboss.org/browse/AS7-5187
         * 
         javax.mail.MessagingException: IOException while sending message;
         nested exception is:
         javax.activation.UnsupportedDataTypeException: no object DCH for MIME type multipart/related;

         Caused by: javax.activation.UnsupportedDataTypeException: no object DCH for MIME type multipart/related;
         boundary="----=_Part_0_1048588440.1349362818237"
         at javax.activation.ObjectDataContentHandler.writeTo(DataHandler.java:877)[:1.6.0_31]
         */
        Thread.currentThread().setContextClassLoader(MimeMultipart.class.getClassLoader());

        // Load in default properties from proerties file
        loadConfigs();

        setJavaMailProperties();
    }

    /**
     * The file path to a report
     *
     * @param reportPath
     */
    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    /**
     * The path to the images
     *
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Set the whole report as a String
     *
     * @param reportBody
     */
    public void setReportBody(byte[] bytes, String reportName, ReportFormatEnum renderFormat) {
        this.reportBytes = bytes;
        this.reportName = reportName;
        this.renderFormat = renderFormat;
    }

    /**
     * comma-separated list of recipient email addresses
     */
    public void setEmailSet(Set<String> emailSet) {
        this.emailSet = (HashSet<String>) emailSet;
    }

    /**
     * The email subject
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * THe email text message
     *
     * @param msgText
     */
    public void setMessageText(String msgText) {
        this.msgText = msgText;
    }

    /**
     * Set "from" email address. This is currently being set from the properties
     * file.
     *
     * @param from Email address
     */
    public void setFrom(String from) {
        this.from = from;
    }

    public boolean sendReport() {
        boolean success = false;

        try {

            Session session = getSession(domain, username, password);
            if (session != null) {

                MimeMessage message = createMessage(session);
                if (message != null) {

                    SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
                    if (transport != null) {

                        if (authen) {
                            transport.connect(smtpHost, username, password);
                        } else {
                            transport.connect();
                        }

                        Address[] addressArray = message.getAllRecipients();
                        for (Address address : addressArray) {
                            if (address == null || address.toString().length() == 0) {
                                throw new MessagingException(MESSAGE_HEADER + "sendReport() Incorrect Address");
                            }
                        }

                        transport.sendMessage(message, message.getAllRecipients());
                        transport.close();
                        success = true;
                    } else {
                        logger.log(Level.SEVERE, MESSAGE_HEADER + "sendReport() SMTPTransport is null!");
                    }
                }
            } else {
                logger.log(Level.SEVERE, MESSAGE_HEADER + "sendReport() session is null!");
            }
        } catch (MessagingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return (success);
    }

    //=======================
    //    Private Methods
    //=======================
    private MimeMessage createMessage(Session session) throws MessagingException {
        MimeMessage message = null;

        if (from != null && subject != null) {
            if (!emailSet.isEmpty()) {
                String mimeType = null;

                message = new MimeMessage(session);

                processEmailSet(message);
                message.setFrom(new InternetAddress(from));
                message.setSubject(subject);

                // This email has 3 parts, the text body, the report 
                // and the embedded images
                MimeMultipart multipart = new MimeMultipart("related");

                // Add the text messsage
                addTextMessage(multipart, 0);

                // For report attachments
                if (reportPath != null) {
                    // Add attachment
                    FileDataSource fds = addAttachment(multipart, 1);
                    mimeType = fds.getContentType();

                    // Add images
                    if (imagePath != null) {
                        addImages(multipart, 2);
                    }
                    // The report as a String body    
                } else if (reportBytes != null && reportBytes.length > 0) {
                    addReportBody(multipart, 1);
                } else {
                    logger.log(Level.SEVERE, MESSAGE_HEADER + "createMessage() Unable to attach the report!");
                }

                // put everything together
                message.setContent(multipart, mimeType);
                message.setSentDate(new Date());
            } else {
                throw new MessagingException(MESSAGE_HEADER + "createMessage() emailSet is empty");
            }
        } else {
            throw new MessagingException(MESSAGE_HEADER + "createMessage() from or subject is null");
        }

        return (message);
    }

    private void addTextMessage(MimeMultipart multipart, int index) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        setDefaultTextMessage();
        messageBodyPart.setContent(msgText, "text/plain");
        multipart.addBodyPart(messageBodyPart, index);
    }

    private void addReportBody(MimeMultipart multipart, int index) throws MessagingException {
        DataSource dataSource = null;
        MimeBodyPart reportBodyPart = new MimeBodyPart();

        switch (this.renderFormat) {
            case HTML:
                reportBodyPart.setContent(new String(reportBytes), "text/html");
                reportBodyPart.setFileName(this.reportName);
                break;
            case PDF:
                dataSource = new ByteArrayDataSource(reportBytes, "application/pdf");
                reportBodyPart.setDataHandler(new DataHandler(dataSource));
                reportBodyPart.setFileName(this.reportName);
                break;
            case EXCEL:
                dataSource = new ByteArrayDataSource(reportBytes, "application/vnd.ms-excel");
                reportBodyPart.setDataHandler(new DataHandler(dataSource));
                reportBodyPart.setFileName(this.reportName);
                break;
        }

        multipart.addBodyPart(reportBodyPart, index);
    }

    private FileDataSource addAttachment(MimeMultipart multipart, int index) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
        FileDataSource fds = new FileDataSource(reportPath);
        mimeBodyPart.setDataHandler(new DataHandler(fds));
        mimeBodyPart.setFileName(fds.getName());
        multipart.addBodyPart(mimeBodyPart, index);
        return (fds);
    }

    private void addImages(MimeMultipart multipart, int index) throws MessagingException {
        try {
            File[] files = FileUtil.getFileArray(imagePath, GIF_FORMAT, SVG_FORMAT);
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile() && files[i].canRead()) {
                        FileDataSource fds = new FileDataSource(files[i].getAbsolutePath());
                        MimeBodyPart mimeBodyPart = new MimeBodyPart();
                        mimeBodyPart.setDataHandler(new DataHandler(fds));
                        mimeBodyPart.setDisposition(MimeBodyPart.INLINE);
                        mimeBodyPart.setFileName(fds.getName());
                        mimeBodyPart.setHeader("Content-ID", "<" + files[i].getName() + ">");
                        multipart.addBodyPart(mimeBodyPart, i + index);
                    } else {
                        logger.log(Level.SEVERE, MESSAGE_HEADER + "addImages() unable to read file!");
                    }
                }
            } else {
                logger.log(Level.SEVERE, MESSAGE_HEADER + "addImages() files is null!");
            }
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private Session getSession(String domain, String username, String password) throws MessagingException {
        Session session = null;

        if (domain != null && username != null && password != null) {

            if (authen) {
                // This may be the prefered authenticator for "some" MS EXCHANGE servers
                //DomainAuthenticator auth = new DomainAuthenticator(domain, username, password);

                SMTPAuthenticator auth = new SMTPAuthenticator(username, password);
                javaMailProperties.put("mail.smtp.auth", "true");
                javaMailProperties.setProperty("mail.smtp.submitter", auth.getPasswordAuthentication().getUserName());
                session = Session.getInstance(javaMailProperties, auth);
            } else {
                javaMailProperties.put("mail.smtp.auth", "false");
                session = Session.getDefaultInstance(javaMailProperties);
            }
        } else {
            throw new MessagingException(MESSAGE_HEADER + "getSession() domain or username or password is null");
        }

        return (session);
    }

    private void loadConfigs() {
        try {
            ClassLoader c = this.getClass().getClassLoader();
            java.net.URL url = null;

            if (USE_MS_OUTLOOK) {
                url = c.getResource(OUTLOOK_CONFIG_FILE);
            } else {
                url = c.getResource(GMAIL_CONFIG_FILE);
            }

            configProps = FileUtil.getProperties(url);

            if (configProps != null) {

                // load settings from config
                smtpHost = configProps.getProperty("smtp.host");
                port = configProps.getProperty("smtp.port", "25");
                username = configProps.getProperty("smtp.user");
                from = configProps.getProperty("smtp.fromAddress");
                password = configProps.getProperty("smtp.password");

                // Test the password to see if it is encrypted
                if (password.endsWith("=")) {
                    password = EncriptAndDecript.decrypt(password);
                } else {
                    String encryptedPassword = EncriptAndDecript.encrypt(password);
                    logger.log(Level.INFO, MESSAGE_HEADER + "Your encrypted password: {0}", encryptedPassword);
                    if (USE_MS_OUTLOOK) {
                        logger.log(Level.INFO, MESSAGE_HEADER + "Please update your config file: {0}", OUTLOOK_CONFIG_FILE);
                    } else {
                        logger.log(Level.INFO, MESSAGE_HEADER + "Please update your config file: {0}", GMAIL_CONFIG_FILE);
                    }
                }

                // for MS Exchange, this should be true
                // for MS Exchange, specify the name of the Windows domain
                domain = configProps.getProperty("smtp.domain", "");
                String authenticate = configProps.getProperty("smtp.authenticate", "true");

                if (authenticate != null && authenticate.equals("true")) {
                    authen = true;
                } else {
                    authen = false;
                }
            } else {
                logger.log(Level.SEVERE, MESSAGE_HEADER + "loadConfigs() Could not load properties");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    // set up JavaMail properties
    private void setJavaMailProperties() {
        javaMailProperties = System.getProperties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.user", username);
        javaMailProperties.put("mail.smtp.host", smtpHost);
        javaMailProperties.put("mail.smtp.port", port);
        javaMailProperties.put("mail.smtp.from", from);
        javaMailProperties.put("mail.smtp.sendpartial", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.socketFactory.port", port);

        if (USE_MS_OUTLOOK) {
            javaMailProperties.put("mail.smtp.starttls.enable", "true");
        } else {
            javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
        javaMailProperties.put("mail.mime.parameters.strict", "false");
    }

    private void setDefaultTextMessage() {
        // Create a default message in case msgText was not set
        messageText = new StringBuilder();

        if (msgText != null) {
            messageText.append(msgText);
        }

        messageText.append("\n\nPlease see your attached reports. These reports have been sent\n");
        messageText.append("to you according to your submitted application request schedule.\n\n");
        msgText = messageText.toString();
    }

    private synchronized void processEmailSet(MimeMessage message) throws AddressException, MessagingException {
        if (emailSet != null) {
            for (String email : emailSet) {
                message.addRecipient(RecipientType.TO, new InternetAddress(email));
            }
        }
    }

    // This class works with gmail
    private static class SMTPAuthenticator extends Authenticator {

        private String username;
        private String password;

        public SMTPAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    // =====================================================================
    // CURRENTLY NOT USED, BUT MAY BE NECESSARY FOR SOME MS Exchange SERVERS
    // =====================================================================
    private static class DomainAuthenticator extends Authenticator {

        private String domain;
        private String username;
        private String password;

        public DomainAuthenticator(String domain, String username,
                String password) {
            super();
            this.domain = domain;
            this.username = username;
            this.password = password;
        }

        //DomainAuthenticator will concatenate the domain and username,
        // separated by "\" - see below
        @Override
        public PasswordAuthentication getPasswordAuthentication() {

            StringBuilder user = new StringBuilder();

            if (!StringUtils.isEmpty(this.domain)) {
                user.append(this.domain).append('\\');
            }

            user.append(username);

            return new PasswordAuthentication(user.toString(), this.password);
        }
    }
}
