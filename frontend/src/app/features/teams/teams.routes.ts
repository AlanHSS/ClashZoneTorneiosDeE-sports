import { Routes } from '@angular/router';
import { authGuard } from '@core/guards/auth-guard';
import { TeamListComponent } from './team-list/team-list';
import { TeamDetailComponent } from './team-detail/team-detail';
import { TeamFormComponent } from './team-form/team-form';

export const TEAMS_ROUTES: Routes = [
  {
    path: '',
    component: TeamListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'create',
    component: TeamFormComponent,
    canActivate: [authGuard]
  },
  {
    path: 'edit/:id',
    component: TeamFormComponent,
    canActivate: [authGuard]
  },
  {
    path: ':id',
    component: TeamDetailComponent,
    canActivate: [authGuard]
  }
];
