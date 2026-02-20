import { Routes } from '@angular/router';
import { authGuard } from '@core/guards/auth-guard';
import { guestGuard } from '@core/guards/guest-guard';
import { HomeComponent } from './pages/home/home';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'auth',
    canActivate: [guestGuard],
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: 'tournaments',
    canActivate: [authGuard],
    loadChildren: () => import('./features/tournaments/tournaments.routes').then(m => m.TOURNAMENTS_ROUTES)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
