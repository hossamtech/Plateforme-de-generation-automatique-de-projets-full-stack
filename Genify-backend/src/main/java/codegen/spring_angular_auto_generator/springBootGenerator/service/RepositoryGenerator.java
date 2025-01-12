package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class RepositoryGenerator {

    public void generateRepositories(ProjectGenerationResponse projectGenerationResponse,
                                       List<DiagramClass> diagramClasses) throws IOException {
        for (DiagramClass diagramClass : diagramClasses) {
            generateRepository(projectGenerationResponse, diagramClass);
        }
    }

    public void generateRepository(ProjectGenerationResponse projectGenerationResponse, DiagramClass diagramClass) throws IOException {

        String packageName = projectGenerationResponse.getPackagePath().replace("/", ".");
        String projectPath  = projectGenerationResponse.getExtractPath();

        String repositoryPackage = packageName + ".repository";
        String repositoryDirectory = projectPath + "/src/main/java/" + repositoryPackage.replace(".", "/");

        createDirectory(repositoryDirectory);

        String className = diagramClass.getClassName();

        // Create the repository interface
        try (FileWriter writer = new FileWriter(repositoryDirectory + "/" + className + "Repository.java")) {
            writer.write("package " + repositoryPackage + ";\n\n");
            writer.write("import org.springframework.data.jpa.repository.JpaRepository;\n");
            writer.write("import " + packageName + ".models." + className + ";\n\n");

            writer.write("public interface " + className + "Repository extends JpaRepository<" + className + ", Long> {\n");
            writer.write("}\n");
        }
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create directory: " + path);
        }
    }

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
