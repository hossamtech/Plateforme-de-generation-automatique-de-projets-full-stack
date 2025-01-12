package codegen.spring_angular_auto_generator.springBootGenerator.service;

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
public class ServiceGenerator {

    private List<DiagramClass> diagramClasses;

    public void generateServices(ProjectGenerationResponse projectGenerationResponse,
                                 List<DiagramClass> diagramClasses) throws IOException {
        this.diagramClasses = diagramClasses;
        for (DiagramClass diagramClass : diagramClasses) {
            generateService(projectGenerationResponse, diagramClass);
            generateMapper(projectGenerationResponse, diagramClass);
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


    public void generateService(ProjectGenerationResponse projectGenerationResponse, DiagramClass diagramClass) throws IOException {
        String packageName = projectGenerationResponse.getPackagePath().replace("/", ".");
        String projectPath = projectGenerationResponse.getExtractPath();

        String servicePackage = packageName + ".service";
        String serviceDirectory = projectPath + "/src/main/java/" + servicePackage.replace(".", "/");

        createDirectory(serviceDirectory);

        String className = diagramClass.getClassName();
        String dtoClassName = className + "Dto";

        try (FileWriter writer = new FileWriter(serviceDirectory + "/" + className + "Service.java")) {
            writer.write("package " + servicePackage + ";\n\n");
            writer.write("import " + packageName + ".models." + className + ";\n");
            writer.write("import " + packageName + ".dto." + dtoClassName + ";\n");
            writer.write("import " + packageName + ".repository." + className + "Repository;\n");
            writer.write("import " + packageName + ".mapper." + className + "Mapper;\n");
            writer.write("import org.springframework.beans.factory.annotation.Autowired;\n");
            writer.write("import org.springframework.stereotype.Service;\n");
            writer.write("import java.util.List;\n");
            writer.write("import java.util.Optional;\n");
            writer.write("import java.util.stream.Collectors;\n\n");

            // Import related entity repositories for ManyToOne or OneToOne relations
            String classId = null;
            for (Attribute attribute : diagramClass.getAttributes()) {
                if (attribute.isItsId()) classId = capitalizeFirstLetter(attribute.getAttributeName());
            }

            for (Relation relation : diagramClass.getRelations()) {
                if (("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType()))
                        ) {

                    String targetClass = relation.getTargetClass();
                    writer.write("import " + packageName + ".models." + targetClass + ";\n");
                    writer.write("import " + packageName + ".repository." + targetClass + "Repository;\n");
                } else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                    String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                    int numVars = Integer.parseInt(numberOfVariables);
                    String targetClass = relation.getTargetClass();
                    writer.write("import " + packageName + ".models." + targetClass + ";\n");
                    writer.write("import " + packageName + ".repository." + targetClass + "Repository;\n");
                } else if ("ManyToMany()".equals(relation.getRelationType())) {
                    String targetClass = relation.getTargetClass();
                    writer.write("import " + packageName + ".models." + targetClass + ";\n");
                    writer.write("import " + packageName + ".repository." + targetClass + "Repository;\n");
                }
            }

            writer.write("\n@Service\n");
            writer.write("public class " + className + "Service {\n\n");

            // Autowire repositories and mapper
            writer.write("    @Autowired\n");
            writer.write("    private " +

                    className + "Repository " + decapitalize(className) + "Repository;\n");

            for (Relation relation : diagramClass.getRelations()) {
                if (("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType()))
                      ) {

                    String targetClass = relation.getTargetClass();
                    writer.write("    @Autowired\n");
                    writer.write("    private " + targetClass + "Repository " + decapitalize(targetClass) + "Repository;\n");
                } else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                    String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                    int numVars = Integer.parseInt(numberOfVariables);
                    String targetClass = relation.getTargetClass();
                    writer.write("    @Autowired\n");
                    writer.write("    private " + targetClass + "Repository " + decapitalize(targetClass) + "Repository;\n");
                } else if ("ManyToMany()".equals(relation.getRelationType())) {
                    String targetClass = relation.getTargetClass();
                    writer.write("    @Autowired\n");
                    writer.write("    private " + targetClass + "Repository " + decapitalize(targetClass) + "Repository;\n");
                }
            }

            writer.write("    @Autowired\n");
            writer.write("    private " + className + "Mapper mapper;\n\n");

            // Add method
            writer.write("    public " + dtoClassName + " add" + className + "(" + dtoClassName + " dto) {\n");
            writer.write("        " + className + " entity = mapper.mapToEntity(dto);\n");

            // Handle ManyToOne and OneToOne relations during addition
            // Handle relations during addition
            for (Relation relation : diagramClass.getRelations()) {
                if (("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType()))
                        ) {

                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);

                    writer.write("        if (dto.get" + targetId + "() != null) {\n");
                    writer.write("            " + targetClass + " related" + targetClass + " = " + decapitalize(targetClass) + "Repository" +
                            ".findById(dto.get" + targetId + "())\n");
                    writer.write("                .orElseThrow(() -> new RuntimeException(\"" + targetClass + " not found\"));\n");
                    writer.write("            entity.set" + targetClass + "(related" + targetClass + ");\n");
                    writer.write("        }\n");
                } else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                    String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                    int numVars = Integer.parseInt(numberOfVariables);
                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);

                    for (int i = 1; i <= numVars; i++) {
                        writer.write("        if (dto.get" + capitalizeFirstLetter(targetClass) + i  + "Id() != null) {\n");
                        writer.write("            " + capitalizeFirstLetter(targetClass) + " related" + capitalizeFirstLetter(targetClass) + i + " = " + decapitalize(targetClass) + "Repository" +
                                ".findById(dto.get" + capitalizeFirstLetter(targetClass) + i  + "Id())\n");
                        writer.write("                .orElseThrow(() -> new RuntimeException(\"" + capitalizeFirstLetter(targetClass) + " not found\"));\n");
                        writer.write("            entity.set" + capitalizeFirstLetter(targetClass) + i + "(related" + capitalizeFirstLetter(targetClass) + i + ");\n");
                        writer.write("        }\n");

                    }
                }
//                else if ("ManyToMany()".equals(relation.getRelationType())) {
//                    String targetClass = relation.getTargetClass();
//                    writer.write("        if (dto.get" + capitalizeFirstLetter(targetClass) + "List() != null) {\n");
//                    writer.write("            List<" + targetClass + "> related" + targetClass + "List = dto.get" + capitalizeFirstLetter(targetClass) + "List()\n");
//                    writer.write("                .stream()\n");
//                    writer.write("                .map(id -> " + decapitalize(targetClass) + "Repository.findById(id)\n");
//                    writer.write("                    .orElseThrow(() -> new RuntimeException(\"" + targetClass + " not found\")))\n");
//                    writer.write("                .collect(Collectors.toList());\n");
//                    writer.write("            entity.set" + capitalizeFirstLetter(targetClass) + "List(related" + targetClass + "List);\n");
//                    writer.write("        }\n");
//                }
            }

            writer.write("        return mapper.mapToDTO(" + decapitalize(className) + "Repository.save(entity));\n");
            writer.write("    }\n\n");

            // Update method
            writer.write("    public " + dtoClassName + " update" + className + "(" + dtoClassName + " dto) {\n");
            writer.write("        Optional<" + className + "> optionalEntity = " + decapitalize(className) + "Repository" +
                    ".findById(dto.get" + classId + "());\n");
            writer.write("        if (optionalEntity.isPresent()) {\n");
            writer.write("            " + className + " existingEntity = optionalEntity.get();\n");
            writer.write("            " + className + " updatedEntity = mapper.mapToEntity(dto);\n");

            for (Attribute attribute : diagramClass.getAttributes()) {
                writer.write("            existingEntity.set" + capitalize(attribute.getAttributeName()) + "(updatedEntity.get" + capitalize(attribute.getAttributeName()) + "());\n");
            }

            for (Relation relation : diagramClass.getRelations()) {
                if (("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType()))
                        ) {

                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);

                    writer.write("            if (dto.get" + targetId + "() != null) {\n");
                    writer.write("                " + targetClass + " related" + targetClass + " = " + decapitalize(targetClass) + "Repository" +
                            ".findById(dto.get" + targetId + "())\n");
                    writer.write("                    .orElseThrow(() -> new RuntimeException(\"" + targetClass + " not found\"));\n");
                    writer.write("                existingEntity.set" + targetClass + "(related" + targetClass + ");\n");
                    writer.write("            }\n");
                } else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                    String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                    int numVars = Integer.parseInt(numberOfVariables);
                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);

                    for (int i = 1; i <= numVars; i++) {
                        writer.write("        if (dto.get" + capitalizeFirstLetter(targetClass) + i  + "Id() != null) {\n");
                        writer.write("            " + capitalizeFirstLetter(targetClass) + " related" + capitalizeFirstLetter(targetClass) + i + " = " + decapitalize(targetClass) + "Repository" +
                                ".findById(dto.get" + capitalizeFirstLetter(targetClass) + i  + "Id())\n");
                        writer.write("                .orElseThrow(() -> new RuntimeException(\"" + capitalizeFirstLetter(targetClass) + " not found\"));\n");
                        writer.write("            existingEntity.set" + capitalizeFirstLetter(targetClass) + i + "(related" + capitalizeFirstLetter(targetClass) + i + ");\n");
                        writer.write("        }\n");

                    }
                }
//               else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
//                    String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
//                    int numVars = Integer.parseInt(numberOfVariables);
//                    String targetClass = relation.getTargetClass();
//                    String targetId = findTargetClassId(this.diagramClasses, targetClass);
//
//                    writer.write("            if (dto.get" + capitalizeFirstLetter(targetClass) + "List() != null) {\n");
//                    writer.write("                List<" + targetClass + "> updated" + targetClass + "List = dto.get" + capitalizeFirstLetter(targetClass) + "List()\n");
//                    writer.write("                    .stream()\n");
//                    writer.write("                    .map(id -> " + decapitalize(targetClass) + "Repository.findById(id)\n");
//                    writer.write("                        .orElseThrow(() -> new RuntimeException(\"" + targetClass + " not found\")))\n");
//                    writer.write("                    .collect(Collectors.toList());\n");
//                    writer.write("                existingEntity.set" + capitalizeFirstLetter(targetClass) + "List(updated" + targetClass + "List);\n");
//                    writer.write("            }\n");
//                } else if ("ManyToMany".equals(relation.getRelationType())) {
//                    String targetClass = relation.getTargetClass();
//                    String targetId = findTargetClassId(this.diagramClasses, targetClass);
//
//                    writer.write("            if (dto.get" + capitalizeFirstLetter(targetClass) + "List() != null) {\n");
//                    writer.write("                List<" + targetClass + "> updated" + targetClass + "List = dto.get" + capitalizeFirstLetter(targetClass) + "List()\n");
//                    writer.write("                    .stream()\n");
//                    writer.write("                    .map(id -> " + decapitalize(targetClass) + "Repository.findById(id)\n");
//                    writer.write("                        .orElseThrow(() -> new RuntimeException(\"" + targetClass + " not found\")))\n");
//                    writer.write("                    .collect(Collectors.toList());\n");
//                    writer.write("                existingEntity.set" + capitalizeFirstLetter(targetClass) + "List(updated" + targetClass + "List);\n");
//                    writer.write("            }\n");
//                }
            }

