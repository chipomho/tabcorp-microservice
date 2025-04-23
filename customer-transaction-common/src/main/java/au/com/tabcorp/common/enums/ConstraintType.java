package au.com.tabcorp.common.enums;

public enum ConstraintType implements BaseEnum {

    LENGTH("length",""),
    BLANK("blank",""),
    VALID("valid",""),
    EXPIRED("expired",""),
    MATCH("match",""),
    STATUS("status",""),
    AUTHORISED("authorised",""),
    EXISTS("exists",""),
    NULL("null",""),
    MINIMUM("minimum",""),
    MAXIMUM("maximum","")
            ;
    private final String code;
    private final String description;

    ConstraintType(final String code, final String description){
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
