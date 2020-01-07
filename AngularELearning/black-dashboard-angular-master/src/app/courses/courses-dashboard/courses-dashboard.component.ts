import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-courses-dashboard',
  templateUrl: 'courses-dashboard.component.html'
})

export class CoursesDashboardComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {}

  public add(): void {
    this.router.navigate(['/courses/form']);
  }
}
