import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { SubCategoryRoutingModule } from './sub-category.routing.module';
import { SubCategoryComponent } from './sub-category.component';
import { SubCategoryFormComponent } from './sub-category-form/sub-category-form.component';
import { SubCategoryInfoComponent } from './sub-category-info/sub-category-info.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    SubCategoryRoutingModule,
  ],
  declarations: [
    SubCategoryComponent,
    SubCategoryFormComponent,
    SubCategoryInfoComponent
  ],
})
export class SubCategoryModule {
}
