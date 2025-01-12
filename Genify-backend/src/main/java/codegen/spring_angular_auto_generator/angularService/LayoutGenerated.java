package codegen.spring_angular_auto_generator.angularService;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;


@Service
public class LayoutGenerated {

    public void generateLayout(ProjectGenerationResponse projectGenerationResponse, List<DiagramClass> diagramClasses) throws IOException {
        String layoutDirPath = projectGenerationResponse.getExtractPath() + "/src/app/components/layout";


        generateCodeHtml_topbar(layoutDirPath);
        generateCodeTs_topbar(layoutDirPath);

        generateCodeHtml_sidebar(projectGenerationResponse.getExtractPath(), layoutDirPath);
        generateCodeTs_sidebar(layoutDirPath);

        generateCodeHtml_footer(layoutDirPath);
        generateCodeTs_Footer(layoutDirPath);

        try (FileWriter writer = new FileWriter(layoutDirPath + "/sidebar/menu.module.ts")) {
            writer.write(
                    "export interface MenuItem {\n" +
                            "    id?: any;\n" +
                            "    label?: any;\n" +
                            "    link?: string;\n" +
                            "}\n"
            );
        }

        try (FileWriter writer = new FileWriter(layoutDirPath + "/sidebar/menu.ts")) {
            writer.write("import { MenuItem } from \"./menu.module\";\n\n");
            writer.write("export const MENU: MenuItem[] = [\n");

            int index = 0;
            for (DiagramClass diagramClass : diagramClasses ) {
                String className = diagramClass.getClassName();

                writer.write("    {\n");
                writer.write("        id: " + ++index + ",\n");
                writer.write("        label: '" + className + "',\n");
                writer.write("        link: '/" + className.toLowerCase() + "',\n");
                writer.write("    },\n");
            }

            writer.write("];\n");
        }

    }

        private void generateCodeTs_sidebar(String layoutSideBarDirPath) throws IOException {
        String topbarPath = "src/main/resources/templates/layout/sidebar/sidebar_ts.text";

        String tailwindConfigContent = readFileContent(topbarPath);

        writeToFile(layoutSideBarDirPath + "/sidebar/sidebar.component.ts", tailwindConfigContent);
    }

    private void generateCodeHtml_sidebar(String pathProject, String layoutSideBarDirPath) throws IOException {
        String assetsPath = pathProject + "/src/assets/images";

        String sourceImagePath = "src/main/resources/img/genify.png";
        String destinationImagePath = assetsPath + "/genify.png";

        try (FileInputStream fis = new FileInputStream(sourceImagePath);
             FileOutputStream fos = new FileOutputStream(destinationImagePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new IOException("Failed to copy image from " + sourceImagePath + " to " + destinationImagePath, e);
        }


        String topbarPath = "src/main/resources/templates/layout/sidebar/sidebar_html.text";

        String tailwindConfigContent = readFileContent(topbarPath);

        writeToFile(layoutSideBarDirPath + "/sidebar/sidebar.component.html", tailwindConfigContent);
    }

    private void generateCodeHtml_topbar(String layoutTopBarPath) throws IOException {
        String topbarPath = "src/main/resources/templates/layout/topbar/topbar_html.text";

        String tailwindConfigContent = readFileContent(topbarPath);

        writeToFile(layoutTopBarPath + "/topbar/topbar.component.html", tailwindConfigContent);
    }

    private void generateCodeTs_topbar(String layoutTopBarPath) throws IOException {
        String topbarPath = "src/main/resources/templates/layout/topbar/topbar_ts.text";

        String tailwindConfigContent = readFileContent(topbarPath);

        writeToFile(layoutTopBarPath + "/topbar/topbar.component.ts", tailwindConfigContent);
    }

    private void generateCodeHtml_footer(String layoutTopBarPath) throws IOException {
        String topbarPath = "src/main/resources/templates/layout/footer/footer_html.text";

        String tailwindConfigContent = readFileContent(topbarPath);

        writeToFile(layoutTopBarPath + "/footer/footer.component.html", tailwindConfigContent);
    }

    private void generateCodeTs_Footer(String layoutTopBarPath) throws IOException {
        String topbarPath = "src/main/resources/templates/layout/footer/footer_ts.text";

        String tailwindConfigContent = readFileContent(topbarPath);

        writeToFile(layoutTopBarPath + "/footer/footer.component.ts", tailwindConfigContent);
    }

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
