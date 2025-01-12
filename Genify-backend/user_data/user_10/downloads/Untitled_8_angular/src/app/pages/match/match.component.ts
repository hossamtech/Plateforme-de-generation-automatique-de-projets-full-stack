import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatchService } from '../../services/match.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Match} from "../../models/match.model";

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  standalone: true,
  imports: [ LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf ],
  styleUrls: ['./match.component.css']
})
export class MatchComponent implements OnInit {
  matchForm!: FormGroup;
  matchList = this.matchService.matchList;
  selectedMatch: any = null;
  deletedMatch: number = undefined;
  arbitreList: any[] = this.arbitreServicearbitreList;
  equipeList: any[] = this.equipeServiceequipeList;
  stadeList: any[] = this.stadeServicestadeList;
,
              private arbitreService: ArbitreService,
              private equipeService: EquipeService,
              private stadeService: StadeService) {

  constructor(private fb: FormBuilder, private matchService: MatchService) {}

  ngOnInit(): void {
    this.matchForm = this.fb.group({
      idmatch: ['', Validators.required],
      datematch: ['', Validators.required],
      heurematch: ['', Validators.required],
      arbitreId: ['', Validators.required],
      equipeId1: ['', Validators.required],
      equipeId2: ['', Validators.required],
      stadeId: ['', Validators.required],
    });

  }

  onEdit(match: Match): void {
    this.selectedMatch = match;
    this.matchForm.patchValue({
      idmatch: match.idmatch,
      datematch: match.datematch,
      heurematch: match.heurematch,
    });
  }

  onSubmit(): void {
    if (this.matchForm.valid) {
      const updatedMatch = this.matchForm.value;

      if (this.selectedMatch) {
        this.matchService.update(updatedMatch);
          this.selectedMatch = null;
      } else {
        this.matchService.add(updatedMatch);
          this.selectedMatch = null;
      }
    }
  }

  onDelete(match: Match): void {
    this.matchService.delete(this.deletedMatch!);  }
  deleteMatch(): void {
    this.matchService.delete(this.deletedMatch!);  }
}
