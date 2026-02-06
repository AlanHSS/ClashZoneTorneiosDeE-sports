import { Routes } from '@angular/router';

export const routes: Routes = [

    {
      path: '',
      redirectTo: 'tournaments',
      pathMatch: 'full'
    },
    {
      path: '**',
      redirectTo: 'tournaments'
    }

];
