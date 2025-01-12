package codegen.spring_angular_auto_generator.angularService;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;

@Service
public class TailwindConfigUpdater {

    public void updateTailwindConfig(ProjectGenerationResponse projectGenerationResponse) throws IOException {
        // Paths for the source file and the target tailwind.config.js file
        String tailwindConfigPath = "src/main/resources/templates/tailwindConfig.text";
        String tailwindConfigJsPath =  projectGenerationResponse.getExtractPath() + "/tailwind.config.js";

        // Read the content of tailwindConfig.txt
        String tailwindConfigContent = readFileContent(tailwindConfigPath);

        // Write the content into tailwind.config.js
        writeToFile(tailwindConfigJsPath, tailwindConfigContent);
    }

    // Function to read the content of a file
    private String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    // Function to write content into a file
    private void writeToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);

        // If the file does not exist, create it
        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }


}
