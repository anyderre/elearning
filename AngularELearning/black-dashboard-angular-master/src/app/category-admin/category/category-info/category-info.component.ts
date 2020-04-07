import { Component, OnInit, OnDestroy } from '@angular/core';
import { Category } from '../shared/category.model';
import { Subscription } from 'rxjs';
import { CategoryService } from '../shared/category.service';

@Component({
  selector: 'app-courses-category-info',
  templateUrl: 'category-info.component.html',
})
export class CategoryInfoComponent implements OnInit, OnDestroy {
   public saving = false;
   public subscription: Subscription;
   public categoryList: Category[];
   public vm: Category;
   public categorySelected = 0;

  constructor(private categoryService: CategoryService) {
  }

  ngOnInit() {
   this.loadVm();
  }

  public loadVm(): void {
    this.subscription = this.categoryService.getAll()
    .subscribe(
      data => {
        this.categoryList = data;
      },
        () => {
        this.saving = false;
        alert('Failed to load categories');
      });
  }

  public getVm(): void {
    this.subscription = this.categoryService.getCategoryViewModel()
    .subscribe(
      data => {
        this.vm = data;
      },
      () => {
        this.saving = false;
        alert('Failed to load category');
      });
  }

  public getById(categoryId: number): void {
    this.subscription = this.categoryService.getCategoryById(categoryId)
    .subscribe(
      data => {
        this.vm = data;
      },
      () => {
        this.saving = false;
        alert('Failed to load category');
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public save(): void {
    if (!this.isValid()) {
      return;
    }
    this.saving = true;
    this.categoryService.saveOrEditCategory(this.vm)
    .subscribe(message => {
      this.saving = false;
      alert(message);
      },
      error => {
        this.saving = false;
        alert(error);
      }
    );
  }

  public edit(index: number): void {
    this.getById(this.categoryList[index].id);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    }
    this.vm = null;
  }

  public isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the category name.');
      return false;
    }
    return true;
  }

  public delete(categoryId: number): void {
    const category = this.categoryList.find(o => o.id === categoryId);
    if (!category) {
      return;
    }

    if (confirm(`Do you really want to delete the category ${category.name}`)) {
      this.subscription = this.categoryService.deleteCategory(categoryId)
      .subscribe(
        data => {
          alert(data);
          this.loadVm();
        },
        error => {
          console.log(error);
          alert('Failed to delete that category');
        });
    }
  }
  public categorySelect(): void {
    setTimeout(() => {
      this.categorySelected = 0;
      const categories = this.categoryList.filter(o => o.selected);
      if (categories && categories.length > 0) {
        this.categorySelected = categories.length;
      }
    }, 0);
  }
  public deleteSelected(): void {
    // TODO: endpoint to delete selected categories
  }
}
