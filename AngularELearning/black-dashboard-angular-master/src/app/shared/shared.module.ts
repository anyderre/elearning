import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';
import { NgxSelectModule } from 'ngx-select-ex';
import { CategorySelectService } from '../category/shared/category-select.service';
import { SectionSelectService } from '../section/shared/section-select.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    NgbModule,
    CommonModule,
    HttpClientModule,
    FormsModule,
    NgSelectModule,
    NgxSelectModule,
    RouterModule
  ],
  providers: [
    CategorySelectService,
    SectionSelectService,
  ],
  exports: [
    NgbModule,
    FormsModule,
    HttpClientModule,
    NgxSelectModule,
    NgSelectModule,
    FormsModule,
  ]
})
export class SharedModule {}
