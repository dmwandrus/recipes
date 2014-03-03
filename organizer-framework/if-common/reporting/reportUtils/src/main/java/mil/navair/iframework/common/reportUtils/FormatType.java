package mil.navair.iframework.common.reportUtils;

import java.io.Serializable;

public enum FormatType implements Serializable {

    HTML("html"),

    PDF("pdf"),

    EXCEL("excel");
    
    private final String value;

    FormatType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FormatType fromValue(String v) {
        for (FormatType c: FormatType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
