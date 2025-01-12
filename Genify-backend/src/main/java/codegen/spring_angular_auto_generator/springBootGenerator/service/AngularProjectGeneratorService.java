package codegen.spring_angular_auto_generator.springBootGenerator.service;

import codegen.spring_angular_auto_generator.angularService.*;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.*;
import codegen.spring_angular_auto_generator.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AngularProjectGeneratorService {

    private final TailwindConfigUpdater tailwindConfigUpdater;
    private final ServicesGenerated servicesGenerated;
    private final ComponentGenerated componentGenerated;
    private final ModelGenerated modelGenerated;
    private final ModalGenerated modalGenerated;
    private final LayoutGenerated layoutGenerated;
    private final RouteGenerated routeGenerated;

    @Value("${generatedProjects.path}")
    private String generatedProjectsPath;

    public void createAngularProject(String projectName, ProjectRequest projectRequest,
                                     Authentication connectedUser) throws IOException, InterruptedException {
        User user = (User) connectedUser.getPrincipal();
        List<DiagramClass> diagramClasses = projectRequest.getDiagramClasses();

        String userDirPath = String.format("%s", user.getDownloads());
        String extractPath = String.format("%s/%s", userDirPath, projectName);
        File generatedProjectsDir = new File(userDirPath); // Create a File object for the generated projects directory

        if (!generatedProjectsDir.exists() && !generatedProjectsDir.mkdirs()) {
            throw new IOException("Failed to create the directory: " + generatedProjectsDir.getAbsolutePath());
        }

        // Step 1: Create Angular project
        ProcessBuilder createProject = new ProcessBuilder(
                "cmd", "/c", "npx", "@angular/cli", "new", projectName,
                "--defaults", "--skip-git", "--style=scss"
        );        createProject.directory(generatedProjectsDir);
        createProject.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        createProject.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = createProject.start();
        process.waitFor();

        // Step 2: Install Tailwind CSS and necessary dependencies
        ProcessBuilder installTailwind = new ProcessBuilder("cmd", "/c", "npm", "install", "-D", "tailwindcss", "postcss", "autoprefixer");
        installTailwind.directory(new File(generatedProjectsDir, projectName));
        installTailwind.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        installTailwind.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process tailwindProcess = installTailwind.start();
        tailwindProcess.waitFor();

        // Step 3: Initialize Tailwind CSS
        ProcessBuilder tailwindInit = new ProcessBuilder("cmd", "/c", "npm", "tailwindcss", "init");
        tailwindInit.directory(new File(generatedProjectsDir, projectName));
        tailwindInit.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        tailwindInit.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process initProcess = tailwindInit.start();
        initProcess.waitFor();

        // Step 4: Install Lucide Angular package
        ProcessBuilder installLucide = new ProcessBuilder("cmd", "/c", "npm", "install", "lucide-angular");
        installLucide.directory(new File(generatedProjectsDir, projectName));
        installLucide.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        installLucide.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process lucideProcess = installLucide.start();
        lucideProcess.waitFor();

        ProcessBuilder installTooltip = new ProcessBuilder("cmd", "/c", "npm", "install", "ng2-tooltip-directive");
        installTooltip.directory(new File(generatedProjectsDir, projectName));
        installTooltip.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        installTooltip.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process tooltipProcess = installTooltip.start();
        tooltipProcess.waitFor();



        ProcessBuilder installSimplebar = new ProcessBuilder("cmd", "/c", "npm", "install", "simplebar-angular");
        installSimplebar.directory(new File(generatedProjectsDir, projectName));
        installSimplebar.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        installSimplebar.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process SimplebarProcess = installSimplebar.start();
        SimplebarProcess.waitFor();

        ProcessBuilder installSimplebarLodash = new ProcessBuilder("cmd", "/c", "npm", "install", "--save-dev @types/lodash");
        installSimplebarLodash.directory(new File(generatedProjectsDir, projectName));
        installSimplebarLodash.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        installSimplebarLodash.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process simplebarLodashProcess = installSimplebarLodash.start();
        simplebarLodashProcess.waitFor();

        // Step 5: Create 'pages' directory in 'src/app'
        File pagesDir = new File(new File(generatedProjectsDir, projectName), "src/app/pages");
        File componentDir = new File(new File(generatedProjectsDir, projectName), "src/app/components/layout");
        if (!pagesDir.exists() && !pagesDir.mkdirs()) {
            throw new IOException("Failed to create the 'pages' directory in the Angular project.");
        }
        if (!componentDir.exists() && !componentDir.mkdirs()) {
            throw new IOException("Failed to create the 'component' directory in the Angular project.");
        }

        // Step 6: Generate component for each DiagramClass
        for (DiagramClass diagramClass : diagramClasses) {
            String className = diagramClass.getClassName().toLowerCase();
            ProcessBuilder createComponent = new ProcessBuilder("cmd", "/c", "ng", "generate", "component", "pages/" + className);
            createComponent.directory(new File(generatedProjectsDir, projectName));
            createComponent.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            createComponent.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process componentProcess = createComponent.start();
            componentProcess.waitFor();
        }

        ProcessBuilder createComponentSidebar = new ProcessBuilder("cmd", "/c", "ng", "generate", "component", "components" +
                "/layout" +
                "/sidebar");
        createComponentSidebar.directory(new File(generatedProjectsDir, projectName));
        createComponentSidebar.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        createComponentSidebar.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process componentSideBarProcess = createComponentSidebar.start();
        componentSideBarProcess.waitFor();

        ProcessBuilder createComponentTopBar = new ProcessBuilder("cmd", "/c", "ng", "generate", "component", "components" +
                "/layout" +
                "/topbar");
        createComponentTopBar.directory(new File(generatedProjectsDir, projectName));
        createComponentTopBar.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        createComponentTopBar.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process componentTopBarProcess = createComponentTopBar.start();
        componentTopBarProcess.waitFor();

        ProcessBuilder createComponentFooter = new ProcessBuilder("cmd", "/c", "ng", "generate", "component", "components" +
                "/layout" +
                "/footer");
        createComponentFooter.directory(new File(generatedProjectsDir, projectName));
        createComponentFooter.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        createComponentFooter.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process componentFooterProcess = createComponentFooter.start();
        componentFooterProcess.waitFor();

        String packagePath = pagesDir.getAbsolutePath().replace("\\", "/");
        ProjectGenerationResponse projectGenerationResponse = ProjectGenerationResponse.builder()
                .extractPath(extractPath)
                .packagePath(packagePath)
                .build();

        // Continue with generating other files...
        tailwindConfigUpdater.updateTailwindConfig(projectGenerationResponse);
        modelGenerated.generateAngularModel(projectGenerationResponse, diagramClasses);
        servicesGenerated.generateAngularService(projectGenerationResponse, diagramClasses);
//        generateAngularService(projectGenerationResponse, diagramClasses);
//        generateAngularComponent(projectGenerationResponse, diagramClasses);
        componentGenerated.generateAngularComponent(projectGenerationResponse, diagramClasses);
        generateAppComponentHtml(projectName, projectGenerationResponse, diagramClasses);
        routeGenerated.generateAppRoutes(projectGenerationResponse, diagramClasses);
        layoutGenerated.generateLayout(projectGenerationResponse, diagramClasses);
        updateStyles(projectGenerationResponse);
        updateIndexHtml(projectName, projectGenerationResponse);
        updateAppComponentTs(projectName, projectGenerationResponse);
        generateAppConfigTs(projectGenerationResponse);
        modalGenerated.generateModal(projectGenerationResponse);
        generatePluginsFolder(projectGenerationResponse);

    }


    // Generate app.component.html with Bootstrap navigation bar
    public void generateAppComponentHtml(String projectName, ProjectGenerationResponse projectGenerationResponse,
                                         List<DiagramClass> diagramClasses) throws IOException {
        String appComponentHtmlPath = projectGenerationResponse.getExtractPath() + "/src/app/app.component.html";

        try (FileWriter appComponentHtmlWriter = new FileWriter(appComponentHtmlPath)) {
            // Start of the main div with styling classes
            appComponentHtmlWriter.write("<div class=\"text-base bg-body-bg text-body font-public ");
            // Topbar and Sidebar components
            appComponentHtmlWriter.write("    <app-topbar></app-topbar>\n");
            appComponentHtmlWriter.write("    <app-sidebar></app-sidebar>\n");

            // Main content area
            appComponentHtmlWriter.write("    <div class=\"relative min-h-screen group-data-[sidebar-size=sm]:min-h-sm\">\n");
            appComponentHtmlWriter.write("      <div class=\"md:ml-vertical-menu sm:ml-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1.5)] pb-[calc(theme('spacing.header')_*_0.3)] px-4 \">\n ");
            // Container for the content
            appComponentHtmlWriter.write("        <div class=\"container-fluid group-data-[content=boxed]:max-w-boxed mx-auto\">\n");
            appComponentHtmlWriter.write("          <router-outlet></router-outlet>\n");
            appComponentHtmlWriter.write("          <app-footer></app-footer>\n");
            appComponentHtmlWriter.write("        </div>\n");
            appComponentHtmlWriter.write("      </div>\n");
            appComponentHtmlWriter.write("    </div>\n");
            appComponentHtmlWriter.write("</div>\n");

            // Generate CSS for app.component.css
//            generateAppComponentCss(projectGenerationResponse);
        }
    }


//    private void generateAppComponentCss(ProjectGenerationResponse projectGenerationResponse) throws IOException {
//        String appComponentCssPath = projectGenerationResponse.getExtractPath() + "/src/app/app.component.css";
//
//        try (FileWriter appComponentCssWriter = new FileWriter(appComponentCssPath)) {
//            // CSS pour la navbar
//            appComponentCssWriter.write(".navbar {\n");
//            appComponentCssWriter.write("  height: 60px;\n");
//            appComponentCssWriter.write("  display: flex;\n");
//            appComponentCssWriter.write("  align-items: center;\n");
//            appComponentCssWriter.write("}\n\n");
//
//            // CSS pour les liens de navigation
//            appComponentCssWriter.write(".nav-link {\n");
//            appComponentCssWriter.write("  border-radius: 5px;\n");
//            appComponentCssWriter.write("  transition: background-color 0.3s, color 0.3s;\n");
//            appComponentCssWriter.write("  padding: 8px 15px;\n");
//            appComponentCssWriter.write("  height: 35px;\n");
//            appComponentCssWriter.write("  line-height: 24px;\n");
//            appComponentCssWriter.write("  display: flex;\n");
//            appComponentCssWriter.write("  align-items: center;\n");
//            appComponentCssWriter.write("}\n\n");
//
//            // CSS pour le lien actif
//            appComponentCssWriter.write(".nav-link.active {\n");
//            appComponentCssWriter.write("  background-color: #24a7e9;\n");
//            appComponentCssWriter.write("  color: white !important;\n");
//            appComponentCssWriter.write("}\n\n");
//
//            // CSS pour le hover des liens inactifs
//            appComponentCssWriter.write(".nav-link:not(.active):hover {\n");
//            appComponentCssWriter.write("  color: #0056b3;\n");
//            appComponentCssWriter.write("  text-decoration: underline;\n");
//            appComponentCssWriter.write("}\n");
//        }
//    }



    public void updateStyles(ProjectGenerationResponse projectGenerationResponse) throws IOException {
        String stylesPath = projectGenerationResponse.getExtractPath() + "/src/styles.scss";

        try (FileWriter writer = new FileWriter(stylesPath, true)) {
            writer.write("@import url('https://fonts.googleapis.com/css2?family=Public+Sans:wght@200;300;400;500;600;" +
                            "700&display=swap');\n\n");
            writer.write("@tailwind base;\n");
            writer.write("@tailwind components;\n");
            writer.write("@tailwind utilities;\n");
        }
    }


    public void updateIndexHtml(String projectName, ProjectGenerationResponse projectGenerationResponse) throws IOException {
        String indexHtmlPath = projectGenerationResponse.getExtractPath() + "/src/index.html";

        try (FileWriter writer = new FileWriter(indexHtmlPath)) {
            writer.write("<!doctype html>\n");
            writer.write("<html lang=\"en\">\n");
            writer.write("<head>\n");
            writer.write("  <meta charset=\"utf-8\">\n");
            writer.write("  <title>" + projectName + "</title>\n");
            writer.write("  <base href=\"/\">\n");
            writer.write("  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n");
            writer.write("  <link href=\"favicon.ico\" rel=\"icon\" type=\"image/x-icon\">\n");
            writer.write("  <script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyAbvyBxmMbFhrzP9Z8moyYr6dCr-pzjhBE\"></script>\n");
            writer.write("</head>\n");
            writer.write("<body class=\"text-base bg-body-bg text-body font-public\">\n");
            writer.write("  <app-root></app-root>\n");
            writer.write("</body>\n");
            writer.write("</html>\n");
        }
    }


    // Update app.component.ts to use Bootstrap components
    public void updateAppComponentTs(String projectName, ProjectGenerationResponse projectGenerationResponse) throws IOException {
        String appComponentTsPath = projectGenerationResponse.getExtractPath() + "/src/app/app.component.ts";

        try (FileWriter appComponentTsWriter = new FileWriter(appComponentTsPath)) {
            appComponentTsWriter.write("import { Component } from '@angular/core';\n");
            appComponentTsWriter.write("import { CommonModule } from '@angular/common';\n");
            appComponentTsWriter.write("import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';\n");
            appComponentTsWriter.write("import {TopbarComponent} from './components/layout/topbar/topbar.component';\n");
            appComponentTsWriter.write("import {SidebarComponent} from './components/layout/sidebar/sidebar.component';\n");
            appComponentTsWriter.write("import {FooterComponent} from \"./components/layout/footer/footer.component\";\n");

            appComponentTsWriter.write("@Component({\n");
            appComponentTsWriter.write("  selector: 'app-root',\n");
            appComponentTsWriter.write("  standalone: true,\n");
            appComponentTsWriter.write("  imports: [CommonModule, RouterOutlet, RouterLinkActive, RouterLink, TopbarComponent, SidebarComponent, FooterComponent],\n");
            appComponentTsWriter.write("  templateUrl: './app.component.html',\n");
            appComponentTsWriter.write("  styleUrls: ['./app.component.scss']\n");
            appComponentTsWriter.write("})\n");
            appComponentTsWriter.write("export class AppComponent {\n");
            appComponentTsWriter.write("  title = '" + projectName + "';\n");
            appComponentTsWriter.write("}\n");
        }
    }

    public void generateAppConfigTs(ProjectGenerationResponse projectGenerationResponse) throws IOException {
        String appConfigTsPath = projectGenerationResponse.getExtractPath() + "/src/app/app.config.ts";

        try (FileWriter writer = new FileWriter(appConfigTsPath)) {
            writer.write("import { ApplicationConfig, importProvidersFrom } from '@angular/core';\n");
            writer.write("import { provideRouter } from '@angular/router';\n");
            writer.write("\n");
            writer.write("import { routes } from './app.routes';\n");
            writer.write("import { HttpClientModule } from '@angular/common/http';\n");
            writer.write("import { icons, LUCIDE_ICONS, LucideIconProvider } from 'lucide-angular';\n");
            writer.write("\n");
            writer.write("export const appConfig: ApplicationConfig = {\n");
            writer.write("  providers: [\n");
            writer.write("    provideRouter(routes),\n");
            writer.write("    importProvidersFrom(HttpClientModule),\n");
            writer.write("    {\n");
            writer.write("      provide: LUCIDE_ICONS,\n");
            writer.write("      multi: true,\n");
            writer.write("      useValue: new LucideIconProvider(icons)\n");
            writer.write("    },\n");
            writer.write("  ],\n");
            writer.write("};\n");
        }
    }






    public void generatePluginsFolder(ProjectGenerationResponse projectGenerationResponse) throws IOException {
        String pluginsDirPath =  projectGenerationResponse.getExtractPath() + "/plugins";

        File pluginsDir = new File(pluginsDirPath);
        if (!pluginsDir.exists() && !pluginsDir.mkdirs()) {
            throw new IOException("Failed to create the plugins directory: " + pluginsDir.getAbsolutePath());
        }

        generateButtonJs(pluginsDirPath);
        generateCardJs(pluginsDirPath);
        generateFormJs(pluginsDirPath);
    }

    private void generateButtonJs(String pluginsDirPath) throws IOException {
        File buttonFile = new File(pluginsDirPath + "/buttons.js");
        try (FileWriter writer = new FileWriter(buttonFile)) {
            writer.write("const plugin = require('tailwindcss/plugin');\n\n");
            writer.write("module.exports = plugin(function ({ addComponents, theme }) {\n");
            writer.write("    addComponents({\n");
            writer.write("        '.btn': {\n");
            writer.write("            '@apply inline-block py-2 px-4 text-center border rounded-md border-transparent bg-transparent text-sm transition-all duration-200 ease-linear': {},\n");
            writer.write("        },\n");
            writer.write("    });\n");
            writer.write("});\n");
        }
    }

    private void generateCardJs(String pluginsDirPath) throws IOException {
        File cardFile = new File(pluginsDirPath + "/card.js");
        try (FileWriter writer = new FileWriter(cardFile)) {
            writer.write("const plugin = require('tailwindcss/plugin');\n\n");
            writer.write("module.exports = plugin(function ({ addComponents }) {\n");
            writer.write("    addComponents({\n");
            writer.write("        '.card': {\n");
            writer.write("            '@apply shadow-md rounded-md shadow-slate-200 border-0 mb-5 border-transparent bg-white': {},\n");
            writer.write("    },\n");
            writer.write("        '.card-body': {\n");
            writer.write("            '@apply p-5 pb-0': {},\n");
            writer.write("        }\n");
            writer.write("    });\n");
            writer.write("});\n");
        }
    }

    private void generateFormJs(String pluginsDirPath) throws IOException {
        File formFile = new File(pluginsDirPath + "/forms.js");
        try (FileWriter writer = new FileWriter(formFile)) {
            writer.write("const plugin = require('tailwindcss/plugin');\n\n");
            writer.write("module.exports = plugin(function ({ addComponents, theme }) {\n");
            writer.write("    addComponents({\n");
            writer.write("        '[type=\"checkbox\"]:checked': {\n");
            writer.write("            '@apply bg-no-repeat': {},\n");
            writer.write("            backgroundImage: 'url(\"/src/assets/images/check-arrow.svg\")',\n");
            writer.write("            backgroundSize: '.9rem .9rem',\n");
            writer.write("            backgroundPosition: '50%',\n");
            writer.write("        },\n");
            writer.write("        '[type=\"radio\"]:checked': {\n");
            writer.write("            '@apply bg-no-repeat': {},\n");
            writer.write("            backgroundImage: 'url(\"/src/assets/images/radio-arrow.svg\")',\n");
            writer.write("            backgroundSize: '.75rem .75rem',\n");
            writer.write("            backgroundPosition: '58%',\n");
            writer.write("        },\n");
            writer.write("        '[type=\"checkbox\"].arrow-none:checked, [type=\"radio\"].arrow-none:checked': {\n");
            writer.write("            '@apply bg-no-repeat': {},\n");
            writer.write("        },\n");
            writer.write("\n");
            writer.write("        '.form-input': {\n");
            writer.write("            '@apply border rounded-md block text-base py-2 px-4 w-full': {},\n");
            writer.write("        },\n");
            writer.write("        '.form-select': {\n");
            writer.write("            '@apply border rounded-md block py-2 pl-3 pr-10 px-4 text-base w-full bg-no-repeat appearance-none': {},\n");
            writer.write("            backgroundImage: `url(\"data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%231f242e' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='m2 5 6 6 6-6'/%3e%3c/svg%3e\")`,\n");
            writer.write("            backgroundPosition: 'right 0.70rem center',\n");
            writer.write("            backgroundRepeat: 'no-repeat',\n");
            writer.write("            backgroundSize: '0.70em 0.70em',\n");
            writer.write("        },\n");
            writer.write("\n");
            writer.write("        'input[type=\"range\"]': {\n");
            writer.write("            '&::-webkit-slider-thumb': {\n");
            writer.write("                '@apply bg-custom-500 h-4 w-4 rounded-full cursor-pointer appearance-none': {},\n");
            writer.write("            },\n");
            writer.write("            '&:focus::-webkit-slider-thumb': {\n");
            writer.write("                boxShadow: `0 0 3px ${theme(\"colors.custom.700\")}`\n");
            writer.write("            }\n");
            writer.write("        },\n");
            writer.write("        // Add other styles here...\n");
            writer.write("    });\n");
            writer.write("});\n");
        }
    }



    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create directory: " + path);
        }
    }
}
