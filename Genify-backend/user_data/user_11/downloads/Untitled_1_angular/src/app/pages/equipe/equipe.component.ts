import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {NgForOf, NgIf} from "@angular/common";
import { equipeService } from '../../services/equipe.service';

@Component({
  selector: 'app-equipe',
  templateUrl: './equipe.component.html',
  standalone: true,
  imports: [ NgForOf, NgIf, ReactiveFormsModule],
  styleUrls: ['./equipe.component.css']
})
export class equipeComponent implements OnInit {
  equipeList: any[] = [];
  equipeForm!: FormGroup;
  showForm: boolean = false;
  editMode: boolean = false;
  currentItemId: number | null = null;

  constructor(private fb: FormBuilder, private service: equipeService) {}

  ngOnInit(): void {
    this.equipeForm = this.fb.group({
      idEquipe: ['', Validators.required],
      nomEquipe: ['', Validators.required],
      pays: ['', Validators.required],
    });

    this.service.getAll().subscribe(data => {
      this.equipeList = data;
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editMode = false;
  }

  closeForm(): void {
    this.showForm = false;
    this.equipeForm.reset();
    this.currentItemId = null;
  }

  addOrUpdateequipe(): void {
    if (this.equipeForm.valid) {
      const equipeData = this.equipeForm.value;
      if (this.editMode && this.currentItemId !== null) {
        equipeData.id = this.currentItemId;
        this.service.update(equipeData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      } else {
        this.service.add(equipeData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      }
    }
  }

  editequipe(item: any): void {
    this.showForm = true;
    this.editMode = true;
    this.currentItemId = item.id;
    this.equipeForm.patchValue(item);
  }

  deleteequipe(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.ngOnInit();
    });
  }
}
