import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: 'dashboard.component.html'
})

export class AdminDashboardComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {}

  // public add(): void {
  //   this.router.navigate(['/courses/0/form']);
  // }
  // public read(): void {
  //   this.router.navigate(['/courses/read']);
  // }
}
