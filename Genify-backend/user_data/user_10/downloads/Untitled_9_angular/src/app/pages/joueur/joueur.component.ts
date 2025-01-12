import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { JoueurService } from '../../services/joueur.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Joueur} from "../../models/joueur.model";

import { ModalService } from '../../modals/modal.service';
import {EquipeService} from "../../services/equipe.service";
import {NgSelectComponent} from "@ng-select/ng-select";
@Component({
  selector: 'app-joueur',
  templateUrl: './joueur.component.html',
  standalone: true,
  imports: [LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf, NgForOf, NgSelectComponent],
  styleUrls: ['./joueur.component.scss'],
  encapsulation: ViewEncapsulation.None  // Disable encapsulation

})
export class JoueurComponent implements OnInit {
  joueurForm!: FormGroup;
  joueurList = this.joueurService.joueurList;
  selectedJoueur: any = null;
  deletedJoueur: number = 0;
  equipeList = this.equipeService.equipeList;

  constructor(private fb: FormBuilder, private equipeService: EquipeService, private joueurService: JoueurService, private modalService: ModalService,) {}

  ngOnInit(): void {
    this.joueurForm = this.fb.group({
      idjoueur: [''],
      nomjoueur: ['', Validators.required],
      poste: ['', Validators.required],
      idequipe: [0, Validators.required],
    });

  }

  onEdit(joueur: Joueur): void {
    this.selectedJoueur = joueur;
    this.joueurForm.patchValue({
      idjoueur: joueur.idjoueur,
      nomjoueur: joueur.nomjoueur,
      poste: joueur.poste,
      idequipe: joueur.idequipe
    });
  }

  onSubmit(): void {
    console.log(this.joueurForm.value)
    if (this.joueurForm.valid) {
      const dataJoueur = this.joueurForm.value;

      if (this.selectedJoueur) {
        this.joueurService.update(dataJoueur);
          this.selectedJoueur = null;
      } else {
        console.log(dataJoueur)
        this.joueurService.add(dataJoueur);
          this.selectedJoueur = null;
      }
      this.modalService.close('joueurModal');
    } else {
        for (const i in this.joueurForm.controls) {
            this.joueurForm.controls[i].markAsTouched();
            this.joueurForm.controls[i].updateValueAndValidity();
        }
    }

  }

  onDelete(joueur: Joueur): void {
    this.deletedJoueur =joueur.idjoueur;
  }
  deleteJoueur(): void {
    this.joueurService.delete(this.deletedJoueur!);    this.modalService.close('deleteJoueur');
  }
}
