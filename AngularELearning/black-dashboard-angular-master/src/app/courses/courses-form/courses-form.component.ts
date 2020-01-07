import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Courses } from '../shared/courses.model';
import { CoursesService } from '../shared/courses.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-courses-form',
  templateUrl: 'courses-form.component.html'
})
export class CoursesFormComponent implements OnInit{
  @Output() goBack = new EventEmitter();
  @Input() vm: Courses;
  public subscription: Subscription;

  public saving = false;
  public coursesList: Courses[];
  private id: number;

  constructor(
    private courseServie: CoursesService,
    private route: ActivatedRoute,
    private router: Router) {}

  ngOnInit() {
    this.id = +this.route.snapshot.paramMap.get('id');
    if (this.id === 0 ) {
      this.getVm();
    } else {
      this.getById(this.id);
    }
  }

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    // if (!this.isValid()) {
    //   return;
    // }
    this.saving = true;
    this.courseServie.saveOrEditCourses(this.vm)
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

  public getVm(): void {
    this.subscription = this.courseServie.getCoursesViewModel()
    .subscribe(
      data => {
        this.vm = data;
      },
      error => {
        this.saving = false;
        alert('Failed to load Course');
      });
  }

  public getById(courseId: number): void {
    this.subscription = this.courseServie.getCoursesById(courseId)
    .subscribe(
      data => {
        this.vm = data;
      },
      error => {
        this.saving = false;
        alert('Failed to load course');
      });
  }

  public back(isBack: boolean): void {
    if (this.goBack.observers.length > 0) {
      this.goBack.emit(isBack);
    }
  }

  // public cancel(): void {
  //   this.vm.description = '';
  //   this.vm.name = '';
  // }

  // public isValid(): boolean {
  //   if (this.vm.name === '') {
  //     alert('You should specify the course name.');
  //     return false;
  //   }
  //   return true;
  // }
}
