import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../../shared/auth/auth-guard.service';
import { RoleFormComponent } from './role-form/role-form.component';
import { RoleInfoComponent } from './role-info/role-info.component';
import { RoleComponent } from './role.component';

export const RolRoutes: Routes = [
  {
    path: '',
    component: RoleComponent,
    canActivate: [ AuthGuard ],
    children : [
      {
        path: 'form',
        component: RoleFormComponent
      },
      {
        path: ':id/form',
        component: RoleFormComponent
      },
      {
        path: 'info',
        component: RoleInfoComponent
      }
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(RolRoutes),
  ],
  exports: [RouterModule]
})
export class RoleRoutingModule {}
