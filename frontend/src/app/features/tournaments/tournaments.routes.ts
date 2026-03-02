import { Routes } from '@angular/router';
import { TournamentListComponent } from './tournament-list/tournament-list';
import { TournamentDetailComponent } from './tournament-detail/tournament-detail';
import { TournamentFormComponent } from './tournament-form/tournament-form';

export const TOURNAMENTS_ROUTES: Routes = [
  {
    path: '',
    component: TournamentListComponent
  },
  {
    path: 'create',
    component: TournamentFormComponent
  },
  {
    path: 'edit/:id',
    component: TournamentFormComponent
  },
  {
    path: ':id',
    component: TournamentDetailComponent
  }
];
