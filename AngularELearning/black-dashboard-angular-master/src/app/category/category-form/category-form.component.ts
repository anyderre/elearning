import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Category } from '../shared/category.model';
import { CategoryService } from '../shared/category.service';
import { Subscription } from 'rxjs';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-courses-category-form',
  templateUrl: 'category-form.component.html'
})
export class CategoryFormComponent {
  @Output() goBack = new EventEmitter();
  @Input() vm: Category;

  public saving = false;
  public categoryList: Category[];

  constructor(private categoryService: CategoryService) {}

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    if (!this.isValid()) {
      return;
    }
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
    if (this.vm.name == '') {
      alert('You should specify the category name.');
      return false;
    }
    return true;
  }
}
