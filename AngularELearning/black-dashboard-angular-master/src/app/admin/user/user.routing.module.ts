import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../../shared/auth/auth-guard.service';
import { UserComponent } from 'src/app/pages/user/user.component';
import { UserFormComponent } from './user-form/user-form.component';

export const UserRoutes: Routes = [
  {
    path: '',
    component: UserComponent,
    canActivate: [ AuthGuard ],
    children : [
      {
        path: 'form',
        component: UserFormComponent
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
