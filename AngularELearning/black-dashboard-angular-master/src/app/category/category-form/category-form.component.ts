import { Component, Input, Output, EventEmitter, OnInit, ViewEncapsulation } from '@angular/core';
import { Category } from '../shared/category.model';
import { CategoryService } from '../shared/category.service';
import { FormGroup } from '@angular/forms';
import { CategorySelectService } from '../shared/category-select.service';

@Component({
  selector: 'app-courses-category-form',
  templateUrl: 'category-form.component.html',
  styleUrls: ['category-form.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CategoryFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() vm: Category;
  public loading = false;
  public selectedCategory = null;

  public saving = false;

  constructor(
    private categoryService: CategoryService,
    public categorySelectService: CategorySelectService) {}

  ngOnInit() {
  }

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    if (!this.isValid()) {
      return;
    }

    this.updateVm();
    this.saving = true;
    this.categoryService.saveOrEditCategory(this.vm)
    .subscribe(() => {
      this.saving = false;
      this.back(false);
      alert('Registration correct');
      },
      () => {
        this.saving = false;
        alert('Registration failed');
      }
    );
  }

  private updateVm(): void {
    // if (this.parentCategoryId) {
    //   this.vm.parentCategory = new Category(this.parentCategoryId, '', '', null, []);
    // }
  }

  public back(isBack: boolean): void {
    if (this.goBack.observers.length > 0) {
      this.goBack.emit(isBack);
    }
  }

  public cancel(): void {
    this.vm.description = '';
    this.vm.name = '';
  }

  public isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the category name.');
      return false;
    }
    return true;
  }
}
