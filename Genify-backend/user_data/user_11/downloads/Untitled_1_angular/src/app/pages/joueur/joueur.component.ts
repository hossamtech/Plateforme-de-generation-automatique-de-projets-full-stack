import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {NgForOf, NgIf} from "@angular/common";
import { joueurService } from '../../services/joueur.service';
import { equipeService } from '../../services/equipe.service';

@Component({
  selector: 'app-joueur',
  templateUrl: './joueur.component.html',
  standalone: true,
  imports: [ NgForOf, NgIf, ReactiveFormsModule],
  styleUrls: ['./joueur.component.css']
})
export class joueurComponent implements OnInit {
  joueurList: any[] = [];
  joueurForm!: FormGroup;
  showForm: boolean = false;
  editMode: boolean = false;
  currentItemId: number | null = null;
  equipeList: any[] = [];

  constructor(private fb: FormBuilder, private service: joueurService, private equipeService: equipeService) {}

  ngOnInit(): void {
    this.joueurForm = this.fb.group({
      idJoueur: ['', Validators.required],
      nomJoueur: ['', Validators.required],
      poste: ['', Validators.required],
      equipeId: ['', Validators.required],
    });

    this.service.getAll().subscribe(data => {
      this.joueurList = data;
    });
    this.equipeService.getAll().subscribe(data => {
      this.equipeList = data;
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editMode = false;
  }

  closeForm(): void {
    this.showForm = false;
    this.joueurForm.reset();
    this.currentItemId = null;
  }

  addOrUpdatejoueur(): void {
    if (this.joueurForm.valid) {
      const joueurData = this.joueurForm.value;
      if (this.editMode && this.currentItemId !== null) {
        joueurData.id = this.currentItemId;
        this.service.update(joueurData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      } else {
        this.service.add(joueurData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      }
    }
  }

  editjoueur(item: any): void {
    this.showForm = true;
    this.editMode = true;
    this.currentItemId = item.id;
    this.joueurForm.patchValue(item);
  }

  deletejoueur(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.ngOnInit();
    });
  }
}
