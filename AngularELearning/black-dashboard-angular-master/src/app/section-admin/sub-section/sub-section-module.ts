import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { SubSectionRoutingModule } from './sub-section.routing.module';
import { SubSectionComponent } from './sub-section.component';
import { SubSectionFormComponent } from './sub-section-form/sub-section-form.component';
import { SubSectionInfoComponent } from './sub-section-info/sub-section-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    SubSectionRoutingModule,
  ],
  declarations: [
    SubSectionComponent,
    SubSectionFormComponent,
    SubSectionInfoComponent
  ],
})
export class SubSectionModule {
}
