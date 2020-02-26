import { Component, Input, Output, EventEmitter, OnInit, ViewEncapsulation } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Courses } from '../shared/courses.model';
import { CoursesService } from '../shared/courses.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Section } from '../../section/shared/section.model';
import { Category } from '../../category/shared/category.model';
import { CategorySelectService } from 'src/app/category/shared/category-select.service';
import { SectionSelectService } from 'src/app/section/shared/section-select.service';
import { Location, JsonPipe } from '@angular/common';
import { User } from 'src/app/admin/user/shared/user.model';
import { Helper } from 'src/app/shared/helper/helper';

@Component({
  selector: 'app-courses-form',
  templateUrl: 'courses-form.component.html',
  encapsulation: ViewEncapsulation.None
})
export class CoursesFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() vm: Courses;
  public subscription: Subscription;

  public saving = false;
  private fNum = Helper.obtenerValorNumerico;
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
    private categorySelectService: CategorySelectService,
    private sectionSelectService: SectionSelectService,
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
  private loadSections(): void {
    this.subscription = this.categorySelectService.getAllFiltered()
    .subscribe(
      data => {
        // this.categories = data;
      },
      () => {
        this.saving = false;
        alert('Failed to load Categories');
      });
  }

  private loadCategories(): void {
    this.subscription = this.sectionSelectService.getAllFiltered()
    .subscribe(
      data => {
        // this.sections = data;
      },
      () => {
        this.saving = false;
        alert('Failed to load Sections');
      });
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
    if (this.courseObj.sectionId) {
      this.vm.section = new Section(this.courseObj.sectionId, '', '');
    }
    if (this.courseObj.categoryId) {
      this.vm.category = new Category(this.courseObj.categoryId, '', '', null, []);
    }
    if (this.courseObj.userId) {
      this.vm.user = new User(this.courseObj.userId, '', '', '', '', false, false);
    }
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
        this.cleanVm = this.vm;
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
        this.cleanVm = this.vm;
        this.fill();
      },
      () => {
        this.saving = false;
        alert('Failed to load course');
      });
  }

  private fill(): void {
    this.courseObj.sectionId = this.vm.section ? this.vm.section.id : 0;
    this.courseObj.categoryId = this.vm.category ? this.vm.category.id : 0;
    this.courseObj.userId = this.vm.user ? this.vm.user.id : 0;
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

    if (this.fNum(this.courseObj.sectionId) <= 0) {
      alert('You should specify the section.');
      return false;
    }

    if (this.fNum(this.courseObj.categoryId) <= 0) {
      alert('You should specify the category.');
      return false;
    }

    if (this.fNum(this.courseObj.userId) <= 0) {
      alert('You should specify the user.');
      return false;
    }

    return true;
  }
}
