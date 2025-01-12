package codegen.spring_angular_auto_generator.angularService;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class RouteGenerated {

    public void generateAppRoutes(ProjectGenerationResponse projectGenerationResponse, List<DiagramClass> diagramClasses) throws IOException {
        String appRoutesFilePath = projectGenerationResponse.getExtractPath() + "/src/app/app.routes.ts";

        try (FileWriter appRoutesWriter = new FileWriter(appRoutesFilePath)) {
            appRoutesWriter.write("import { Routes } from '@angular/router';\n");
            for (DiagramClass diagramClass : diagramClasses) {
                String className = diagramClass.getClassName();
                String componentName = toPascalCase(className); // Convertir en PascalCase
                String kebabCaseName = className.toLowerCase();
                appRoutesWriter.write("import { " + componentName + "Component " +
                        "} from './pages/" + kebabCaseName + "/" + kebabCaseName + ".component';\n");
            }
            appRoutesWriter.write("\nexport const routes: Routes = [\n");
            for (DiagramClass diagramClass : diagramClasses) {
                String kebabCaseName = diagramClass.getClassName().toLowerCase();
                appRoutesWriter.write("  { path: '" + kebabCaseName + "', component: " + diagramClass.getClassName() + "Component },\n");
            }
            appRoutesWriter.write("];\n");
        }
    }

    private String toPascalCase(String input) {
        StringBuilder pascalCase = new StringBuilder();
        String[] words = input.split("(?=[A-Z])"); // Séparer par les majuscules

        for (String word : words) {
            pascalCase.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
        }

        return pascalCase.toString();
    }
}