            writer.write("            return mapper.mapToDTO(" + decapitalize(className) + "Repository.save(existingEntity));\n");
            writer.write("        } else {\n");
            writer.write("            throw new RuntimeException(\"Entity not found\");\n");
            writer.write("        }\n");
            writer.write("    }\n\n");


            // Delete method
            writer.write("    public void delete" + className + "(Long id) {\n");
            writer.write("        if (!" + decapitalize(className) + "Repository.existsById(id)) {\n");
            writer.write("            throw new RuntimeException(\"" + className + " not found\");\n");
            writer.write("        }\n");
            writer.write("        " + decapitalize(className) + "Repository.deleteById(id);\n");
            writer.write("    }\n\n");

            // Get by ID method
            writer.write("    public Optional<" + dtoClassName + "> getById(Long id) {\n");
            writer.write("        return " + decapitalize(className) + "Repository.findById(id)\n");
            writer.write("            .map(mapper::mapToDTO);\n");
            writer.write("    }\n\n");

            // Get all method
            writer.write("    public List<" + dtoClassName + "> getAll() {\n");
            writer.write("        return " + decapitalize(className) + "Repository.findAll().stream()\n");
            writer.write("            .map(mapper::mapToDTO)\n");
            writer.write("            .collect(Collectors.toList());\n");
            writer.write("    }\n");

