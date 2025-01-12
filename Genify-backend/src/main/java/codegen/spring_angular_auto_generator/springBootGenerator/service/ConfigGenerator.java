package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class ConfigGenerator {

    public void generateConfig(ProjectGenerationResponse projectGenerationResponse) throws IOException {

        String packageName = projectGenerationResponse.getPackagePath().replace("/", ".");
        String projectPath  = projectGenerationResponse.getExtractPath();

        // Adjust package and directory for SimpleCorsFilter
        String configPackage = packageName + ".config";
        String configDirectory = projectPath + "/src/main/java/" + configPackage.replace(".", "/");

        createDirectory(configDirectory);

        String className = "SimpleCorsFilter";

        // Create the SimpleCorsFilter class
        try (FileWriter writer = new FileWriter(configDirectory + "/" + className + ".java")) {
            writer.write("package " + configPackage + ";\n\n");
            writer.write("import jakarta.servlet.*;\n");
            writer.write("import jakarta.servlet.http.HttpServletRequest;\n");
            writer.write("import jakarta.servlet.http.HttpServletResponse;\n");
            writer.write("import org.springframework.core.Ordered;\n");
            writer.write("import org.springframework.core.annotation.Order;\n");
            writer.write("import org.springframework.stereotype.Component;\n\n");
            writer.write("import java.io.IOException;\n");
            writer.write("import java.util.HashMap;\n");
            writer.write("import java.util.Map;\n\n");

            writer.write("@Component\n");
            writer.write("@Order(Ordered.HIGHEST_PRECEDENCE)\n");
            writer.write("public class " + className + " implements Filter {\n\n");

            writer.write("    public " + className + "() {\n");
            writer.write("    }\n\n");

            writer.write("    @Override\n");
            writer.write("    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)\n");
            writer.write("            throws IOException, ServletException {\n");
            writer.write("        HttpServletResponse response = (HttpServletResponse) res;\n");
            writer.write("        HttpServletRequest request = (HttpServletRequest) req;\n");
            writer.write("        Map<String, String> map = new HashMap<>();\n");
            writer.write("        String originHeader = request.getHeader(\"origin\");\n");
            writer.write("        response.setHeader(\"Access-Control-Allow-Origin\", originHeader);\n");
            writer.write("        response.setHeader(\"Access-Control-Allow-Methods\", \"POST, GET, PUT, OPTIONS, DELETE\");\n");
            writer.write("        response.setHeader(\"Access-Control-Max-Age\", \"3600\");\n");
            writer.write("        response.setHeader(\"Access-Control-Allow-Headers\", \"*\");\n\n");

            writer.write("        if (\"OPTIONS\".equalsIgnoreCase(request.getMethod())) {\n");
            writer.write("            response.setStatus(HttpServletResponse.SC_OK);\n");
            writer.write("        } else {\n");
            writer.write("            chain.doFilter(req, res);\n");
            writer.write("        }\n");
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
