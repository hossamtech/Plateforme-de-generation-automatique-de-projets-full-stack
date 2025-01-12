import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { StadeService } from '../../services/stade.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Stade} from "../../models/stade.model";

import { ModalService } from '../../modals/modal.service';
@Component({
  selector: 'app-stade',
  templateUrl: './stade.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf, NgForOf ],
  styleUrls: ['./stade.component.scss']
})
export class StadeComponent implements OnInit {
  stadeForm!: FormGroup;
  stadeList = this.stadeService.stadeList;
  selectedStade: any = null;
  deletedStade: number = 0;

  constructor(private fb: FormBuilder, private stadeService: StadeService, private modalService: ModalService,) {}

  ngOnInit(): void {
    this.stadeForm = this.fb.group({
      idstade: [''],
      nomstade: ['', Validators.required],
      ville: ['', Validators.required],
    });

  }

  onEdit(stade: Stade): void {
    this.selectedStade = stade;
    this.stadeForm.patchValue({
      idstade: stade.idstade,
      nomstade: stade.nomstade,
      ville: stade.ville,
    });
  }

  onSubmit(): void {
    if (this.stadeForm.valid) {
      const dataStade = this.stadeForm.value;
      console.log(dataStade)
      if (this.selectedStade) {
        this.stadeService.update(dataStade);
          this.selectedStade = null;
      } else {
        this.stadeService.add(dataStade);
          this.selectedStade = null;
      }
      this.modalService.close('stadeModal');
    } else {
        for (const i in this.stadeForm.controls) {
            this.stadeForm.controls[i].markAsTouched();
            this.stadeForm.controls[i].updateValueAndValidity();
        }
    }

  }

  onDelete(stade: Stade): void {
    this.deletedStade =stade.idstade;
  }
  deleteStade(): void {
    this.stadeService.delete(this.deletedStade!);    this.modalService.close('deleteStade');
  }
}
