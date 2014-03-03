/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mweigel
 */
public class ScheduleType implements Serializable {

    private Set<String> time;
    private Set<DayType> day;
    private Boolean onDemand;

    @SuppressWarnings("SetReplaceableByEnumSet")
    public ScheduleType() {
        time = new HashSet<String>();
        day = new HashSet<DayType>();
        onDemand = false;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public Set<String> getTime() {
        return time;
    }

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public void setTime(Set<String> time) {
        this.time = time;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public Set<DayType> getDay() {
        return day;
    }

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public void setDay(Set<DayType> day) {
        this.day = day;
    }

    public boolean isOnDemand() {
        return onDemand;
    }

    public void setOnDemand(Boolean onDemand) {
        this.onDemand = onDemand;
    }
    
    
    
    @Override
    public String toString() {
        return "MessageTarget ["
                + "\n\tonDemand => " + onDemand
                + "\n\ttime => " + time
                + "\n\tdaySet => " + day+ ']';
    }
}
