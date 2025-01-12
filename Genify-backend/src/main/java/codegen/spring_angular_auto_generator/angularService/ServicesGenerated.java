package codegen.spring_angular_auto_generator.angularService;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ServicesGenerated {

    public void generateAngularService(ProjectGenerationResponse projectGenerationResponse, List<DiagramClass> diagramClasses) throws IOException, InterruptedException {
        String angularServicePath = projectGenerationResponse.getExtractPath() + "/src/app/services";

        createDirectory(angularServicePath);

        for (DiagramClass diagramClass : diagramClasses) {
            String className = diagramClass.getClassName();

            ProcessBuilder generateService = new ProcessBuilder(
                    "cmd", "/c", "ng", "generate", "service", "services/" + className.toLowerCase()
            );

            generateService.directory(new File(projectGenerationResponse.getExtractPath()));
            generateService.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            generateService.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process process = generateService.start();
            process.waitFor();

            String filePath = angularServicePath + "/" + className.toLowerCase() + ".service.ts";

            try (FileWriter writer = new FileWriter(filePath)) {
                // Import statements including the corresponding model
                writer.write("import { Injectable, signal } from '@angular/core';\n");
                writer.write("import { HttpClient } from '@angular/common/http';\n");
                writer.write("import { " + className + " } from '../models/" + className.toLowerCase() + ".model';\n\n");

                writer.write("@Injectable({\n");
                writer.write("  providedIn: 'root',\n");
                writer.write("})\n");

                writer.write("export class " + className + "Service {\n");
                writer.write("    private baseUrl = 'http://localhost:8085';\n");
                writer.write("    private apiUrl = `${this.baseUrl}/api/" + className.toLowerCase() + "s`;\n\n");

                // Signal for storing the list of objects
                writer.write( "    " + className.toLowerCase() + "List = signal<" + className + "[]>([]);\n\n");

                writer.write("    constructor(private http: HttpClient) {\n");
                writer.write("        this.fetchAll();\n");
                writer.write("    }\n\n");


                // Fetch all method using signals
                writer.write("    fetchAll(): void {\n");
                writer.write("        this.http.get<" + className + "[]>(this.apiUrl).subscribe((data) => {\n");
                writer.write("            this." + className.toLowerCase() + "List.set(data);\n");
                writer.write("        });\n");
                writer.write("    }\n\n");

                // Add method using signals
                writer.write("    add(data: " + className + "): void {\n");
                writer.write("        this.http.post<" + className + ">(this.apiUrl, data).subscribe((new" + className + ") => {\n");
                writer.write("            this." + className.toLowerCase() + "List.update((currentList) => [...currentList, new" + className + "]);\n");
                writer.write("        });\n");
                writer.write("    }\n\n");

                // Update method using signals
                writer.write("    update(data: " + className + "): void {\n");
                writer.write("        this.http.put<" + className + ">(this.apiUrl, data).subscribe((updated" + className + ") => {\n");
                writer.write("            this." + className.toLowerCase() + "List.update((currentList) =>\n");
                writer.write("                currentList.map((item) => (item.id" + className.toLowerCase() + " === updated" + className + ".id" + className.toLowerCase() + " ? updated" + className + " : item))\n");
                writer.write("            );\n");
                writer.write("        });\n");
                writer.write("    }\n\n");

                // Delete method using signals
                writer.write("    delete(id: number): void {\n");
                writer.write("        this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe(() => {\n");
                writer.write("            this." + className.toLowerCase() + "List.update((currentList) =>\n");
                writer.write("                currentList.filter((item) => item.id" + className.toLowerCase() + " !== id)\n");
                writer.write("            );\n");
                writer.write("        });\n");
                writer.write("    }\n");

                writer.write("}\n");
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
