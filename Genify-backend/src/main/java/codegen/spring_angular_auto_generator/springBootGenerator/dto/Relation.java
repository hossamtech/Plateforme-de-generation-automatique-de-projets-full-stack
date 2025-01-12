package codegen.spring_angular_auto_generator.springBootGenerator.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Relation {
    private String relationType; // "OneToOne", "ManyToOne"
    private String targetClass;   // the name of the target class for the relation
    private String mappedBy;      // used in bidirectional relations
}