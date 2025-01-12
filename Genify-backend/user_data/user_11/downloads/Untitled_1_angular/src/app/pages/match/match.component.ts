import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {NgForOf, NgIf} from "@angular/common";
import { matchService } from '../../services/match.service';
import { arbitreService } from '../../services/arbitre.service';
import { stadeService } from '../../services/stade.service';

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  standalone: true,
  imports: [ NgForOf, NgIf, ReactiveFormsModule],
  styleUrls: ['./match.component.css']
})
export class matchComponent implements OnInit {
  matchList: any[] = [];
  matchForm!: FormGroup;
  showForm: boolean = false;
  editMode: boolean = false;
  currentItemId: number | null = null;
  arbitreList: any[] = [];
  stadeList: any[] = [];

  constructor(private fb: FormBuilder, private service: matchService, private arbitreService: arbitreService, private stadeService: stadeService) {}

  ngOnInit(): void {
    this.matchForm = this.fb.group({
      idMatch: ['', Validators.required],
      dateMatch: ['', Validators.required],
      heureMatch: ['', Validators.required],
      arbitreId: ['', Validators.required],
      stadeId: ['', Validators.required],
    });

    this.service.getAll().subscribe(data => {
      this.matchList = data;
    });
    this.arbitreService.getAll().subscribe(data => {
      this.arbitreList = data;
    });
    this.stadeService.getAll().subscribe(data => {
      this.stadeList = data;
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editMode = false;
  }

  closeForm(): void {
    this.showForm = false;
    this.matchForm.reset();
    this.currentItemId = null;
  }

  addOrUpdatematch(): void {
    if (this.matchForm.valid) {
      const matchData = this.matchForm.value;
      if (this.editMode && this.currentItemId !== null) {
        matchData.id = this.currentItemId;
        this.service.update(matchData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      } else {
        this.service.add(matchData).subscribe(() => {
          this.ngOnInit();
          this.closeForm();
        });
      }
    }
  }

  editmatch(item: any): void {
    this.showForm = true;
    this.editMode = true;
    this.currentItemId = item.id;
    this.matchForm.patchValue(item);
  }

  deletematch(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.ngOnInit();
    });
  }
}
