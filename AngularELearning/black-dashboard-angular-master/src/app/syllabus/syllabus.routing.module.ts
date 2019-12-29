import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CommonModule } from '@angular/common';
import { SyllabusInfoComponent } from './syllabus-info/syllabus-info.component';

export const SyllabusRoutes: Routes = [
  {
    path: '',
    component: SyllabusInfoComponent,
    canActivate: [ AuthGuard ],
    children: [

    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(SyllabusRoutes),
  ],
  exports: [RouterModule]
})
export class SyllabusRoutingModule {}
