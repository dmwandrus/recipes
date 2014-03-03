package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;
import java.util.Set;

public class ReportType implements Serializable {

    private Integer pkId;
    private String reportName;
    private String template;
    private FormatType format;
    private ScheduleType schedule;
    private DestinationType destination;
    private DateTimeFilterType dateTimeFilter;

    public ReportType() {
        schedule = new ScheduleType();
        destination = new DestinationType();
        dateTimeFilter = new DateTimeFilterType();
    }

    public Integer getDaysPast() {
        if (dateTimeFilter == null) {
            dateTimeFilter = new DateTimeFilterType();
        }

        return dateTimeFilter.getDaysPast();
    }

    public void setDaysPast(Integer daysPast) {
        if (dateTimeFilter == null) {
            dateTimeFilter = new DateTimeFilterType();
        }
        dateTimeFilter.setDaysPast(daysPast);
    }

    public String getStartDateTime() {
        return dateTimeFilter.getStartDateTime();
    }

    public void setStartDateTime(String startDateTime) {
        dateTimeFilter.setStartDateTime(startDateTime);
    }

    public String getEndDateTime() {
        return dateTimeFilter.getEndDateTime();
    }

    public void setEndDateTime(String endDateTime) {
        dateTimeFilter.setEndDateTime(endDateTime);
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    /**
     * Gets the value of the reportName property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * Sets the value of the reportName property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setReportName(String value) {
        this.reportName = value;
    }

    /**
     * Gets the value of the template property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Sets the value of the template property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setTemplate(String value) {
        this.template = value;
    }

    /**
     * Gets the value of the format property.
     *
     * @return possible object is {@link FormatType }
     *
     */
    public FormatType getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     *
     * @param value allowed object is {@link FormatType }
     *
     */
    public void setFormat(FormatType value) {
        this.format = value;
    }

    /**
     * Gets the value of the schedule property.
     *
     * @return possible object is {@link ScheduleType }
     *
     */
    public ScheduleType getSchedule() {
        return schedule;
    }

    /**
     * Sets the value of the schedule property.
     *
     * @param value allowed object is {@link ScheduleType }
     *
     */
    public void setSchedule(ScheduleType value) {
        this.schedule = value;
    }

    public boolean isScheduleOnDemand() {

        return schedule.isOnDemand();
    }

    public void setScheduleOnDemand(Boolean value) {
        schedule.setOnDemand(value);
    }

    public Set<String> getTimeSet() {
        return schedule.getTime();
    }

    public void setTimeSet(Set<String> timeSet) {
        schedule.setTime(timeSet);
    }

    public Set<DayType> getDaySet() {
        return schedule.getDay();
    }

    public void setDaySet(Set<DayType> daySet) {
        schedule.setDay(daySet);
    }

    /**
     * Gets the value of the destination property.
     *
     * @return possible object is {@link DestinationType }
     *
     */
    public DestinationType getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     *
     * @param value allowed object is {@link DestinationType }
     *
     */
    public void setDestination(DestinationType value) {
        this.destination = value;
    }

    public void setEmails(Set<String> emails) {
        destination.setEmail(emails);
    }

    public Set<String> getEmails() {
        return destination.getEmail();
    }

    public void setFiles(Set<FileDestinationType> files) {
        destination.setFileDestination(files);
    }

    public Set<FileDestinationType> getFiles() {
        return destination.getFileDestination();
    }

    public DateTimeFilterType getDateTimeFilter() {
        return dateTimeFilter;
    }

    public void setDateTimeFilter(DateTimeFilterType dateTimeFilter) {
        this.dateTimeFilter = dateTimeFilter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Report Type[ ");
        sb.append("\n\tReport Name: ").append(this.reportName);
        sb.append("\n\tTemplate: ").append(this.template);

        if (format != null) {
            sb.append("\n\tFormat: ").append(this.format.value());
        }

        if (schedule != null) {
            sb.append("\n\tSchedule: ").append(this.schedule.toString());
        }

        if (destination != null) {
            sb.append("\n\tDestination: ").append(this.destination.toString());
        }

        if (dateTimeFilter != null) {
            sb.append("\n\tDateTimeFilter: ").append(this.dateTimeFilter.toString());
        }
        sb.append("\n]");
        return sb.toString();

    }
}
