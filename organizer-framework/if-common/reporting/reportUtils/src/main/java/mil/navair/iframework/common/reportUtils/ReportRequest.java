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
public class ReportRequest implements Serializable {

    private String renderFormat;
    private String reportName;
    private String templateName;
    private String message;
    private Integer daysPast;
    private String startDateTime;
    private String endDateTime;
    private DestinationType destinationType;

    public Integer getDaysPast() {
        return daysPast;
    }

    public void setDaysPast(Integer daysPast) {
        this.daysPast = daysPast;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setRenderFormat(String renderFormat) {
        this.renderFormat = renderFormat;
    }

    public String getRenderFormat() {
        return renderFormat;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportName() {
        return (this.reportName);
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return (this.templateName);
    }

    public void setDestinationType(DestinationType destinationType) {
        this.destinationType = destinationType;
    }

    public DestinationType getDestinationType() {
        return destinationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
