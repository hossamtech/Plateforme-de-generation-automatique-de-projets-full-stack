import { Component, ElementRef, EventEmitter, HostListener, Input, Output, Renderer2, ViewEncapsulation } from '@angular/core';
import { ModalService } from './modal.service';

@Component({
  selector: 'ng-modals',
  template: `
    <ng-container *ngIf="isOpen">
      <div [attr.modal-center]="placement === 'modal-center' ? true : null"
           [attr.modal-top]="placement === 'modal-top' ? true : null"
           [attr.modal-bottom]="placement === 'modal-bottom' ? true : null"
           [ngClass]="className">
        <ng-content></ng-content>
      </div>
    </ng-container>
    <div class="fixed inset-0 bg-slate-900/40 dark:bg-zink-800/70 z-[1049] backdrop-overlay" [ngClass]="{'hidden': !isOpen}" id="backDropDiv"></div>
  `,
  encapsulation: ViewEncapsulation.None,
})
export class MDModalsComponent {
  @Input() id!: string;
  @Input() className!: string;
  @Input() placement!: string;
  @Output() closed = new EventEmitter<void>();

  constructor(private modalService: ModalService) {}

  get isOpen() {
    return this.modalService.isOpen(this.id);
  }

  close() {
    this.modalService.close(this.id);
    this.closed.emit();
  }
}
