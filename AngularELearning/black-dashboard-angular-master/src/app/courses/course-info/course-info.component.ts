import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Courses } from '../shared/courses.model';
import { CoursesService } from '../shared/courses.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-course-info',
  templateUrl: 'course-info.component.html'
})
export class CourseInfoComponent implements OnDestroy, OnInit {
   public saving = false;
   public subscription: Subscription;
   public courses: Courses[];

  constructor(private courseService: CoursesService, private router: Router) { }

  ngOnInit() {
    this.loadCourses();
  }

  private loadCourses(): void {
    this.subscription = this.courseService.getAll()
    .subscribe( data => {
        this.courses = data;
      },
        () => {
        this.saving = false;
        alert('Failed to load courses');
      });
  }

  public add(): void {
    this.router.navigate(['/courses/0/form']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public edit(index: number): void {
    const id = this.courses[index].id;
    this.router.navigate([`/courses/${id}/form`]);
  }

}
