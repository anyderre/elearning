import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './admin.component';
import { AdminDashboardComponent } from './dashboard/admin-dashboard.component';

export const AdminRoutes: Routes = [
  {
    path: '',
    component: AdminComponent,
    canActivate: [ AuthGuard ],
    children: [
      {
        path: 'user',
        loadChildren: () => import('./user/user-module').then(m => m.UserModule)
      },
      {
        path: 'role',
        loadChildren: () => import('./role/role-module').then(m => m.RoleModule)
      },
      {
        path: '',
        component: AdminDashboardComponent,
      }
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminRoutes),
  ],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
