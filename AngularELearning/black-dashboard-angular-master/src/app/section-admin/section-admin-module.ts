import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { SectionAdminComponent } from './section-admin.component';
import { SectionAdminRoutingModule } from './section-admin.routing.module';
import { SectionAdminDashboardComponent } from './dashboard/section-admin-dashboard.component';

@NgModule({
  imports: [
    SharedModule,
    CommonModule,
    SectionAdminRoutingModule,
  ],
  declarations: [
    SectionAdminComponent,
    SectionAdminDashboardComponent
  ],
})
export class SectionAdminModule {
}
