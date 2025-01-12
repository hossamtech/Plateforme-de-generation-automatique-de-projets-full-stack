package codegen.spring_angular_auto_generator.angularService;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.Attribute;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.Relation;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ModelGenerated {

    public void generateAngularModel(ProjectGenerationResponse projectGenerationResponse, List<DiagramClass> diagramClasses) throws IOException {
        String modelsDirPath = projectGenerationResponse.getExtractPath() + "/src/app/models";

        createDirectory(modelsDirPath);

        for (DiagramClass diagramClass : diagramClasses) {
            String className = diagramClass.getClassName();
            String modelFilePath = modelsDirPath + "/" + className.toLowerCase() + ".model.ts";

            try (FileWriter writer = new FileWriter(modelFilePath)) {
                writer.write("export interface " + className + " {\n");
                for (Attribute attribute : diagramClass.getAttributes()) {
                    writer.write("    " + attribute.getAttributeName().toLowerCase() + ": " + mapToTypeScriptType(attribute.getAttributeType()) + ";\n");
                }

                // Handle relations for ManyToOne and OneToOne
                for (Relation relation : diagramClass.getRelations()) {
                    if ("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType())) {
                        String relatedClass = relation.getTargetClass();
                        writer.write("    " + relatedClass.toLowerCase() + "Id: number;\n");
                    } else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                        String targetClass = relation.getTargetClass();
                        // Extract the number from the relation type string (e.g., 3 from "ManyToMany(3)")
                        String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                        int numVars = Integer.parseInt(numberOfVariables);
                        // Generate the required number of variables (ID fields)
                        for (int i = 1; i <= numVars; i++) {
                            writer.write("    " + targetClass.toLowerCase() + i + "Id: number;\n");
                        }
                    }
                }

                writer.write("}\n");
            }
        }
    }

    private String mapToTypeScriptType(String javaType) {
        return switch (javaType) {
            case "String" -> "string";
            case "Long", "int", "Integer", "Double", "Float" -> "number";
            case "Date" -> "Date";
            case "boolean", "Boolean" -> "boolean";
            default -> "any";
        };
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create directory: " + path);
        }
    }

}
