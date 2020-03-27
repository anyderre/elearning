import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: 'admin-dashboard.component.html'
})

export class AdminDashboardComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {}

  public addUser(): void {
    this.router.navigate(['/admin/user/form']);
  }
  public readUser(): void {
    this.router.navigate(['/admin/user/info']);
  }
  public addRole(): void {
    this.router.navigate(['/admin/role/form']);
  }
  public readRole(): void {
    this.router.navigate(['/admin/role/info']);
  }
}
