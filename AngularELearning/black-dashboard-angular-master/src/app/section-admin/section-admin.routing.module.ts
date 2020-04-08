import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CommonModule } from '@angular/common';
import { SectionAdminComponent } from './section-admin.component';
import { SectionAdminDashboardComponent } from './dashboard/section-admin-dashboard.component';

export const SectionAdminRoutes: Routes = [
  {
    path: '',
    component: SectionAdminComponent,
    canActivate: [ AuthGuard ],
    children: [
      {
        path: 'sub-section',
        loadChildren: () => import('./sub-section/sub-section-module').then(m => m.SubSectionModule)
      },
      {
        path: 'section',
        loadChildren: () => import('./section/section-module').then(m => m.SectionModule)
      },
      {
        path: '',
        component: SectionAdminDashboardComponent,
      }
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(SectionAdminRoutes),
  ],
  exports: [RouterModule]
})
export class SectionAdminRoutingModule {}
