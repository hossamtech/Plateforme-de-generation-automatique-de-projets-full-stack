package codegen.spring_angular_auto_generator.databaseConnection.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseRequest {
    @NotNull(message = "dataBaseName is mandatory")
    private String dataBaseName;

    @NotNull(message = "port is mandatory")
    private String port;

    @NotNull(message = "username is mandatory")
    private String username;

    @NotNull(message = "password is mandatory")
    private String password;
}
