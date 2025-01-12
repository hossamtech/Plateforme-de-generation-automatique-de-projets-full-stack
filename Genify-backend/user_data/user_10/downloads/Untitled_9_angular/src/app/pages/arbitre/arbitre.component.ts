import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ArbitreService } from '../../services/arbitre.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Arbitre} from "../../models/arbitre.model";

import { ModalService } from '../../modals/modal.service';
@Component({
  selector: 'app-arbitre',
  templateUrl: './arbitre.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf, NgForOf ],
  styleUrls: ['./arbitre.component.scss']
})
export class ArbitreComponent implements OnInit {
  arbitreForm!: FormGroup;
  arbitreList = this.arbitreService.arbitreList;
  selectedArbitre: any = null;
  deletedArbitre: number = 0;

  constructor(private fb: FormBuilder, private arbitreService: ArbitreService, private modalService: ModalService,) {}

  ngOnInit(): void {
    this.arbitreForm = this.fb.group({
      idarbitre: [''],
      nom: ['', Validators.required],
      nationalite: ['', Validators.required],
    });

  }

  onEdit(arbitre: Arbitre): void {
    this.selectedArbitre = arbitre;
    this.arbitreForm.patchValue({
      idarbitre: arbitre.idarbitre,
      nom: arbitre.nom,
      nationalite: arbitre.nationalite,
    });
  }

  onSubmit(): void {
    if (this.arbitreForm.valid) {
      const dataArbitre = this.arbitreForm.value;

      if (this.selectedArbitre) {
        this.arbitreService.update(dataArbitre);
          this.selectedArbitre = null;
      } else {
        this.arbitreService.add(dataArbitre);
          this.selectedArbitre = null;
      }
      this.modalService.close('arbitreModal');
      this.arbitreForm.reset();
    } else {
        for (const i in this.arbitreForm.controls) {
            this.arbitreForm.controls[i].markAsTouched();
            this.arbitreForm.controls[i].updateValueAndValidity();
        }
    }

  }

  onDelete(arbitre: Arbitre): void {
    this.deletedArbitre = arbitre.idarbitre;
  }
  deleteArbitre(): void {
    this.arbitreService.delete(this.deletedArbitre!);    this.modalService.close('deleteArbitre');
  }
}
