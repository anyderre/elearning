import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from '../shared/auth/auth-guard.service';
import { CommonModule } from '@angular/common';
import { CategoryAdminComponent } from './category-admin.component';
import { CategoryAdminDashboardComponent } from './dashboard/category-admin-dashboard.component';

export const CategoryAdminRoutes: Routes = [
  {
    path: '',
    component: CategoryAdminComponent,
    canActivate: [ AuthGuard ],
    children: [
      {
        path: 'sub-category',
        loadChildren: () => import('./sub-category/sub-category-module').then(m => m.SubCategoryModule)
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category-module').then(m => m.CategoryModule)
      },
      {
        path: '',
        component: CategoryAdminDashboardComponent,
      }
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(CategoryAdminRoutes),
  ],
  exports: [RouterModule]
})
export class CategoryAdminRoutingModule {}
