package codegen.spring_angular_auto_generator.springBootGenerator.dto;

import codegen.spring_angular_auto_generator.springBootGenerator.enums.DatabaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
    public class DataBaseConfig {
        private String dataBaseName;
        private String username;
        private String password;
        private String port;
        private DatabaseType databaseType;
    }
