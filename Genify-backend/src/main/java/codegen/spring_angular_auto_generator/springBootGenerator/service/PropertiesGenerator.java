package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DataBaseConfig;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class PropertiesGenerator {
    public void generateDataBaseProperties(String extractPath, DataBaseConfig dataBaseConfig) throws IOException, IOException {
        String propertiesPath = extractPath + "/src/main/resources/application.properties";

        switch (dataBaseConfig.getDatabaseType()) {
            case MYSQL:
                propertiesMysql(propertiesPath, dataBaseConfig);
                break;
            case H2:

                break;
            case MONGODB:
                break;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dataBaseConfig.getDatabaseType());
        }
    }

    public void propertiesMysql(String propertiesPath, DataBaseConfig dataBaseConfig) {
        try (FileWriter propertiesFile = new FileWriter(propertiesPath, true)) { // 'true' for appending
            propertiesFile.write("# MySQL Database Configuration\n");
            propertiesFile.write("spring.datasource.url=jdbc:mysql://localhost:" + dataBaseConfig.getPort() + "/" + dataBaseConfig.getDataBaseName() + "\n");
            propertiesFile.write("spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n");
            propertiesFile.write("spring.datasource.username=" + dataBaseConfig.getUsername() + "\n");
            propertiesFile.write("spring.datasource.password=" + dataBaseConfig.getPassword() + "\n");
            propertiesFile.write("spring.jpa.hibernate.ddl-auto=update\n");
            propertiesFile.write("spring.jpa.show-sql=true\n");
            propertiesFile.write("spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect\n");
            propertiesFile.write("\n"); // Add a new line for better readability
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
