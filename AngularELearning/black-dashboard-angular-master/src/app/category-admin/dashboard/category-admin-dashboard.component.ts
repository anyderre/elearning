import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-category-admin-dashboard',
  templateUrl: 'category-admin-dashboard.component.html'
})

export class CategoryAdminDashboardComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {}

  public addCategory(): void {
    this.router.navigate(['/category-admin/category']);
  }
  // public readCategory(): void {
  //   this.router.navigate(['/category-admin/category/info']);
  // }
  public addSubCategory(): void {
    this.router.navigate(['/category-admin/sub-category/form']);
  }
  public readSubCategory(): void {
    this.router.navigate(['/category-admin/sub-category/info']);
  }
}
