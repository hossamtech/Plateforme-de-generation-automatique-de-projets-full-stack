package codegen.spring_angular_auto_generator.springBootGenerator.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectGenerationResponse {
    private String extractPath;
    private String packagePath;
}
