import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoursesRoutingModule } from './courses.routing.module';
import { CoursesDashboardComponent } from './courses-dashboard/courses-dashboard.component';
import { SharedModule } from '../shared/shared.module';
import { CoursesFormComponent } from './courses-form/courses-form.component';
import { SyllabusFormComponent } from './syllabus-form/syllabus-form.component';
import { CourseInfoComponent } from './course-info/course-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    CoursesRoutingModule,
  ],
  declarations: [
    CoursesDashboardComponent,
    CoursesFormComponent,
    SyllabusFormComponent,
    CourseInfoComponent,
  ],
})
export class CoursesModule {
}
