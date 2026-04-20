import { Routes } from '@angular/router';
import { authGuard } from '@core/guards/auth-guard';
import { UserProfileViewComponent } from './user-profile-view/user-profile-view';
import { UserProfileEditComponent } from './user-profile-edit/user-profile-edit';

export const PROFILE_ROUTES: Routes = [
  {
    path: '',
    component: UserProfileViewComponent,
    canActivate: [authGuard]
  },
  {
    path: 'edit',
    component: UserProfileEditComponent,
    canActivate: [authGuard]
  }
];
