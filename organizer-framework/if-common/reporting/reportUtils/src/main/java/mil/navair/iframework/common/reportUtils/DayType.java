package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;

public enum DayType implements Serializable {

    MON("mon"),

    TUE("tue"),

    WED("wed"),

    THU("thu"),

    FRI("fri"),

    SAT("sat"),

    SUN("sun");
    
    private final String value;

    DayType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DayType fromValue(String v) {
        for (DayType c: DayType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
