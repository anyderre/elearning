import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoursesRoutingModule } from './courses.routing.module';
import { CoursesDashboardComponent } from './courses-dashboard/courses-dashboard.component';
import { SharedModule } from '../shared/shared.module';
import { CoursesFormComponent } from './courses-form/courses-form.component';
import { CourseSyllabusFormComponent } from './courses-form/syllabus-form/syllabus-form.component';
import { CourseInfoComponent } from './course-info/course-info.component';
import { CourseObjectiveFormComponent } from './courses-form/objective/objective-form.component';
import { CourseOverviewRequirementFormComponent } from './courses-form/requirement/requirement-form.component';
import { CourseOverviewFeatureFormComponent } from './courses-form/feature/feature-form.component';
import { CourseSyllabusVideoFormComponent } from './courses-form/syllabus-form/video/video-form.component';
import { ModalModule } from 'ngb-modal';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    ModalModule,
    CoursesRoutingModule,
  ],
  declarations: [
    CoursesDashboardComponent,
    CoursesFormComponent,
    CourseSyllabusFormComponent,
    CourseInfoComponent,
    CourseObjectiveFormComponent,
    CourseOverviewRequirementFormComponent,
    CourseOverviewFeatureFormComponent,
    CourseSyllabusVideoFormComponent
  ],
})
export class CoursesModule {
}
