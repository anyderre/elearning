import { Component, Input, Output, EventEmitter, OnInit, ViewEncapsulation } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Courses } from '../shared/courses.model';
import { CoursesService } from '../shared/courses.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Helper } from 'src/app/shared/helper/helper';
import { Objective } from '../shared/objective.model';

@Component({
  selector: 'app-courses-form',
  templateUrl: 'courses-form.component.html',
  encapsulation: ViewEncapsulation.None
})
export class CoursesFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() vm: Courses;
  public subscription: Subscription;
  public listObjectives: Objective[] = [];

  public saving = false;
  private fNum = Helper.getNumericValue;
  public coursesList: Courses[];
  public courseObj = {
    sectionId: 0,
    userId: 0,
    categoryId: 0,
    start: null,
    end: null,
  };

  private id: number;
  public cleanVm: Courses;

  constructor(
    private courseService: CoursesService,
    private location: Location,
    private route: ActivatedRoute) {}

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

    if (!this.isValid()) {
      return;
    }

    this.updateVm();

    this.saving = true;
    this.courseService.saveOrEditCourses(this.vm)
    .subscribe(() => {
      this.saving = false;
      this.location.back();
      alert('Registration correct');
      }, () => {
        this.saving = false;
        alert('Registration failed');
      }
    );
  }

  private updateVm(): void {
    if (!this.vm.premium) {
      this.vm.price = 0;
    }
    if (this.courseObj.start) {
      const startDate = this.courseObj.start;
      this.vm.startDate = new Date(startDate.year, startDate.month, startDate.day);
    }
    if (this.courseObj.end) {
      const endDate = this.courseObj.end;
      this.vm.endDate = new Date(endDate.year, endDate.month, endDate.day);
    }
  }

  public getVm(): void {
    this.subscription = this.courseService.getCoursesViewModel()
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      }, () => {
        this.saving = false;
        alert('Failed to load Course');
      });
  }

  public getById(courseId: number): void {
    this.subscription = this.courseService.getCoursesById(courseId)
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
        this.fill();
      },
      () => {
        this.saving = false;
        alert('Failed to load course');
      });
  }

  private fill(): void {
    this.courseObj.start = this.vm.startDate ?
    {
      year: 2020,
      month: 2,
      day: 25
    } : null;
    this.courseObj.end = this.vm.endDate ?
    {
      year: 2020,
      month: 2,
      day: 25
    } : null;
  }

  public cancel(): void {
    if (JSON.stringify(this.cleanVm) !== JSON.stringify(this.vm)) {
      if (confirm('There are changes that will be lost. Do you really want to leave?')) {
        this.location.back();
      }
    } else {
      this.location.back();
    }
  }

  public isValid(): boolean {
    if (this.vm.title === '') {
      alert('You should specify the course title.');
      return false;
    }

    if (this.vm.premium) {
      if (+this.vm.price <= 0) {
        alert('You should specify the course price.');
        return false;
      }
    }

    if (this.vm.section && this.fNum(this.vm.section.id) <= 0) {
      alert('You should specify the section.');
      return false;
    }

    if (this.vm.category && this.fNum(this.vm.category.id) <= 0) {
      alert('You should specify the category.');
      return false;
    }

    if (this.vm.user && this.fNum(this.vm.user.id) <= 0) {
      alert('You should specify the user.');
      return false;
    }

    return true;
  }

  // Category
  public categoryValueChange(data: any): void {
    if (data > 0) {
      // filter Categories
    }
  }
  // Section
  public sectionValueChange(data: any): void {
    if (data > 0) {
       // filter Sections
    }
  }
}
