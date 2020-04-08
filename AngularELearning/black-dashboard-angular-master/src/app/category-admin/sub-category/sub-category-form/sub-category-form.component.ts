import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { Location } from '@angular/common';
import { Helper } from 'src/app/shared/helper/helper';
import { SubCategory } from '../shared/sub-category.model';
import { SubCategoryService } from '../shared/sub-category.service';

@Component({
  selector: 'app-sub-category-form',
  templateUrl: 'sub-category-form.component.html'
})
export class SubCategoryFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() vm: SubCategory;
  public subscription: Subscription;
  public saving = false;
  public subCategoryList: SubCategory[];
  public cleanVm: SubCategory;

  constructor(
    private subCategoryService: SubCategoryService,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute) {}

  ngOnInit() {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id === 0 ) {
      this.getVm();
    } else {
      this.getById(id);
    }
  }

  public getVm(): void {
    this.subscription = this.subCategoryService.getSubCategoryViewModel()
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      }, () => {
        this.saving = false;
        alert('Failed to load the sub-Category');
      });
  }

  public getById(subCategoryId: number): void {
    this.subscription = this.subCategoryService.getSubCategoryById(subCategoryId)
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      },
      () => {
        this.saving = false;
        alert('Failed to load the sub-Category');
      });
  }

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    if (!this.isValid()) {
      return;
    }
    this.saving = true;
    this.subCategoryService.saveOrEditSubCategory(this.vm)
    .subscribe(() => {
      this.saving = false;
      this.router.navigate(['/category-admin/sub-category/info']);
      alert('Registration correct');
      },
      () => {
        this.saving = false;
        alert('Registration failed');
      }
    );
  }

  public back(): void {
    this.location.back();
  }

  public cancel(): void {
    if (JSON.stringify(this.cleanVm) !== JSON.stringify(this.vm)) {
      if (confirm('There are changes that will be lost. Do you really want to leave?')) {
        this.location.back();
      }
    } else {
      this.location.back();
    }
  }


  private isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the name.');
      return false;
    }
    if (this.vm.category && Helper.getNumericValue(this.vm.category.id) <= 0) {
      alert('You should specify the category.');
      return false;
    }
    return true;
  }
}
