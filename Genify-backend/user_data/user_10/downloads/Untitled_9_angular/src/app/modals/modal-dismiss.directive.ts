import { Directive, HostListener, Input } from '@angular/core';
import { ModalService } from './modal.service';

@Directive({
  selector: '[dismissModal]'
})
export class ModalDismissDirective {
  @Input('ModalDismiss') modalId!: string;

  constructor(private modalService: ModalService) {}

  @HostListener('click')
  onClick() {
    this.modalService.close(this.modalId);
  }
}
