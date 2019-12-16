import { Routes, RouterModule } from "@angular/router";
import { NgModule } from '@angular/core';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CommonModule } from '@angular/common';
import { SectionInfoComponent } from './section-info/section-info.component';

export const SectionRoutes: Routes = [
  {
    path: '',
    component: SectionInfoComponent,
    canActivate: [ AuthGuard ],
    children: [

    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(SectionRoutes),
  ],
  exports: [RouterModule]
})
export class SectionRoutingModule {}
