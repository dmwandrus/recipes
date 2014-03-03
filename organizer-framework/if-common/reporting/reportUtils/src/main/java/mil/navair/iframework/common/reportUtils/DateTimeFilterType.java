package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateTimeFilterType implements Serializable{
    private Integer daysPast;
    private String startDateTime;
    private String endDateTime;

    public DateTimeFilterType() {
        daysPast = new Integer(0);
    }

    /**
     * Gets the value of the startDateTime property.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     *
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the value of the startDateTime property.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     *
     */
    public void setStartDateTime(String value) {
        this.startDateTime = value;
    }

    /**
     * Gets the value of the endDateTime property.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     *
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the value of the endDateTime property.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     *
     */
    public void setEndDateTime(String value) {
        this.endDateTime = value;
    }

    public Integer getDaysPast() {
        return daysPast;
    }

    /**
     * Sets the value of the daysPast property.
     *
     * @param value allowed object is {@link BigInteger }
     *
     */
    public void setDaysPast(Integer value) {
        this.daysPast = value;
    }
}
