import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-section-admin-dashboard',
  templateUrl: 'section-admin-dashboard.component.html'
})

export class SectionAdminDashboardComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {}

  public addSection(): void {
    this.router.navigate(['/section-admin/section']);
  }
  // public readSection(): void {
  //   this.router.navigate(['/section-admin/section/info']);
  // }
  public addSubSection(): void {
    this.router.navigate(['/section-admin/sub-section/form']);
  }
  public readSubSection(): void {
    this.router.navigate(['/section-admin/sub-section/info']);
  }
}
