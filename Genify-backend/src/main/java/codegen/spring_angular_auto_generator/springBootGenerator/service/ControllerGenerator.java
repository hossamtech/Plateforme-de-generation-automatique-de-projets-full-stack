package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ControllerGenerator {

    public void generateControllers(ProjectGenerationResponse projectGenerationResponse,
                                    List<DiagramClass> diagramClasses) throws IOException {
        for (DiagramClass diagramClass : diagramClasses) {
            generateController(projectGenerationResponse, diagramClass);
        }
    }

    public void generateController(ProjectGenerationResponse projectGenerationResponse, DiagramClass diagramClass) throws IOException {
        String packageName = projectGenerationResponse.getPackagePath().replace("/", ".");
        String projectPath = projectGenerationResponse.getExtractPath();

        String controllerPackage = packageName + ".controller";
        String controllerDirectory = projectPath + "/src/main/java/" + controllerPackage.replace(".", "/");

        createDirectory(controllerDirectory); // Ensure correct directory creation

        String className = diagramClass.getClassName();
        String dtoClassName = className + "Dto"; // Assuming the DTO follows this naming convention

        // Create the controller class
        try (FileWriter writer = new FileWriter(controllerDirectory + "/" + className + "Controller.java")) {
            writer.write("package " + controllerPackage + ";\n\n");
            writer.write("import org.springframework.beans.factory.annotation.Autowired;\n");
            writer.write("import org.springframework.web.bind.annotation.*;\n");
            writer.write("import " + packageName + ".service." + className + "Service;\n");
            writer.write("import " + packageName + ".dto." + dtoClassName + ";\n"); // Use DTO
            writer.write("import java.util.List;\n");
            writer.write("import java.util.Optional;\n\n");

            writer.write("@RestController\n");
            writer.write("@RequestMapping(\"/api/" + className.toLowerCase() + "s\")\n");
            writer.write("public class " + className + "Controller {\n\n");

            writer.write("    @Autowired\n");
            writer.write("    private " + className + "Service service;\n\n");

            // Add method
            writer.write("    @PostMapping\n");
            writer.write("    public " + dtoClassName + " add(@RequestBody " + dtoClassName + " dto) {\n");
            writer.write("        return service.add" + className + "(dto);\n"); // Call service with DTO
            writer.write("    }\n\n");

            // Update method
            writer.write("    @PutMapping\n");
            writer.write("    public " + dtoClassName + " update(@RequestBody " + dtoClassName + " dto) {\n");
            writer.write("        return service.update" + className + "(dto);\n"); // Call service with DTO
            writer.write("    }\n\n");

            // Delete method
            writer.write("    @DeleteMapping(\"/{id}\")\n");
            writer.write("    public void delete(@PathVariable Long id) {\n");
            writer.write("        service.delete" + className + "(id);\n");
            writer.write("    }\n\n");

            // Get by ID
            writer.write("    @GetMapping(\"/{id}\")\n");
            writer.write("    public Optional<" + dtoClassName + "> getById(@PathVariable Long id) {\n");
            writer.write("        return service.getById(id);\n");
            writer.write("    }\n\n");

            // Get all
            writer.write("    @GetMapping\n");
            writer.write("    public List<" + dtoClassName + "> getAll() {\n");
            writer.write("        return service.getAll();\n");
            writer.write("    }\n");

            writer.write("}\n");
        }
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create directory: " + path);
        }
    }
}
