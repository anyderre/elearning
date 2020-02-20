import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../../shared/auth/auth-guard.service';
import { UserFormComponent } from './user-form/user-form.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { UserComponent } from './user.component';

export const UserRoutes: Routes = [
  {
    path: '',
    component: UserComponent,
    canActivate: [ AuthGuard ],
    children : [
      {
        path: 'form',
        component: UserFormComponent
      },
      {
        path: ':id/form',
        component: UserFormComponent
      },
      {
        path: 'info',
        component: UserInfoComponent
      }
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(UserRoutes),
  ],
  exports: [RouterModule]
})
export class UserRoutingModule {}
