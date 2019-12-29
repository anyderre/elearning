import { NgModule } from '@angular/core';
import { SyllabusRoutingModule} from './syllabus.routing.module';
import { SyllabusComponent } from './syllabus.component';
import { SyllabusFormComponent } from './syllabus-form/syllabus-form.component';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { SyllabusInfoComponent } from './syllabus-info/syllabus-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    SyllabusRoutingModule,
  ],
  declarations: [
    SyllabusComponent,
    SyllabusFormComponent,
    SyllabusInfoComponent,
  ],
})
export class SyllabusModule {
}
