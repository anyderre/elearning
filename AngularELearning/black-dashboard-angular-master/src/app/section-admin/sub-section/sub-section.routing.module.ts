import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../../shared/auth/auth-guard.service';
import { SubSectionComponent } from './sub-section.component';
import { SubSectionFormComponent } from './sub-section-form/sub-section-form.component';
import { SubSectionInfoComponent } from './sub-section-info/sub-section-info.component';

export const SubSectionRoutes: Routes = [
  {
    path: '',
    component: SubSectionComponent,
    canActivate: [ AuthGuard ],
    children : [
      {
        path: 'form',
        component: SubSectionFormComponent
      },
      {
        path: ':id/form',
        component: SubSectionFormComponent
      },
      {
        path: 'info',
        component: SubSectionInfoComponent
      }
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(SubSectionRoutes),
  ],
  exports: [RouterModule]
})
export class SubSectionRoutingModule {}
