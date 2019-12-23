import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CommonModule } from '@angular/common';
import { CategoryInfoComponent } from './category-info/category-info.component';

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
