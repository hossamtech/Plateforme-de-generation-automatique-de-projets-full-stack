import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { ArbitreService } from '../../services/arbitre.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import {NgIf} from "@angular/common";
import {subscribeOn} from "rxjs";
import {FlatpickrModule} from "../flatpickr/flatpickr.module";
import {French} from "flatpickr/dist/l10n/fr";

@Component({
  selector: 'app-arbitre',
  templateUrl: './arbitre.component.html',
  standalone: true,
  imports: [
    LucideAngularModule,
    MDModalModule,
    ReactiveFormsModule,
    NgIf,
    FlatpickrModule
  ],
  styleUrls: ['./arbitre.component.css']
})
export class ArbitreComponent implements OnInit {
  arbitreForm!: FormGroup;
  arbitreData = this.service.arbitreList;
  selectedArbitre: any = null; // Store the arbitre being edited

  constructor(private fb: FormBuilder, private service: ArbitreService, private modalService: NgbModal) {}

  ngOnInit(): void {
    // Initialize form
    this.arbitreForm = this.fb.group({
      idarbitre: ['', Validators.required],
      nom: ['', Validators.required],
      nationalite: ['', Validators.required],
      date: ['', Validators.required],
      time: ['', Validators.required],
    });

    // Fetch arbitres directly from the service (arbitreList$ signal)
    this.service.fetchAllArbitre();
  }

  // Handle the selection of an arbitre for editing
  onEdit(arbitre: any): void {
    this.selectedArbitre = arbitre;
    this.arbitreForm.patchValue({
      idarbitre: arbitre.idarbitre,
      nom: arbitre.name,
      nationalite: arbitre.nationalite,
    });
  }

  // Handle form submission for adding or updating an arbitre
  onSubmit(): void {
    if (this.arbitreForm.valid) {
      const updatedArbitre = this.arbitreForm.value;

      if (this.selectedArbitre) {
        // Update existing arbitre
        this.service.update({
          idarbitre: updatedArbitre.idarbitre,
          nom: updatedArbitre.nom,
          nationalite: updatedArbitre.nationalite,
        });
      } else {
        // Add new arbitre
        this.service.add({
          idarbitre: 0, // Backend assigns ID
          nom: updatedArbitre.nom,
          nationalite: updatedArbitre.nationalite,
        });
      }

      // Clear form
      this.arbitreForm.reset();
      this.selectedArbitre = null;
    } else {
      for (const i in this.arbitreForm.controls) {
        this.arbitreForm.controls[i].markAsTouched();
        this.arbitreForm.controls[i].updateValueAndValidity();
      }
    }
  }

  // Delete an arbitre
  deleteArbitre(id: number): void {
    this.service.delete(id);
  }

  protected readonly subscribeOn = subscribeOn;
  protected readonly French = French;
}
