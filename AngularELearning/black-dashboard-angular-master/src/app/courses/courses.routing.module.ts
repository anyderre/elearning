import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CoursesFormComponent } from './courses-form/courses-form.component';
import { CourseInfoComponent } from './course-info/course-info.component';

export const CoursesRoutes: Routes = [
  {
    path: ':id/form',
    component: CoursesFormComponent,
    canActivate: [ AuthGuard ],
  },
  {
    path: '',
    component: CourseInfoComponent,
    canActivate: [ AuthGuard ],
  },
  {
    path: 'read',
    component: CoursesFormComponent,
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