            writer.write("}\n");
        }
    }
    private String decapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public void generateMapper(ProjectGenerationResponse projectGenerationResponse, DiagramClass diagramClass) throws IOException {
        String packageName = projectGenerationResponse.getPackagePath().replace("/", ".");
        String projectPath = projectGenerationResponse.getExtractPath();

        String mapperPackage = packageName + ".mapper";
        String mapperDirectory = projectPath + "/src/main/java/" + mapperPackage.replace(".", "/");

        createDirectory(mapperDirectory);

        String className = diagramClass.getClassName();
        String dtoClassName = className + "Dto";

        // Create the mapper class
        try (FileWriter writer = new FileWriter(mapperDirectory + "/" + className + "Mapper.java")) {
            writer.write("package " + mapperPackage + ";\n\n");
            writer.write("import " + packageName + ".dto." + dtoClassName + ";\n");
            writer.write("import " + packageName + ".models." + className + ";\n");
            writer.write("import org.springframework.stereotype.Component;\n\n");

            writer.write("@Component\n");
            writer.write("public class " + className + "Mapper {\n\n");

            // Map from entity to DTO
            writer.write("    public " + dtoClassName + " mapToDTO(" + className + " entity) {\n");
            writer.write("        " + dtoClassName + " dto = new " + dtoClassName + "();\n");
            for (Attribute attribute : diagramClass.getAttributes()) {
                writer.write("        dto.set" + capitalize(attribute.getAttributeName()) + "(entity.get" + capitalize(attribute.getAttributeName()) + "());\n");
            }

            // Handle relations by setting IDs in DTO
            for (Relation relation : diagramClass.getRelations()) {
                if ("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType())
                        ) {
                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);

                    writer.write("        if (entity.get" + targetClass + "() != null) {\n");
                    writer.write("            dto.set" + targetId + "(entity.get" + targetClass + "().get" + targetId + "());\n");
                    writer.write("        }\n");
                }
//                else if ("ManyToMany".equals(relation.getRelationType())) {
//                    String targetClass = relation.getTargetClass();
//                    String targetId = findTargetClassId(this.diagramClasses, targetClass);
//
//                    writer.write("        if (entity.get" + targetClass + "() != null) {\n");
//                    writer.write("            dto.set" + targetId + "List(entity.get" + targetId + "List().stream()\n");
//                    writer.write("                .map(" + targetClass + "::get"+ targetId +")\n");
//                    writer.write("                .collect(Collectors.toList()));\n");
//                    writer.write("        }\n");
//                }
                else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                    String numberOfVariables = relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1");
                    int numVars = Integer.parseInt(numberOfVariables);
                    String targetClass = relation.getTargetClass();
                    String targetId = findTargetClassId(this.diagramClasses, targetClass);

                    for (int i = 1; i <= numVars; i++) {

                        writer.write("        if (entity.get" + targetClass + i + "() != null) {\n");
                        writer.write("            dto.set" + targetClass + i + "Id(entity.get" + targetClass + i + "().get" + targetId + "());\n");
                        writer.write("        }\n");
                    }
                }
            }
            writer.write("        return dto;\n");
            writer.write("    }\n\n");

            // Map from DTO to entity
            writer.write("    public " + className + " mapToEntity(" + dtoClassName + " dto) {\n");
            writer.write("        " + className + " entity = new " + className + "();\n");
            for (Attribute attribute : diagramClass.getAttributes()) {
                writer.write("        entity.set" + capitalize(attribute.getAttributeName()) + "(dto.get" + capitalize(attribute.getAttributeName()) + "());\n");
            }
            writer.write("        return entity;\n");
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

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;  // Return input as is if it's null or empty
        }

        // Convert the first character to uppercase and concatenate with the rest of the string
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
