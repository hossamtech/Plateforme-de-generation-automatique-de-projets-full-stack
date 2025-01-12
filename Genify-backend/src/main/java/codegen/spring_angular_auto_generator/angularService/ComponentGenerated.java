package codegen.spring_angular_auto_generator.angularService;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.Attribute;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.Relation;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ComponentGenerated {

    public void generateAngularComponent(ProjectGenerationResponse projectGenerationResponse, List<DiagramClass> diagramClasses) throws IOException {
        String angularComponentPath = projectGenerationResponse.getExtractPath() + "/src/app/pages";
        String assetsPath = projectGenerationResponse.getExtractPath() + "/src/assets/images";

        File directory = new File(angularComponentPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + angularComponentPath);
            }
        }

        // Ensure the assets/images directory exists
        File assetsDirectory = new File(assetsPath);
        if (!assetsDirectory.exists()) {
            if (!assetsDirectory.mkdirs()) {
                throw new IOException("Failed to create directory: " + assetsPath);
            }
        }

        // Source and destination paths for the image
        String sourceImagePath = "src/main/resources/img/delete.png";
        String destinationImagePath = assetsPath + "/delete.png";

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


        for (DiagramClass diagramClass : diagramClasses) {
            String className = diagramClass.getClassName();
            String kebabCaseName = className.toLowerCase();

            String componentPath = angularComponentPath + "/" + kebabCaseName;

            try (FileWriter htmlWriter = new FileWriter(componentPath + "/" + kebabCaseName + ".component.html")) {
                // Generate the outer card container
                htmlWriter.write("<div class=\"col-span-12 md:order-8 2xl:col-span-9 card\">\n");
                htmlWriter.write("  <div class=\"card-body\">\n");
                htmlWriter.write("    <div class=\"grid items-center grid-cols-1 gap-3 mb-5 xl:grid-cols-12\">\n");
                htmlWriter.write("      <div class=\"xl:col-span-3\">\n");
                htmlWriter.write("        <h6 class=\"text-15 text-slate-600 font-medium\">Manage " + className + "</h6>\n");
                htmlWriter.write("      </div><!--end col-->\n");
                htmlWriter.write("      <div class=\"xl:col-span-4 xl:col-start-10\">\n");
                htmlWriter.write("        <div class=\"flex justify-end gap-3\">\n");
                htmlWriter.write("          <button type=\"button\" [appModalTrigger]=\"'" + kebabCaseName + "Modal'\" " +
                        "class=\"text-custom" +
                        "-500 btn " +
                        "bg-custom-100 hover:text-white hover:bg-custom-600 focus:text-white focus:bg-custom-600 focus:ring " +
                        "focus:ring-custom-100 active:text-white active:bg-custom-600 active:ring active:ring-custom-100\">\n");
                htmlWriter.write("            Add " + className + "\n");
                htmlWriter.write("          </button>\n");
                htmlWriter.write("        </div>\n");
                htmlWriter.write("      </div>\n");
                htmlWriter.write("    </div><!--end grid-->\n");

                // Generate table headers dynamically based on class attributes and relations
                htmlWriter.write("    <div class=\"-mx-5 overflow-x-auto\">\n");
                htmlWriter.write("      <table class=\"w-full whitespace-nowrap\">\n");
                htmlWriter.write("        <thead class=\"bg-slate-100 text-slate-500\">\n");
                htmlWriter.write("          <tr>\n");

                // Generate table headers for attributes
                for (Attribute attribute : diagramClass.getAttributes()) {
                    htmlWriter.write("            <th class=\"text-left px-3.5 py-2.5 first:pl-5 last:pr-5 font-semibold" +
                            " border-y border-slate-200\">"+ capitalize(attribute.getAttributeName()) +"</th>\n");
                }

                // Handle relations for ManyToOne, OneToOne, ManyToMany
                for (Relation relation : diagramClass.getRelations()) {
                    String relationType = relation.getRelationType();
                    if ("ManyToOne".equals(relationType) || "OneToOne".equals(relationType)) {
                        String relationName = relation.getTargetClass().toLowerCase();
                        htmlWriter.write("            <th class=\"text-left px-3.5 py-2.5 first:pl-5 last:pr-5 " +
                                "font-semibold border-y border-slate-200\">"+ capitalize(relationName) +" ID</th>\n");
                    } else if (relationType.matches("ManyToMany\\((\\d+)\\)")) {
                        int numVars = Integer.parseInt(relationType.replaceAll("ManyToMany\\((\\d+)\\)", "$1"));
                        String relationName = relation.getTargetClass().toLowerCase();
                        for (int i = 1; i <= numVars; i++) {
                            htmlWriter.write("            <th class=\"text-left px-3.5 py-2.5 first:pl-5 " +
                                    "last:pr-5 font-semibold border-y border-slate-200\">"+ capitalize(relationName) + " ID " + i + "</th>\n");
                        }
                    }
                }

                htmlWriter.write("            <th class=\"text-left px-3.5 py-2.5 first:pl-5 last:pr-5 font-semibold border-y border-slate-200\">Actions</th>\n");
                htmlWriter.write("          </tr>\n");
                htmlWriter.write("        </thead>\n");
                htmlWriter.write("        <tbody>\n");

                // Dynamic rows based on data list
                htmlWriter.write("          <tr *ngFor=\"let item of " + kebabCaseName + "List()\">\n");
                for (Attribute attribute : diagramClass.getAttributes()) {
                    htmlWriter.write("            <td class=\"px-3.5 py-2.5 first:pl-5 last:pr-5 border-y " +
                            "border-slate-200\">{{ item." + attribute.getAttributeName() + " }}</td>\n");
                }

                // Relations: ManyToOne, OneToOne, ManyToMany
                for (Relation relation : diagramClass.getRelations()) {
                    String relationType = relation.getRelationType();
                    if ("ManyToOne".equals(relationType) || "OneToOne".equals(relationType)) {
                        String relationName = relation.getTargetClass().toLowerCase();
                        htmlWriter.write("            <td class=\"px-3.5 py-2.5 first:pl-5 last:pr-5 border-y border-slate-200\">{{ item." + relationName + "Id }}</td>\n");
                    } else if (relationType.matches("ManyToMany\\((\\d+)\\)")) {
                        int numVars = Integer.parseInt(relationType.replaceAll("ManyToMany\\((\\d+)\\)", "$1"));
                        String relationName = relation.getTargetClass().toLowerCase();
                        for (int i = 1; i <= numVars; i++) {
                            htmlWriter.write("            <td class=\"px-3.5 py-2.5 first:pl-5 last:pr-5 border-y " +
                                    "border-slate-200\">{{ item." + relationName + i + "Id }}</td>\n");
                        }
                    }
                }

                // Action buttons
                htmlWriter.write("            <td class=\"px-3.5 py-2.5 first:pl-5 last:pr-5 border-y border-slate-200 \">\n");
                htmlWriter.write("              <div class=\"flex gap-2 \">\n");

                htmlWriter.write("                  <a [appModalTrigger]=\"'" + kebabCaseName+ "Modal'\" (click)" +
                        "=\"onEdit(item)\" class=\"flex items-center justify-center size-8 transition-all duration-200 ease-linear " +
                        "rounded-md bg-slate-100  text-slate-500 hover:text-custom-500  hover:bg-custom-100\">\n");
                htmlWriter.write("                      <lucide-angular name=\"pencil\" [class]=\"'size-4'\"></lucide-angular>\n");
                htmlWriter.write("                  </a>\n");

                htmlWriter.write("                  <a [appModalTrigger]=\"'delete" + className + "'\" (click)=\"onDelete" +
                        "(item)\" " +
                        "class=\"flex items-center justify-center " +
                        "size-8 transition-all duration-200 ease-linear " +
                        "rounded-md bg-slate-100  text-slate-500 hover:text-red-500  hover:bg-red-100\">\n");
                htmlWriter.write("                      <lucide-angular name=\"trash-2\" [class]=\"'size-4'\"></lucide-angular>\n");
                htmlWriter.write("                  </a>\n");

                htmlWriter.write("              </div>\n");
                htmlWriter.write("            </td>\n");
                htmlWriter.write("          </tr>\n");
                htmlWriter.write("        </tbody>\n");
                htmlWriter.write("      </table>\n");
                htmlWriter.write("    </div><!--end table-->\n");
                htmlWriter.write("  </div><!--end card-body-->\n");
                htmlWriter.write("</div><!--end card-->\n");

                // Modal for editing/adding
                    htmlWriter.write("<ng-modals id=\"" + kebabCaseName + "Modal\" [placement]=\"'modal-center'\" " +
                            "className=\"fixed flex flex-col transition-all duration-300 ease-in-out left-2/4 z-drawer -translate-x-2/4 -translate-y-2/4\">\n");
                    htmlWriter.write("  <div class=\"w-[93vw] md:w-[32rem] bg-white shadow rounded-md\">\n");
                    htmlWriter.write("    <div class=\"flex items-center justify-between p-5 border-b\">\n");
                    htmlWriter.write("      <h5 class=\"text-16 font-medium\"> {{ selected" + className + "? 'Edit " + className + "' : 'Add " +
                            className + "' }} </h5>\n");
                    htmlWriter.write("      <button dismissModal [ModalDismiss]=\"'" + kebabCaseName + "Modal'\" " +
                            "class=\"transition-all duration-200 ease-linear text-slate-400 hover:text-red-500\">\n");
                    htmlWriter.write("        <lucide-angular name=\"x\" [class]=\"'size-5'\"></lucide-angular>\n");
                    htmlWriter.write("      </button>\n");
                    htmlWriter.write("    </div>\n");
                    htmlWriter.write("    <div class=\"max-h-[calc(theme('height.screen')_-_180px)] p-5 overflow-y-auto\">\n");
                    htmlWriter.write("      <form [formGroup]=\"" + kebabCaseName + "Form\" class=\"grid grid-cols-1 xl:grid-cols-12 gap-x-5\">\n");
                    htmlWriter.write("        <div class=\"xl:col-span-12 grid gap-4 mb-3\">\n");

                    // Generate fields dynamically for attributes
                    for (Attribute attribute : diagramClass.getAttributes()) {
                        if (attribute.isItsId()) continue;

                        htmlWriter.write("          <div>\n");
                        htmlWriter.write("            <label class=\"inline-block mb-2 text-base text-slate-800 font-medium\">" + capitalize(attribute.getAttributeName()) + "</label>\n");
                        htmlWriter.write("            <input type=\"text\" " +
                                "[class]=\"{'!border-red-500': " + kebabCaseName + "Form.get('" + attribute.getAttributeName() + "')?.invalid && " +
                                kebabCaseName + "Form.get('" + attribute.getAttributeName() + "')?.touched}\" " +
                                "class=\"input form-input w-full border-slate-200 focus:outline-none focus:border-custom-500\" " +
                                "formControlName=\"" + attribute.getAttributeName() + "\" placeholder=\"" + attribute.getAttributeName() + "\" required>\n");
                        htmlWriter.write("            <div *ngIf=\"" + kebabCaseName + "Form.get('" + attribute.getAttributeName() + "')?.invalid && " +
                                "(" + kebabCaseName + "Form.get('" + attribute.getAttributeName() + "')?.dirty || " + kebabCaseName + "Form.get('" + attribute.getAttributeName() + "')?.touched)\" " +
                                "class=\"mt-1 text-sm text-red-500\">\n");
                        htmlWriter.write("              Please enter your " + attribute.getAttributeName() + ".\n");
                        htmlWriter.write("            </div>\n");
                        htmlWriter.write("          </div>\n");
                    }

                    // Add fields for relations
                    for (Relation relation : diagramClass.getRelations()) {
                        if ("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType())) {
                            String relationName = relation.getTargetClass().toLowerCase();
                            htmlWriter.write("          <div>\n");
                            htmlWriter.write("            <label>Select " + capitalize(relationName) + "</label>\n");
                            htmlWriter.write("            <select class=\"form-select border-slate-200 focus:outline-none focus:border-custom-500\" " +
                                    "formControlName=\"" + relationName + "Id\">\n");
                            // Find the corresponding DiagramClass for the relationName
                            DiagramClass targetDiagramClass = getDiagramClassByName(relationName, diagramClasses);

                            if (targetDiagramClass != null) {
                                // Find the ID attribute and name attribute in the target DiagramClass
                                String idAttribute = null;
                                String nameAttribute = null;

                                for (Attribute attribute : targetDiagramClass.getAttributes()) {
                                    // Check if the attribute is marked as the ID
                                    if (attribute.isItsId()) {
                                        idAttribute = attribute.getAttributeName();
                                    }
                                    // Otherwise, check if the attribute is the name attribute
                                    if ((attribute.getAttributeName().contains("nom") || attribute.getAttributeName().contains("name"))
                                            && "String".equals(attribute.getAttributeType())) {
                                        nameAttribute = attribute.getAttributeName();
                                    }
                                }

                                // If no specific "nom" attribute is found, fallback to the first attribute
                                if (nameAttribute == null && !targetDiagramClass.getAttributes().isEmpty()) {
                                    for (Attribute attribute : targetDiagramClass.getAttributes()) {
                                        if ("String".equals(attribute.getAttributeType())) {
                                            nameAttribute = attribute.getAttributeName();
                                            break; // Stop after finding the first String attribute
                                        }
                                    }                                }

                                // Output the options for the select dropdown
                                htmlWriter.write("              <option *ngFor=\"let item of " + relationName + "List()\" " +
                                        "[value]=\"item." + idAttribute + "\">{{ item." + nameAttribute + " }}</option>\n");
                            }

                            htmlWriter.write("            </select>\n");
                            htmlWriter.write("          </div>\n");
                        }
                    }

                    htmlWriter.write("        </div>\n");
                    htmlWriter.write("      </form>\n");
                    htmlWriter.write("    </div>\n");
                    htmlWriter.write("    <div class=\"border-t flex justify-end gap-2 p-5\">\n");
                    htmlWriter.write("      <button dismissModal [ModalDismiss]=\"'" + kebabCaseName+ "Modal'\" " +
                            "class=\"text-red-500 bg-white py-2 px-4 btn hover:text-red-500 hover:bg-red-100\">Cancel</button>\n");
                    htmlWriter.write("      <button (click)=\"onSubmit()\" type=\"submit\" " +
                            "class=\"text-white bg-custom-500 border-custom-500 hover:bg-custom-600 focus:bg-custom-600 py-2 px-4 btn\">\n");
                    htmlWriter.write("        {{ selected" + className + " ? 'Modifier' : 'Add' }}\n");
                    htmlWriter.write("      </button>\n");
                    htmlWriter.write("    </div>\n");
                    htmlWriter.write("  </div>\n");
                    htmlWriter.write("</ng-modals>\n");

                    htmlWriter.write("<ng-modals id=\"delete" + className +  "\" [placement]=\"'modal-center'\" ");
                    htmlWriter.write("    className=\"fixed flex flex-col transition-all duration-300 ease-in-out left-2/4 z-drawer -translate-x-2/4 -translate-y-2/4\">\n");
                    htmlWriter.write("  <div class=\"w-screen md:w-[28rem] bg-white shadow rounded-md\">\n");
                    htmlWriter.write("    <div class=\"h-full px-4 py-4\">\n");
                    htmlWriter.write("      <div class=\"float-right\">\n");
                    htmlWriter.write("        <button dismissModal [ModalDismiss]=\"'delete" + className + "'\" class=\"transition-all duration-200 ease-linear text-slate-400 hover:text-red-500\">\n");
                    htmlWriter.write("          <lucide-angular name=\"x\" [class]=\"'size-5'\"></lucide-angular>\n");
                    htmlWriter.write("        </button>\n");
                    htmlWriter.write("      </div>\n");
                    htmlWriter.write("      <img src=\"assets/images/delete.png\" alt=\"\" class=\"mt-5 block h-12 mx-auto\">\n");
                    htmlWriter.write("      <div class=\"mt-7 text-center\">\n");
                    htmlWriter.write("        <h5 class=\"mb-1 text-16 font-medium\">Please Confirm</h5>\n");
                    htmlWriter.write("        <p class=\"text-slate-500 px-4 font-medium\">You have unsaved changes. Are you sure you want to continue without saving</p>\n");
                    htmlWriter.write("        <div class=\"flex justify-center mb-3 gap-2 mt-6\">\n");
                    htmlWriter.write("          <button type=\"reset\" dismissModal [ModalDismiss]=\"'delete" + className + "'\" ");
                    htmlWriter.write(" class=\"transition-all duration-200 ease-linear bg-white border-white text-slate-500 btn hover:text-slate-600 focus:text-slate-600 active:text-slate-600 dark:bg-zink-500 dark:border-zink-500\">Discard</button>\n");
                    htmlWriter.write("          <button type=\"submit\" (click)=\"delete" + className + "()\" ");
                    htmlWriter.write(" class=\"text-white transition-all duration-200 ease-linear bg-red-500 border-red-500 btn hover:text-white hover:bg-red-600 hover:border-red-600 focus:text-white focus:bg-red-600 focus:border-red-600 focus:ring focus:ring-red-100 active:text-white active:bg-red-600 active:border-red-600 active:ring active:ring-red-100 dark:ring-red-400/20\">Yes, Save It!</button>\n");
                    htmlWriter.write("        </div>\n");
                    htmlWriter.write("      </div>\n");
                    htmlWriter.write("    </div>\n");
                    htmlWriter.write("  </div>\n");
                    htmlWriter.write("</ng-modals>\n");
                    htmlWriter.write("<!--end delete modal-->\n");




            }

            try (FileWriter tsWriter = new FileWriter(componentPath + "/" + kebabCaseName + ".component.ts")) {
                tsWriter.write("import { Component, OnInit } from '@angular/core';\n");
                tsWriter.write("import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';\n");
                tsWriter.write("import { " + className + "Service } from '../../services/" + kebabCaseName + ".service';\n");
                for (Relation relation : diagramClass.getRelations()) {
                    if ("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType())) {
                        tsWriter.write("import { " + relation.getTargetClass() + "Service } from '../../services/" + relation.getTargetClass().toLowerCase() +
                                ".service';\n");

                    }
                }
                tsWriter.write("import { LucideAngularModule } from 'lucide-angular';\n");
                tsWriter.write("import { MDModalModule } from '../../modals/modal.module';\n");
                tsWriter.write("import { NgForOf, NgIf } from '@angular/common';\n");
                tsWriter.write("import {" + className + "} from \"../../models/"+ kebabCaseName +".model\";\n");
                tsWriter.write("import { ModalService } from '../../modals/modal.service';\n\n");


                tsWriter.write("@Component({\n");
                tsWriter.write("  selector: 'app-" + kebabCaseName + "',\n");
                tsWriter.write("  templateUrl: './" + kebabCaseName + ".component.html',\n");
                tsWriter.write("  standalone: true,\n");
                tsWriter.write("  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf, NgForOf ],\n");
                tsWriter.write("  styleUrls: ['./" + kebabCaseName + ".component.scss']\n");
                tsWriter.write("})\n");

                tsWriter.write("export class " + className + "Component implements OnInit {\n");
                tsWriter.write("  " + kebabCaseName + "Form!: FormGroup;\n");
                tsWriter.write("  " + kebabCaseName + "List = this." + kebabCaseName + "Service." + kebabCaseName + "List;\n");
                tsWriter.write("  selected" + className + ": any = null;\n");
                tsWriter.write("  deleted" + className + ": number = 0;\n");

                // Declare lists for related entities
                for (Relation relation : diagramClass.getRelations()) {
                    if ("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType()) ||
                            relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                        String relationName = relation.getTargetClass().toLowerCase();
                        tsWriter.write("  " + relationName + "List = this." + relationName + "Service."  +
                                relationName + "List;\n");

                    }
                }



                tsWriter.write("\n  constructor(private fb: FormBuilder,\n" +
                        "               private " + kebabCaseName + "Service: " + className + "Service,\n" +
                        "               private modalService: ModalService,\n");

                for (Relation relation : diagramClass.getRelations()) {
                    if ("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType())) {
                        String relationName = relation.getTargetClass().toLowerCase();
                        String relationServiceName = relationName + "Service";

                        tsWriter.write("               private " + relationServiceName + ": " + relation.getTargetClass() +
                                "Service,\n");
                    }
                }

                tsWriter.write("                           ){}\n");


                // ngOnInit for form initialization and data loading
                tsWriter.write("\n  ngOnInit(): void {\n");
                tsWriter.write("    this." + kebabCaseName + "Form = this.fb.group({\n");
                for (Attribute attribute : diagramClass.getAttributes()) {
                    if (attribute.isItsId()) tsWriter.write("      " + attribute.getAttributeName() + ": [''],\n");
                    else tsWriter.write("      " + attribute.getAttributeName() + ": ['', Validators.required],\n");
                }
                for (Relation relation : diagramClass.getRelations()) {
                    if ("ManyToOne".equals(relation.getRelationType()) || "OneToOne".equals(relation.getRelationType())) {
                        String relationName = relation.getTargetClass().toLowerCase();
                        tsWriter.write("      " + relationName + "Id: ['', Validators.required],\n");
                    } else if (relation.getRelationType().matches("ManyToMany\\((\\d+)\\)")) {
                        int numVars = Integer.parseInt(relation.getRelationType().replaceAll("ManyToMany\\((\\d+)\\)", "$1"));
                        String relationName = relation.getTargetClass().toLowerCase();
                        for (int i = 1; i <= numVars; i++) {
                            tsWriter.write("      " + relationName + "Id" + i + ": ['', Validators.required],\n");
                        }
                    }
                }
                tsWriter.write("    });\n\n");

//                tsWriter.write("    this." + kebabCaseName + "Service.fetchAll();\n");
                tsWriter.write("  }\n\n");

                // Generate onEdit method
                tsWriter.write("  onEdit(" + kebabCaseName + ": " + className + "): void {\n");
                tsWriter.write("    this.selected" + className + " = " + kebabCaseName + ";\n");
                tsWriter.write("    this." + kebabCaseName + "Form.patchValue({\n");
                for (Attribute attribute : diagramClass.getAttributes()) {
                    tsWriter.write("      " + attribute.getAttributeName() + ": " + kebabCaseName + "." + attribute.getAttributeName() + ",\n");
                }
                tsWriter.write("    });\n");
                tsWriter.write("  }\n\n");

                // Generate onSubmit method
                tsWriter.write("  onSubmit(): void {\n");
                tsWriter.write("    if (this." + kebabCaseName + "Form.valid) {\n");
                tsWriter.write("      const data" + className + " = this." + kebabCaseName + "Form.value;\n");
                tsWriter.write("\n");
                tsWriter.write("      if (this.selected" + className + ") {\n");
                tsWriter.write("        this." + kebabCaseName + "Service.update(data" + className + ");\n");
                tsWriter.write("          this.selected" + className + " = null;\n");
                tsWriter.write("      } else {\n");
                tsWriter.write("        this." + kebabCaseName + "Service.add(data" + className + ");\n");
                tsWriter.write("          this.selected" + className + " = null;\n");
                tsWriter.write("      }\n");
                tsWriter.write("      this.modalService.close('" + kebabCaseName + "Modal');\n");
                tsWriter.write("    } else {\n");
                tsWriter.write("        for (const i in this." + kebabCaseName + "Form.controls) {\n" +
                                   "            this." + kebabCaseName +"Form.controls[i].markAsTouched();\n" +
                                   "            this." + kebabCaseName +"Form.controls[i].updateValueAndValidity();\n" +
                                   "        }\n");
                tsWriter.write("    }\n\n");
                tsWriter.write("  }\n\n");

                tsWriter.write("  onDelete(" + kebabCaseName + ": " + className + "): void {\n");
                for (Attribute attribute : diagramClass.getAttributes()) {
                    if (attribute.isItsId()) tsWriter.write("    this.deleted" + className + " = " + kebabCaseName + "." + attribute.getAttributeName() + ";\n");
                }
                tsWriter.write("  }\n\n");

                // Generate delete method
                tsWriter.write("  delete" + className + "(): void {\n");
                tsWriter.write("    this." + kebabCaseName + "Service.delete(this.deleted" + className + "!);\n");
                tsWriter.write("    this.modalService.close('delete" + className + "');\n");
                tsWriter.write("  }\n");

                tsWriter.write("}\n");
            }

        }
    }

    private DiagramClass getDiagramClassByName(String className, List<DiagramClass> diagramClasses) {
        for (DiagramClass diagramClass : diagramClasses) {
            if (diagramClass.getClassName().equals(className)) {
                return diagramClass;
            }
        }
        return null;
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
