import { Routes } from '@angular/router';
import { TournamentListComponent } from './tournament-list/tournament-list';
import { TournamentDetailComponent } from './tournament-detail/tournament-detail';
import { TournamentFormComponent } from './tournament-form/tournament-form';
import { adminGuard } from '@core/guards/admin-guard';

export const TOURNAMENTS_ROUTES: Routes = [
  {
    path: '',
    component: TournamentListComponent
  },
  {
    path: 'create',
    component: TournamentFormComponent,
    canActivate: [adminGuard]
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
