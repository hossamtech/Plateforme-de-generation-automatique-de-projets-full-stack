import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private modals: { [key: string]: any } = {}; // Stores both state and data

  constructor() {}

  open(id: string) {
    this.modals[id] = true;
  }

  close(id: string) {
    this.modals[id] = false;
  }

  isOpen(id: string) {
    return this.modals[id];
  }

  getModalData(id: string) {
    return this.modals[id]?.data;
  }
}
