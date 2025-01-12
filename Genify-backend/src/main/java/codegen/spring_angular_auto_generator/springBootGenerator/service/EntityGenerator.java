package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.Attribute;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.Relation;
import codegen.spring_angular_auto_generator.springBootGenerator.enums.EntityAnnotations;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EntityGenerator {

    public void generateEntities(ProjectGenerationResponse projectGenerationResponse, List<DiagramClass> diagramClasses) throws IOException {
        for (DiagramClass diagramClass : diagramClasses) {
            generateEntity(projectGenerationResponse, diagramClass);
        }
    }

    private void generateEntity(ProjectGenerationResponse projectGenerationResponse, DiagramClass diagramClass) throws IOException {
        String modelsPath = projectGenerationResponse.getExtractPath() + "/src/main/java/" + projectGenerationResponse.getPackagePath().replace(".", "/") + "/models";
        createDirectory(modelsPath);

        String entityPath = modelsPath + "/" + diagramClass.getClassName() + ".java";

        try (FileWriter entityFile = new FileWriter(entityPath)) {
            // Déclaration du package
            String packagePath = projectGenerationResponse.getPackagePath().replace("/", ".");
            entityFile.write("package " + packagePath + ".models;\n\n");

            // Importation des bibliothèques nécessaires
            entityFile.write("import jakarta.persistence.*;\n");
            entityFile.write("import lombok.*;\n");
            entityFile.write("import java.util.*;\n\n");

            // Annotations de classe
            for (EntityAnnotations annotation : EntityAnnotations.values()) {
                entityFile.write(annotation.getAnnotation() + "\n");
            }
            entityFile.write("public class " + diagramClass.getClassName() + " {\n\n");

            // Générer les attributs
            generateAttributes(entityFile, diagramClass.getAttributes());

            // Générer les relations
            generateRelations(diagramClass.getClassName(), entityFile, diagramClass.getRelations());

            entityFile.write("}\n");
        }
    }

    private void generateAttributes(FileWriter entityFile, List<Attribute> attributes) throws IOException {
        for (Attribute attribute : attributes) {
            if (attribute.isItsId()) {
                entityFile.write("    @Id\n");
                entityFile.write("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }
            entityFile.write("    private " + attribute.getAttributeType() + " " + attribute.getAttributeName() + ";\n\n");
        }
    }

    private void generateRelations(String className, FileWriter entityFile, List<Relation> relations) throws IOException {
        for (Relation relation : relations) {
            StringBuilder annotation = new StringBuilder();
            String relationType = relation.getRelationType();
            String targetClassVariableName = relation.getTargetClass().substring(0, 1).toLowerCase() + relation.getTargetClass().substring(1);

            // Handle the case for relation type "ManyToMany" or "ManyToMany(x)"
            if (relationType.matches("ManyToMany(\\(\\d+\\))?")) {
                if (relationType.equals("ManyToMany")) {

                    Optional<String> oppositeManyToManyNumber = relations.stream()
                            .filter(r -> r.getTargetClass().equals(className) &&
                                    r.getRelationType().matches("ManyToMany\\(\\d+\\)"))
                            .map(r -> r.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1"))
                            .findFirst();

                    if (oppositeManyToManyNumber.isPresent()) {
                        int num = Integer.parseInt(oppositeManyToManyNumber.get());
                        for (int i = 0; i < num; i++) {
                            annotation.append("    @ManyToMany");
                            if (relation.getMappedBy() != null) {
                                annotation.append("(mappedBy = \"").append(className.toLowerCase()).append(i).append("\")");
                            }
                            annotation.append("\n    private List<").append(relation.getTargetClass()).append("> ")
                                    .append(targetClassVariableName).append("sAs").append(className).append(i).append(";\n\n");
                            entityFile.write(annotation.toString());
                        }

                    }

                    annotation.append("    @ManyToMany");
                    if (relation.getMappedBy() != null) {
                        annotation.append("(mappedBy = \"").append(className.toLowerCase()).append("\")");
                    }
                    annotation.append("\n    private List<").append(relation.getTargetClass()).append("> ")
                            .append(targetClassVariableName).append("s;\n\n");
                    entityFile.write(annotation.toString());
                } else {
                    // Custom ManyToMany(x) relationship
                    // Extract the number from the relation type like ManyToMany(3)
                    String numberOfVariables = relationType.replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                    int numVars = Integer.parseInt(numberOfVariables);

                    for (int i = 1; i <= numVars; i++) {
                        // Add the annotation for each generated variable
                        annotation.setLength(0); // Reset the annotation string builder for each new variable
                        annotation.append("    @ManyToOne\n");
                        annotation.append("    private ").append(relation.getTargetClass()).append(" ")
                                .append(targetClassVariableName).append(i).append(";\n");
                        entityFile.write(annotation.toString());
                    }
                    entityFile.write("\n");
                }
            } else {
                // Handle other types of relations (OneToOne, OneToMany, ManyToOne)
                switch (relationType) {
                    case "OneToOne":
                        annotation.append("    @OneToOne");
                        if (relation.getMappedBy() != null) {
                            annotation.append("(");
                            annotation.append("mappedBy = \"").append(className.toLowerCase()).append("\"");
                            annotation.append(")");
                        }
                        annotation.append("\n    private ").append(relation.getTargetClass()).append(" ")
                                .append(targetClassVariableName).append(";\n\n");
                        entityFile.write(annotation.toString());
                        break;

                    case "OneToMany":
                        annotation.append("    @OneToMany(mappedBy = \"").append(className.toLowerCase()).append("\"");
                        annotation.append(")\n    private List<").append(relation.getTargetClass()).append("> ")
                                .append(targetClassVariableName).append("s;\n\n");
                        entityFile.write(annotation.toString());
                        break;

                    case "ManyToOne":
                        annotation.append("    @ManyToOne\n");
                        annotation.append("    private ").append(relation.getTargetClass()).append(" ")
                                .append(targetClassVariableName).append(";\n\n");
                        entityFile.write(annotation.toString());
                        break;

                    default:
                        throw new UnsupportedOperationException("Unknown relation type: " + relationType);
                }
            }
        }
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create directory: " + path);
        }
    }
}
