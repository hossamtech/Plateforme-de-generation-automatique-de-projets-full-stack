import { Routes } from '@angular/router';
import { ArbitreComponent } from './pages/arbitre/arbitre.component';
import { EquipeComponent } from './pages/equipe/equipe.component';
import { JoueurComponent } from './pages/joueur/joueur.component';
import { StadeComponent } from './pages/stade/stade.component';

export const routes: Routes = [
  { path: 'arbitre', component: ArbitreComponent },
  { path: 'equipe', component: EquipeComponent },
  { path: 'joueur', component: JoueurComponent },
  { path: 'stade', component: StadeComponent },
];
