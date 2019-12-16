import { Component } from '@angular/core';
import { Category } from '../shared/category.model';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '../shared/category.service';

@Component({
  selector: 'app-courses-category-info',
  templateUrl: 'category-info.component.html',
  styleUrls: ['category-info.component.css']
})
export class CategoryInfoComponent {
   public saving = false;
   public subscription: Subscription;
   public categoryList: Category[];
   public vm: Category;

   constructor(private categoryService: CategoryService) {
    }
  ngOnInit(){
   this.loadVm();
  }

  public loadVm():void {
    this.subscription = this.categoryService.getAll()
    .subscribe(
      data => {
        this.categoryList = data;
        console.log(data);
      },
      error => {
        this.saving = false;  
        alert('Failed to load categorys');
      });
  }

  public getVm(): void {
    this.subscription = this.categoryService.getCategoryViewModel()
    .subscribe(
      data => {
        this.vm = data;
      },
      error => {
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
      error => {
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
    // this.vm = <Category>JSON.parse(JSON.stringify(this.categoryList[index]));
    this.getById(this.categoryList[index].id);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    } 
    this.vm = null
  }

  public isValid(): boolean {
    if (this.vm.name == '') {
      alert('You should specify the category name.');
      return false;
    }
    return true;
  }

  public vmChanged(data: any): void {
    this.loadVm(); 
  }

  public delete (categoryId: number): void {
    const category = this.categoryList.find(o => o.id === categoryId);
    if (!category){
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
  
}