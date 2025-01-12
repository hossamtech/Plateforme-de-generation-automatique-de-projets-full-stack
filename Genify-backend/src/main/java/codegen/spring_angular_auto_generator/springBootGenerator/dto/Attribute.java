package codegen.spring_angular_auto_generator.springBootGenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Attribute {
    private String attributeName;
    private String attributeType;
    private boolean itsId;
}
