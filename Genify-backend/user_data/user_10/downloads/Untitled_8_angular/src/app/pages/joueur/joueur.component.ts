import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { JoueurService } from '../../services/joueur.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Joueur} from "../../models/joueur.model";

@Component({
  selector: 'app-joueur',
  templateUrl: './joueur.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf ],
  styleUrls: ['./joueur.component.css']
})
export class JoueurComponent implements OnInit {
  joueurForm!: FormGroup;
  joueurList = this.joueurService.joueurList;
  selectedJoueur: any = null;
  deletedJoueur: number = undefined;
  equipeList: any[] = this.equipeServiceequipeList;
,
              private equipeService: EquipeService) {

  constructor(private fb: FormBuilder, private joueurService: JoueurService) {}

  ngOnInit(): void {
    this.joueurForm = this.fb.group({
      idjoueur: ['', Validators.required],
      nomjoueur: ['', Validators.required],
      poste: ['', Validators.required],
      equipeId: ['', Validators.required],
    });

  }

  onEdit(joueur: Joueur): void {
    this.selectedJoueur = joueur;
    this.joueurForm.patchValue({
      idjoueur: joueur.idjoueur,
      nomjoueur: joueur.nomjoueur,
      poste: joueur.poste,
    });
  }

  onSubmit(): void {
    if (this.joueurForm.valid) {
      const updatedJoueur = this.joueurForm.value;

      if (this.selectedJoueur) {
        this.joueurService.update(updatedJoueur);
          this.selectedJoueur = null;
      } else {
        this.joueurService.add(updatedJoueur);
          this.selectedJoueur = null;
      }
    }
  }

  onDelete(joueur: Joueur): void {
    this.joueurService.delete(this.deletedJoueur!);  }
  deleteJoueur(): void {
    this.joueurService.delete(this.deletedJoueur!);  }
}
