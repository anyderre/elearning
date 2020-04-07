import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { CategoryAdminComponent } from './category-admin.component';
import { CategoryAdminDashboardComponent } from './dashboard/category-admin-dashboard.component';
import { CategoryAdminRoutingModule } from './category-admin.routing.module';

@NgModule({
  imports: [
    SharedModule,
    CommonModule,
    CategoryAdminRoutingModule,
  ],
  declarations: [
    CategoryAdminComponent,
    CategoryAdminDashboardComponent
  ],
})
export class CategoryAdminModule {
}
