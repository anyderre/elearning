import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { SubCategory } from '../shared/sub-category.model';
import { SubCategoryService } from '../shared/sub-category.service';

@Component({
  selector: 'app-sub-category-info',
  templateUrl: 'sub-category-info.component.html',
})
export class SubCategoryInfoComponent implements OnInit, OnDestroy {
  public saving = false;
  public subscription: Subscription;
  public subCategoryList: SubCategory[];
  public vm: SubCategory;
  public isSuperAdmin = false;
  public subCategorySelected = 0;

  constructor(private subCategoryService: SubCategoryService, private router: Router) { }

  ngOnInit() {
    this.loadVm();
  }

  public loadVm(): void {
    this.subscription = this.subCategoryService.getInfo()
    .subscribe(
      data => {
        this.subCategoryList = data;
      },
      () => {
        this.saving = false;
        alert('Failed to load subCategories');
      });
  }

  public add(): void {
    this.router.navigate(['/category-admin/sub-category/form']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public edit(index: number): void {
    this.router.navigate([`/category-admin/sub-category/${this.subCategoryList[index].id}/form`]);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    }
    this.vm = null;
  }

  public delete(subCategoryId: number): void {
    const subCategory = this.subCategoryList.find(o => o.id === subCategoryId);
    if (!subCategory) {
      return;
    }

    if (confirm(`Do you really want to delete the subCategory ${subCategory.name}`)) {
      this.subscription = this.subCategoryService.deleteSubCategory(subCategoryId)
      .subscribe(
        data => {
          alert(data);
          this.loadVm();
        },
        error => {
          console.log(error);
          alert('Failed to delete that subCategory');
        });
    }
  }

  public subCategorySelect(): void {
    setTimeout(() => {
      this.subCategorySelected = 0;
      const subCategories = this.subCategoryList.filter(o => o.selected);
      if (subCategories && subCategories.length > 0) {
        this.subCategorySelected = subCategories.length;
      }
    }, 0);
  }
  public deleteSelected(): void {
    // TODO: endpoint to delete selected sub-categories
  }

}
