import { NgModule } from "@angular/core";
import { CategoryRoutingModule} from './category.routing.module';
import { CategoryComponent } from './category.component';
import { CategoryFormComponent } from './category-form/category-form.component';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { CategoryInfoComponent } from './category-info/category-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    CategoryRoutingModule,
  ],
  declarations: [
    CategoryComponent,
    CategoryFormComponent,
    CategoryInfoComponent,
  ],
})
export class CategoryModule {
}
