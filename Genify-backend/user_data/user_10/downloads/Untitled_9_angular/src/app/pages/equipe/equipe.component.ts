import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EquipeService } from '../../services/equipe.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Equipe} from "../../models/equipe.model";

import { ModalService } from '../../modals/modal.service';
@Component({
  selector: 'app-equipe',
  templateUrl: './equipe.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf, NgForOf ],
  styleUrls: ['./equipe.component.scss']
})
export class EquipeComponent implements OnInit {
  equipeForm!: FormGroup;
  equipeList = this.equipeService.equipeList;
  selectedEquipe: any = null;
  deletedEquipe: number = 0;

  constructor(private fb: FormBuilder, private equipeService: EquipeService, private modalService: ModalService,) {}

  ngOnInit(): void {
    this.equipeForm = this.fb.group({
      idequipe: [''],
      nomequipe: ['', Validators.required],
      pays: ['', Validators.required],
    });

  }

  onEdit(equipe: Equipe): void {
    this.selectedEquipe = equipe;
    this.equipeForm.patchValue({
      idequipe: equipe.idequipe,
      nomequipe: equipe.nomequipe,
      pays: equipe.pays,
    });
  }

  onSubmit(): void {
    if (this.equipeForm.valid) {
      const dataEquipe = this.equipeForm.value;

      if (this.selectedEquipe) {
        this.equipeService.update(dataEquipe);
          this.selectedEquipe = null;
      } else {
        this.equipeService.add(dataEquipe);
          this.selectedEquipe = null;
      }
      this.modalService.close('equipeModal');
    } else {
        for (const i in this.equipeForm.controls) {
            this.equipeForm.controls[i].markAsTouched();
            this.equipeForm.controls[i].updateValueAndValidity();
        }
    }

  }

  onDelete(equipe: Equipe): void {
    this.deletedEquipe =equipe.idequipe;
  }
  deleteEquipe(): void {
    this.equipeService.delete(this.deletedEquipe!);    this.modalService.close('deleteEquipe');
  }
}
