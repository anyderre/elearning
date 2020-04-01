import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { RoleRoutingModule } from './role.routing.module';
import { RoleComponent } from './role.component';
import { RoleFormComponent } from './role-form/role-form.component';
import { RoleInfoComponent } from './role-info/role-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    RoleRoutingModule,
  ],
  declarations: [
    RoleComponent,
    RoleFormComponent,
    RoleInfoComponent
  ],
})
export class RoleModule {
}
