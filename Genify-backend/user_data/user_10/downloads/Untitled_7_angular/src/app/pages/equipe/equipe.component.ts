import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EquipeService } from '../../services/equipe.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Equipe} from "../../models/equipe.model";

@Component({
  selector: 'app-equipe',
  templateUrl: './equipe.component.html',
  standalone: true,
  imports: [LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf, NgForOf],
  styleUrls: ['./equipe.component.css']
})
export class EquipeComponent implements OnInit {
  equipeForm!: FormGroup;
  equipeList = this.equipeService.equipeList;
  selectedEquipe: any = null;
  deletedEquipe: number | undefined;


  constructor(private fb: FormBuilder, private equipeService: EquipeService) {}

  ngOnInit(): void {
    this.equipeForm = this.fb.group({
      idequipe: ['', Validators.required],
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
      const updatedEquipe = this.equipeForm.value;

      if (this.selectedEquipe) {
        this.equipeService.update(updatedEquipe);
          this.selectedEquipe = null;
      } else {
        this.equipeService.add(updatedEquipe);
          this.selectedEquipe = null;
      }
    }
  }

  onDelete(equipe: Equipe): void {
    this.deletedEquipe = equipe.idequipe;
  }
  deleteEquipe(): void {
    this.equipeService.delete(this.deletedEquipe!);  }
}
