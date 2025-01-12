import { Routes } from '@angular/router';
import { ArbitreComponent } from './pages/arbitre/arbitre.component';
import { MatchComponent } from './pages/match/match.component';
import { EquipeComponent } from './pages/equipe/equipe.component';
import { JoueurComponent } from './pages/joueur/joueur.component';
import { StadeComponent } from './pages/stade/stade.component';

export const routes: Routes = [
  { path: 'arbitre', component: arbitreComponent },
  { path: 'match', component: matchComponent },
  { path: 'equipe', component: equipeComponent },
  { path: 'joueur', component: joueurComponent },
  { path: 'stade', component: stadeComponent },
];
