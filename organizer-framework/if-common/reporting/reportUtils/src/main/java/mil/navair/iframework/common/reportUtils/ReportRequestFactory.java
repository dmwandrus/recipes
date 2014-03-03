/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.reportUtils;


import java.util.logging.Level;
import java.util.logging.Logger;
import mil.navair.iframework.common.utilities.DateUtil;

/**
 *
 * @author mweigel
 */
public class ReportRequestFactory {

    private static final String MESSAGE_HEADER = "ReportRequestFactory: ";
    private static final Logger logger = Logger.getLogger(ReportRequestFactory.class.getName());

    public static ReportRequest getReportRequest(ReportType reportType, App parentApp,
            String templateDir, String blanksDir) {

        String timeStamp = DateUtil.getDateString(System.currentTimeMillis(), DateUtil.FORMAT5);
        StringBuilder message = new StringBuilder(MESSAGE_HEADER);

        message.append(" ReportTask created: ");
        message.append(timeStamp);
        message.append("\n");

        // Create a Report Request
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setMessage(message.toString());
        
        reportRequest.setTemplateName(reportType.getTemplate());

        FormatType formatType = reportType.getFormat();
        if (formatType != null) {
            reportRequest.setRenderFormat(formatType.value());
        } else {
            reportRequest.setRenderFormat(FormatType.PDF.value());
        }

        // Append appId to the reportname for later lookup and matching in cache
        StringBuilder reportName = new StringBuilder(reportType.getReportName());
        reportName.append('-');
        reportName.append(parentApp.getAppId());
        reportRequest.setReportName(reportName.toString());

        reportRequest.setDestinationType(reportType.getDestination());
        reportRequest.setDaysPast(reportType.getDaysPast());
        reportRequest.setStartDateTime(reportType.getStartDateTime());
        reportRequest.setEndDateTime(reportType.getEndDateTime());

        // NOTE: Aquire template parameters from the Service App
        if (TemplateEditor.setTemplateParameters(parentApp, reportRequest, templateDir, blanksDir)) {
            logger.log(Level.INFO, MESSAGE_HEADER + "Successfully set template parameters");
        } else {
            logger.log(Level.INFO, MESSAGE_HEADER + "Failed to set template parameters");
        }

        return reportRequest;
    }
}
