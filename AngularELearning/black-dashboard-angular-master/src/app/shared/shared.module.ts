import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';
import { NgxSelectModule } from 'ngx-select-ex';
import { SectionSelectService } from '../section-admin/section/shared/section-select.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CustomModalComponent } from './component/modal.component';
import { CategorySelectService } from '../category-admin/category/shared/category-select.service';

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
    // CustomModalComponent,
  ]
})
export class SharedModule {}
