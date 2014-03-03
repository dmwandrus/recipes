package mil.navair.iframework.common.reportUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.navair.iframework.common.utilities.*;


/**
 *
 * @author mweigel
 */
public class TemplateEditor {

    private static final int MILLIS_PER_DAY = 86400000;
    private static final Logger logger = Logger.getLogger(TemplateEditor.class.getName());
    private static final String MESSAGE_HEADER = "TemplateEditor: ";
    private static final String DATABASE = "DATABASE";
    private static final String HOST = "HOST";
    private static final String PORT = "PORT";
    private static final String START_DATE_STRING = "START_DATE_STRING";
    private static final String END_DATE_STRING = "END_DATE_STRING";

    public static boolean setTemplateParameters(App app, ReportRequest reportRequest,
            String templateDir, String blanksDir) {

        boolean success = false;

        // Aquire template parameters from the Service App
        if (app != null) {
            try {
                String targetLocation = null;
                Integer targetPort = null;
                String targetHostname = null;

                Set<MessageTarget> targetSet = app.getTargets();
                if (targetSet != null) {
                    for (MessageTarget target : targetSet) {
                        if (target.getTargetType() == TargetType.DATABASE) {
                            targetHostname = target.getHostname();
                            targetPort = target.getPort();
                            targetLocation = target.getLocation();
                            break;
                        }
                    }

                    if (targetLocation != null && targetPort != null && targetHostname != null) {
                        if (blanksDir != null && templateDir != null) {

                            // Add the template parameters to template blanks
                            TemplateEditor.edit(blanksDir, templateDir, targetHostname,
                                    String.valueOf(targetPort), targetLocation, reportRequest);
                            success = true;
                        } else {
                            logger.log(Level.SEVERE, MESSAGE_HEADER
                                    + "setTemplateParameters() templateBlankDir or templateDir is null");
                        }
                    } else {
                        logger.log(Level.SEVERE, MESSAGE_HEADER + "setTemplateParameters() parameter(s) is null");
                    }
                } else {
                    logger.log(Level.SEVERE, MESSAGE_HEADER + "setTemplateParameters() targetSet is null");
                }
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

        return (success);
    }

    private static void edit(String blanksDir, String templateDir, String host,
            String port, String database, ReportRequest reportRequest) throws FileNotFoundException, IOException {

        String startDateTime = reportRequest.getStartDateTime();
        String endDateTime = reportRequest.getEndDateTime();
        Integer daysPast = reportRequest.getDaysPast();

        // Delete the old templates
        FileUtil.deleteFiles(templateDir, "rptdesign");

        // Get the list of template blank files.
        File[] fileList = FileUtil.getFileArray(blanksDir, "rptdesign");
        ArrayList<String> newList = new ArrayList<String>();

        if (fileList != null) {
            for (File file : fileList) {
                List<String> fileLineList = FileUtil.fileToStringList(file);
                if (fileLineList != null) {

                    // Here the body of the template is contained
                    // in fileLineList and is edited
                    boolean ahead = false;
                    for (String line : fileLineList) {
                        if (line.contains("<data-sources>") || line.contains("<data-sets>")) {
                            ahead = true;
                        }

                        // The order here is important
                        if (ahead && line.contains("odaURL")) {
                            line = line.replace(HOST, host);
                            line = line.replace(PORT, port);
                            line = line.replace(DATABASE, database);
                            ahead = false;
                        }


                        // Edit the data time filter
                        if (ahead && line.contains("START_DATE_STRING")) {

                            // Days past takes precidence. We do not do both.
                            if (daysPast != null && daysPast > 0) {
                                long currentTime = System.currentTimeMillis();
                                long pastTime = currentTime - (daysPast * MILLIS_PER_DAY);
                                startDateTime = DateUtil.getDateString(pastTime, DateUtil.FORMAT2);
                                endDateTime = DateUtil.getDateString(currentTime, DateUtil.FORMAT2);
                                line = line.replace(START_DATE_STRING, startDateTime);
                                line = line.replace(END_DATE_STRING, endDateTime);
                            } else {
                                if (startDateTime != null && endDateTime != null && !startDateTime.equals(endDateTime)) {
                                    line = line.replace(START_DATE_STRING, startDateTime);
                                    line = line.replace(END_DATE_STRING, endDateTime);
                                } else if (startDateTime != null && endDateTime == null) {
                                    endDateTime = DateUtil.getDateString(System.currentTimeMillis(), DateUtil.FORMAT2);
                                    line = line.replace(START_DATE_STRING, startDateTime);
                                    line = line.replace(END_DATE_STRING, endDateTime);
                                } else if (endDateTime != null && startDateTime == null) {
                                    startDateTime = DateUtil.getDateString(0, DateUtil.FORMAT2);
                                    line = line.replace(START_DATE_STRING, startDateTime);
                                    line = line.replace(END_DATE_STRING, endDateTime);
                                } else { // Everything
                                    startDateTime = DateUtil.getDateString(0, DateUtil.FORMAT2);
                                    endDateTime = DateUtil.getDateString(System.currentTimeMillis(), DateUtil.FORMAT2);
                                    line = line.replace(START_DATE_STRING, startDateTime);
                                    line = line.replace(END_DATE_STRING, endDateTime);
                                }
                            }
                            ahead = false;
                        }

                        // The rest of the template is also loaded
                        newList.add(line);
                    }

                    // Now write the new Template to the actual template dir
                    StringBuilder builder = new StringBuilder(templateDir);
                    builder.append("/");
                    builder.append(file.getName());

                    File newFile = new File(builder.toString());
                    FileUtil.stringListToFile(newFile, newList);

                    // Help out the garbage collector :)                
                    fileLineList.clear();
                    fileLineList = null;

                    // Re-use the list
                    newList.clear();
                } else {
                    logger.log(Level.SEVERE, MESSAGE_HEADER + "edit() fileLineList is null");
                    throw new IOException(MESSAGE_HEADER + "edit() fileLineList is null");
                }
            }
        } else {
            logger.log(Level.SEVERE, MESSAGE_HEADER + "edit() fileList is null");
            throw new FileNotFoundException(MESSAGE_HEADER + "edit() fileList is null");
        }
    }
}
