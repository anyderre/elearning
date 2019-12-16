import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CoursesRoutingModule } from './courses.routing.module';
import { CoursesDashboardComponent } from './courses-dashboard/courses-dashboard.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    CoursesRoutingModule,
  ],
  declarations: [
    CoursesDashboardComponent,
  ],
})
export class CoursesModule {
}
