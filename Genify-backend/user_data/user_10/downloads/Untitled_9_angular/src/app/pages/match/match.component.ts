import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatchService } from '../../services/match.service';
import { LucideAngularModule } from 'lucide-angular';
import { MDModalModule } from '../../modals/modal.module';
import { NgForOf, NgIf } from '@angular/common';

import {Match} from "../../models/match.model";

import { ModalService } from '../../modals/modal.service';
import {ArbitreService} from "../../services/arbitre.service";
import {EquipeService} from "../../services/equipe.service";
import {StadeService} from "../../services/stade.service";
import {NgSelectComponent} from "@ng-select/ng-select";
@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  standalone: true,
  imports: [LucideAngularModule, MDModalModule, ReactiveFormsModule, NgIf, NgForOf, NgSelectComponent],
  styleUrls: ['./match.component.scss']
})
export class MatchComponent implements OnInit {
  matchForm!: FormGroup;
  matchList = this.matchService.matchList;
  selectedMatch: any = null;
  deletedMatch: number = 0;
  arbitreList = this.arbitreService.arbitreList;
  stadeList = this.stadeService.stadeList;
  equipeList = this.equipeService.equipeList;



  constructor( private arbitreService: ArbitreService,
               private equipeService: EquipeService,
               private stadeService: StadeService,
               private fb: FormBuilder, private matchService: MatchService, private modalService: ModalService,)
  {}

  ngOnInit(): void {
    this.matchForm = this.fb.group({
      idmatch: [''],
      datematch: ['', Validators.required],
      heurematch: ['', Validators.required],
      arbitreId: ['', Validators.required],
      equipe_1Id: ['', Validators.required],
      equipe_2Id: ['', Validators.required],
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
      const dataMatch = this.matchForm.value;

      if (this.selectedMatch) {
        this.matchService.update(dataMatch);
          this.selectedMatch = null;
      } else {
        this.matchService.add(dataMatch);
        console.log(dataMatch)
          this.selectedMatch = null;
      }
      this.modalService.close('matchModal');
    } else {
        for (const i in this.matchForm.controls) {
            this.matchForm.controls[i].markAsTouched();
            this.matchForm.controls[i].updateValueAndValidity();
        }
    }

  }

  onDelete(match: Match): void {
    this.deletedMatch =match.idmatch;
  }
  deleteMatch(): void {
    this.matchService.delete(this.deletedMatch!);    this.modalService.close('deleteMatch');
  }
}
