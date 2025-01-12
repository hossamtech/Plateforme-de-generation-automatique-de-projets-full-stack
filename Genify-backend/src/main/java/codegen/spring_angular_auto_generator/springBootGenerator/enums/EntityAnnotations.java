package codegen.spring_angular_auto_generator.springBootGenerator.enums;

import lombok.Getter;

@Getter
public enum EntityAnnotations {
    ENTITY("@Entity"),
    DATA("@Data"),
    NO_ARGS_CONSTRUCTOR("@NoArgsConstructor"),
    ALL_ARGS_CONSTRUCTOR("@AllArgsConstructor");

    private final String annotation;

    EntityAnnotations(String annotation) {
        this.annotation = annotation;
    }

}
