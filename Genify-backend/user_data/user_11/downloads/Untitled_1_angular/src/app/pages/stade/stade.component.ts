import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {NgForOf, NgIf} from "@angular/common";
import { stadeService } from '../../services/stade.service';

@Component({
  selector: 'app-stade',
  templateUrl: './stade.component.html',
  standalone: true,
  imports: [ NgForOf, NgIf, ReactiveFormsModule],
  styleUrls: ['./stade.component.css']
})
export class stadeComponent implements OnInit {
  stadeList: any[] = [];
  stadeForm!: FormGroup;
  showForm: boolean = false;
  editMode: boolean = false;
  currentItemId: number | null = null;

  constructor(private fb: FormBuilder, private service: stadeService) {}

  ngOnInit(): void {
    this.stadeForm = this.fb.group({
      idStade: ['', Validators.required],
      nomStade: ['', Validators.required],
      ville: ['', Validators.required],
    });

    this.service.getAll().subscribe(data => {
      this.stadeList = data;
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editMode = false;
  }

  closeForm(): void {
    this.showForm = false;
    this.stadeForm.reset();
    this.currentItemId = null;
  }

  addOrUpdatestade(): void {
    if (this.stadeForm.valid) {
      const stadeData = this.stadeForm.value;
      if (this.editMode && this.currentItemId !== null) {
        stadeData.id = this.currentItemId;
        this.service.update(stadeData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      } else {
        this.service.add(stadeData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      }
    }
  }

  editstade(item: any): void {
    this.showForm = true;
    this.editMode = true;
    this.currentItemId = item.id;
    this.stadeForm.patchValue(item);
  }

  deletestade(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.ngOnInit();
    });
  }
}
