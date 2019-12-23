import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';
import { NgxSelectModule } from 'ngx-select-ex';
import { CategorySelectService } from '../category/shared/category-select.service';

@NgModule({
  imports: [
    NgSelectModule,
    NgxSelectModule,
    FormsModule,
    RouterModule],
  declarations: [],
  providers: [
    CategorySelectService,
  ],
  exports: [NgxSelectModule, NgSelectModule, FormsModule, ]
})
export class SharedModule {}
