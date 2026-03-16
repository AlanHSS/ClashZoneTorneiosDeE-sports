import { Routes } from '@angular/router';
import { authGuard } from '@core/guards/auth-guard';
import { InscriptionsHomeComponent } from './inscriptions-home/inscriptions-home';
import { MyInscriptionsComponent } from './my-inscriptions/my-inscriptions';
import { InscriptionFormComponent } from './inscription-form/inscription-form';
import { TournamentInscriptionsComponent } from './tournament-inscriptions/tournament-inscriptions';

export const INSCRIPTIONS_ROUTES: Routes = [
  {
    path: '',
    component: InscriptionsHomeComponent,
    canActivate: [authGuard]
  },
  {
    path: 'my',
    component: MyInscriptionsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'create',
    component: InscriptionFormComponent,
    canActivate: [authGuard]
  },
  {
    path: 'tournament/:id',
    component: TournamentInscriptionsComponent,
    canActivate: [authGuard]
  }
];
