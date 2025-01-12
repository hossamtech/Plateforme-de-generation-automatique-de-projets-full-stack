import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EquipeService } from '../../services/equipe.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-equipe',
  templateUrl: './equipe.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf ],
  styleUrls: ['./equipe.component.css']
})
export class EquipeComponent implements OnInit {
  equipeForm!: FormGroup;
  equipeList = this.equipeService.equipeList;
  selectedEquipe: any = null;

  constructor(private fb: FormBuilder,
              private equipeService: EquipeService) {

  constructor(private fb: FormBuilder, private equipeService: EquipeService) {}

  ngOnInit(): void {
    this.equipeForm = this.fb.group({
      idequipe: ['', Validators.required],
      nomequipe: ['', Validators.required],
      pays: ['', Validators.required],
    });

  }

  onEdit(equipe: any): void {
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

  deleteEquipe(id: number): void {
    this.equipeService.delete(id).subscribe(() => {
      this.ngOnInit();
    });
  }
}
