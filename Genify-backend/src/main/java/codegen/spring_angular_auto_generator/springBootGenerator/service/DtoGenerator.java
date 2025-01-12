package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.Attribute;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.Relation;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
@Service
public class DtoGenerator {
    private List<DiagramClass> diagramClasses;

    public void generateDTOs(ProjectGenerationResponse projectGenerationResponse,
                             List<DiagramClass> diagramClasses) throws IOException {
        this.diagramClasses = diagramClasses;
        for (DiagramClass diagramClass : diagramClasses) {
            generateDTO(projectGenerationResponse, diagramClass);
        }
    }

    public String findTargetClassId(List<DiagramClass> diagramClasses, String targetClassName) {
        for (DiagramClass diagramClass : diagramClasses) {
            if (diagramClass.getClassName().equals(targetClassName)) {
                for (Attribute attribute : diagramClass.getAttributes()) {
                    if (attribute.isItsId()) return capitalizeFirstLetter(attribute.getAttributeName());
                }
            }
        }
        return null;
    }

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;  // Return input as is if it's null or empty
        }

        // Convert the first character to uppercase and concatenate with the rest of the string
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public void generateDTO(ProjectGenerationResponse projectGenerationResponse, DiagramClass diagramClass) throws IOException {
        String packageName = projectGenerationResponse.getPackagePath().replace("/", ".");
        String projectPath = projectGenerationResponse.getExtractPath();

        String dtoPackage = packageName + ".dto";
        String dtoDirectory = projectPath + "/src/main/java/" + dtoPackage.replace(".", "/");

        createDirectory(dtoDirectory);

        String className = diagramClass.getClassName();
        boolean needsDateImport = diagramClass.getAttributes().stream()
                .anyMatch(attribute -> "Date".equals(attribute.getAttributeType()));

        boolean needsListImport = diagramClass.getRelations().stream()
                .anyMatch(relation -> "ManyToMany".equals(relation.getRelationType()));

        try (FileWriter writer = new FileWriter(dtoDirectory + "/" + className + "Dto.java")) {
            writer.write("package " + dtoPackage + ";\n\n");

            // Import required classes
            if (needsDateImport) writer.write("import java.util.Date;\n");
            if (needsListImport) writer.write("import java.util.List;\n");

            writer.write("import lombok.Data;\n");

            writer.write("\n@Data\n");
            writer.write("public class " + className + "Dto {\n\n");

            // Generate fields for attributes
            for (Attribute attribute : diagramClass.getAttributes()) {
                writer.write("    private " + attribute.getAttributeType() + " " + attribute.getAttributeName() + ";\n");
            }

            // Handle ManyToOne and aOneToOne relations
            for (Relation relation : diagramClass.getRelations()) {
                if (("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType())))
                         {
                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);
                    System.out.println(targetId);

                    writer.write("    private Long " + targetId + ";\n");
                }
            }

            for (Relation relation : diagramClass.getRelations()) {
                // First check if the relation type is "ManyToMany(x)" (with a number)
                if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);

                    // Extract the number from the relation type string (e.g., 3 from "ManyToMany(3)")
                    String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                    int numVars = Integer.parseInt(numberOfVariables);

                    // Generate the required number of variables (ID fields)
                    for (int i = 1; i <= numVars; i++) {
                        writer.write("    private Long " + decapitalize(targetClass) + i + "Id;\n");
                    }
                }
//                else if ("ManyToMany".equals(relation.getRelationType())) {
//                    // Fallback for the standard "ManyToMany" case (without a number)
//                    String targetClass = relation.getTargetClass();
//                    writer.write("    private List<Long> list" + decapitalize(targetClass) + "Ids;\n");
//                }

            }


            writer.write("}\n");
        }
    }

    private String decapitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toLowerCase(input.charAt(0)) + input.substring(1);
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create directory: " + path);
        }
    }
}
