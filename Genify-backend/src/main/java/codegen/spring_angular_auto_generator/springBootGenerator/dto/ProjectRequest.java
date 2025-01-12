package codegen.spring_angular_auto_generator.springBootGenerator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectRequest {
    @NotNull(message = "Project name is mandatory")
    private String projectName;

    @NotNull(message = "Group ID is mandatory")
    private String groupId;

    @NotNull(message = "Diagram classes must not be null")
    @Size(min = 1, message = "At least one diagram class must be provided")
    private List<DiagramClass> diagramClasses;
    private DataBaseConfig dataBase;
}
