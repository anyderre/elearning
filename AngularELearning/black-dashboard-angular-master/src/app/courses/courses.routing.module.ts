import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CoursesDashboardComponent } from './courses-dashboard/courses-dashboard.component';
import { CoursesFormComponent } from './courses-form/courses-form.component';

export const CoursesRoutes: Routes = [
  {
    path: 'form',
    component: CoursesFormComponent,
    canActivate: [ AuthGuard ],
  },
  {
    path: '',
    component: CoursesDashboardComponent,
    canActivate: [ AuthGuard ],
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(CoursesRoutes)
  ],
  exports: [RouterModule]
})
export class CoursesRoutingModule {}
