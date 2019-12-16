import { Routes, RouterModule } from "@angular/router";
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CoursesDashboardComponent } from './courses-dashboard/courses-dashboard.component';

export const CoursesRoutes: Routes = [
  {
    path: '',
    component: CoursesDashboardComponent,
    canActivate: [ AuthGuard ],
    children: [
     
    ]
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
