package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data()
@SuperBuilder()
@AllArgsConstructor()
@NoArgsConstructor()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValidationErrorModel.class, name = "VALIDATION"),
        @JsonSubTypes.Type(value = WarningErrorModel.class, name = "WARNING"),
        @JsonSubTypes.Type(value = GenericErrorModel.class, name = "GENERIC")
})
public abstract class StandardErrorModel implements AbstractModel {
    private String code;
    private String message;
}
