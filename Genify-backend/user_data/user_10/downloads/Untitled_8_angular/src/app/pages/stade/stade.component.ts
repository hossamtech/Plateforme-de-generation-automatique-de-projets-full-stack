import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { StadeService } from '../../services/stade.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Stade} from "../../models/stade.model";

@Component({
  selector: 'app-stade',
  templateUrl: './stade.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf ],
  styleUrls: ['./stade.component.css']
})
export class StadeComponent implements OnInit {
  stadeForm!: FormGroup;
  stadeList = this.stadeService.stadeList;
  selectedStade: any = null;
  deletedStade: number = undefined;
) {

  constructor(private fb: FormBuilder, private stadeService: StadeService) {}

  ngOnInit(): void {
    this.stadeForm = this.fb.group({
      idstade: ['', Validators.required],
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
      const updatedStade = this.stadeForm.value;

      if (this.selectedStade) {
        this.stadeService.update(updatedStade);
          this.selectedStade = null;
      } else {
        this.stadeService.add(updatedStade);
          this.selectedStade = null;
      }
    }
  }

  onDelete(stade: Stade): void {
    this.stadeService.delete(this.deletedStade!);  }
  deleteStade(): void {
    this.stadeService.delete(this.deletedStade!);  }
}
