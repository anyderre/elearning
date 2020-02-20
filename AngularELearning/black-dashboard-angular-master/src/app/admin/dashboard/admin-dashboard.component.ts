import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: 'admin-dashboard.component.html'
})

export class AdminDashboardComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {}

  public add(): void {
    this.router.navigate(['/admin/user/form']);
  }
  public read(): void {
    this.router.navigate(['/admin/user/info']);
  }
}
