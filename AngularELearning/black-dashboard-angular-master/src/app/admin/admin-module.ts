import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { AdminComponent } from './admin.component';
import { AdminRoutingModule } from './admin.routing.module';
import { AdminDashboardComponent } from './dashboard/dashboard.component';

@NgModule({
  imports: [
    SharedModule,
    CommonModule,
    AdminRoutingModule,
  ],
  declarations: [
    AdminComponent,
    AdminDashboardComponent
  ],
})
export class AdminModule {
}
