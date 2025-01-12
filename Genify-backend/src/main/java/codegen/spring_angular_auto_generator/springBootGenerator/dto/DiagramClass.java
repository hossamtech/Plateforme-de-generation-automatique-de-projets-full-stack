package codegen.spring_angular_auto_generator.springBootGenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DiagramClass {
    private String className;
    private List<Attribute> attributes;
    private List<Relation> relations;
}
