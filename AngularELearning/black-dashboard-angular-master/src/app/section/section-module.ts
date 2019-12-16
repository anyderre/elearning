import { NgModule } from "@angular/core";
import { SectionRoutingModule} from './section.routing.module';
import { SectionComponent } from './section.component';
import { SectionFormComponent } from './section-form/section-form.component';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { SectionInfoComponent } from './section-info/section-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    SectionRoutingModule,
  ],
  declarations: [
    SectionComponent,
    SectionFormComponent,
    SectionInfoComponent,
  ],
})
export class SectionModule {
}
