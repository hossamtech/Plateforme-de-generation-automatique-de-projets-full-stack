package codegen.spring_angular_auto_generator.angularService;

import codegen.spring_angular_auto_generator.springBootGenerator.dto.DiagramClass;
import codegen.spring_angular_auto_generator.springBootGenerator.dto.ProjectGenerationResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ModalGenerated {

    public void generateModal(ProjectGenerationResponse projectGenerationResponse) throws IOException {
        String modalsDirPath = projectGenerationResponse.getExtractPath() + "/src/app/modals";

        createDirectory(modalsDirPath);

        generateModalModule(modalsDirPath);
        generateModalService(modalsDirPath);
        generateModalsComponent(modalsDirPath);
        generateModalTriggerDirective(modalsDirPath);
        generateModalDismissDirective(modalsDirPath);
    }

    private void generateModalModule(String modalsDirPath) throws IOException {
        try (FileWriter writer = new FileWriter(modalsDirPath + "/modal.module.ts")) {
            writer.write("import { CommonModule } from '@angular/common';\n");
            writer.write("import { NgModule, ModuleWithProviders } from '@angular/core';\n");
            writer.write("import { MDModalsComponent } from './modals.component';\n");
            writer.write("import { ModalService } from './modal.service';\n");
            writer.write("import { ModalTriggerDirective } from './modal-trigger.directive';\n");
            writer.write("import { ModalDismissDirective } from './modal-dismiss.directive';\n");
            writer.write("\n");
            writer.write("@NgModule({\n");
            writer.write("    imports: [CommonModule],\n");
            writer.write("    declarations: [\n");
            writer.write("        MDModalsComponent,\n");
            writer.write("        ModalTriggerDirective,\n");
            writer.write("        ModalDismissDirective\n");
            writer.write("    ],\n");
            writer.write("    providers: [ModalService],\n");
            writer.write("    exports: [MDModalsComponent, ModalTriggerDirective, ModalDismissDirective]\n");
            writer.write("})\n");
            writer.write("export class MDModalModule {\n");
            writer.write("    static forRoot(): ModuleWithProviders<MDModalModule> {\n");
            writer.write("        return { ngModule: MDModalModule, providers: [] };\n");
            writer.write("    }\n");
            writer.write("}\n");
        }
    }

    private void generateModalService(String modalsDirPath) throws IOException {
        try (FileWriter writer = new FileWriter(modalsDirPath + "/modal.service.ts")) {
            writer.write("import { Injectable } from '@angular/core';\n");
            writer.write("\n");
            writer.write("@Injectable({\n");
            writer.write("  providedIn: 'root'\n");
            writer.write("})\n");
            writer.write("export class ModalService {\n");
            writer.write("  private modals: { [key: string]: any } = {}; // Stores both state and data\n");
            writer.write("\n");
            writer.write("  constructor() {}\n");
            writer.write("\n");
            writer.write("  open(id: string) {\n");
            writer.write("    this.modals[id] = true;\n");
            writer.write("  }\n");
            writer.write("\n");
            writer.write("  close(id: string) {\n");
            writer.write("    this.modals[id] = false;\n");
            writer.write("  }\n");
            writer.write("\n");
            writer.write("  isOpen(id: string) {\n");
            writer.write("    return this.modals[id];\n");
            writer.write("  }\n");
            writer.write("\n");
            writer.write("  getModalData(id: string) {\n");
            writer.write("    return this.modals[id]?.data;\n");
            writer.write("  }\n");
            writer.write("}\n");
        }
    }

    private void generateModalsComponent(String modalsDirPath) throws IOException {
        try (FileWriter writer = new FileWriter(modalsDirPath + "/modals.component.ts")) {
            writer.write("import { Component, ElementRef, EventEmitter, HostListener, Input, Output, Renderer2, ViewEncapsulation } from '@angular/core';\n");
            writer.write("import { ModalService } from './modal.service';\n");
            writer.write("\n");
            writer.write("@Component({\n");
            writer.write("  selector: 'ng-modals',\n");
            writer.write("  template: `\n");
            writer.write("    <ng-container *ngIf=\"isOpen\">\n");
            writer.write("      <div [attr.modal-center]=\"placement === 'modal-center' ? true : null\"\n");
            writer.write("           [attr.modal-top]=\"placement === 'modal-top' ? true : null\"\n");
            writer.write("           [attr.modal-bottom]=\"placement === 'modal-bottom' ? true : null\"\n");
            writer.write("           [ngClass]=\"className\">\n");
            writer.write("        <ng-content></ng-content>\n");
            writer.write("      </div>\n");
            writer.write("    </ng-container>\n");
            writer.write("    <div class=\"fixed inset-0 bg-slate-900/40 dark:bg-zink-800/70 z-[1049] backdrop-overlay\" [ngClass]=\"{'hidden': !isOpen}\" id=\"backDropDiv\"></div>\n");
            writer.write("  `,\n");
            writer.write("  encapsulation: ViewEncapsulation.None,\n");
            writer.write("})\n");
            writer.write("export class MDModalsComponent {\n");
            writer.write("  @Input() id!: string;\n");
            writer.write("  @Input() className!: string;\n");
            writer.write("  @Input() placement!: string;\n");
            writer.write("  @Output() closed = new EventEmitter<void>();\n");
            writer.write("\n");
            writer.write("  constructor(private modalService: ModalService) {}\n");
            writer.write("\n");
            writer.write("  get isOpen() {\n");
            writer.write("    return this.modalService.isOpen(this.id);\n");
            writer.write("  }\n");
            writer.write("\n");
            writer.write("  close() {\n");
            writer.write("    this.modalService.close(this.id);\n");
            writer.write("    this.closed.emit();\n");
            writer.write("  }\n");
            writer.write("}\n");
        }
    }

    private void generateModalTriggerDirective(String modalsDirPath) throws IOException {
        try (FileWriter writer = new FileWriter(modalsDirPath + "/modal-trigger.directive.ts")) {
            writer.write("import { Directive, ElementRef, HostListener, Input } from '@angular/core';\n");
            writer.write("import { ModalService } from './modal.service';\n");
            writer.write("\n");
            writer.write("@Directive({\n");
            writer.write("  selector: '[appModalTrigger]'\n");
            writer.write("})\n");
            writer.write("export class ModalTriggerDirective {\n");
            writer.write("  @Input('appModalTrigger') modalId!: string;\n");
            writer.write("\n");
            writer.write("  constructor(private modalService: ModalService) {}\n");
            writer.write("\n");
            writer.write("  @HostListener('click')\n");
            writer.write("  onClick() {\n");
            writer.write("    this.modalService.open(this.modalId);\n");
            writer.write("  }\n");
            writer.write("}\n");
        }
    }

    private void generateModalDismissDirective(String modalsDirPath) throws IOException {
        try (FileWriter writer = new FileWriter(modalsDirPath + "/modal-dismiss.directive.ts")) {
            writer.write("import { Directive, HostListener, Input } from '@angular/core';\n");
            writer.write("import { ModalService } from './modal.service';\n");
            writer.write("\n");
            writer.write("@Directive({\n");
            writer.write("  selector: '[dismissModal]'\n");
            writer.write("})\n");
            writer.write("export class ModalDismissDirective {\n");
            writer.write("  @Input('ModalDismiss') modalId!: string;\n");
            writer.write("\n");
            writer.write("  constructor(private modalService: ModalService) {}\n");
            writer.write("\n");
            writer.write("  @HostListener('click')\n");
            writer.write("  onClick() {\n");
            writer.write("    this.modalService.close(this.modalId);\n");
            writer.write("  }\n");
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
