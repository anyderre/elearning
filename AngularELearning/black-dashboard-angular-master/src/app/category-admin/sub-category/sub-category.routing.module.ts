import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from '../../shared/auth/auth-guard.service';
import { SubCategoryComponent } from './sub-category.component';
import { SubCategoryFormComponent } from './sub-category-form/sub-category-form.component';
import { SubCategoryInfoComponent } from './sub-category-info/sub-category-info.component';

export const SubCategoryRoutes: Routes = [
  {
    path: '',
    component: SubCategoryComponent,
    canActivate: [ AuthGuard ],
    children : [
      {
        path: 'form',
        component: SubCategoryFormComponent
      },
      {
        path: ':id/form',
        component: SubCategoryFormComponent
      },
      {
        path: 'info',
        component: SubCategoryInfoComponent
      }
    ]
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(SubCategoryRoutes),
  ],
  exports: [RouterModule]
})
export class SubCategoryRoutingModule {}
