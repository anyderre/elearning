import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryInfoComponent } from './category-info/category-info.component';
import { AuthGuard } from '../../shared/auth/auth-guard.service';

export const CategoryRoutes: Routes = [
  {
    path: '',
    component: CategoryInfoComponent,
    canActivate: [ AuthGuard ],
    children: [
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(CategoryRoutes),
  ],
  exports: [RouterModule]
})
export class CategoryRoutingModule {}
