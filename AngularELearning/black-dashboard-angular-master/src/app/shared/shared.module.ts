import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';
import { NgxSelectModule } from 'ngx-select-ex';
import { CategorySelectService } from '../category/shared/category-select.service';
import { SectionSelectService } from '../section/shared/section-select.service';

@NgModule({
  imports: [
    NgSelectModule,
    NgxSelectModule,
    FormsModule,
    RouterModule],
  declarations: [],
  providers: [
    CategorySelectService,
    SectionSelectService,
  ],
  exports: [NgxSelectModule, NgSelectModule, FormsModule, ]
})
export class SharedModule {}
