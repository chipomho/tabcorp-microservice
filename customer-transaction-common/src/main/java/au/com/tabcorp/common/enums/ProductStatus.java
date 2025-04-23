package au.com.tabcorp.common.enums;

public enum ProductStatus implements BaseEnum {

    ACTIVE("Active",""),
    INACTIVE("Inactive","");

    private final String code;
    private final String description;

    ProductStatus(final String code, final String description){
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
