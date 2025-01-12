import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ArbitreService } from '../../services/arbitre.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Arbitre} from "../../models/arbitre.model";

@Component({
  selector: 'app-arbitre',
  templateUrl: './arbitre.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf ],
  styleUrls: ['./arbitre.component.css']
})
export class ArbitreComponent implements OnInit {
  arbitreForm!: FormGroup;
  arbitreList = this.arbitreService.arbitreList;
  selectedArbitre: any = null;
  deletedArbitre: number = undefined;
) {

  constructor(private fb: FormBuilder, private arbitreService: ArbitreService) {}

  ngOnInit(): void {
    this.arbitreForm = this.fb.group({
      idarbitre: ['', Validators.required],
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
      const updatedArbitre = this.arbitreForm.value;

      if (this.selectedArbitre) {
        this.arbitreService.update(updatedArbitre);
          this.selectedArbitre = null;
      } else {
        this.arbitreService.add(updatedArbitre);
          this.selectedArbitre = null;
      }
    }
  }

  onDelete(arbitre: Arbitre): void {
    this.arbitreService.delete(this.deletedArbitre!);  }
  deleteArbitre(): void {
    this.arbitreService.delete(this.deletedArbitre!);  }
}
