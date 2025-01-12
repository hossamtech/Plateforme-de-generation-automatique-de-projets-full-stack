import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {NgForOf, NgIf} from "@angular/common";
import { arbitreService } from '../../services/arbitre.service';

@Component({
  selector: 'app-arbitre',
  templateUrl: './arbitre.component.html',
  standalone: true,
  imports: [ NgForOf, NgIf, ReactiveFormsModule],
  styleUrls: ['./arbitre.component.css']
})
export class arbitreComponent implements OnInit {
  arbitreList: any[] = [];
  arbitreForm!: FormGroup;
  showForm: boolean = false;
  editMode: boolean = false;
  currentItemId: number | null = null;

  constructor(private fb: FormBuilder, private service: arbitreService) {}

  ngOnInit(): void {
    this.arbitreForm = this.fb.group({
      idArbitre: ['', Validators.required],
      nom: ['', Validators.required],
      Nationalite: ['', Validators.required],
    });

    this.service.getAll().subscribe(data => {
      this.arbitreList = data;
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editMode = false;
  }

  closeForm(): void {
    this.showForm = false;
    this.arbitreForm.reset();
    this.currentItemId = null;
  }

  addOrUpdatearbitre(): void {
    if (this.arbitreForm.valid) {
      const arbitreData = this.arbitreForm.value;
      if (this.editMode && this.currentItemId !== null) {
        arbitreData.id = this.currentItemId;
        this.service.update(arbitreData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      } else {
        this.service.add(arbitreData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      }
    }
  }

  editarbitre(item: any): void {
    this.showForm = true;
    this.editMode = true;
    this.currentItemId = item.id;
    this.arbitreForm.patchValue(item);
  }

  deletearbitre(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.ngOnInit();
    });
  }
}
